package com.edmwat.switchlink.api;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.edmwat.switchlink.models.TransactionResponse;
import com.edmwat.switchlink.services.CashService;

@RestController
@RequestMapping("/api")
public class CashController { 
	@Autowired
	private CashService cashService;
	
	@GetMapping("/all/accounts")
	public ResponseEntity<List<Account>> getAllUserAccounts() {			
		return ResponseEntity.ok().body(cashService.getAllUserAccounts());	
	}  	
	@GetMapping("/user/accounts")
	public ResponseEntity<List<Optional<Account>>> getUserAccounts() {	
		return ResponseEntity.ok().body(cashService.getUserAccounts());	
	}  

	@GetMapping("/balance/{accNo}")
	public ResponseEntity<Optional<Account>> getAccountBal(@PathVariable String accNo) {
		System.out.println("Acc no: "+accNo);
		return ResponseEntity.ok().body(cashService.getAccountBal(accNo));	
	} 
	@PostMapping("/transfer")
	public ResponseEntity<TransactionResponse> transferFunds(@RequestBody FundsTransfer fundsTransfer) {	
		return ResponseEntity.status(HttpStatus.OK).body(cashService.transferFunds(fundsTransfer));	
	}
	@PostMapping("/atmWithdraw")
	public ResponseEntity<TransactionResponse> atmWithdrawal(@RequestBody AtmWithdrawal atmWithdrawal) {	
	
		return ResponseEntity.status(HttpStatus.OK).body(cashService.atmWithdrawal(atmWithdrawal));		
	} 
	@PutMapping
	public ResponseEntity<Account> updateAccount(@RequestBody Account account) {		
		return ResponseEntity.ok().body(null);		
	}
}
