package com.edmwat.switchlink.filters;

import static java.util.Arrays.stream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j 
public class CustomAuthorizationFilter extends OncePerRequestFilter{

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if(request.getServletPath().equals("/auth/login/**") || request.getServletPath().equals("/token/refresh")) {
			filterChain.doFilter(request, response);
		}else {
			String authorizationHeader = request.getHeader("Authorization");
			if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {		
				try {
					String token = authorizationHeader.substring("Bearer ".length());
					Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());				
					JWTVerifier verifier = JWT.require(algorithm).build();
					DecodedJWT decodedJwt = verifier.verify(token);
					String username = decodedJwt.getSubject();
					String[] roles = decodedJwt.getClaim("roles").asArray(String.class);
					Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
					stream(roles).forEach(role->{
						authorities.add(new SimpleGrantedAuthority(role));
					});
					UsernamePasswordAuthenticationToken authToken = 
							new UsernamePasswordAuthenticationToken(username,null,authorities);		
					SecurityContextHolder.getContext().setAuthentication(authToken);
					filterChain.doFilter(request, response);
				}catch(Exception ex) {
					log.error("Error Loggin ",ex.getMessage());
					response.setStatus(403);
					Map<String,String> error = new HashMap<>();
					error.put("CustomAuthorizationFilter_Erro", ex.getMessage());
					response.setContentType(MediaType.APPLICATION_JSON_VALUE); 					
					new ObjectMapper().writeValue(response.getOutputStream(),error);
				}
			}else {
				filterChain.doFilter(request, response);
			}
		}
		
	}

}
