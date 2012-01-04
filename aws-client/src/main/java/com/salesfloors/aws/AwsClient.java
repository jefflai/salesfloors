package com.salesfloors.aws;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

public class AwsClient {
	
	public static final String defaultBucketName = "FacePics";
	
    private AmazonS3 s3;

    public AwsClient() throws IOException {
        s3 = new AmazonS3Client(new PropertiesCredentials(this.getClass().getClassLoader().getResourceAsStream("aws.properties")));
    }
    
    public AmazonS3 getS3() {
        return s3;
    }
    
    public void uploadFileToS3(File file) throws AmazonServiceException, AmazonClientException, FileNotFoundException, InterruptedException {
    	uploadFileToS3(file, CannedAccessControlList.PublicRead);
    }
    
    public void uploadFileToS3(File file, CannedAccessControlList controlList) {
    	uploadFileToS3(file, controlList, defaultBucketName);
    }
    
    public void uploadFileToS3(File file, CannedAccessControlList controlList, String bucketName) {
    	PutObjectRequest por = new PutObjectRequest(bucketName, file.getName(), file);
    	por.setCannedAcl(controlList);
    	s3.putObject(por);
    }
    
    public S3Object readFileFromS3(String fileName) {
    	return s3.getObject(new GetObjectRequest(defaultBucketName, fileName));
    }
    
    public void deleteFileOnS3(String fileName) throws AmazonServiceException, AmazonClientException, FileNotFoundException, InterruptedException {
    	s3.deleteObject(defaultBucketName, fileName);
    }
}
