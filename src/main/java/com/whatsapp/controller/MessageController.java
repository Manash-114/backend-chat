package com.whatsapp.controller;

import com.whatsapp.model.Message;
import com.whatsapp.model.User;
import com.whatsapp.payload.ApiResponse;
import com.whatsapp.request.SendMessageRequest;
import com.whatsapp.services.ChatService;
import com.whatsapp.services.MessageService;
import com.whatsapp.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private MessageService messageService;
    private UserService userService;

    public MessageController(MessageService messageService , UserService userService){
        this.messageService = messageService;
        this.userService = userService;
    }

    @PostMapping("/send")
    public ResponseEntity<Message> sendMessageHandler(@RequestBody SendMessageRequest messageRequest , @RequestHeader("Authorization") String jwtToken) throws Exception {

        //login user
        User userProfile = userService.findUserProfile(jwtToken);
        messageRequest.setUserId(userProfile.getId());
        Message message = messageService.sendMessage(messageRequest);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }


    @GetMapping("/chat/{chatId}")
    public ResponseEntity<List<Message>> getMessageHandler(@PathVariable Integer chatId , @RequestHeader("Authorization") String jwtToken) throws Exception {

        //login user
        User userProfile = userService.findUserProfile(jwtToken);
        List<Message> chatMessages = messageService.getChatMessages(chatId, userProfile);
        return new ResponseEntity<>(chatMessages, HttpStatus.OK);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<ApiResponse> deleteMessageHandler(@PathVariable Integer messageId , @RequestHeader("Authorization") String jwtToken) throws Exception {

        //login user
        User userProfile = userService.findUserProfile(jwtToken);
        String r = messageService.deleteMessage(messageId, userProfile);
        return new ResponseEntity<>(new ApiResponse(r,true), HttpStatus.OK);
    }


}
