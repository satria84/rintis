package com.astrapay.rintis.domain.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoreRefundIssuerResponse {
    public String customerReferenceNumber;
    public ErrorJson errorJson;
}
