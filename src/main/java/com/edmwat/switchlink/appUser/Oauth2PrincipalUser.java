package com.edmwat.switchlink.appUser;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@Entity
@NoArgsConstructor  
public class Oauth2PrincipalUser implements OAuth2User {
	@Id
	@SequenceGenerator(
			name="oauth_user",
			sequenceName="oauth_user",
			allocationSize=1
			)
	@GeneratedValue(
			strategy=GenerationType.SEQUENCE,
			generator="oauth_user"			
			)
	private Long id;
	private String name;
	private String email;
	private String imageUrl;
	private String userId;
	//private Map<String, Object> attributes = new HashMap<>();
	@Enumerated(EnumType.STRING)
	private AppUserRole roles;
	

	@Override
	public Map<String, Object> getAttributes() {
		return null;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
		return Collections.singletonList(authority);
	}

	@Override
	public String getName() {
		return name;
	}

}
