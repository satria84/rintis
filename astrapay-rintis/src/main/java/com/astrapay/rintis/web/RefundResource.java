package com.astrapay.rintis.web;

import com.astrapay.rintis.service.RefundService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class RefundResource {
    private final RefundService refundService;

    @PostMapping(path = "/refund")
    public String refund(@RequestBody String request) throws Exception {
        return refundService.refundIssuer(request);
    }
}
