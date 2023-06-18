package com.example.usermicroservice.testing;

import com.example.usermicroservice.controller.UserController;
import com.example.usermicroservice.jwt.JwtGeneratorInterface;
import com.example.usermicroservice.model.User;
import com.example.usermicroservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UserControllerTest {
    @Mock
    private UserService userService;

    @Mock
    private JwtGeneratorInterface jwtGenerator;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(userService, jwtGenerator, passwordEncoder);
    }

    @Test
    void postUser_ValidUser_ReturnsCreatedStatus() {
        // Arrange
        User user = new User();
        ResponseEntity<?> expectedResponse = new ResponseEntity<>(user, HttpStatus.CREATED);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        // Act
        ResponseEntity<?> response = userController.postUser(user);
        verify(userService).saveUser(any(User.class));

        // Assert
        assertEquals(expectedResponse.getStatusCode(), response.getStatusCode());
        assertEquals(expectedResponse.getBody(), response.getBody());
    }

    @Test
    void postUser_ExceptionThrown_ReturnsConflictStatus() {
            // Arrange
            User user = new User();
            user.setUserName("testuser");
            user.setPassword("password");
            User storedUser = null;
            String errorMessage = "UserName or Password is Invalid";
            ResponseEntity<?> expectedResponse = new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);

            // Act
            ResponseEntity<?> response = userController.loginUser(user);
            verify(jwtGenerator, never()).generateToken(any(User.class));

            // Assert
            assertEquals(expectedResponse.getStatusCode(), response.getStatusCode());
            assertTrue(response.getBody().toString().contains(errorMessage));
    }

    @Test
    void loginUser_InvalidUser_ReturnsConflictStatus() {
        // Arrange
        User user = new User();
        user.setUserName("testuser");
        user.setPassword("password");
        String errorMessage = "Invalid user credentials.";
        ResponseEntity<?> expectedResponse = new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);

        // Act
        ResponseEntity<?> response = userController.loginUser(user);
        verifyNoMoreInteractions(passwordEncoder, jwtGenerator);

        // Assert
        assertEquals(expectedResponse.getStatusCode(), response.getStatusCode());
    }

    @Test
    void getAllUsers_ReturnsListOfUsers() {
        // Arrange
        List<User> userList = new ArrayList<>();
        when(userService.getAllUsers()).thenReturn(userList);

        // Act
        List<User> users = userController.getAllUsers();

        // Assert
        assertEquals(userList, users);
        verify(userService).getAllUsers();
    }

    @Test
    void getUserById_ValidId_ReturnsOptionalUser() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        Optional<User> expectedUser = Optional.of(user);
        when(userService.getUserById(userId)).thenReturn(expectedUser);

        // Act
        Optional<User> result = userController.getUserById(userId);

        // Assert
        assertEquals(expectedUser, result);
        verify(userService).getUserById(userId);
    }

    @Test
    void getUserById_InvalidId_ReturnsEmptyOptional() {
        // Arrange
        Long userId = 1L;
        Optional<User> expectedUser = Optional.empty();
        when(userService.getUserById(userId)).thenReturn(expectedUser);

        // Act
        Optional<User> result = userController.getUserById(userId);

        // Assert
        assertEquals(expectedUser, result);
        verify(userService).getUserById(userId);
    }

    @Test
    void updateUser_ValidIdAndUser_ReturnsUpdatedUser() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        when(userService.updateUser(userId, user)).thenReturn(user);

        // Act
        User updatedUser = userController.updateUser(userId, user);

        // Assert
        assertEquals(user, updatedUser);
        verify(userService).updateUser(userId, user);
    }

    @Test
    void deleteUser_ValidId_CallsDeleteUser() {
        // Arrange
        Long userId = 1L;

        // Act
        userController.deleteUser(userId);

        // Assert
        verify(userService).deleteUser(userId);
    }
}
