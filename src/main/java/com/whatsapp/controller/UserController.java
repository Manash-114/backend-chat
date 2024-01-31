package com.whatsapp.controller;


import com.whatsapp.model.User;
import com.whatsapp.payload.ApiResponse;
import com.whatsapp.request.UpdateUserRequest;
import com.whatsapp.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfileHandler(@RequestHeader("Authorization")  String token) throws Exception {
        User user = userService.findUserProfile(token);
        return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{query}")
    public ResponseEntity<List<User>> searchUserHandler(@PathVariable String query) throws Exception {
        List<User> users = userService.searchUser(query);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateUserHandler(@RequestBody UpdateUserRequest updateUserRequest , @RequestHeader("Authorization")  String token ) throws Exception {
        User user = userService.findUserProfile(token);
        userService.updateUser(user.getId(),updateUserRequest);

        return new ResponseEntity<>(new ApiResponse("user updated successfully",true),HttpStatus.ACCEPTED);
    }

    @GetMapping("/about")
    public String about(){
        return "abouts us pageg";
    }
}
