package com.molokotech.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public abstract class Person {

	@Id
	private ObjectId id;
	private int age;
	private String name;

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
