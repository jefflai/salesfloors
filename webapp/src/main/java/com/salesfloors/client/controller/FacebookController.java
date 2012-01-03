package com.salesfloors.client.controller;

import java.util.List;

import javax.inject.Inject;

import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class FacebookController {
	
	@Inject
	private ConnectionRepository connectionRepository;

	@Inject
	private Facebook facebook;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model model) {
		List<Reference> friends = facebook.friendOperations().getFriends();
		System.out.println(connectionRepository.getPrimaryConnection(Facebook.class).createData().getAccessToken());

		model.addAttribute("friends", friends);
		
		return "home";
	}
	
}
