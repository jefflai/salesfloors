package com.salesfloors.aws;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsyncClient;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.salesfloors.aws.model.FaceDotComTrainResponse;

public class AwsClient {
	
	public static final String bucketName = "FacePics";
	public static final String queueUrl = "";
	
    private AmazonS3 s3;
    private AmazonSQS sqs;
    private ObjectMapper mapper;

    public AwsClient() throws IOException {
    	PropertiesCredentials props = new PropertiesCredentials(this.getClass().getClassLoader().getResourceAsStream("aws.properties"));
        s3 = new AmazonS3Client(props);
        sqs = new AmazonSQSAsyncClient(props);
        
        mapper = new ObjectMapper();
    	mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    }
    
    public AmazonS3 getS3() {
        return s3;
    }
    
    public AmazonSQS getSqs() {
    	return sqs;
    }
    
    public PutObjectResult uploadFileToS3(File file) throws AmazonServiceException, AmazonClientException, FileNotFoundException, InterruptedException {
    	return uploadFileToS3(file, CannedAccessControlList.PublicRead);
    }
    
    public PutObjectResult uploadFileToS3(File file, CannedAccessControlList controlList) {
    	PutObjectRequest por = new PutObjectRequest(bucketName, file.getName(), file);
    	por.setCannedAcl(controlList);
    	return s3.putObject(por);
    }
    
    public void deleteFileOnS3(String fileName) throws AmazonServiceException, AmazonClientException, FileNotFoundException, InterruptedException {
    	s3.deleteObject(bucketName, fileName);
    }
    
    public SendMessageResult enqueueMessage(String msg) {
    	return getSqs().sendMessage(new SendMessageRequest(queueUrl, msg));
    }
    
    /**
     * Dequeues one message from queue and converts json String into @{FaceDotComTrainResponse}
     * @return @{FaceDotComTrainResponse}
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonParseException 
     */
	public FaceDotComTrainResponse dequeueMessage() throws JsonParseException, JsonMappingException, IOException {
        ReceiveMessageRequest request = new ReceiveMessageRequest(queueUrl).withMaxNumberOfMessages(1);
        List<Message> msg = getSqs().receiveMessage(request).getMessages();
        if (msg.size() == 0 ) {
            return null;
        } else {
        	FaceDotComTrainResponse response = mapper.readValue(msg.get(0).getBody(), FaceDotComTrainResponse.class);
            getSqs().deleteMessage(new DeleteMessageRequest().withQueueUrl(queueUrl)
                    .withReceiptHandle(msg.get(0).getReceiptHandle()));
            return response;
        }
    }
    
}
