package com.whatsapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.whatsapp.model.Message;
import com.whatsapp.model.User;
import com.whatsapp.request.SendMessageRequest;
import com.whatsapp.services.MessageService;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MessageController.class)
@AutoConfigureMockMvc(addFilters = false)
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    @MockBean
    private UserService userService;
    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void sendMessageHandler() throws Exception {
        User user = User.builder().id(101)
                .fullName("Manash Jyoti Handique")
                .build();
        Message message = Message.builder().id(101).content("hii").build();
        when(userService.findUserProfile(anyString())).thenReturn(user);
        when(messageService.sendMessage(any(SendMessageRequest.class)))
                .thenReturn(message);


        SendMessageRequest sendMessageRequest = new SendMessageRequest();
        sendMessageRequest.setUserId(101);
        sendMessageRequest.setContent("hi sexi jjj");
        sendMessageRequest.setChatId(201);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE,false);
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        String requestData = objectMapper.writeValueAsString(sendMessageRequest);
        System.out.println(requestData);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/messages/send")
                .header("Authorization","myjwttoken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestData)
                        .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getMessageHandler() throws Exception{
        User user = User.builder().id(101)
                .fullName("Manash Jyoti Handique")
                .build();

        Message message1 = Message.builder().id(101).content("hii").build();
        Message message2 = Message.builder().id(102).content("hi sexy").build();
        List<Message> messages = new ArrayList<>();
        messages.add(message1);
        messages.add(message2);

        when(userService.findUserProfile(anyString())).thenReturn(user);
        when(messageService.getChatMessages(anyInt(),any(User.class)))
                .thenReturn(messages);

        String chatid = "102";
        mockMvc.perform(MockMvcRequestBuilders.get("/api/messages/chat/"+chatid)
                .header("Authorization","myjwt token"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void deleteMessageHandler() throws Exception {
        User user = User.builder().id(101)
                .fullName("Manash Jyoti Handique")
                .build();
        when(userService.findUserProfile("myjwttoken")).thenReturn(user);
        when(messageService.deleteMessage(193,user))
                .thenReturn("Deleted successfully");

        Integer messageId = 193;
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/messages/"+messageId)
                .header("Authorization","myjwttoken"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService,times(1)).findUserProfile("myjwttoken");
        verify(messageService,times(1)).deleteMessage(messageId,user);
    }
}