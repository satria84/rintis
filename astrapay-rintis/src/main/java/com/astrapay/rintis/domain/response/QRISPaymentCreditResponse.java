package com.astrapay.rintis.domain.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QRISPaymentCreditResponse {
    private String command;
    private String response_code;
    private String response_text;
    private QRISPaymentCreditDataResponse data;
}
