package com.edmwat.switchlink.api;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edmwat.switchlink.models.Account;
import com.edmwat.switchlink.models.AtmWithdrawal;
import com.edmwat.switchlink.models.FundsTransfer;
import com.edmwat.switchlink.services.CashService;

@RestController
@RequestMapping("/api")
public class CashController { 
	@Autowired
	private CashService cashService;
	 
	@GetMapping("/balance/{accNo}")
	public ResponseEntity<Optional<Account>> getAccountBal(@PathVariable String accNo) {		
		return ResponseEntity.ok().body(cashService.getAccountBal(accNo));	
	} 
	@PostMapping("/transfer")
	public ResponseEntity<String> transferFunds(@RequestBody FundsTransfer fundsTransfer) {		
		return ResponseEntity.ok().body(cashService.transferFunds(fundsTransfer));	
	}
	
	@PostMapping("/atmWithdraw")
	public ResponseEntity<String> atmWithdrawal(@RequestBody AtmWithdrawal atmWithdrawal) {		
		return ResponseEntity.ok().body(cashService.atmWithdrawal(atmWithdrawal));		
	} 
	@PutMapping
	public ResponseEntity<Account> updateAccount(@RequestBody Account account) {		
		return ResponseEntity.ok().body(null);		
	}
}
