package com.astrapay.rintis.web;

import java.math.BigDecimal;

import com.astrapay.rintis.domain.request.CutOverRequest;
import com.astrapay.rintis.domain.request.EchoTestRequest;
import com.astrapay.rintis.domain.request.LogoffRequest;
import com.astrapay.rintis.domain.request.LogonRequest;
import com.astrapay.rintis.domain.response.LogonResponse;
import com.astrapay.rintis.service.LogonService;
import com.astrapay.rintis.service.MessageLayoutService;
import com.astrapay.rintis.util.DataElementConverter;

import org.springframework.beans.factory.annotation.Value;
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
public class MessageLayoutResources {
    private final MessageLayoutService messageService;
    @PostMapping(path = "/logon")
    public Object logon(@RequestBody LogonRequest request) throws Exception{
        return messageService.logon(request);
    }

    @PostMapping(path = "/logoff")
    public Object logoff(@RequestBody LogoffRequest request) throws Exception {
        return messageService.logoff(request);
    }

    @PostMapping(path = "/testEcho")
    public Object echoTest(@RequestBody EchoTestRequest request) throws Exception {
        return messageService.echoTest(request);
    }

    @PostMapping(path = "/cutOver")
    public Object cutOver(@RequestBody CutOverRequest request) throws Exception {
        return messageService.cutOver(request);
    }

    @PostMapping(path = "/testLogon")
    public String testLogon() throws Exception {
        return messageService.testLogon();
    }

    @PostMapping(path = "/testLogon2")
    public String testLogon2() throws Exception {
        return messageService.testLogon2();
    }

    @PostMapping(path = "/logon_v2")
    public Object logon_v2(@RequestBody LogonRequest request) throws Exception{
        return messageService.logon_v2(request);
    }
    
    @PostMapping(path ="/logon_v3")
    public Object logon_v3(@RequestBody LogonRequest request) throws Exception{
        return messageService.logon_v3(request);
    }
}
