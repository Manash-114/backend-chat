package com.whatsapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.whatsapp.model.User;
import com.whatsapp.request.LoginRequest;
import com.whatsapp.request.UpdateUserRequest;
import com.whatsapp.services.CustomUserService;
import com.whatsapp.services.UserService;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {


    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;
    private User userOne,userTwo;
    private List<User> users;
    @BeforeEach
    void setup() throws Exception {
        userOne = User.builder().id(101)
                .fullName("Manash Jyoti Handique")
                .password("xxxcccc")
                .email("m@gmail.com")
                .build();

        userTwo = User.builder().id(102)
                .fullName("Minku Jyoti Handique")
                .password("dfsf")
                .email("minku@gmail.com")
                .build();
        users = new ArrayList<>();
        users.add(userOne);
        users.add(userTwo);
    }

    @AfterEach
    void tearDown(){

    }

    @Test
    void getUserProfileHandler() throws Exception {
        when(userService.findUserProfile(anyString()))
                .thenReturn(userOne);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/profile")
                        .header("Authorization", "myjwttoken")
                ).andDo(print())
                .andExpect(status().isAccepted());
    }

    @Test
    void searchUserHandler() throws Exception {
        when(userService.searchUser(anyString())).thenReturn(users);
        String query = "manas";
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/"+query)
                ).andDo(print()).andExpect(status().isOk());
    }

    @Test
    void updateUserHandler() throws Exception {
        when(userService.findUserProfile(anyString()))
                .thenReturn(userOne);
        when(userService.updateUser(anyInt(),any(UpdateUserRequest.class))).thenReturn(userTwo);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE,false);
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(new UpdateUserRequest("new name", "profileimage"));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/update")
                        .header("Authorization","jwt")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andDo(print()).andExpect(status().isAccepted());
    }


}