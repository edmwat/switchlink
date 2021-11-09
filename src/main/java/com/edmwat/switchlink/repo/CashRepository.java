package com.edmwat.switchlink.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edmwat.switchlink.models.Account;

public interface CashRepository extends JpaRepository<Account, Long> {

}
