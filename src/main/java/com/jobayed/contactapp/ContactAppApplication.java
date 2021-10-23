package com.jobayed.contactapp;

import com.jobayed.contactapp.auth_user.domain.Role;
import com.jobayed.contactapp.auth_user.domain.User;
import com.jobayed.contactapp.auth_user.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;

@SpringBootApplication
public class ContactAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContactAppApplication.class, args);
	}


//	@Bean
//	CommandLineRunner run(UserService userService){
//		return args->{
//			userService.saveRole(new Role(null,"ROLE_ADMIN"));
//			userService.saveRole(new Role(null,"ROLE_USER"));
//
//			userService.saveUser(new User(null,"Jobayed Ullah","jobayed","1234",new ArrayList<>()));
//			userService.saveUser(new User(null,"Altaf Mahmood","altaf",  "1234",new ArrayList<>()));
//			userService.saveUser(new User(null,"Jasim Uddin","jasim",  "1234",new ArrayList<>()));
//
//			userService.assignRole("jobayed","ROLE_ADMIN");
//			userService.assignRole("altaf",  "ROLE_USER");
//			userService.assignRole("jasim",  "ROLE_USER");
//
//
//		};
//	}

}
