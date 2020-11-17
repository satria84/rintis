package com.astrapay.rintis.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QRISPaymentDataMerchantRequest {
    private String pan;
    private String id;
    private String criteria;
    private String name;
    private String city;
    private String mcc;
    private String postal_code;
    private String country_code;
}
