package com.mk.orderbooks.service;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class Messages {

    private final MessageSource messageSource;

    public Messages(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String messageKey) {
        return this.messageSource.getMessage(messageKey, null, null);
    }

}
