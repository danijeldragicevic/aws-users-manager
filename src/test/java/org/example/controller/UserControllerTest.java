package org.example.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.UserDetails;
import org.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit test for {@link UserController}.
 *
 * Ensures correct API behavior when retrieving users.
 */
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private List<UserDetails> testUsers;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        testUsers = loadTestUsers();
    }

    /**
     * Loads test user data from a JSON file located in src/test/resources.
     */
    private List<UserDetails> loadTestUsers() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = new String(Files.readAllBytes(Paths.get("src/test/resources/test-users.json")));
        return objectMapper.readValue(json, new TypeReference<>() {});
    }

    @Test
    void shouldReturnListOfUsers() throws Exception {
        // Given (Mock Service Response)
        when(userService.getUsers()).thenReturn(testUsers);

        // When & Then (Perform GET Request & Validate Response)
        mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(testUsers.size()))
                .andExpect(jsonPath("$[0].userName").value(testUsers.get(0).getUserName()))
                .andExpect(jsonPath("$[0].userId").value(testUsers.get(0).getUserId()))
                .andExpect(jsonPath("$[0].mfaEnabled").value(testUsers.get(0).isMfaEnabled()))
                .andExpect(jsonPath("$[0].groups").isArray());
    }
}
