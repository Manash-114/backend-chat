package com.whatsapp.services.impl;

import com.whatsapp.model.Chat;
import com.whatsapp.model.Message;
import com.whatsapp.model.User;
import com.whatsapp.repository.MessageRepository;
import com.whatsapp.request.SendMessageRequest;
import com.whatsapp.services.ChatService;
import com.whatsapp.services.UserService;
import com.whatsapp.services.impl.MessageServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class MessageServiceTest {
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private UserService userService;

    @Mock
    private ChatService chatService;

    @InjectMocks
    private MessageServiceImpl messageService;
    
    private AutoCloseable autoCloseable;
    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void sendMessage() {
        User reqUser = User.builder().id(101).fullName("Manash").build();
        Chat chat = Chat.builder().id(701).chatName("chatwithmanash").build();
        Message message = Message.builder().chat(chat).content("Hi how are you")
                .id(89)
                .user(reqUser)
                .timestamp(LocalDateTime.now())
                .build();


        when(messageRepository.save(any(Message.class))).thenReturn(message);
        when(userService.findUserById(anyInt())).thenReturn(reqUser);
        when(chatService.findChatById(anyInt())).thenReturn(chat);
        assertThat(messageService.
                sendMessage(new SendMessageRequest(101,901,"hi")).getId())
                .isEqualTo(89);
    }

    @Test
    void getChatMessages() {
        User reqUser = User.builder().id(101).fullName("Manash").build();
        Chat chat = Chat.builder()
                .id(701)
                .chatName("chatwithmanash")
                .users(new HashSet<>())
                .build();
        chat.getUsers().add(reqUser);
        when(chatService.findChatById(anyInt())).thenReturn(chat);

        List<Message> messages = new ArrayList<>();
        Message m1 = Message.builder().chat(chat).content("Hi how are you")
                .id(89)
                .user(User.builder().build())
                .timestamp(LocalDateTime.now())
                .build();
        Message m2 = Message.builder().chat(chat).content("Hi")
                .id(90)
                .user(User.builder().build())
                .timestamp(LocalDateTime.now())
                .build();
        messages.add(m1);
        messages.add(m2);

        when(messageRepository.findByChatId(anyInt())).thenReturn(messages);
        assertThat(messageService
                .getChatMessages(101,reqUser).get(0).getId())
                .isEqualTo(89);

    }

    @Test
    void findMessageById() {
        Message message = Message.builder()
                .id(90)
                .content("Hi how are you")
                .timestamp(LocalDateTime.now())
                .build();
        when(messageRepository.findById(anyInt())).thenReturn(Optional.ofNullable(message));

        assertThat(messageService
                .findMessageById(101).getId())
                .isEqualTo(90);
    }

    @Test
    void deleteMessage() {
        User reqUser = User.builder().id(101).fullName("Manash").build();
        User user2 = User.builder().id(102).fullName("Manash").build();
        Message message = Message.builder()
                .id(90)
                .content("Hi how are you")
                .user(reqUser)
                .timestamp(LocalDateTime.now())
                .build();
        when(messageRepository.findById(anyInt())).thenReturn(Optional.ofNullable(message));
        messageService.deleteMessage(90,reqUser);
        verify(messageRepository,times(1)).deleteById(90);

    }
}