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
		Optional<Account> sourceAccount = null;
		Optional<Account> destAccount = null;
		String username ="";
		Object principal = SecurityContextHolder. getContext(). getAuthentication(). getPrincipal();
		if (principal instanceof UserDetails) {
			username = ((UserDetails)principal). getUsername();
		}
		if(username != "") {
			sourceAccount = cashRepository.findAccountByUsernameAndAccNumber(username,fundsTransfer.getSourceAcc());
			destAccount = cashRepository.findAccountByAccNumber(fundsTransfer.getDestinationAcc());
		
			if(!sourceAccount.isEmpty()) {
				Double accBalance = sourceAccount.get().getBalance();
				
				if(destAccount == null) {
					return "Destination account is not valid!";
				}
									
				if(fundsTransfer.getAmount() < accBalance) {
					Double srcAccNewBal = accBalance - fundsTransfer.getAmount();
					sourceAccount.get().setBalance(srcAccNewBal);
					cashRepository.save(sourceAccount.get());	
					Double destAccNewBal = destAccount.get().getBalance() + fundsTransfer.getAmount();
					destAccount.get().setBalance(destAccNewBal);
					cashRepository.save(destAccount.get());	
					
					return "Transfered "+fundsTransfer.getAmount()+" from "+fundsTransfer.getSourceAcc() +" To "+fundsTransfer.getDestinationAcc();	
					
				}else {
					return "Account "+fundsTransfer.getSourceAcc() +" does not have enough funds";
				}				
			}
			return "Account "+sourceAccount.get().getAccNumber()+" does not exist!";
		}
		return "User "+username+ "does not exist!";		
	}
	
	public String atmWithdrawal(AtmWithdrawal atmWithdrawal) {	
		
		Optional<Account> sourceAccount = null;
		Double atmBalance = 3000d;
		String username ="";
		
		Object principal = SecurityContextHolder. getContext(). getAuthentication(). getPrincipal();
		if (principal instanceof UserDetails) {
			username = ((UserDetails)principal). getUsername();
		}
		if(username != "") {
			sourceAccount = cashRepository.findAccountByUsernameAndAccNumber(username,atmWithdrawal.getSrcAcc());
		
			if(!sourceAccount.isEmpty()) {
				Double accBalance = sourceAccount.get().getBalance();
									
				if(atmWithdrawal.getAmount() < accBalance ) {
					if(atmWithdrawal.getAmount() > atmBalance) {
						return "ATM under maintainance!!";
					}
					Double srcAccNewBal = accBalance - atmWithdrawal.getAmount();
					sourceAccount.get().setBalance(srcAccNewBal);
					cashRepository.save(sourceAccount.get());	
					atmBalance -= atmWithdrawal.getAmount();
		
					return "Withdrawal of Ksh. "+ atmWithdrawal.getAmount() + " From "+atmWithdrawal.getSrcAcc() +" Successful!";	
					
				}else {
					return "You dont have enough funds to withdraw "+atmWithdrawal.getAmount();
				}				
			}			
			return "Accounts "+atmWithdrawal.getSrcAcc() + " does not exist!";
		}
		return "User does not exist!";				
	}	
}
