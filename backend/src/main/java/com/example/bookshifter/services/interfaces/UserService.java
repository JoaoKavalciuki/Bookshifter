package com.example.bookshifter.services.interfaces;

import com.example.bookshifter.dto.RegisterUserDTO;
import com.example.bookshifter.dto.UserDTO;
import com.example.bookshifter.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserDTO> findAll();

    User registerUser(RegisterUserDTO dto);

    Optional<User> findByEmail(String email);
}