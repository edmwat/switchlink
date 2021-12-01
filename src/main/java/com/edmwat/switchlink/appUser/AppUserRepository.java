package com.edmwat.switchlink.appUser;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<PrincipalUser,Long> {
	Optional<PrincipalUser> findByUsername(String email);
}
