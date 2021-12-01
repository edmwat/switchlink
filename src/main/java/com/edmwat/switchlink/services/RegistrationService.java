package com.edmwat.switchlink.services;

import org.springframework.stereotype.Service;

import com.edmwat.switchlink.appUser.AppUserRole;
import com.edmwat.switchlink.appUser.AppUserService;
import com.edmwat.switchlink.appUser.PrincipalUser;
import com.edmwat.switchlink.models.NewPrincipalUser;
import com.edmwat.switchlink.utilities.EmailValidator;

import lombok.AllArgsConstructor;

@Service 
@AllArgsConstructor 
public class RegistrationService {
	
	private final EmailValidator emailValidator;
	private final AppUserService appUserService;
	
	public String registerNewUser(PrincipalUser user){
		boolean isValidEmail = emailValidator.test(user.getUsername());
		
		if(!isValidEmail) {
			throw new IllegalStateException("Invalid email");
		}
		return appUserService.registerNewUser(user);
				
	}

}
