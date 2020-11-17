package com.astrapay.rintis.util;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.stereotype.Component;

@Component
@MessagingGateway(defaultRequestChannel = "outboundChannel")
public interface TcpClientGateway {
    byte[] send(byte[] message);
}
