package com.edmwat.switchlink.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FundsTransfer {
	private String sourceAcc;
	private String destinationAccString;
	private Double amount;
}
