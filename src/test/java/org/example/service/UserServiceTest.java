package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.UserDetails;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Unit test for {@link UserService}.
 *
 * Ensures correct interaction between the service layer and repository.
 */
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private List<UserDetails> testUsers;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        testUsers = loadTestUsers();
    }

    /**
     * Loads test user data from a JSON file located in src/test/resources.
     */
    private List<UserDetails> loadTestUsers() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = new String(Files.readAllBytes(Paths.get("src/test/resources/test-users.json")));
        return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, UserDetails.class));
    }

    @Test
    void shouldReturnListOfUsers() {
        // Given (Mock Repository Response)
        when(userRepository.getUsers()).thenReturn(testUsers);

        // When (Fetching users from service)
        List<UserDetails> users = userService.getUsers();

        // Then (Verify correct data retrieval)
        assertEquals(testUsers.size(), users.size(), "Expected number of users to match test data.");
        assertEquals(testUsers.get(0).getUserName(), users.get(0).getUserName(), "User 1 name mismatch.");
        assertEquals(testUsers.get(1).getUserName(), users.get(1).getUserName(), "User 2 name mismatch.");
    }
}
