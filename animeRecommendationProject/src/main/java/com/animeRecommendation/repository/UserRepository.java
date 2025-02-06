package com.animeRecommendation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.animeRecommendation.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	boolean existsByName(String name);
	
	User findByName(String name);

}
