package com.astrapay.rintis.service;

import java.io.IOException;

public interface SocketService {

    String sendMessage(String isoMessage) throws IOException;
    String sendMessagePayment(String isoMessage) throws IOException;
    String sendMessageInquiry(String isoMessage) throws IOException;
    String sendMessageCheckStatus(String isoMessage) throws IOException;
    String sendMessage2(String message);
    void sendMessageWithoutClose(String isoMessage) throws IOException;
}
