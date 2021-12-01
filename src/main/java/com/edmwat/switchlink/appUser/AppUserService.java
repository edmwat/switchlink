package com.edmwat.switchlink.appUser;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service  
@AllArgsConstructor 
public class AppUserService implements UserDetailsService{
	
	private final AppUserRepository appUserRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return appUserRepository.findByUsername(username)
				.orElseThrow(()-> new UsernameNotFoundException("User not found"));
	}
	
	
	public String registerNewUser(PrincipalUser user) {
		boolean userExist= appUserRepository
				.findByUsername(user.getUsername())
				.isPresent();
		
		if(userExist) {
			throw new IllegalStateException("email exist");
		}
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		
		appUserRepository.save(user);
		
		return "User Added";
	}
}