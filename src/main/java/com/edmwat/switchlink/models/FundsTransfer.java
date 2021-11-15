package com.edmwat.switchlink.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class FundsTransfer {
	private String sourceAcc;
	private String destinationAcc;
	private Double amount;
}
