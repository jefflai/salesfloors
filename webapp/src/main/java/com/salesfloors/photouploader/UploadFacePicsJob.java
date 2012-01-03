package com.salesfloors.photouploader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.salesfloors.aws.AwsClient;

/**
 * This class uploads photo from the local directory to AWS S3
 * @author dtia
 *
 */

public class UploadFacePicsJob implements Job {

	public static final String photoDirName = "/Users/dtia/Pictures/Automator Photos";
	public AwsClient aws = null;
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @throws AmazonClientException 
	 * @throws AmazonServiceException 
	 */
	public static void main(String[] args) throws IOException, AmazonServiceException, AmazonClientException, InterruptedException {
				
	}

	public UploadFacePicsJob() throws IOException {
		aws = new AwsClient();		
	}
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		int dirSize = 0;
		File[] photos;
		
		// check files in dir
				File photoDir  = new File(photoDirName);
				
				if(photoDir.exists()) {
					photos = photoDir.listFiles();
					dirSize = photos.length;
					
					// upload files if dir is not empty
					if(dirSize > 0) {
						for(File photo : photos) {
							try {
								aws.uploadFileToS3(photo);
							} catch (AmazonServiceException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (AmazonClientException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							// delete photo afterwards
							photo.delete();
						}				
					}		
				}
		
	}

}
