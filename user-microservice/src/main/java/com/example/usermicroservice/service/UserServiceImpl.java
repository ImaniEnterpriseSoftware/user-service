package com.example.usermicroservice.service;

import com.example.usermicroservice.model.User;
import com.example.usermicroservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    @Autowired
    public UserServiceImpl(UserRepository userRepository){
        this.userRepository=userRepository;
    }
    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }
    @Override
    public User getUserByNameAndPassword(String name, String password) throws Exception {
        User user = userRepository.findByUserNameAndPassword(name, password);
        if(user == null){
            throw new Exception("Invalid id and password");
        }
        return user;
    }

    @Override
    public User getUserByName(String userName) {
        return userRepository.findByUserName(userName);
    }
}