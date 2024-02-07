package com.whatsapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.whatsapp.model.Chat;
import com.whatsapp.model.User;
import com.whatsapp.payload.ApiResponse;
import com.whatsapp.request.GroupChatRequest;
import com.whatsapp.request.SingleChatReq;
import com.whatsapp.services.ChatService;
import com.whatsapp.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatController.class)
@AutoConfigureMockMvc(addFilters = false)
class ChatControllerTest {

    @MockBean
    private ChatService chatService;
    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;
    private User user;
    private Chat chat;
    private Chat chattwo;
    private List<Chat> chats = new ArrayList<>();
    @BeforeEach
    void setUp() {
        user  = User.builder().id(101)
                .fullName("Manash Jyoti Handique")
                .email("m@gmail.com").build();
        chat = Chat.builder().id(101).chatName("chatwithminku")
                .build();
        chattwo = Chat.builder().id(102).chatName("chatwithmaina")
                .build();
        chats.add(chat);
        chats.add(chattwo);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createSingleChatHandler() throws Exception{
        when(userService.findUserProfile(anyString()))
                .thenReturn(user);
        when(chatService.createChat(any(User.class),anyInt()))
                .thenReturn(chat);

        SingleChatReq singleChatReq = new SingleChatReq();
        singleChatReq.setUserId(101);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE,false);
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(singleChatReq);
        System.out.println(requestJson);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/chats/single")
                .header("Authorization","myjwttoken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .characterEncoding("utf-8")
        ).andDo(print())
                .andExpect(status().isCreated());

        verify(userService,times(1)).findUserProfile("myjwttoken");
        verify(chatService,times(1)).createChat(user,singleChatReq.getUserId());
    }

    @Test
    void createGroupHandler() throws Exception {
        when(userService.findUserProfile(anyString()))
                .thenReturn(user);
        when(chatService.createGroup(any(GroupChatRequest.class),any(User.class)))
                .thenReturn(chat);

        GroupChatRequest groupChatRequest = new GroupChatRequest(new HashSet<>(),
                "mygroup","g-image");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE,false);
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(groupChatRequest);
        System.out.println(requestJson);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/chats/group")
                .header("Authorization","myjwttoken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andDo(print())
                .andExpect(status().isCreated());

        verify(userService,times(1)).findUserProfile("myjwttoken");
        verify(chatService,times(1)).createGroup(any(GroupChatRequest.class),any(User.class));
    }

    @Test
    void findChatByIdHandler() throws Exception{
        when(chatService.findChatById(anyInt()))
                .thenReturn(chat);
        Integer chatId = 103;
        mockMvc.perform(MockMvcRequestBuilders.get("/api/chats/"+chatId)
                .header("Authorization","myjwttoken"))
                .andDo(print())
                .andExpect(status().isOk());
        verify(chatService,times(1)).findChatById(chatId);

    }

    @Test
    void findAllChatByUserHandler() throws Exception {
        when(userService.findUserProfile(anyString())).thenReturn(user);
        when(chatService.findAllChatByUserId(anyInt()))
                .thenReturn(chats);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/chats/user")
                .header("Authorization","myjwttoken"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(chatService,times(1)).findAllChatByUserId(user.getId());

    }

    @Test
    void addUserHandler() throws Exception{
        when(userService.findUserProfile(anyString()))
                .thenReturn(user);
        when(chatService.addUserToGroup(anyInt(),anyInt(),any(User.class)))
                .thenReturn(chat);

        Integer chatId = 1;
        Integer userId = 2;

        mockMvc.perform(MockMvcRequestBuilders.put("/api/chats/"+chatId+"/add/"+userId)
                .header("Authorization","myjwttoken"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService,times(1)).findUserProfile("myjwttoken");
        verify(chatService,times(1)).addUserToGroup(anyInt(),anyInt(),any(User.class));
    }

    @Test
    void deleteUserHandler() throws Exception {
        when(userService.findUserProfile(anyString()))
                .thenReturn(user);
        when(chatService.removeFromGroup(anyInt(),anyInt(),any(User.class)))
                .thenReturn(chat);

        Integer chatId = 1;
        Integer userId = 2;

        mockMvc.perform(MockMvcRequestBuilders.put("/api/chats/"+chatId+"/remove/"+userId)
                        .header("Authorization","myjwttoken"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService,times(1)).findUserProfile("myjwttoken");
        verify(chatService,times(1)).removeFromGroup(anyInt(),anyInt(),any(User.class));
    }

    @Test
    void deletChatHandler() throws Exception {
        when(userService.findUserProfile(anyString()))
                .thenReturn(user);
        Integer chatId = 1;

       mockMvc.perform(MockMvcRequestBuilders.delete("/api/chats/delete/"+chatId)
               .header("Authorization","myjwttoken"));

        verify(userService,times(1)).findUserProfile("myjwttoken");
        verify(chatService,times(1)).deleteChat(anyInt(),any(User.class));

    }
}