package com.astrapay.rintis.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QRISPaymentDataCustomerRequest {
    private String pan;
    private String name;
    private String account_type;
}
