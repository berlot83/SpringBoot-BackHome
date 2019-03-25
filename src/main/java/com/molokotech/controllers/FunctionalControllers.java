package com.molokotech.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.google.zxing.WriterException;
import com.molokotech.model.QR;
import com.molokotech.service.QrService;
import com.molokotech.utilities.QRCodeGenerator;
import com.molokotech.utilities.TokenCreator;

@RestController
public class FunctionalControllers {

	@Autowired
	QrService qrService;
	
	@GetMapping("/create-qr")
	public @ResponseBody String createQrCode() {
		String result;
		QR qr = new QR();
		
		qr.setShortId(TokenCreator.createSpecialId());
		byte[] imageData = null;
		
		try {
			imageData = QRCodeGenerator.generateQRCodeImageToByte("https://return-home.com/id/" + qr.getShortId(), 300, 300);
		} catch (WriterException | IOException e) {
			e.printStackTrace();
		}
		
		String strBase64 = QRCodeGenerator.toBase64(imageData);
		qr.setStrBase64(strBase64);
		qr.setStatus(true);
		qr = qrService.createQR(qr);
		
		if(qr != null) {
			result = "Lookslike everything is ok, shortId ="+ qr.getShortId() ;
		}else {
			result = "Cannot create and inster QR into DB"; 
		}
		return result;
	}
	
}
