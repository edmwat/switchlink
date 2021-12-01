package com.edmwat.switchlink.api;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.edmwat.switchlink.models.Account;
import com.edmwat.switchlink.models.UserCred;

import org.springframework.http.HttpHeaders;

import lombok.AllArgsConstructor;

@RestController 
@AllArgsConstructor 
public class Auth {
	private final AuthenticationManager authManager;
	
	@GetMapping("/getAuth")
	public Object getAuth() throws IOException, InterruptedException {
		Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		System.out.println("/getAuth Authenticated!!!!! "+SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		return user;
	}
	@PostMapping("/authentikate")
	public String authMethodd(@RequestBody UserCred userCred) {
		try {
			UsernamePasswordAuthenticationToken authReq
			 = new UsernamePasswordAuthenticationToken(userCred.getUsername(), userCred.getPassword());
			
			Authentication auth = authManager.authenticate(authReq);
			
			SecurityContext sc = SecurityContextHolder.getContext();
			sc.setAuthentication(auth);	
			
			System.out.println("Authenticated!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! "+SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		}catch(BadCredentialsException e) {
			System.out.println("Exception "+e);
		}
		
		
		return "Authenticated "+userCred.getUsername();
		
	}	
	
	/*@RequestMapping("/user")
	@ResponseBody
	public String user(@AuthenticationPrincipal OAuth2User user) {	
		Map<String,Object> cred = user.getAttributes();
		String emailString = (String)cred.get("email");
		return emailString;
	}*/
}
