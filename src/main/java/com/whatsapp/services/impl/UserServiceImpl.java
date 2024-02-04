package com.whatsapp.services.impl;

import com.whatsapp.config.TokenProvider;
import com.whatsapp.exception.UserException;
import com.whatsapp.model.User;
import com.whatsapp.repository.UserRepository;
import com.whatsapp.request.UpdateUserRequest;
import com.whatsapp.services.UserService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {


    private UserRepository userRepository;
    private TokenProvider tokenProvider;

    public UserServiceImpl(UserRepository userRepository, TokenProvider tokenProvider){
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
    }


    @Override
    public User findUserById(Integer id) throws UserException{
        User user = userRepository.findById(id).orElseThrow(() -> new UserException("User not found with id " + id));
        return user;
    }

    @Override
    public User findUserProfile(String jwtToken) throws Exception {
        String email = tokenProvider.getEmailFromToken(jwtToken);

        if(email == null){
            throw new BadCredentialsException("received invalid token");
        }
        User user = userRepository.findByEmail(email);
        if(user == null)
            throw new UserException("user not found with email "+email);
        return user ;
    }

    @Override
    public User updateUser(Integer id, UpdateUserRequest updateUser) throws UserException {

        User user = findUserById(id);

        if(updateUser.getFullName() != null){
            user.setFullName(updateUser.getFullName());
        }

        if(updateUser.getProfileImage() !=null){
            user.setProfileImage(updateUser.getProfileImage());
        }

        return userRepository.save(user);
    }

    @Override
    public List<User> searchUser(String keyword) {
        return userRepository.searchUser(keyword);
    }
}
