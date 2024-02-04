package com.whatsapp.services.impl;

import com.whatsapp.config.TokenProvider;
import com.whatsapp.model.User;
import com.whatsapp.repository.UserRepository;
import com.whatsapp.request.UpdateUserRequest;
import com.whatsapp.services.impl.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private TokenProvider tokenProvider;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private List<User> users = new ArrayList<>();
    private AutoCloseable autoCloseable;
    @BeforeEach
    void setUp(){
        autoCloseable = MockitoAnnotations.openMocks(this);
        user = User.builder().id(101).fullName("Manash Jyoti Handique").build();
    }
    @AfterEach
    void tearDown() throws Exception{
        autoCloseable.close();
        user = null;
    }
    @Test
    void findUserById() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(user));
        assertThat(userService.findUserById(user.getId()).getId()).isEqualTo(user.getId());
    }

    @Test
    void findUserProfile() throws Exception{

        // Mocking data
        String jwtToken = "mockJwtToken";
        String userEmail = "user@example.com";
        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setFullName("manash jyoti handique");
        mockUser.setEmail(userEmail);

        when(tokenProvider.getEmailFromToken(jwtToken)).thenReturn(userEmail);
        when(userRepository.findByEmail(userEmail)).thenReturn(mockUser);

        assertThat(userService.findUserProfile(jwtToken).getEmail()).isEqualTo(userEmail);
        verify(tokenProvider,times(1)).getEmailFromToken(jwtToken);
        verify(userRepository,times(1)).findByEmail(userEmail);
    }

    @Test
    void updateUser() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(user));
        when(userRepository.save(user)).thenReturn(user);
        assertThat(userService.updateUser(101,new UpdateUserRequest("newName","profile")).getFullName()).isEqualTo("newName");

    }

    @Test
    void searchUser() {
        users = List.of(new User(101,"Manash jyoti","m@gmail.com","profileurl",
                        "12345"),
                new User(102,"Minku jyoti","minku12@gmail.com","profileurl",
                        "12345"),
                new User(103,"Maina jyoti","maina@gmail.com","profileurl",
                        "12345")
        );

        when(userRepository.searchUser(anyString())).thenReturn(users);

        assertThat(userService.searchUser("jyoti").get(0).getId()).isEqualTo(users.get(0).getId());
    }
}