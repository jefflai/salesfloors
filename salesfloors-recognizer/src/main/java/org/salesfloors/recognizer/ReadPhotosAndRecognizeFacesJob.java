package org.salesfloors.recognizer;

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

import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.salesfloors.aws.AwsClient;
import com.salesfloors.client.TrainFaces;

/**
 * This class reads from AWS and calls face.com to recognize faces in the pictures
 * @author dtia
 *
 */

public class ReadPhotosAndRecognizeFacesJob implements Job {

	public static final String baseFacePhotoBucketUri = "https://s3.amazonaws.com/FacePics/";
	public static final String fbUserInfoURL = "https://graph.facebook.com/{userId}";
	
	public AwsClient aws;
	public ObjectMapper mapper;	
	public TrainFaces tf;
	
	public ReadPhotosAndRecognizeFacesJob() throws IOException {
		aws = new AwsClient();	
		mapper = new ObjectMapper();	
		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		tf = new TrainFaces();		
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
				JsonNode firstUidNode = uidsNode.path(0);
				uid = firstUidNode.get("uid").getTextValue();
				confidence = firstUidNode.get("confidence").getIntValue();
				
				System.out.println("uid: " + uid);
				System.out.println("confidence: " + confidence);
				
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// get username from fb id
			try {
				String userName = getFbUsername(uid);
				System.out.println("your username: " + userName);
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// call sfdc api
			
			
			// delete file from S3
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
	
	public static void main(String[] args) throws IOException, JobExecutionException {
		ReadPhotosAndRecognizeFacesJob job = new ReadPhotosAndRecognizeFacesJob();
		job.execute(null);
	}
		
}
