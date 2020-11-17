package com.astrapay.rintis.domain.request;

import lombok.Getter;

import lombok.Setter;

@Getter
@Setter
public class CheckStatusDataMerchantRequest {

	String pan;
	String name;
	String city;
	String country_code;
	String merchant_criteria;
}
