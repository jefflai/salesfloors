package org.salesfloors.recognizer;

import java.io.IOException;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.salesfloors.aws.AwsClient;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

/**
 * This class reads from AWS and calls face.com to recognize faces in the pictures
 * @author dtia
 *
 */

public class ReadPhotosAndRecognizeFacesJob implements Job {

	public static final String photoDirName = "/Users/dtia/Pictures/Automator Photos";
	public AwsClient aws = null;
	
	public ReadPhotosAndRecognizeFacesJob() throws IOException {
		aws = new AwsClient();		
	}
	
		
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		ObjectListing ol = aws.getS3().listObjects(AwsClient.bucketName);
		List<S3ObjectSummary> os = ol.getObjectSummaries();
		
		for(S3ObjectSummary obj : os) {
			// read file from S3
			S3Object photo = aws.readFileFromS3(obj.getKey());
			//photo.
			
			// call face.com api with photo
			
			
			// delete file from S3
		}
	}
		
}
