package com.salesfloors.aws;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsyncClient;


public class AwsClient {
	
	public static final String defaultBucketName = "FacePics";
	public static final String queueUrl = "https://queue.amazonaws.com/701065011543/PhotoQueue";
	
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
    	return uploadFileToS3(file, controlList, defaultBucketName);
    }
    
    public PutObjectResult uploadFileToS3(File file, CannedAccessControlList controlList, String bucketName) {
    	PutObjectRequest por = new PutObjectRequest(bucketName, file.getName(), file);
    	por.setCannedAcl(controlList);
    	return s3.putObject(por);
    }
    
    public S3Object readFileFromS3(String fileName) {
    	return readFileFromS3(fileName, defaultBucketName);
    }
    
    public S3Object readFileFromS3(String fileName, String bucketName) {
    	return s3.getObject(new GetObjectRequest(bucketName, fileName));
    }
    
    public void deleteFileOnS3(String fileName) throws AmazonServiceException, AmazonClientException, FileNotFoundException, InterruptedException {
    	s3.deleteObject(defaultBucketName, fileName);
    }
    
}
