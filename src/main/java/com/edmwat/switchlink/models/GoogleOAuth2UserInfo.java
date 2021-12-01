package com.edmwat.switchlink.models;

import lombok.Data;

@Data 
public class GoogleOAuth2UserInfo {
	private String name;
	private String email;
	private String imageUrl;
	private String id;
}
