package com.molokotech.controllers;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.molokotech.model.Coordinates;
import com.molokotech.model.QR;
import com.molokotech.service.QrService;
import com.molokotech.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;

@Component
@RestController
public class MailSenderUtilities {

	@Autowired
	public UserService userService;

	@Autowired
	public QrService qrService;

	/* Lib needed fot JavaMail */
	@Autowired
	public JavaMailSender emailSender;
	
	@Bean
	public ClassJavaMailSender javaMailSender() {
		return new ClassJavaMailSender();
	}
	/* Lib needed fot JavaMail */
	

	@PostMapping("/sendCoordinatesToMail")
	public void sendCoordinatesToMail(String latitude, String longitude, String mail, String dateTime, String shortId) {

		Coordinates coordinates = new Coordinates();
		coordinates.setLatitude(latitude);
		coordinates.setLongitude(longitude);
		coordinates.setDateTime(dateTime);
		List<Coordinates> list = new ArrayList<>();
		
		
		// String mapAddress =
		// "https://www.google.com.ar/maps/@"+latitude+","+longitude+"z"; Old and
		// ambicius without precision
		String mapAddress = "https://www.google.com.ar/maps/search/?api=1&query=" + latitude + "," + longitude;

		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				mimeMessage.setSubject("Coordenadas de la última lectura");
				mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(mail));
				mimeMessage.setFrom(new InternetAddress("info@molokotech.com"));
				mimeMessage.setText("EL QR de la mascota fue escaneada en:\n\nLatitud: " + latitude + "\nLongitud: "
						+ longitude + "\nDia y hora: " + dateTime + " Siga el siguiente link para ver en el mapa => "
						+ mapAddress);
			}
		};

		try {
			this.emailSender.send(preparator);
		} catch (MailException ex) {
			System.err.println(ex.getMessage());
		}
		
		System.out.println(shortId);
		QR qr = qrService.findBySHortId(shortId);
		qr.setLastLatitude(latitude);
		qr.setLastLongitude(longitude);
		
		if(qr.getCoordinates() == null) {
			list.add(coordinates);
			qr.setCoordinates(list);
		}else {
			qr.getCoordinates().add(coordinates);
			qr.setCoordinates(qr.getCoordinates());
		}
		qrService.createQR(qr);
	}
	
//	@GetMapping("/sendQrCodeToEmail")
//	public void sendQrCodeToEmail(PrepaidQR prepaidQR, User user) {
//
//		String id = prepaidQR.getId().toString();
//
//		MimeMessagePreparator preparator = new MimeMessagePreparator() {
//			public void prepare(MimeMessage mimeMessage) throws Exception {
//				mimeMessage.setSubject("Id del QR adquirido.");
//				mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
//				mimeMessage.setFrom(new InternetAddress("info@molokotech.com"));
//				mimeMessage.setText("EL id del QR es = " + id);
//			}
//		};
//
//		try {
//			this.emailSender.send(preparator);
//		} catch (MailException ex) {
//			System.err.println(ex.getMessage());
//		}
//	}

}
