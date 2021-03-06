
package com.edmwat.switchlink.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edmwat.switchlink.models.Account;
import com.edmwat.switchlink.models.AccountTransactions;
import com.edmwat.switchlink.models.AtmWithdrawal;
import com.edmwat.switchlink.models.FundsTransfer;
import com.edmwat.switchlink.models.GoogleOAuth2UserInfo;
import com.edmwat.switchlink.models.TransactionResponse;
import com.edmwat.switchlink.repo.CashRepository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Service
public class CashService {
	private TransactionResponse transResponse;
	private final CashRepository cashRepository;
	
	public CashService(CashRepository cashRepository,
			TransactionResponse transResponse) {
		this.cashRepository= cashRepository;
		this.transResponse =transResponse;
	}
	public List<Account> getAllUserAccounts() {	
		
		return this.cashRepository.findAll();
		
	}
	
	public List<Optional<Account>> getUserAccounts() {
		List<Optional<Account>> userAccountOptional = null;
		//String username ="";
		
		String username = (String)SecurityContextHolder. getContext(). getAuthentication(). getPrincipal();
		//username = ((UserDetails)principal).getUsername();
		if(username != null) {
			userAccountOptional = cashRepository.findAccountByUsername(username);
		}
		
		/*if (principal instanceof UserDetails) {
			System.out.println("in if statement: ");
					
			userAccountOptional = cashRepository.findAccountByUsername(username);
			System.out.println("USERNAME: "+username);
		} 
		else if (principal instanceof OAuth2User) {
			OAuth2User user = (OAuth2User) principal;
			
			Map attributes = user.getAttributes();
			String email= (String) attributes.get("email");
			userAccountOptional = cashRepository.findAccountByUsername(email);
			
			//System.out.println("in if statement: OAuth2User "+email);			
		} */
		return userAccountOptional;
	}
	
	public Optional<Account> getAccountBal(String accNo) {
		Optional<Account> userAccountOptional = null;
		String username ="";
		
		username = (String) SecurityContextHolder. getContext(). getAuthentication(). getPrincipal();
		if (username != null) {
			//username = ((UserDetails)principal). getUsername();			
			userAccountOptional = cashRepository.findAccountByUsernameAndAccNumber(username,accNo);
			//System.out.println("USERNAME: "+username+" ACCNO: "+accNo);
			
		}/*else if(principal instanceof OAuth2User) {
			OAuth2User user = (OAuth2User) principal;		
			Map attributes = user.getAttributes();
			String email= (String) attributes.get("email");
			userAccountOptional = cashRepository.findAccountByUsernameAndAccNumber(email,accNo);
			
		}*/
		
		return userAccountOptional;
	}
	
	public void saveAccount(Account account) {
		cashRepository.save(account);
	}
	
	public void updateAccount(Account account) {
		/*Optional<Account> accExist = cashRepository.findAccountByUsernameAndAccNumber(account.getUsername(), account.getAccNumber());
		if(!accExist.isPresent()) {
			return;
		}*/
		//accExist.get().setUsername(account.getUsername());
		//accExist.get().setAccName(account.getAccName());
		//accExist.get().setBalance(account.getBalance());
		//accExist.get().setUsername(account.getUsername());
		cashRepository.save(account);
	}
	
	@Transactional
	public TransactionResponse transferFunds(FundsTransfer fundsTransfer) {
		Optional<Account> sourceAccount = null;
		Optional<Account> destAccount = null;
		String username ="";
		 username = (String)SecurityContextHolder. getContext(). getAuthentication(). getPrincipal();
	
		if(username != "") {
			sourceAccount = cashRepository.findAccountByUsernameAndAccNumber(username,fundsTransfer.getSourceAcc());
			destAccount = cashRepository.findAccountByAccNumber(fundsTransfer.getDestinationAcc());
		
			if(!sourceAccount.isEmpty()) {
				Double accBalance = sourceAccount.get().getBalance();
				
				if(destAccount.isEmpty()) {
					transResponse.setMessage("Destination account is not valid!");
					 return transResponse;
				}
									
				if(fundsTransfer.getAmount() <= accBalance) {
					Double srcAccNewBal = accBalance - fundsTransfer.getAmount();
					sourceAccount.get().setBalance(srcAccNewBal);
					/*sourceAccount.get().getAccountTransactions()
						.add(new AccountTransactions());*/
					cashRepository.save(sourceAccount.get());
					
					Double destAccNewBal = destAccount.get().getBalance() + fundsTransfer.getAmount();
					destAccount.get().setBalance(destAccNewBal);
					cashRepository.save(destAccount.get());	
					transResponse.setMessage("Transfered "+fundsTransfer.getAmount()+" from "+fundsTransfer.getSourceAcc() +" To "+fundsTransfer.getDestinationAcc());
					 return transResponse;
				}else {
					transResponse.setMessage("Account "+fundsTransfer.getSourceAcc() +" does not have enough funds");
					 return transResponse;
				}				
			}else
				transResponse.setMessage("Account "+sourceAccount.get().getAccNumber()+" does not exist!");
			    return transResponse;
		}
			transResponse.setMessage("User "+username+ "does not exist!");	
			return transResponse;
	}
	
	@Transactional
	public TransactionResponse atmWithdrawal(AtmWithdrawal atmWithdrawal) {	
		//TransactionResponse transResponse = new TransactionResponse();
		 
		Optional<Account> sourceAccount = null;
		Double atmBalance = 5000d;
		
		String username = (String) SecurityContextHolder. getContext(). getAuthentication(). getPrincipal();
		
		if(username != "") {
			sourceAccount = cashRepository.findAccountByUsernameAndAccNumber(username,atmWithdrawal.getSrcAcc());
		
			if(!sourceAccount.isEmpty()) {
				Double accBalance = sourceAccount.get().getBalance();
									
				if(atmWithdrawal.getAmount() < accBalance ) {
					System.out.println("ATM BALANCE::BEFORE::::::::"+atmBalance);
					if(atmWithdrawal.getAmount() > atmBalance) {
						System.out.println("Withdraw amt:"+atmWithdrawal.getAmount() +" atm");
						 transResponse.setMessage("ATM under maintainance!!");
						 return transResponse;
					}
					Double srcAccNewBal = accBalance - atmWithdrawal.getAmount(); 
					sourceAccount.get().setBalance(srcAccNewBal);
					cashRepository.save(sourceAccount.get());	
					atmBalance -= atmWithdrawal.getAmount();
					System.out.println("ATM BALANCE after::::::::::"+atmBalance);

					transResponse.setMessage("Withdrawal of Ksh. "+ atmWithdrawal.getAmount() + " From "+atmWithdrawal.getSrcAcc() +" Successful!");
					return transResponse;
					
				}else {
					transResponse.setMessage("You dont have enough funds to withdraw "+atmWithdrawal.getAmount());	
					return transResponse;
				}				
			}else			
				transResponse.setMessage("Accounts "+atmWithdrawal.getSrcAcc() + " does not exist!");
			    return transResponse;
		}
		  transResponse.setMessage("User does not exist!");		
		return transResponse;
		
	}	
}

