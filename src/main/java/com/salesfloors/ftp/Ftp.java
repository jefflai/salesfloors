package com.salesfloors.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;

public class Ftp {
	private String endpoint;
	private String username;
	private String password;
	FTPClient client;
	
	public Ftp(String endpoint, String username, String password) {
		this.endpoint = endpoint;
		this.username = username;
		this.password = password;
		client = new FTPClient();
	}
	
	/**
	 * 
	 * @return true if upload is successful
	 * @throws IOException 
	 * @throws SocketException 
	 */
	public boolean upload(String remotePath, File file) throws SocketException, IOException {
		boolean success = false;
		initializeClient();
		InputStream is = new FileInputStream(file);
		success = client.storeFile(remotePath, is);
		return success;
	}
	
	public FTPClient getClient() {
		return client;
	}
	
	private void initializeClient() throws SocketException, IOException {
		client.connect(endpoint);
		client.login(username, password);
	}

}
