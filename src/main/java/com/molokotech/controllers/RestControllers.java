package com.molokotech.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.molokotech.model.QR;
import com.molokotech.model.User;
import com.molokotech.service.QrService;
import com.molokotech.service.UserService;
import com.molokotech.utilities.UserUtilities;

@RestController
public class RestControllers {

	
	@Autowired
	UserService userService;
	@Autowired
	QrService qrService;
	
	
	@GetMapping("/assign-qr")
	public @ResponseBody String assignQrToUser() {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUser(auth.getName());
		String result = null;
		
		List<QR> list = qrService.findAllQR();
		
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getUsername() == null) {
				QR qr = list.get(i);
				list.get(i).setUsername(user.getName());
				qrService.createQR(qr);
				result = "QR code finded and attached user, QR code id =  " + list.get(i).getShortId();
				System.out.println("QR code finded and attached user, QR code id =  " + list.get(i).getShortId()  );
			break;
			}else {
				result = "There is not any QR created with username null";
				System.out.println("There is not any QR created with username null");
			}
			
		}
		return result;
	}
	
}
