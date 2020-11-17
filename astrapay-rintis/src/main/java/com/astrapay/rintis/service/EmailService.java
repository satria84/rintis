package com.astrapay.rintis.service;

public interface EmailService {
    public void sendMail(final String to, final String subject,final String content) throws Exception;
}