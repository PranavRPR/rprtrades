package com.ysm.rprtrades.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ysm.rprtrades.dto.LoginDTO;
import com.ysm.rprtrades.dto.LoginResponseDTO;
import com.ysm.rprtrades.entity.User;
import com.ysm.rprtrades.exception.ResourceNotFoundException;
import com.ysm.rprtrades.repository.UserRepository;
import com.ysm.rprtrades.security.JwtUtil;
import com.ysm.rprtrades.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public LoginResponseDTO login(LoginDTO dto) {

        Optional<User> userOpt = repository.findByEmail(dto.getEmail());

        if (userOpt.isEmpty()) {
            throw new RuntimeException("Invalid email or password.");
        }

        User user = userOpt.get();

        if (!user.isEnabled()) {
            throw new RuntimeException("Account is disabled. Contact administrator.");
        }

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password.");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        return new LoginResponseDTO(
                token,
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name());
    }

    @Override
    public User enableUser(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
        user.setEnabled(true);
        return repository.save(user);
    }

    @Override
    public User disableUser(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
        user.setEnabled(false);
        return repository.save(user);
    }
}
