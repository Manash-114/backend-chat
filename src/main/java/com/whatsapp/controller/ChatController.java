package com.whatsapp.controller;

import com.whatsapp.model.Chat;
import com.whatsapp.payload.ApiResponse;
import com.whatsapp.request.GroupChatRequest;
import com.whatsapp.model.User;
import com.whatsapp.request.SingleChatReq;
import com.whatsapp.services.ChatService;
import com.whatsapp.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/chats")
public class ChatController {
    private ChatService chatService;
    private UserService userService;

    public ChatController(ChatService chatService , UserService userService){
        this.chatService = chatService;
        this.userService = userService;
    }

    @PostMapping("/single")
    public ResponseEntity<Chat> createSingleChatHandler(@RequestBody SingleChatReq singleChat , @RequestHeader("Authorization") String jwtToken) throws Exception {
        User reqUser = userService.findUserProfile(jwtToken);
        Chat chat = chatService.createChat(reqUser, singleChat.getUserId());
        return new ResponseEntity<>(chat, HttpStatus.CREATED);
    }

    @PostMapping("/group")
    public ResponseEntity<Chat> createGroupHandler(@RequestBody GroupChatRequest groupChatRequest , @RequestHeader("Authorization") String jwtToken) throws Exception {

        User reqUser = userService.findUserProfile(jwtToken);
        Chat group = chatService.createGroup(groupChatRequest, reqUser);
        return new ResponseEntity<>(group, HttpStatus.CREATED);
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<Chat> findChatByIdHandler(@PathVariable Integer chatId , @RequestHeader("Authorization") String jwtToken) throws Exception {
        Chat chatById = chatService.findChatById(chatId);
        return new ResponseEntity<>(chatById, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Chat>> findAllChatByUserHandler(@RequestHeader("Authorization") String jwtToken) throws Exception {

        User reqUser = userService.findUserProfile(jwtToken);
        List<Chat> allChatByUserId = chatService.findAllChatByUserId(reqUser.getId());
        return new ResponseEntity<>(allChatByUserId, HttpStatus.OK);
    }

    @PutMapping("/{chatId}/add/{userId}")
    public ResponseEntity<Chat> AddUserHandler( @PathVariable Integer chatId , @PathVariable Integer userId , @RequestHeader("Authorization") String jwtToken) throws Exception {

        User reqUser = userService.findUserProfile(jwtToken);
        Chat chat = chatService.addUserToGroup(userId, chatId, reqUser);
        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    @PutMapping("/{chatId}/remove/{userId}")
    public ResponseEntity<Chat> deleteUserHandler( @PathVariable Integer chatId , @PathVariable Integer userId , @RequestHeader("Authorization") String jwtToken) throws Exception {

        User reqUser = userService.findUserProfile(jwtToken);
        Chat chat = chatService.removeFromGroup(chatId,userId,reqUser);
        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{chatId}")
    public ResponseEntity<ApiResponse> deletChatHandler(@PathVariable Integer chatId, @RequestHeader("Authorization") String jwtToken) throws Exception {

        User reqUser = userService.findUserProfile(jwtToken);
        chatService.deleteChat(chatId,reqUser);
        return new ResponseEntity<>(new ApiResponse("chat is deleted successfully",true),HttpStatus.OK);
    }


}
