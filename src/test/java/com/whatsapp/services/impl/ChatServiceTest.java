package com.whatsapp.services.impl;

import com.whatsapp.exception.UserException;
import com.whatsapp.model.Chat;
import com.whatsapp.model.User;
import com.whatsapp.repository.ChatRepository;
import com.whatsapp.repository.UserRepository;
import com.whatsapp.request.GroupChatRequest;
import com.whatsapp.services.UserService;
import com.whatsapp.services.impl.ChatServiceImpl;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotNull;


class ChatServiceTest {
    @Mock
    private ChatRepository chatRepository;
    @Mock
    private UserService userService;

    @InjectMocks
    private ChatServiceImpl chatService;

    private AutoCloseable autoCloseable;
    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        chatService = new ChatServiceImpl(chatRepository, userService);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testCreateChat() {

        User sender = User.builder().id(103).email("mj@gmail.com").fullName("Manash jyoti").profileImage("profileImage").password("1232").build();
        User receiver = User.builder().id(102).email("mk@gmail.com").fullName("Minku").profileImage("profileImage2").password("1232").build();

        Chat chat = new Chat();
        chat.setId(201);
        chat.setCreatedBy(sender);
        chat.getUsers().add(sender);
        chat.getUsers().add(receiver);
        chat.setGroup(false);
        chat.setChatName("c-1");
        chat.setChatImage("image-1");

        when(userService.findUserById(102)).thenReturn(receiver);
        when(chatRepository.findSingleChatByUserIds(sender,receiver)).thenReturn(chat);
        when(chatRepository.save(any(Chat.class))).thenReturn(chat);

        assertThat(chatService.createChat(sender,receiver.getId()).getChatName()).isEqualTo(chat.getChatName());
    }

    @Test
    void testFindChatById() {
        User sender = User.builder().id(103).email("mj@gmail.com").fullName("Manash jyoti").profileImage("profileImage").password("1232").build();
        User receiver = User.builder().id(102).email("mk@gmail.com").fullName("Minku").profileImage("profileImage2").password("1232").build();

        Chat chat = new Chat();
        chat.setId(201);
        chat.setCreatedBy(sender);
        chat.getUsers().add(sender);
        chat.getUsers().add(receiver);
        chat.setGroup(false);
        chat.setChatName("c-1");
        chat.setChatImage("image-1");

        when(chatRepository.findById(203)).thenReturn(Optional.ofNullable(chat));
        assertThat(chatService.findChatById(203)).isEqualTo(chat);
    }

    @Test
    void testFindAllChatByUserId() {
        User user = User.builder().id(102).fullName("manash").email("m@gmail.com").build();

        List<Chat> chatList = new ArrayList<>();
        Chat chat1 = Chat.builder().id(500).chatName("chat-1").build();
        Chat chat2 = Chat.builder().id(501).chatName("chat-1").build();
        chatList.add(chat1);
        chatList.add(chat2);

        when(userService.findUserById(102)).thenReturn(user);
        when(chatRepository.findChatByUserId(user.getId())).thenReturn(chatList);
        assertThat(chatService.findAllChatByUserId(user.getId()).get(0).getId()).isEqualTo(chatList.get(0).getId());
    }

    @Test
    void TestCreateGroup() throws UserException {

        User reqUser = User.builder().id(101).fullName("manash").email("m@gmail.com").build();
        GroupChatRequest gr = new GroupChatRequest();
        gr.setGroupName("g1");
        Set<Integer> userIds = new HashSet<>(Arrays.asList(23, 33, 423));
        gr.setUserId(userIds);

        Chat chatGroup = new Chat();
        chatGroup.setGroup(true);
        chatGroup.setChatImage(gr.getGroupImage());
        chatGroup.setChatName(gr.getGroupName());
        chatGroup.setCreatedBy(reqUser);
        chatGroup.getAdmin().add(reqUser);

        //mocking the data
        when(userService.findUserById(anyInt())).thenReturn(reqUser);
        when(chatRepository.save(any(Chat.class))).thenReturn(chatGroup);
        assertThat(chatService.createGroup(gr,reqUser).getChatName()).isEqualTo(chatGroup.getChatName());

    }

    @Test
    void addUserToGroup() {

        User reqUser = User.builder().id(101).fullName("manash").email("m@gmail.com").build();


        Chat chatGroup = new Chat();
        chatGroup.setGroup(true);
        chatGroup.setChatName("ch-1");
        chatGroup.setCreatedBy(reqUser);
        chatGroup.getAdmin().add(reqUser);

        when(chatRepository.findById(anyInt())).thenReturn(Optional.ofNullable(chatGroup));
        when(chatRepository.save(any(Chat.class))).thenReturn(chatGroup);
        assertThat(chatService.addUserToGroup(12,21,reqUser).getChatName()).isEqualTo(chatGroup.getChatName());
    }

    @Test
    void renameGroup() {
        User reqUser = User.builder().id(101).fullName("manash").build();

        Chat chatGroup = new Chat();
        chatGroup.setGroup(true);
        chatGroup.setChatName("ch-1");
        chatGroup.setCreatedBy(reqUser);
//        chatGroup.getAdmin().add(reqUser);
        chatGroup.getUsers().add(reqUser);

        when(chatRepository.findById(anyInt())).thenReturn(Optional.ofNullable(chatGroup));
        when(chatRepository.save(any(Chat.class))).thenReturn(chatGroup);

        assertThat(chatService.renameGroup(101,"new name",reqUser).getChatName()).isEqualTo("new name");

    }

    @Test
    void removeFromGroup() {
        User reqUser = User.builder().id(101).fullName("manash").build();
        User member = User.builder().id(102).fullName("minku").build();

        Chat chatGroup = new Chat();
        chatGroup.setGroup(true);
        chatGroup.setChatName("ch-1");
        chatGroup.setCreatedBy(reqUser);
        chatGroup.getUsers().add(member);
        chatGroup.getAdmin().add(reqUser);

        when(userService.findUserById(anyInt())).thenReturn(member);
        when(chatRepository.findById(anyInt())).thenReturn(Optional.ofNullable(chatGroup));
        when(chatRepository.save(any(Chat.class))).thenReturn(chatGroup);
        assertThat(chatService.removeFromGroup(101,102,reqUser).getChatName()).isEqualTo(chatGroup.getChatName());
    }

    @Test
    void deleteChat() {
        User reqUser = User.builder().id(101).fullName("manash").build();

        Chat chatGroup = new Chat();
        chatGroup.setGroup(true);
        chatGroup.setChatName("ch-1");
        chatGroup.setCreatedBy(reqUser);
        chatGroup.getAdmin().add(reqUser);

        when(userService.findUserById(anyInt())).thenReturn(reqUser);
        when(chatRepository.findById(anyInt())).thenReturn(Optional.ofNullable(chatGroup));

        chatService.deleteChat(8989, reqUser);

        // Assert
        verify(chatRepository, times(1)).findById(8989);
        verify(chatRepository, times(1)).delete(chatGroup);
    }
}