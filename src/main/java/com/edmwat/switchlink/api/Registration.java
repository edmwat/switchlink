package com.edmwat.switchlink.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edmwat.switchlink.appUser.PrincipalUser;
import com.edmwat.switchlink.appUser.AppUserRole;
import com.edmwat.switchlink.appUser.AppUserService;

import lombok.AllArgsConstructor;

@RestController 
@RequestMapping(path="/api")
@AllArgsConstructor 
public class Registration {
	private final AppUserService userService;
	
	
	@GetMapping("/{userId}")
	public String getUser(@PathVariable String userId) {
		return "Application works "+userId;
	}
	
	@PostMapping("/add")
	public String getUser(@RequestBody PrincipalUser appUser) {
		String usn ="";
		return userService.registerNewUser(new PrincipalUser(
				appUser.getUsername(),
				appUser.getPassword(),
				AppUserRole.USER,
				true,
				true,
				true,
				true) );
	}

}
