package com.molokotech.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.molokotech.model.Coordinates;
import com.molokotech.model.Lost;
import com.molokotech.model.QR;
import com.molokotech.model.Tutor;
import com.molokotech.model.User;
import com.molokotech.service.QrService;
import com.molokotech.service.UserService;
import com.molokotech.utilities.PrintName;
import com.molokotech.utilities.UserUtilities;
@Component
@Controller
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
	
	@GetMapping("/id/{shortId}")
	public String seePublicData(@PathVariable String shortId, Model model) {
		QR qr = qrService.findBySHortId(shortId);
		PrintName.printUser(model);
		String result = null;
		if(qr != null) {
			model.addAttribute("qr",qr);
			result = "id";
		}else {
			result = "error";
		}
		return result;
	}

	@PostMapping("/lost")
	public String prepareLostObject(@ModelAttribute QR qr, Model model) {
		model.addAttribute("qr", qr);
		PrintName.printUser(model);
		return "lost";
	}
	
	@PostMapping("/create-lost")
	public @ResponseBody void createLostPerson(@RequestParam String shortId, @ModelAttribute Lost lost) {
		QR qr = qrService.findBySHortId(shortId);
		if(qr != null) {
			qr.setLost(lost);
			qrService.createQR(qr);
		}else {
			System.out.println("QR appears to be null");
		}
	}
	
	@PostMapping("/tutor")
	public String prepareTutorObject(@ModelAttribute QR qr, Model model) {
		model.addAttribute("qr", qr);
		PrintName.printUser(model);
		return "tutor";
	}
	
	@PostMapping("/create-tutor")
	public @ResponseBody void createLostPerson(@RequestParam String shortId, @ModelAttribute Tutor tutor) {
		QR qr = qrService.findBySHortId(shortId);
		qr.setTutor(tutor);
		qrService.createQR(qr);
	}
	
	@PostMapping("/activate-qr")
	public @ResponseBody boolean activatePublicData(@RequestParam String shortId, @RequestParam boolean status) {
		QR qr = qrService.findBySHortId(shortId);
		if(qr != null) {
			qr.setStatus(status);
			qrService.createQR(qr);
		}else {
			System.out.println("Unable reach this QR code, should be a DB problem or param error type.");
		}
		return qr.isStatus();
	}
	
	/* Start Without use right now, solved with Thymeleaf variables */
	@GetMapping("/get-status")
	public @ResponseBody boolean getStatusQR(@RequestParam String shortId){
		boolean status = false;
		QR qr = qrService.findBySHortId(shortId);
		if(qr != null) {
			status = qr.isStatus();
			System.out.println(status);
		}else {
			System.out.println("Cannot reach this QR code, maybe it doesn't exist or just the parameter changes on js side or server side.");
		}
		return status;
	}
	/* End Without use right now, solved with Thymeleaf variables */
	
	@GetMapping("/coordinates/{shortId}")
	public String getAllCoordinates(@PathVariable String shortId, Model model) {
		PrintName.printUser(model);
		String result = null;
		
		if(shortId != null){
			QR qr = qrService.findBySHortId(shortId);
			if(qr != null) {
				if(qr.getCoordinates() != null) {
					List<Coordinates> coordinates = qr.getCoordinates();
					Collections.reverse(coordinates);
					model.addAttribute("coordinates", coordinates);
					result = "coordinates";
				}else {
					System.out.println("The coordinates apperas to be null");
					result = "coordinates";
				}
			}else {
				result = "coordinates";
				System.out.println("QR code appears to be null");
			}
		}else {
			System.out.println("shortId apperas to be null, cannot reach the QR code");
			result = "dashboard";
		}
	return result;
	}
	
}
