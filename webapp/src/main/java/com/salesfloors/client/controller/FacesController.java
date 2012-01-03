package com.salesfloors.client.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.salesfloors.client.TrainFaces;

//@Controller
public class FacesController {
//	@RequestMapping(value = "/faces/fb_oauth", method = RequestMethod.POST)
    public ModelAndView getOauthToken(@RequestParam(value="access_token") String oauthToken)
    {
		ModelAndView mav = new ModelAndView();
    	mav.addObject("oauth_token", oauthToken);    	
    	TrainFaces.oauthToken = oauthToken;
    	TrainFaces.trainFace(TrainFaces.desiredUserIds);	
    	mav.setViewName("fb_oauth_success");
    	return mav;
    }
	
//	@RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView callFB()
    {
		ModelAndView mav = new ModelAndView();
    	TrainFaces.getFbOauth();
    	mav.addObject("oauth_token", "hello");    	
    	mav.setViewName("fb_oauth_success");
    	return mav;
    }
}