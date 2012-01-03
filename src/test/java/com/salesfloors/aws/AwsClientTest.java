package com.salesfloors.aws;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

public class AwsClientTest {
	private final String testPropertiesFileName = "test.properties";

	private AwsClient aws;
	
	@BeforeClass
	public void setup() throws IOException {
		Properties prop = new Properties();
		prop.load(this.getClass().getClassLoader().getResourceAsStream(testPropertiesFileName));
		String access = prop.getProperty("aws.access");
		String secret = prop.getProperty("aws.secret");
		String bucket = prop.getProperty("aws.s3bucket");
		AwsConfig config = new AwsConfig(access, secret, bucket);
		aws = new AwsClient(config);
	}
	
	@Test
	public void testUpload() throws AmazonServiceException, AmazonClientException, InterruptedException, IOException {
		// upload test file to s3
		String testFileName = "simple.txt";
		File fileToUplaod = FileUtils.toFile(this.getClass().getClassLoader().getResource(testFileName));
		String testPrefix = "test";
		aws.uploadFileToS3(testPrefix, fileToUplaod);
		// download from s3
		String fullObjName = testPrefix + "/" + testFileName;
		S3Object obj = aws.getS3().getObject(new GetObjectRequest(aws.getConfig().getBucket(), fullObjName));
		// verify downloaded contents
		InputStream objInputStream = null;
		List<String> lines = null;
		try {
			objInputStream = obj.getObjectContent();
			lines = IOUtils.readLines(objInputStream);
		} finally {
			if (objInputStream != null) objInputStream.close();
		}
		
		Assert.assertEquals(lines.size(), "1");
		Assert.assertEquals(lines.get(0), "Hello");
		// delete from s3
		aws.getS3().deleteObject(aws.getConfig().getBucket(), fullObjName);
	}
	
}
