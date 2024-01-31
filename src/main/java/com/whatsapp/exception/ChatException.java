package com.whatsapp.exception;

import com.whatsapp.model.Chat;

public class ChatException extends RuntimeException{
    public ChatException(String message){
        super(message);
    }
}
