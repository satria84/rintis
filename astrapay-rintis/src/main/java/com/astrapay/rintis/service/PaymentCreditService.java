package com.astrapay.rintis.service;

import com.astrapay.rintis.domain.request.QRISPaymentRequest;

public interface PaymentCreditService {
    Object paymentCreditRintis(QRISPaymentRequest request);
}
