package com.ysm.rprtrades.config;

import com.ysm.rprtrades.entity.Role;
import com.ysm.rprtrades.entity.User;
import com.ysm.rprtrades.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * DataInitializer
 *
 * Runs once on application startup.
 * Seeds the fixed ADMIN account if it does not already exist in the database.
 *
 * Admin credentials:
 *   Email    : admin@rprtrades.com
 *   Password : Admin@123
 */
@Component
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {

        // ── Seed Admin ────────────────────────────────────────────────────────
        final String adminEmail = "admin@rprtrades.com";

        if (userRepository.findByEmail(adminEmail).isEmpty()) {

            User admin = new User();
            admin.setName("Admin");
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode("Admin@123"));
            admin.setRole(Role.ADMIN);
            admin.setEnabled(true);

            userRepository.save(admin);

            System.out.println("✅ DataInitializer: Admin account created → " + adminEmail);

        } else {
            System.out.println("ℹ️  DataInitializer: Admin account already exists.");
        }
    }
}
