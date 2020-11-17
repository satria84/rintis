package com.astrapay.rintis.domain.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class QRISPaymentCreditDataResponse {
    private String customer_reference_number;
    private String forwarding_customer_reference_number;
    private String invoice_no;
    private String currency_code;
    private BigDecimal amount;
    private BigDecimal fee;
}
