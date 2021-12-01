package com.edmwat.switchlink.auth;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import com.edmwat.switchlink.appUser.AppUserRepository;
import com.edmwat.switchlink.appUser.AppUserService;
import com.edmwat.switchlink.appUser.Oauth2PrincipalUser;
import com.edmwat.switchlink.appUser.PrincipalUser;
import com.edmwat.switchlink.models.Account;
import com.edmwat.switchlink.models.GoogleOAuth2UserInfo;
import com.edmwat.switchlink.repo.CashRepository;
import com.edmwat.switchlink.repo.OidcUserRepository;
import com.edmwat.switchlink.services.CashService;

import lombok.AllArgsConstructor;

@Service 
@AllArgsConstructor 
public class CustomOidcUserService extends OidcUserService{
	
    private final CashService cashService;
	private final CashRepository cashRepo;
	private final OidcUserRepository oidcUserRepository;
	private final AppUserRepository appUserRepository;
	
    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        Map attributes = oidcUser.getAttributes();
        Oauth2PrincipalUser userInfo = new Oauth2PrincipalUser();
        userInfo.setEmail((String) attributes.get("email"));
        userInfo.setUserId((String) attributes.get("sub"));
        userInfo.setImageUrl((String) attributes.get("picture"));
        userInfo.setName((String) attributes.get("name"));
       // System.out.println("EMAIL: "+userInfo.getEmail() +" NAME: "+userInfo.getName() +" Image: "+userInfo.getImageUrl() +" id: "+userInfo.getId());
        try {
         updateUser(userInfo);
        }catch(NoSuchElementException e) {
        	System.out.println(e);      	
        }
      
        return oidcUser;
    }

   private void updateUser(Oauth2PrincipalUser userInfo) {
        Optional<Account> account = cashRepo.findAccountByUsernameAndAccNumber(userInfo.getEmail(), userInfo.getUserId());
        Optional<Oauth2PrincipalUser> oauthUser = oidcUserRepository.findByEmail(userInfo.getEmail());
        Optional<PrincipalUser> principalUser = appUserRepository.findByUsername(userInfo.getEmail());
        if(!principalUser.isPresent() && !oauthUser.isPresent()) {
        	oidcUserRepository.save(userInfo);
        }
        
        if(!account.isPresent()) {
           Account newAccount = new Account();
           newAccount.setUsername(userInfo.getEmail());
           //account.setImageUrl(userInfo.getImageUrl());
           newAccount.setAccName(userInfo.getName());
           newAccount.setAccNumber(userInfo.getUserId());
           newAccount.setBalance(0.0);
           cashService.saveAccount(newAccount);
        }
        account.get().setUsername(userInfo.getEmail());
        //account.setImageUrl(userInfo.getImageUrl());
        account.get().setAccName(userInfo.getName());
        account.get().setAccNumber(userInfo.getUserId());
        account.get().setBalance(0.0);
        cashService.updateAccount(account.get());
    }

}
