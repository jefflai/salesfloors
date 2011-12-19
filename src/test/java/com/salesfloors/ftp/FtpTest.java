package com.salesfloors.ftp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.List;
import java.util.Properties;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class FtpTest {
	
	private String endpoint;
	private String username;
	private String password;
	
	private final String testPropertiesFileName = "test.properties";
	
	@BeforeClass
	public void setup() throws IOException {
		Properties prop = new Properties();
		prop.load(this.getClass().getClassLoader().getResourceAsStream(testPropertiesFileName));
		endpoint = prop.getProperty("ftp.endpoint");
		username = prop.getProperty("ftp.username");
		password = prop.getProperty("ftp.password");
	}
	
	@Test
	public void testUpload() throws SocketException, IOException {
		String testFileName = "simple.txt";
		File fileToUplaod = FileUtils.toFile(this.getClass().getClassLoader().getResource(testFileName));
		
		Ftp client = null;
		try {
			client = new Ftp(endpoint, username, password);
			String testPath = "salesfloors-images/" + testFileName;
			boolean success = client.upload(testPath, fileToUplaod);
			Assert.assertTrue(success);
			InputStream download = null;
			List<String> lines = null;
			try {
				download = client.getClient().retrieveFileStream(testPath);
				lines = IOUtils.readLines(download);
			} finally {
				if (download != null) download.close();
			}
			
			Assert.assertEquals(1, lines.size());
			Assert.assertEquals("Hello",lines.get(0));
			
			client.getClient().deleteFile(testPath);
		} finally {
			if (client != null) client.getClient().disconnect();
		}
		
	}
	
	

}
