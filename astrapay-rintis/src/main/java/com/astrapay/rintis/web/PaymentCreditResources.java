package com.astrapay.rintis.web;

import com.astrapay.rintis.domain.request.QRISPaymentRequest;
import com.astrapay.rintis.service.impl.PaymentCreditServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@AllArgsConstructor
@RestController

public class PaymentCreditResources {
    private final PaymentCreditServiceImpl paymentCreditService;

    @PostMapping(path = "/paymentCredit")
    public Object paymentCreditRintis(@RequestBody QRISPaymentRequest request) {
        return paymentCreditService.paymentCreditRintis(request);
    }
}
