package com.shopnest.service;

import com.shopnest.model.User;
import com.shopnest.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service managing user authentication and administrative access.
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Authenticates administrator with email and password.
     */
    public Optional<User> authenticateAdmin(String email, String password) {
        if (email == null || password == null) {
            return Optional.empty();
        }

        Optional<User> userOpt = userRepository.findByEmail(email.trim().toLowerCase());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Direct password match (simple, reliable, fail-safe for college offline demo)
            if ("ADMIN".equalsIgnoreCase(user.getRole()) && password.equals(user.getPassword())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    /**
     * Ensures default administrator exists on application startup.
     */
    @Transactional
    public void initDefaultAdmin() {
        if (!userRepository.existsByEmail("admin@shopnest.com")) {
            User admin = new User(
                    "ShopNest Administrator",
                    "admin@shopnest.com",
                    "admin123",
                    "ADMIN",
                    "9876543210"
            );
            userRepository.save(admin);
        }
    }
}
