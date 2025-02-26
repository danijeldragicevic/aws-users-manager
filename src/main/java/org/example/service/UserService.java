package org.example.service;

import org.example.model.UserDetails;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer for handling IAM user operations.
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Fetches all IAM users from AWS.
     *
     * @return List of IAM user details.
     */
    public List<UserDetails> getUsers() {
        return userRepository.getUsers();
    }
}
