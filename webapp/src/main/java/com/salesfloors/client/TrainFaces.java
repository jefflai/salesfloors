package com.salesfloors.client;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

public class TrainFaces {

	public static String oauthToken = "AAADMH1YfpJwBAMNUYyNSefXE2kL2HkMhomBZAtmFOEbUi0eQsaZBDE2vP4yIeVeY5YRpOuIz94ZCx33MtRQPzDIlPbMlz8F3wALuXSEowZDZD";
	
	public static final String faceDotComApiKey = "eed2f515182570f4617551a8a7827188"; 
	public static final String faceDotComApiSecret = "de832406e48d700fd97956f98f800153"; 
	public static final String desiredUserIds = //"1224055"; // Jeff
										 		"517172868"; // Derek
	public static final String callbackUrl = "http://gentle-samurai-2258.herokuapp.com/";
		
	public static final String nameSpace = "facebook.com";
	public static final String userAuth = "fb_user:517172868,fb_oauth_token:" + oauthToken;
	
	public static final String faceDotComTrainURL = "http://api.face.com/faces/train.json?api_key={apiKey}&api_secret={apiSecret}&uids={userIds}&namespace={nameSpace}&user_auth={userAuth}&callback_url={callbackUrl}";
	//public static final String faceDotComRecognizeURL = ""
	
	public static final String fbClientId = "224434960966812";
	public static final String fbRedirectUri = "http://gentle-samurai-2258.herokuapp.com/faces/fb_oauth";
	
	public static final String facebookAuthURL = "https://www.facebook.com/dialog/oauth?client_id={fbClientId}&redirect_uri={fbRedirectUri}&response_type=token"; 
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//getFbOauth();		
		trainFace(desiredUserIds);
	}
	
	public static void getFbOauth() {
		Map<String,String> vars = new HashMap<String,String>();
		vars.put("fbClientId", fbClientId);
		vars.put("fbRedirectUri", fbRedirectUri);
		
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getForObject(facebookAuthURL, String.class, vars);		
	}
 
	public static void trainFace(String userIds) {
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
	
	private static void recognizeFaces() {
		
	}

}
