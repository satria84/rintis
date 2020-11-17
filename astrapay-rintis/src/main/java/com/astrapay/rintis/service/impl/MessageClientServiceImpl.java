package com.astrapay.rintis.service.impl;

import com.astrapay.rintis.service.MessageClientService;
import com.astrapay.rintis.util.TcpClientGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.MessageTimeoutException;
import org.springframework.stereotype.Service;

@Service
public class MessageClientServiceImpl implements MessageClientService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageClientServiceImpl.class);

    private TcpClientGateway tcpClientGateway;
    @Autowired
    public MessageClientServiceImpl(TcpClientGateway tcpClientGateway) {
        this.tcpClientGateway = tcpClientGateway;
    }

    @Override
    public String sendMessage(String message) {
        String response = "";
        try {
            LOGGER.info("Send message ke Rintis: {}", message);
            byte[] responseBytes = tcpClientGateway.send(message.getBytes());
            response = new String(responseBytes);
            LOGGER.info("Receive response dari rintis: {}", response);
        }catch (MessageTimeoutException m){
            response = "Timeout";
            return response;
        }
        return response;
    }
}
