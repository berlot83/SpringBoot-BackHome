package com.molokotech.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.molokotech.model.QR;

public interface QrRepository extends MongoRepository<QR, String>{

	Optional<QR> findByShortId(String shortId);
	
}
