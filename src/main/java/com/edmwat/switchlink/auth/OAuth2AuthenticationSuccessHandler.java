package com.edmwat.switchlink.auth;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.edmwat.switchlink.appUser.Oauth2PrincipalUser;
import com.edmwat.switchlink.repo.OidcUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;

@Component 
@AllArgsConstructor 
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final OidcUserRepository oidcUserRepo;
	
	//private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
	
	  @Override
	    public void onAuthenticationSuccess(HttpServletRequest request,
	    		HttpServletResponse response, 
	    		Authentication authentication) throws IOException, ServletException {
		  
		  System.out.println("ON AUTHENTICATION SUCCESS");
		  String targetUrl = "http://localhost:4200/user";
		  if (response.isCommitted()) {
	            return;
	        }
	        DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
	        Map attributes = oidcUser.getAttributes();
	        String email = (String) attributes.get("email");
	        Oauth2PrincipalUser user = oidcUserRepo.findByEmail(email).get();


			Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
			String access_token = JWT.create()
					.withSubject(email)
					.withExpiresAt(new Date(System.currentTimeMillis() * 10 *60 * 1000))
					.withIssuer(request.getRequestURL().toString())
					.withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
					.sign(algorithm);
			
	        String redirectionUrl = UriComponentsBuilder.fromUriString(targetUrl)
	                .queryParam("auth_token", access_token)
	                .build().toUriString();
	        
	      //  new ObjectMapper().writeValue(response.getOutputStream(),access_token);
	        getRedirectStrategy().sendRedirect(request, response, redirectionUrl);
	    }
	  
	  
	  

}
