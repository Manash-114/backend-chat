package com.whatsapp.services.impl;

import com.whatsapp.exception.ChatException;
import com.whatsapp.exception.MessageException;
import com.whatsapp.exception.UserException;
import com.whatsapp.model.Chat;
import com.whatsapp.model.Message;
import com.whatsapp.model.User;
import com.whatsapp.repository.MessageRepository;
import com.whatsapp.request.SendMessageRequest;
import com.whatsapp.services.ChatService;
import com.whatsapp.services.MessageService;
import com.whatsapp.services.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private MessageRepository messageRepository;
    private UserService userService;
    private ChatService chatService;

    public MessageServiceImpl(MessageRepository messageRepository,UserService userService,ChatService chatService){
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.chatService = chatService;
    }
    @Override
    public Message sendMessage(SendMessageRequest req) throws UserException, ChatException {
        User user = userService.findUserById(req.getUserId());
        Chat chatById = chatService.findChatById(req.getChatId());
        Message message = new Message();
        message.setChat(chatById);
        message.setUser(user);
        message.setContent(req.getContent());
        message.setTimestamp(LocalDateTime.now());
        return messageRepository.save(message);
    }

    @Override
    public List<Message> getChatMessages(Integer chatId , User reqUser) throws ChatException , UserException{

        Chat chat = chatService.findChatById(chatId);
        boolean isContain = chat.getUsers().contains(reqUser);
        if(isContain)
        {
            List<Message> byChatId = messageRepository.findByChatId(chat.getId());
            return byChatId;
        }
        throw new UserException("You are not authorized.you are not related to this chat");

    }

    @Override
    public Message findMessageById(Integer messageId) throws MessageException {
      return messageRepository.findById(messageId).orElseThrow(() -> new MessageException("message not found"));
            }

    @Override
    public String deleteMessage(Integer messageId , User reqUser) throws MessageException , UserException{
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new MessageException("message not found"));

        if(message.getUser().getId().equals(reqUser.getId())){
            messageRepository.deleteById(messageId);
            return "message deleted successfully";
        }else
            throw new UserException("You are not allow to this message");
    }
}
