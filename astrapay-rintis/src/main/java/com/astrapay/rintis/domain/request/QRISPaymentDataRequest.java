package com.astrapay.rintis.domain.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class QRISPaymentDataRequest {
    private String date_time;
    private String customer_reference_number;
    private String authorization_id;
    private String currency_code;
    private BigDecimal amount;
    private BigDecimal fee;
    private String issuer_nns;
    private String national_mid;
    private String additional_data;
    private String terminal_label;
    private String currency_number;
    private String additional_data_national;
    private String qris_type;
    private QRISPaymentDataMerchantRequest merchant;
    private QRISPaymentDataCustomerRequest customer;
}
