package com.molokotech.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.molokotech.model.QR;
import com.molokotech.model.User;
import com.molokotech.service.QrService;
import com.molokotech.service.UserService;
import com.molokotech.utilities.PrintName;
import com.molokotech.utilities.UserUtilities;

@Controller
public class MainController {
	@Autowired
	UserService userService;
	@Autowired
	QrService qrService;
	@Autowired
	UserUtilities userUtilities;

	@Bean
	public UserUtilities userUtilitiesMain() {
		return new UserUtilities();
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model, String error, String logout) {
		if (error != null)
			model.addAttribute("errorMsg", "Usuario o Password incorrectos.");

		if (logout != null)
			model.addAttribute("msg", "Saliste.");
		PrintName.printUser(model);
		return "login"; /* login */
	}

	@GetMapping("/sign-up")
	public String greetingForm(Model model) {
		model.addAttribute("user", new User());
		PrintName.printUser(model);
		return "sign-up";
	}

	@PostMapping("/sign-up")
	public String greetingSubmit(@ModelAttribute User user, Model model) {
		/* Search existing users Start */
		PrintName.printUser(model);
		/* This works fine finding the null object by name */
		if (userService.findUser(user.getName()) == null) {
			/*
			 * For some reason use.getEmamail not return null, so i needed to take
			 * (user.getEmail()).getEmail to take the null value
			 */
			if (userService.findUserByEmail(user.getEmail()).getEmail() == null) {
				try {
					String[] authorities = { "ROLE_USER" };
					user.setAuthorities(authorities);
					userService.createUser(user);

				} catch (Exception e) {
					System.out.println("Entró en exceptción");
					System.out.println(e.getMessage());
					return "sign-up";
				}
				/*
				 * End send email confirmation to admins, delete if exception could be Throw for
				 * out of space in email container or other
				 */

				return "login";

			} else {
				model.addAttribute("errorDuplicateEmailMsg", "Ese mail ya está registrado");
				System.out.println("parece que el mail no es null");
				return "sign-up";
			}
		} else {
			model.addAttribute("errorMsg", "Ese usuario ya está registrado");
			System.out.println("parece que el usuario no es null");
			return "sign-up";
		}
		/* Search existing users End */
	}
	/* End Sign-up */

	@RequestMapping("/")
	public String firstIndex(Model model) {
		PrintName.printUser(model);
		return "index";
	}

	@RequestMapping("/index")
	public String secondIndex(Model model) {
		PrintName.printUser(model);
		return "index";
	}

	@RequestMapping("/logout")
	public String logout(Model model) {
		PrintName.printUser(model);
		return "logout";
	}

	@RequestMapping("/dashboard")
	public String admin(Model model) {
		PrintName.printUser(model);

		List<QR> list = qrService.findAllQR();
		List<QR> qrListUser = new ArrayList<>();

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getUsername() != null) {
				if (list.get(i).getUsername().equals(userUtilities.userName())) {
					qrListUser.add(list.get(i));
					System.out.println(list.get(i).getShortId());
				} else {
					System.out.println("It's not assosiated to a QR");
				}
			} else {
				System.out.println("Username appears to be null");
			}
			model.addAttribute("list", qrListUser);
		}

		return "dashboard";
	}

	@RequestMapping("/user")
	public String user(Model model) {
		PrintName.printUser(model);
		return "user";
	}
}
