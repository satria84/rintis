package com.astrapay.rintis.domain.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MerchantDataResponse {
    public String merchant_id;
    public String merchant_pan;
    public String merchant_criteria;
}
