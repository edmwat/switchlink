package com.edmwat.switchlink.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edmwat.switchlink.appUser.Oauth2PrincipalUser;

public interface OidcUserRepository extends JpaRepository<Oauth2PrincipalUser, Long>{
	Optional<Oauth2PrincipalUser> findByEmail(String email);
	
}
