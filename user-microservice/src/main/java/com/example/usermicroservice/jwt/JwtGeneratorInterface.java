package com.example.usermicroservice.jwt;

import com.example.usermicroservice.model.User;
import java.util.Map;

public interface JwtGeneratorInterface {
    Map<String, String> generateToken(User user);
}
