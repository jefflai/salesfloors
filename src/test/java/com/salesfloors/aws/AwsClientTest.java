package com.salesfloors.aws;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

public class AwsClientTest {
	private AwsClient aws;
	
	@BeforeClass
	public void setup() throws IOException {
		aws = new AwsClient();
	}
	
	@Test
	public void testUpload() throws AmazonServiceException, AmazonClientException, InterruptedException, IOException {
		// upload test file to s3
		String testFileName = "simple.txt";
		File fileToUpload = FileUtils.toFile(this.getClass().getClassLoader().getResource(testFileName));
		
		String buckets = "";
		System.out.println("Listing buckets");
        for (Bucket bucket : aws.getS3().listBuckets()) {
        	buckets += bucket.getName();
            System.out.println(" - " + bucket.getName());
        }
		Assert.assertTrue(buckets.contains(AwsClient.bucketName));        
        
		System.out.println("Uploading a new object to S3 from a file\n");
		aws.uploadFileToS3(fileToUpload);
		
		// download from s3
		S3Object obj = aws.getS3().getObject(new GetObjectRequest(AwsClient.bucketName, testFileName));
		// verify downloaded contents
		InputStream objInputStream = null;
		List<String> lines = null;
		try {
			objInputStream = obj.getObjectContent();
			lines = IOUtils.readLines(objInputStream);
		} finally {
			if (objInputStream != null) objInputStream.close();
		}
		
		Assert.assertEquals(lines.size(), 1);
		Assert.assertEquals(lines.get(0), "Hello");
		// delete from s3
		aws.getS3().deleteObject(AwsClient.bucketName, testFileName);
	}
	
}
