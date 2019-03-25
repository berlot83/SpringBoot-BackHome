package com.molokotech.service;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.molokotech.model.QR;
import com.molokotech.repository.QrRepository;

@Service
public class QrService {

	@Autowired
	QrRepository qrRepository;
	
	public QR createQR(QR qr) {
		return qrRepository.save(qr);	
	}
	
	public QR findById(String id) {
		Optional<QR> opt = qrRepository.findById(id);
		QR qr = new QR();
		if (opt.isPresent()) {
			qr = opt.get();
		} else {
			System.out.println("There is not any QR");
		}
		return qr;
	}
	
	public QR findBySHortId(String shortId) {
		Optional<QR> opt = qrRepository.findByShortId(shortId);
		QR qr = new QR();
		if (opt.isPresent()) {
			qr = opt.get();
		} else {
			System.out.println("There is not any QR");
		}
		return qr;		
	}
	
	public List<QR> findAllQR(){
		return qrRepository.findAll();
	}
}
