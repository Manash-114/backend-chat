package com.whatsapp.services;

import com.whatsapp.exception.ChatException;
import com.whatsapp.exception.MessageException;
import com.whatsapp.exception.UserException;
import com.whatsapp.model.Message;
import com.whatsapp.model.User;
import com.whatsapp.request.SendMessageRequest;

import java.util.List;

public interface MessageService {
    Message sendMessage(SendMessageRequest req) throws UserException,ChatException;
    List<Message> getChatMessages(Integer chatId , User reqUser) throws ChatException , UserException;
    public Message findMessageById(Integer messageId) throws MessageException;

    public String deleteMessage(Integer messageId , User reqUser) throws MessageException , UserException;

}
