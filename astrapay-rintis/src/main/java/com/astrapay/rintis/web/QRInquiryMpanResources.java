package com.astrapay.rintis.web;

import com.astrapay.rintis.domain.request.QRInquiryMpanRequest;
import com.astrapay.rintis.domain.response.QRInquiryMpanResponse;
import com.astrapay.rintis.service.QRInquiryMpanService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class QRInquiryMpanResources {
    private final QRInquiryMpanService inquiryService;
    @PostMapping(path = "/inquiryMpan")
    public Object inquiryMpan(@RequestBody QRInquiryMpanRequest request) throws Exception {
        return inquiryService.inquiryMpan(request);
    }
}