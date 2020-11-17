package com.astrapay.rintis.web;

import java.io.IOException;

import org.jpos.iso.ISOException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.astrapay.rintis.domain.request.CheckStatusRequest;
import com.astrapay.rintis.domain.request.QRCheckStatusRequest;
import com.astrapay.rintis.service.QRCheckStatusService;
import com.astrapay.rintis.service.impl.QRCheckStatusServiceImpl;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
public class QRCheckStatusResources {

	private final QRCheckStatusServiceImpl checkStatusService;
	
	 @PostMapping(path = "/requestQRCheckStatus")
	    public Object requestQRCheckStatus (@RequestBody CheckStatusRequest request) throws IOException, NullPointerException, IllegalArgumentException, ISOException{
	        return checkStatusService.requestQRCheckStatus(request);
	    }
}
