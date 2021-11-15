package com.edmwat.switchlink.services;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edmwat.switchlink.models.Account;
import com.edmwat.switchlink.models.AtmWithdrawal;
import com.edmwat.switchlink.models.FundsTransfer;
import com.edmwat.switchlink.repo.CashRepository;

@Service
public class CashService {
	
	private final CashRepository cashRepository;
	
	public CashService(CashRepository cashRepository) {
		this.cashRepository= cashRepository;
	}

	public Optional<Account> getAccountBal(String accNo) {
		Optional<Account> userAccountOptional = null;
		String username ="";
		
		Object principal = SecurityContextHolder. getContext(). getAuthentication(). getPrincipal();
		if (principal instanceof UserDetails) {
			username = ((UserDetails)principal). getUsername();			
			userAccountOptional = cashRepository.findAccountByUsernameAndAccNumber(username,accNo);
		} 		
		return userAccountOptional;
	}
	
	public void saveAccount(Account account) {
		cashRepository.save(account);
	}
	
	public void updateAccount(Account account) {
		cashRepository.save(account);
	}
	
	@Transactional
	public String transferFunds(FundsTransfer fundsTransfer) {
		Account sourceAccount = null;
		Account destAccount = null;
		String username ="";
		Object principal = SecurityContextHolder. getContext(). getAuthentication(). getPrincipal();
		if (principal instanceof UserDetails) {
			username = ((UserDetails)principal). getUsername();
		}
		if(username != "") {
			sourceAccount = cashRepository.findAccountByUsernameAndAccNumber(username,fundsTransfer.getSourceAcc()).get();
			destAccount = cashRepository.findAccountByAccNumber(fundsTransfer.getDestinationAcc()).get();
		
			if(sourceAccount != null) {
				//Account theAccount = sourceAccount.get();
				Double accBalance = sourceAccount.getBalance();
				
				if(destAccount == null) {
					return "Destination account is not valid!";
				}
									
				if(fundsTransfer.getAmount() < accBalance) {
					Double srcAccNewBal = accBalance - fundsTransfer.getAmount();
					sourceAccount.setBalance(srcAccNewBal);
					cashRepository.save(sourceAccount);	
					Double destAccNewBal = destAccount.getBalance() + fundsTransfer.getAmount();
					destAccount.setBalance(destAccNewBal);
					cashRepository.save(destAccount);	
					
					return "Funds Transfer successful";	
					
				}else {
					return "You dont have enough funds";
				}				
			}			 
		}
		return "Accounts does not exist!";		
	}
	
	public AtmWithdrawal atmWithdrawal(AtmWithdrawal atmWithdrawal) {		
		return null;		
	}
	
}
