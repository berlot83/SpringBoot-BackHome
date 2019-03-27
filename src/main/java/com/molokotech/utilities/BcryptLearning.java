package com.molokotech.utilities;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class BcryptLearning {
	public static void main(String[] args) {
		System.out.println(BCrypt.checkpw("1234", "$2a$10$AJNQwIMUTIbHhhPzPH.dre58HnNiKTZ2LsVBXLrsQFB6ZD7CM0Mtu"));
	}
}
