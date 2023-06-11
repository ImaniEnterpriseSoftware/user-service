package com.example.usermicroservice.repository;

import com.example.usermicroservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, String> {
    public User findByUserNameAndPassword(String userName, String password);
    public User findByUserName(String userName);
    Optional<User> findById(Long id);
    void deleteById(Long id);
}
