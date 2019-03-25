package com.molokotech.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.molokotech.model.QR;
import com.molokotech.model.Tutor;
import com.molokotech.model.User;
import com.molokotech.service.QrService;
import com.molokotech.service.UserService;
import com.molokotech.utilities.PrintName;
import com.molokotech.utilities.UserUtilities;

@Controller
//@RestController
public class DashboardController {
	
	/* To display username starts */
	@Autowired
	UserUtilities userUtilities;

	@Bean
	public UserUtilities userUtilitiesDashboard() {
		return new UserUtilities();
	}
	/* To display Username end */
	
	@Autowired
	UserService userService;
	@Autowired
	QrService qrService;
	
	@GetMapping("/edit-contact")
	public void editDataContact(@ModelAttribute Tutor tutor) {
		User user = userService.findUser(userUtilities.userName());
		if(user != null) {
			user.setTutor(tutor);
			userService.createUser(user);
		}else {
			System.out.println("It seems it user doesn't exist");
		}
	}
	
	@GetMapping("/id/{shortId}")
	public String seePublicData(@PathVariable String shortId, Model model) {
		QR qr = qrService.findBySHortId(shortId);
		User user = userService.findUser(userUtilities.userName());
		PrintName.printUser(model);
		String result = null;
		if(qr != null && user != null) {
			model.addAttribute("qr",qr);
			model.addAttribute("user", user);
			result = "id";
		}else {
			result = "error";
		}
		return result;
	}
	
}
