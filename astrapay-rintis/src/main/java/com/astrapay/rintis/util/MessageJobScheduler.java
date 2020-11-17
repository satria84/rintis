package com.astrapay.rintis.util;

import com.astrapay.rintis.service.MessageClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MessageJobScheduler {
    private MessageClientService messageClientService;

    @Autowired
    public MessageJobScheduler(MessageClientService messageClientService) {
        this.messageClientService = messageClientService;
    }

    @Scheduled(fixedDelay = 1000L)
    public void sendMessageJob() {
        //messageClientService.sendMessage("Hello world");
    }
}
