package com.edmwat.switchlink.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtmWithdrawal {
	private String srcAcc;
	private Double amount;

}
