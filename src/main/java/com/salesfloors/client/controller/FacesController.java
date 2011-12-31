package com.salesfloors.client.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FacesController {
	@RequestMapping(value = "/faces/fb_oauth", method = RequestMethod.POST)
    public ModelAndView getOauthToken(@RequestParam(value="access_token") String oauthToken)
    		 {

		ModelAndView mav = new ModelAndView();
    	mav.addObject("oauth_token", oauthToken);    	
    	mav.setViewName("random_view");
    	
    	return mav;
    }
}
