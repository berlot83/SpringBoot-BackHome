package com.molokotech.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class Role {
	@Id
	private ObjectId id;
	private String role;

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
