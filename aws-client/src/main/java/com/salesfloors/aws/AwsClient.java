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
import com.amazonaws.services.s3.model.PutObjectRequest;

public class AwsClient {
	
	public static final String bucketName = "FacePics";
	
    private AmazonS3 s3;

    public AwsClient() throws IOException {
        s3 = new AmazonS3Client(new PropertiesCredentials(this.getClass().getClassLoader().getResourceAsStream("aws.properties")));
    }
    
    public AmazonS3 getS3() {
        return s3;
    }
    
    public void uploadFileToS3(File file) throws AmazonServiceException, AmazonClientException, FileNotFoundException, InterruptedException {
    	PutObjectRequest por = new PutObjectRequest(bucketName, file.getName(), file);
    	por.setCannedAcl(CannedAccessControlList.PublicRead);
    	s3.putObject(por);
    }
    
    public void deleteFileOnS3(String fileName) throws AmazonServiceException, AmazonClientException, FileNotFoundException, InterruptedException {
    	s3.deleteObject(bucketName, fileName);
    }
}
