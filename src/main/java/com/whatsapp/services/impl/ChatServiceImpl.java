package com.whatsapp.services.impl;

import com.whatsapp.exception.ChatException;
import com.whatsapp.exception.UserException;
import com.whatsapp.model.Chat;
import com.whatsapp.request.GroupChatRequest;
import com.whatsapp.model.User;
import com.whatsapp.repository.ChatRepository;
import com.whatsapp.services.ChatService;
import com.whatsapp.services.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    private ChatRepository chatRepository;
    private UserService userService;

    public ChatServiceImpl(ChatRepository chatRepository, UserService userService){
        this.chatRepository = chatRepository;
        this.userService = userService;
    }
    @Override
    public Chat createChat(User sender, Integer receiver) throws UserException {

        User user2 = userService.findUserById(receiver);

        Chat isChatExists = chatRepository.findSingleChatByUserIds(sender, user2);
        if( isChatExists != null)
            return isChatExists;

        //create new Chat
        Chat chat = new Chat();
        chat.setCreatedBy(sender);
        chat.getUsers().add(sender);
        chat.getUsers().add(user2);
        chat.setGroup(false);
        return chat;
    }

    @Override
    public Chat findChatById(Integer chatId) throws ChatException {
        Chat c = chatRepository.findById(chatId).orElseThrow(()-> new ChatException("Chat not found"));
        return c;
    }

    @Override
    public List<Chat> findAllChatByUserId(Integer id) throws UserException {
        User userById = userService.findUserById(id);
        List<Chat> chatByUserId = chatRepository.findChatByUserId(userById.getId());
        return  chatByUserId;
    }

    @Override
    public Chat createGroup(GroupChatRequest req, User reqUser) throws UserException {
        Chat chatGroup = new Chat();
        chatGroup.setGroup(true);
        chatGroup.setChatImage(req.getGroupImage());
        chatGroup.setChatName(req.getGroupName());
        chatGroup.setCreatedBy(reqUser);
        chatGroup.getAdmin().add(reqUser);
        for(Integer userId : req.getUserId()){
            User userById = userService.findUserById(userId);
            chatGroup.getUsers().add(userById);
        }
        return chatGroup;
    }

    @Override
    public Chat addUserToGroup(Integer userId, Integer chatId , User reqUser) throws UserException, ChatException {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new ChatException("Group  not found"));
        boolean isAdmin = chat.getAdmin().contains(reqUser);
        if(isAdmin) {
            User userById = userService.findUserById(userId);
            chat.getUsers().add(userById);
            chatRepository.save(chat);
            return chat;
        }else {
            throw new UserException("You are not admin");
        }

    }

    @Override
    public Chat renameGroup(Integer chatId, String groupName, User reqUser) throws ChatException, UserException {

        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new ChatException("Chat not found"));
        boolean isMember = chat.getUsers().contains(reqUser);
        if(isMember){
            chat.setChatName(groupName);
            chatRepository.save(chat);
            return chat;
        }

        throw new UserException("You are not a group member");
    }

    @Override
    public Chat removeFromGroup(Integer chatId, Integer userId, User reqUser) throws UserException, ChatException {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new ChatException("Chat not found"));
        //deleted user
        User userById = userService.findUserById(userId);

        boolean isAdmin = chat.getAdmin().contains(reqUser);


        if(isAdmin){
            chat.getUsers().remove(userById);
            return chatRepository.save(chat);
        }else if(chat.getUsers().contains(userById)){
            chat.getUsers().remove(userById);
            return chatRepository.save(chat);
        }

        throw new UserException("you cannot remove user");
    }

    @Override
    public void deleteChat(Integer chatId, User user) throws ChatException, UserException {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new ChatException("Chat not found"));
        if(chat.getAdmin().contains(user)){
            chatRepository.delete(chat);
        }
    }
}
