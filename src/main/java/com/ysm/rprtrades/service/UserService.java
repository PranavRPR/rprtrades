package com.ysm.rprtrades.service;

import java.util.List;

import com.ysm.rprtrades.dto.LoginDTO;
import com.ysm.rprtrades.dto.LoginResponseDTO;
import com.ysm.rprtrades.entity.User;

public interface UserService {

    User register(User user);

    List<User> getAllUsers();

    User getUserById(Long id);

   LoginResponseDTO login(LoginDTO loginDTO);

    User enableUser(Long id);

    User disableUser(Long id);
}
