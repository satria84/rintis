package com.astrapay.rintis.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QRISPaymentRequest {
    private String command;
    private QRISPaymentDataRequest data;
}
