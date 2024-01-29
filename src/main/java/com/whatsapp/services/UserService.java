package com.whatsapp.services;

import com.whatsapp.exception.UserException;
import com.whatsapp.modal.User;
import com.whatsapp.request.UpdateUserRequest;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {
    User findUserById(Integer id) throws UserException;
    User findUserProfile(String jwt) throws Exception;

    User updateUser(Integer id, UpdateUserRequest updateUser) throws UserException;

    List<User> searchUser(String keyword);
}
