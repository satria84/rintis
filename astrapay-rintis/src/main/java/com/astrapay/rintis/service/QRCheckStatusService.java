package com.astrapay.rintis.service;

import org.jpos.iso.ISOException;

import com.astrapay.rintis.domain.request.CheckStatusRequest;

public interface QRCheckStatusService {
	Object requestQRCheckStatus(CheckStatusRequest request) throws ISOException;
}
