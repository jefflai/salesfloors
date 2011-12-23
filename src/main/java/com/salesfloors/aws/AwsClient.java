package com.salesfloors.aws;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;

public class AwsClient {
	
    private TransferManager tm;
    private AwsConfig config;
    private AmazonS3 s3;

    public AwsClient(AwsConfig config) {
        this.config = config;
        tm = new TransferManager(config);
        s3 = new AmazonS3Client(config);
    }
    
    
    public TransferManager getTransferManager() {
        return tm;
    }
    
    public AwsConfig getConfig() {
        return config;
    }
    
    public AmazonS3 getS3() {
        return s3;
    }
    
    public void uploadFileToS3(String objNamePrefix, File file) throws AmazonServiceException, AmazonClientException, FileNotFoundException, InterruptedException {
    	// aws object name
    	String objName = objNamePrefix + "/" + file.getName();
    	ObjectMetadata md = new ObjectMetadata();
    	md.setContentLength(file.getTotalSpace());
    	Upload upload = getTransferManager().upload(getConfig().getBucket(), objName,
                new FileInputStream(file), md);
    	upload.waitForCompletion();
    }

}
