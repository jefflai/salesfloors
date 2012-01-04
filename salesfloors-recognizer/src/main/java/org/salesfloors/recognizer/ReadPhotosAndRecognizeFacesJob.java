package org.salesfloors.recognizer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.web.client.RestTemplate;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.salesfloors.aws.AwsClient;
import com.salesfloors.sfdc.SfdcConnector;
import com.sforce.ws.ConnectionException;

/**
 * This class reads from AWS and calls face.com to recognize faces in the pictures
 * @author dtia
 *
 */

public class ReadPhotosAndRecognizeFacesJob implements Job {

	public static final String baseFacePhotoBucketUri = "https://s3.amazonaws.com/FacePics/";
	public static final String fbUserInfoURL = "https://graph.facebook.com/{userId}";
	public static final int confidenceThreshold = 70;
	
	public AwsClient aws;
	public ObjectMapper mapper;	
	public TrainFaces tf;
	public SfdcConnector sfdcConn;
	
	public ReadPhotosAndRecognizeFacesJob() throws IOException, ConnectionException {
		aws = new AwsClient();	
		mapper = new ObjectMapper();	
		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		tf = new TrainFaces();		
		sfdcConn = new SfdcConnector();
	}
		
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		ObjectListing ol = aws.getS3().listObjects(AwsClient.defaultBucketName);
		List<S3ObjectSummary> os = ol.getObjectSummaries();
		
		for(S3ObjectSummary obj : os) {
			// read file from S3
			S3Object photo = aws.readFileFromS3(obj.getKey());
			
			// call face.com api with photo
			String recognitionResponse = tf.recognizeFaces(baseFacePhotoBucketUri + getAWSFilename(photo.getKey()));
			
			// parse response from face.com api	
			String uid = null;
			int confidence = 0;
			try {
				JsonNode rootNode = mapper.readValue(recognitionResponse, JsonNode.class); 
				JsonNode photosNode = rootNode.get("photos");
				JsonNode firstPath = photosNode.path(0);				
				JsonNode tagsNode = firstPath.get("tags");				
				JsonNode uidsNode = tagsNode.path(0).get("uids");
				
				if (uidsNode.size() > 0) {
					JsonNode firstUidNode = uidsNode.path(0);
					uid = firstUidNode.get("uid").getTextValue();
					confidence = firstUidNode.get("confidence").getIntValue();
					
					System.out.println("uid: " + uid);
					System.out.println("confidence: " + confidence);
					
					// get username from fb id
					String userName = null;
					try {
						userName = getFbUsername(uid);				
						System.out.println("your username: " + userName);
					} catch (JsonParseException e) {
						e.printStackTrace();
					} catch (JsonMappingException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					// set is present only if confidence is greater than threshold
					if(confidence > confidenceThreshold) {
						// call sfdc api
						SfdcConnector forceConn;
						try {
							String[] splitUserName = userName.split(" ");
							String firstName = splitUserName[0];
							String lastName = splitUserName[1];
							
							forceConn = new SfdcConnector(); 	
					    	forceConn.setIsPresent(firstName, lastName, true);
						} catch (ConnectionException e) {
							e.printStackTrace();
						}	    		    	
					}
				}
				
				
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			
			// delete file from S3
			try {
				aws.deleteFileOnS3(photo.getKey());
			} catch (AmazonServiceException e) {
				e.printStackTrace();
			} catch (AmazonClientException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private String getAWSFilename(String fileName) {
		return fileName.replace(" ", "+");
	}
	
	private String getFbUsername(String completeUserId) throws JsonParseException, JsonMappingException, IOException {
		String fbUserId = extractFbUserId(completeUserId);
		Map<String,String> vars = new HashMap<String,String>();
		vars.put("userId", fbUserId);
		
		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.getForObject(fbUserInfoURL, String.class, vars);
		System.out.println("here's your result: " + result);
		
		JsonNode rootNode = mapper.readValue(result, JsonNode.class);
		return rootNode.get("name").getTextValue();		
	}
	
	private String extractFbUserId(String userId) {
		int i = userId.indexOf("@");
		return userId.substring(0, i);
	}
	
	public static void main(String[] args) throws IOException, JobExecutionException, ConnectionException {
		ReadPhotosAndRecognizeFacesJob job = new ReadPhotosAndRecognizeFacesJob();
		job.execute(null);
	}
		
}
