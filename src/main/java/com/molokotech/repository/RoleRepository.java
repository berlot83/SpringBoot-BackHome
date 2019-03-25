package com.molokotech.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.molokotech.model.Role;

public interface RoleRepository extends MongoRepository<Role, String> {
	Role findByRole(String role);
}
