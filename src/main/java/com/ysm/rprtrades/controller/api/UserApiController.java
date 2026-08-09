package com.ysm.rprtrades.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.ysm.rprtrades.dto.LoginResponseDTO;
import com.ysm.rprtrades.dto.LoginDTO;
import com.ysm.rprtrades.entity.User;
import com.ysm.rprtrades.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserApiController {

    @Autowired
    private UserService userService;

    // ==========================
    // USER REGISTRATION
    // PUBLIC API
    // ==========================

    @PostMapping("/register")
    public User register(@RequestBody User user) {

        System.out.println("REGISTER API HIT");

        return userService.register(user);
    }

    // ==========================
    // USER LOGIN
    // PUBLIC API
    // ==========================

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginDTO loginDTO) {

        try {

            LoginResponseDTO response = userService.login(loginDTO);

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            return ResponseEntity
                    .status(401)
                    .body(e.getMessage());
        }

    }

    // ==========================
    // ADMIN VIEW ALL USERS
    // ==========================

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<User> getAllUsers() {

        return userService.getAllUsers();

    }

    // ==========================
    // ADMIN VIEW USER BY ID
    // ==========================

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public User getUserById(
            @PathVariable Long id) {

        return userService.getUserById(id);

    }

    // ==========================
    // ADMIN ENABLE USER
    // ==========================

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/enable/{id}")
    public User enableUser(@PathVariable Long id) {
        return userService.enableUser(id);
    }

    // ==========================
    // ADMIN DISABLE USER
    // ==========================

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/disable/{id}")
    public User disableUser(@PathVariable Long id) {
        return userService.disableUser(id);
    }

}
