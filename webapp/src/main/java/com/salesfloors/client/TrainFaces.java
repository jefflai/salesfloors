package com.salesfloors.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.web.client.RestTemplate;

import com.salesfloors.aws.AwsClient;
import com.salesfloors.client.controller.FacebookController;

public class TrainFaces {

	public static final String faceDotComApiKey = "eed2f515182570f4617551a8a7827188"; 
	public static final String faceDotComApiSecret = "de832406e48d700fd97956f98f800153"; 
	public static final String desiredUserIds = "1224055@facebook.com," + // Jeff
										 		"517172868@facebook.com"; // Derek
	public static final String callbackUrl = "http://gentle-samurai-2258.herokuapp.com/";
		
	public static final String nameSpace = "facebook.com";
	private static String userAuth;
	
	public static final String faceDotComTrainURL = "http://api.face.com/faces/train.json?api_key={apiKey}&api_secret={apiSecret}&uids={userIds}&namespace={nameSpace}&user_auth={userAuth}&callback_url={callbackUrl}";
	public static final String faceDotComRecognizeURL = "http://api.face.com/faces/recognize.json?api_key={apiKey}&api_secret={apiSecret}&urls={photoUrl}&uids={userIds}&namespace=facebook.com&detector=Normal&attributes=all&user_auth={userAuth}";
	
	public static final String fbClientId = "224434960966812";
	public static final String fbRedirectUri = "http://gentle-samurai-2258.herokuapp.com/faces/fb_oauth";
	
	public static final String facebookAuthURL = "https://www.facebook.com/dialog/oauth?client_id={fbClientId}&redirect_uri={fbRedirectUri}&response_type=token"; 
	
	private AwsClient aws;
	
	public TrainFaces() throws IOException {
		String oauthToken = getFbOauth();
		userAuth = "fb_user:517172868,fb_oauth_token:" + oauthToken;
	}
	
	public String getFbOauth() throws IOException {
		aws = new AwsClient();
		InputStream objInputStream = null;
		List<String> lines = null;
		try {
			objInputStream = aws.readFileFromS3("facebook_token", FacebookController.fbTokenBucket).getObjectContent();
			lines = IOUtils.readLines(objInputStream);
		} finally {
			if (objInputStream != null) objInputStream.close();
		}
		
		assert lines.size() == 1;
		return lines.get(0);
	}
 
	public void trainFace(String userIds) {
		Map<String,String> vars = new HashMap<String,String>();
		vars.put("apiKey", faceDotComApiKey);
		vars.put("apiSecret", faceDotComApiSecret);
		vars.put("userIds", userIds);
		vars.put("nameSpace", nameSpace);
		vars.put("userAuth", userAuth);
		vars.put("callbackUrl", callbackUrl);
		
		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.getForObject(faceDotComTrainURL, String.class, vars);
		System.out.println("here's your result: " + result);
	}
	
	public String recognizeFaces(String photoUrl) {
		Map<String,String> vars = new HashMap<String,String>();
		vars.put("apiKey", faceDotComApiKey);
		vars.put("apiSecret", faceDotComApiSecret);
		vars.put("photoUrl", photoUrl);
		vars.put("userIds", desiredUserIds);
		vars.put("userAuth", userAuth);
		
		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.getForObject(faceDotComRecognizeURL, String.class, vars);
		System.out.println("here's your result: " + result);
		return result;
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// only call this when training user id's
		//trainFace(desiredUserIds);
		TrainFaces tf = new TrainFaces();
		tf.recognizeFaces("https://s3.amazonaws.com/FacePics/CustomerPhoto.tiff");
	}

}
