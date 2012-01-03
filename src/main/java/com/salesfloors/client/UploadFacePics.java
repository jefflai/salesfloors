package com.salesfloors.client;

import java.io.File;
import java.io.IOException;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.salesfloors.aws.AwsClient;

/**
 * This class uploads photo from the local directory to AWS S3
 * @author dtia
 *
 */

public class UploadFacePics {

	public static final String photoDirName = "/Users/dtia/Pictures/Automator Photos";
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @throws AmazonClientException 
	 * @throws AmazonServiceException 
	 */
	public static void main(String[] args) throws IOException, AmazonServiceException, AmazonClientException, InterruptedException {
		
		int dirSize = 0;
		AwsClient aws = new AwsClient();
		File[] photos;
		
		// check files in dir
		File photoDir  = new File(photoDirName);
		
		if(photoDir.exists()) {
			photos = photoDir.listFiles();
			dirSize = photos.length;
			
			// upload files if dir is not empty
			if(dirSize > 0) {
				for(File photo : photos) {
					aws.uploadFileToS3(photo);
					
					// delete photo afterwards
					photo.delete();
				}				
			}		
		}
		
		
	}

}
