package com.edmwat.switchlink.api;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static java.util.Arrays.stream;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.edmwat.switchlink.appUser.AppUserService;
import com.edmwat.switchlink.appUser.PrincipalUser;
import com.edmwat.switchlink.models.Account;
import com.edmwat.switchlink.models.UserCred;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpHeaders;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController 
@AllArgsConstructor 
@Slf4j 
public class Auth {
	private final AuthenticationManager authManager;
	private final AppUserService appUserService;
	
	@GetMapping("/getAuth")
	public ResponseEntity<AuthObject> getAuth() throws IOException, InterruptedException {
		AuthObject ao = new AuthObject();
		ao.setUsername((String)SecurityContextHolder.getContext().getAuthentication().getPrincipal());

		System.out.println("/getAuth Authenticated!!!!! "+SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		return ResponseEntity.ok().body(ao);
	}
	@GetMapping("/token/refresh")
	public void getRefreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String authorizationHeader = request.getHeader("AUTHORIZATION");
		//System.out.println("Athorization header::::::::::::"+authorizationHeader);
		if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {		
			try {
				String refresh_token = authorizationHeader.substring("Bearer ".length());
				//System.out.println("refresh token::::::::::"+refresh_token);
				Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());				
				JWTVerifier verifier = JWT.require(algorithm).build();
				DecodedJWT decodedJwt = verifier.verify(refresh_token);
				String username = decodedJwt.getSubject();
				String s ="";
				PrincipalUser user = (PrincipalUser)appUserService.loadUserByUsername(username);
				
				//System.out.println("Referesh token method::::::::::::::::::::::::::::::::: "+user.getUsername());
				String access_token = JWT.create()
						.withSubject(user.getUsername())
						.withExpiresAt(new Date(System.currentTimeMillis() + (10 *60 * 1000)))
						.withIssuer(request.getRequestURL().toString())
						.withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
						.sign(algorithm);
				//System.out.println("Referesh token method:TOKEN IS::::::::::::::::::::::::: "+user.getUsername());
				response.setHeader("access_token", access_token);
				response.setHeader("refresh_token", refresh_token);
				Map<String,String> tokens = new HashMap<>();
				tokens.put("access_token", access_token);
				tokens.put("refresh_token", refresh_token);
				
				//System.out.println(to);
				response.setContentType(MediaType.APPLICATION_JSON_VALUE); 
				
				
				
				new ObjectMapper().writeValue(response.getOutputStream(),tokens);
				
			}catch(Exception ex) {
				Map<String,String> error = new HashMap<>();
				error.put("CustomAuthorizationFilter_Erro", ex.getMessage());
				response.setContentType(MediaType.APPLICATION_JSON_VALUE); 					
				new ObjectMapper().writeValue(response.getOutputStream(),error);
			}
		}else {
			throw new RuntimeException("Missing refresh token");
		}		
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
@Data 
@AllArgsConstructor
@NoArgsConstructor
class AuthObject{
	private String username;
}
