package org.example.controller;

import org.example.model.UserDetails;
import org.example.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST API Controller for AWS IAM users.
 * Provides endpoints to retrieve user details.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves all IAM users from AWS.
     *
     * @return List of users with username, ID, MFA status and groups they belong to.
     */
    @GetMapping
    public List<UserDetails> getUsers() {
        return userService.getUsers();
    }
}
