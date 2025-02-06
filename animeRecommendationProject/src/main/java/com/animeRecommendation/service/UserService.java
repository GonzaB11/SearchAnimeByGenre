package com.animeRecommendation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.animeRecommendation.model.User;
import com.animeRecommendation.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	public List<User> getUsers(){
		return userRepository.findAll();
	}
	
	public int getSizeUser() {
		return (int) userRepository.count();
	}

	public User getUserById(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("The user doesn't exist"));
	}
	
	public void addUser(User user) {
		boolean exists = userRepository.existsByName(user.getName());
		if (exists) {
	        throw new RuntimeException("The user already exists");
		}
		userRepository.save(user);
	}
	
	public void deleteUser(Long id) {
		if(!userRepository.existsById(id)) {
			throw new RuntimeException("The user doesn't exist");
		}
		userRepository.deleteById(id);
	}
	
	public User updateUser(Long id, User user) {
		User existingUser = userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("The user doesn't exist"));
		
		existingUser.setName(user.getName());			

		return userRepository.save(existingUser);
	}
	
	public User getUserByName(String name) {
	    return userRepository.findByName(name);
	}
}
