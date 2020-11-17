package com.astrapay.rintis.service;

import com.astrapay.rintis.domain.request.QRInquiryMpanRequest;
import com.astrapay.rintis.domain.response.QRInquiryMpanResponse;

public interface QRInquiryMpanService {
    Object inquiryMpan(QRInquiryMpanRequest request) throws Exception;
    QRInquiryMpanResponse inquiryMpan2(QRInquiryMpanRequest request);
}
