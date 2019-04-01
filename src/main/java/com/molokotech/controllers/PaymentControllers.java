package com.molokotech.controllers;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.mercadopago.MP;
import com.mercadopago.MercadoPago;
import com.molokotech.model.Buyer;
import com.molokotech.model.QR;
import com.molokotech.model.User;
import com.molokotech.service.BuyerService;
import com.molokotech.service.QrService;
import com.molokotech.service.UserService;
import com.paypal.ipn.IPNMessage;

/* Previusly this code was entirelly in RestControllers, no changes were made, if fail just comeback to the original position */
@RestController
public class PaymentControllers {
	
	@Autowired
	QrService qrService;
	@Autowired
	JavaMailSender emailSender;
	@Autowired
	BuyerService buyerService;
	@Autowired
	UserService userService;
//
//	/* PayPal listener and sender of PrepaidQR code to new User */
//	@RequestMapping(value = "/cgi-bin/webscr", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.ALL_VALUE})
//	public ResponseEntity<?> listener(HttpServletRequest request, HttpServletRequest response) {
//
//		Map<String, String> configMap = new HashMap<>();
//		configMap.put("mode", "live");
//		configMap.put("acct1.UserName", "berlot83_api1.yahoo.com.ar");
//		configMap.put("acct1.Password", "DUU3SLX82NFKPSEQ");
//		configMap.put("acct1.Signature", "AVO4ngS4QK8KXIH04mEbcSuZHWY4AD2YGHkFDxfa1O13qrWw-RK31nTp");
//		
//		IPNMessage ipnlistener = new IPNMessage(request, configMap);
//		boolean isIpnVerified = ipnlistener.validate();
//		String transactionType = ipnlistener.getTransactionType();
//		
//		Map<String, String> map = ipnlistener.getIpnMap();
//		
//		System.out.println("******* IPN (name:value) pair : " + map + "  " + "######### TransactionType : "
//				+ transactionType + "  ======== IPN verified : " + isIpnVerified);
//		
//		System.out.println("");
//		System.out.println("");
//		String payerEmail = map.get("payer_email");
//		
//		System.out.println(payerEmail);
//		
//		/* Check if is verified */
//		if (isIpnVerified) {
//			System.out.println("Verified IPN");
//		/* End check */
//
//			/* Start checking, sending and reemplacing selledOnline java attribute*/
//			QR qr = null;
//			List<QR> list = qrService.findAllQR();
//			
//			if(list != null) {
//				for (int i = 0; i < list.size(); i++) {
//					if (list.get(i).getUsername() == null) {
//						qr = list.get(i);
//						System.out.println("This is the first null value to assign: " + list.get(i).getId());
//						break;
//					}else {
//						System.out.println("No Username attribute null into QR object");
//					}
//				}
//			}else {
//				System.out.println("List appears to be null");
//			}
//			/* End selecting 'En venta match' */
//
//			/* Send email with id start */
//			String idQR = qr.getId().toString();
//			
//			MimeMessagePreparator preparator = new MimeMessagePreparator() {
//				public void prepare(MimeMessage mimeMessage) throws Exception {
//					mimeMessage.setSubject("Código QR adquirido.");
//					mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(payerEmail));
//					mimeMessage.setFrom(new InternetAddress("info@molokotech.com"));
//					mimeMessage.setText("Gracias por tu compra, el id del QR es = " + idQR +", el mail asignado es "+ payerEmail +",  lo que tenés que hacer es ir a nuestra web, ingresar a Pet-QR ==> Activar un QR prepago ==> Ingresá el código que recibiste junto con el mail y listo, fijate de completar todo lo que puedas del formulario. Una vez completo Tocá el botón rojo y cuando te salga el QR probalo desde la PC, si sale todo bien tendrías que ver todos los datos que pusiste.");
//				}
//			};
//
//			try {
//				this.emailSender.send(preparator);
//			} catch (MailException ex) {
//				System.err.println(ex.getMessage());
//			}
//			
//			/* Set username into QR to stop resending other Users and to verify buyer*/
//			try {
//				User user = userService.findUserByEmail(payerEmail.trim());
//				if(user != null) {
//					qr.setUsername(user.getName());
//					qrService.createQR(qr);
//				}
//			}catch(Exception error) {
//				System.out.println("This User appears to be null");
//			}
//			/* Send email with id end */
//
//			/* Test upload buyer start */
//			try {
//				String payerName = map.get("first_name");
//				String payerLastname = map.get("last_name");
//				String payerAddressCountry = map.get("address_country");
//				String payerAddressCity = map.get("address_city");
//				String payerId = map.get("payer_id");
//				
//				Buyer buyer = new Buyer();
//				buyer.setName(payerName);
//				buyer.setLastname(payerLastname);
//				buyer.setEmail(payerEmail);
//				buyer.setAddressCountry(payerAddressCountry);
//				buyer.setAddressCity(payerAddressCity);
//				buyer.setiDprepaidQR(idQR);
//				buyer.setPayerId(payerId);
//				buyerService.createBuyer(buyer);
//			}catch(Exception error) {
//				System.out.println("Some error occurre, verify if the payment was succesfull and if this data was uploaded.");
//			}
//			/* Test upload buyer end */
//			
//		}		
//		else{
//			System.out.println("Not a valid IPN Request!");
//		}
//		return new ResponseEntity<>(HttpStatus.OK);
//	}
//	
	/* MercadoPago controllers */
	@RequestMapping(value = "/notifications", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<?> notifications(String topic, String id) throws JSONException, Exception {
		MercadoPago.SDK.setClientSecret("782c8fRMOn6dw6UbjlgC4WknXNj3ped3");
		MercadoPago.SDK.setClientId("8393146112599783");
		MercadoPago.SDK.configure("8393146112599783", "782c8fRMOn6dw6UbjlgC4WknXNj3ped3");
		String accessToken = MercadoPago.SDK.getAccessToken();
		System.out.println(accessToken);

		MP mp = new MP(accessToken);
		JSONObject json = mp.get("/v1/payments/"+id);
		
		if(topic.equals("payment")) {
			String email = json.getJSONObject("response").getJSONObject("payer").getString("email");
			System.out.println(email);

			
			//System.out.println(json.toString(4));
			
			/* Start checking, sending and reemplacing sellecdOnline java attribute*/
			QR qr = null;
			List<QR> list = qrService.findAllQR();
			
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getUsername() == null) {
					qr = list.get(i);
					System.out.println("First null Username value in QR object to assign a new User: " + list.get(i).getId());
					break;
				}else {
					System.out.println("No QR code with null values");
				}
			}
			/* End selecting 'En venta match' */

			/* Send email with id start */
			String idQR = qr.getId().toString();
			
			MimeMessagePreparator preparator = new MimeMessagePreparator() {
				public void prepare(MimeMessage mimeMessage) throws Exception {
					mimeMessage.setSubject("Código QR adquirido.");
					mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
					mimeMessage.setFrom(new InternetAddress("info@molokotech.com"));
					mimeMessage.setText("Gracias por tu compra, el id del QR es = " + idQR +", el mail asignado es "+ email);
				}
			};

			try {
				this.emailSender.send(preparator);
			} catch (MailException ex) {
				System.err.println(ex.getMessage());
			}
			/* Send email with id end */
			
			/* Set username into QR to stop resending other Users and to verify buyer*/
			try {
				User user = userService.findUserByEmail(email.trim());
				if(user != null) {
					qr.setUsername(user.getName());
					qrService.createQR(qr);
				}
			}catch(Exception error) {
				System.out.println("This User appears to be null");
			}
		}
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
}
