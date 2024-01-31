package com.whatsapp.services;

import com.whatsapp.exception.ChatException;
import com.whatsapp.exception.UserException;
import com.whatsapp.model.Chat;
import com.whatsapp.request.GroupChatRequest;
import com.whatsapp.model.User;

import java.util.List;

public interface ChatService {

    Chat createChat(User sender , Integer receiver) throws UserException;
    Chat findChatById(Integer chatId)  throws ChatException;

    //all chat of a user
    List<Chat> findAllChatByUserId(Integer id) throws UserException;

    Chat createGroup(GroupChatRequest req , User reqUser) throws UserException;
    Chat addUserToGroup(Integer userId , Integer chatId , User reqUser) throws UserException , ChatException;
    Chat renameGroup(Integer chatId , String groupName , User user) throws ChatException , UserException;

    Chat removeFromGroup(Integer chatId , Integer userId , User reqUser) throws UserException,ChatException;
    void deleteChat(Integer chatId , User user)  throws ChatException,UserException;

}
