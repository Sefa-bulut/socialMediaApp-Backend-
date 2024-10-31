package com.project.questapp.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.project.questapp.entities.User;
import com.project.questapp.repos.UserRepository;
import com.project.questapp.responses.UserResponse;
import com.project.questapp.services.UserService;

@RestController
//usersa gelen istekleri yönlendirmek için ana root olarak /users kullandık
@RequestMapping("/users")
public class UserController {

	private UserService userService;
	
	//constructor injection (Spring uygulamalarında bağımlılık yönetimini etkili
	//bir şekilde sağlamak için yaygın olarak kullanılan bir tekniktir.)
	public UserController(UserService userService) {
		//arka planda spring boot getirdiği bean'i bizim repository'mize atıyacak
		//Bean, Spring Boot uygulamalarında nesnelerin yönetimi için kullanılan bir kavramdır.
		//Spring, bu nesnelerin yaşam döngüsünü yönetir, yani nesnelerin ne zaman oluşturulacağını,
		//ne zaman güncelleneceğini ve ne zaman yok edileceğini belirler.
		this.userService = userService;
	}
	
	@GetMapping
	public List<User> getAllUsers(){
		return userService.getAllUsers();
	}
	
	@PostMapping
	public User createUser(@RequestBody User newUser) {
		return userService.saveOneUser(newUser);
	}
	
	//verdiğimiz pathi sınıfın başında yazdığımız ana pathe ekliyor
	@GetMapping("/{userId}")
	public UserResponse getOneUser(@PathVariable Long userId) {
		//custom exception
		return new UserResponse(userService.getOneUser(userId));
	}
	
	/*
	@PutMapping("/{userId}")
	public User updateOneUser(@PathVariable Long userId, @RequestBody User newUser) {
		return userService.updateOneUser(userId, newUser);
	}
	*/
	
	@PutMapping("/{userId}")
	public ResponseEntity<Void> updateOneUserPatch(@PathVariable Long userId, @RequestBody User newUser) {
		User user = userService.updateOneUser(userId, newUser);
		if(user != null) 
			return new ResponseEntity<>(HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

	}
	
	
	@DeleteMapping("/{userId}")
	public void deleteOneUser(@PathVariable Long userId) {
		userService.deleteOne(userId);
	}
	
	@GetMapping("/activity/{userId}")
	public List<Object> getUserActivity(@PathVariable Long userId){
		return userService.getUserActivity(userId);
	}
	
	
	
}
