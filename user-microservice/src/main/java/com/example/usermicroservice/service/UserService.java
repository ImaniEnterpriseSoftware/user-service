package com.example.usermicroservice.service;

import com.example.usermicroservice.model.User;
import com.example.usermicroservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {
    public void saveUser(User user);
    public User getUserByNameAndPassword(String name, String password) throws Exception;
    public User getUserByName(String name);
}