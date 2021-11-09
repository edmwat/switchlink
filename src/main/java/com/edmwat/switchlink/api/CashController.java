package com.edmwat.switchlink.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edmwat.switchlink.models.Account;
import com.edmwat.switchlink.models.AtmWithdrawal;
import com.edmwat.switchlink.models.FundsTransfer;

@RestController
@RequestMapping("/api")
public class CashController {
	
	@GetMapping("/balance")
	public ResponseEntity<Account> getAccountBal() {
		
		return ResponseEntity.ok().body(null);	
	}
	
	@PostMapping("/transfer")
	public ResponseEntity<FundsTransfer> transferFunds(@RequestBody FundsTransfer fundsTransfer) {
		
		return ResponseEntity.ok().body(null);	
	}
	
	@PostMapping("/atmWithdraw")
	public ResponseEntity<AtmWithdrawal> atmWithdrawal(@RequestBody AtmWithdrawal atmWithdrawal) {		
		return ResponseEntity.ok().body(null);		
	}
}
