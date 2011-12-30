package com.salesfloors.client;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

public class TrainFaces {

	public static final String oauthToken = "AAADMH1YfpJwBAJgTMSknw0CrZAvrGBanuVI8KMPteFy4uUj6fBaHenpsjOS7zwyqZCZBltUW76YaPxIcqGHRO1wZAOteeZAIatlOMkFJ91wZDZD";
	
	public static final String apiKey = "eed2f515182570f4617551a8a7827188"; 
	public static final String apiSecret = "de832406e48d700fd97956f98f800153"; 
	public static final String desiredUserIds = //"1224055"; // Jeff
										 		"517172868"; // Derek
		
	public static final String nameSpace = "facebook.com";
	public static final String userAuth = "fb_user:517172868,fb_oauth_token:" + oauthToken;
	
	public static final String faceDotComTrainURL = "http://api.face.com/faces/train.json?api_key={apiKey}&api_secret={apiSecret}&uids={userIds}&namespace={nameSpace}&user_auth={userAuth}";
	//public static final String faceDotComRecognizeURL = ""
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		trainFace(desiredUserIds);		
	}
 
	private static void trainFace(String userIds) {
		Map<String,String> vars = new HashMap<String,String>();
		vars.put("apiKey", apiKey);
		vars.put("apiSecret", apiSecret);
		vars.put("userIds", userIds);
		vars.put("nameSpace", nameSpace);
		vars.put("userAuth", userAuth);
		
		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.getForObject(faceDotComTrainURL, String.class, vars);
		System.out.println("here's your result: " + result);
	}
	
	private static void recognizeFaces() {
		
	}

}
