package com.edmwat.switchlink.models;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component 
@AllArgsConstructor 
@NoArgsConstructor
@Data 
public class TransactionResponse {
	private String message;
}
