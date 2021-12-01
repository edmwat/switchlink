package com.edmwat.switchlink.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edmwat.switchlink.models.Account;

@Repository
public interface CashRepository extends JpaRepository<Account, Long> {
	
	Optional<Account> findAccountByUsernameAndAccNumber(String username,String accountNo);
	
	Optional<Account> findAccountByAccNumber(String accountNo);
	
	List<Optional<Account>> findAccountByUsername(String username);

}
