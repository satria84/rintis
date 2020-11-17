package com.astrapay.rintis.util;

import com.astrapay.rintis.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

@MessageEndpoint
public class TcpServerEndpoint {
    private MessageService messageService;

    @Autowired
    public TcpServerEndpoint(MessageService messageService) {
        this.messageService = messageService;
    }

    @ServiceActivator(inputChannel = "inboundChannel")
    public byte[] process(byte[] message) {
        return messageService.processMessage(message);
    }
}
