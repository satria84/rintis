package com.astrapay.rintis.service;

import com.astrapay.rintis.domain.request.CutOverRequest;
import com.astrapay.rintis.domain.request.EchoTestRequest;
import com.astrapay.rintis.domain.request.LogoffRequest;
import com.astrapay.rintis.domain.request.LogonRequest;

public interface MessageLayoutService {
    Object logon(LogonRequest request) throws Exception;
    Object logon_v2(LogonRequest request) throws Exception;
    Object logoff(LogoffRequest request) throws Exception;
    Object echoTest(EchoTestRequest request) throws Exception;
    Object cutOver(CutOverRequest request) throws Exception;
    String testLogon() throws Exception;
    String testLogon2() throws Exception;
    Object logon_v3(LogonRequest request) throws Exception;

}
