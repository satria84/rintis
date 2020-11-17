package com.astrapay.rintis.domain.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RefundIssuerCoreRequest {
    public String invoiceNo;
    public BigDecimal amount;
    public String dateTime;
}
