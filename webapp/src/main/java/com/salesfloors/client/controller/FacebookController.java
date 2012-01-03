package com.salesfloors.client.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.salesfloors.aws.AwsClient;

@Controller
public class FacebookController {
	
	@Inject
	private ConnectionRepository connectionRepository;

	@Inject
	private Facebook facebook;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model model) throws IOException, AmazonServiceException, AmazonClientException, InterruptedException {
		List<Reference> friends = facebook.friendOperations().getFriends();
		storeTokenInS3();

		model.addAttribute("friends", friends);
		
		return "home";
	}
	
	private void storeTokenInS3() throws IOException, AmazonServiceException, AmazonClientException, InterruptedException {
		String token = connectionRepository.getPrimaryConnection(Facebook.class).createData().getAccessToken();
		AwsClient aws = new AwsClient();
		File file = new File("target/facebook_token");
		FileUtils.write(file, token);
		aws.uploadFileToS3(file, CannedAccessControlList.Private);
	}
	
}
