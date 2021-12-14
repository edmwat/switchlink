package com.edmwat.switchlink.appUser;

import java.util.Random;

import javax.transaction.Transactional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.edmwat.switchlink.models.Account;
import com.edmwat.switchlink.services.CashService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service  
@AllArgsConstructor
@Slf4j 
public class AppUserService implements UserDetailsService{
	
	private final AppUserRepository appUserRepository;
	private final PasswordEncoder passwordEncoder;
	private final CashService cashService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return appUserRepository.findByUsername(username)
				.orElseThrow(()-> new UsernameNotFoundException("User not found"));
	}
	
	@Transactional 
	public String registerNewUser(PrincipalUser user) {
		String toReturn ="";
		try {
			boolean userExist= appUserRepository
					.findByUsername(user.getUsername())
					.isPresent();
			
			if(userExist) {		
				throw new IllegalArgumentException("Email already exist!!!!!!!!!!");
				//throw new IllegalStateException("email exist");
			}
			String encodedPassword = passwordEncoder.encode(user.getPassword());
			user.setPassword(encodedPassword);
			
			appUserRepository.save(user);
			Account account = new Account();
			Random random = new Random();
			account.setUsername(user.getUsername());
			account.setAccNumber(String.valueOf(random.nextInt(100000)+100000));
			account.setBalance(10000.00);
			account.setAccName(user.getUsername());
			cashService.saveAccount(account);
			System.out.println("User added!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			toReturn = "User Added";
			//return "User Added";
			
		}catch(IllegalArgumentException e) {
			toReturn = e.getMessage();
			log.error(toReturn);			
		}
		return toReturn;
	}
}