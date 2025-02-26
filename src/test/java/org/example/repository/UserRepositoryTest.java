package org.example.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.UserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.services.iam.IamClient;
import software.amazon.awssdk.services.iam.model.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/*
* Unit test for {@link UserRepository}.
*
* Validates interactions with AWS IAM service using mocked responses.
* */
class UserRepositoryTest {

    @Mock
    private IamClient iamClient;

    @InjectMocks
    private UserRepository userRepository;

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
        // Given (Mock IAM Users)
        User awsUser1 = User.builder().userName(testUsers.get(0).getUserName()).userId(testUsers.get(0).getUserId()).build();
        User awsUser2 = User.builder().userName(testUsers.get(1).getUserName()).userId(testUsers.get(1).getUserId()).build();

        ListUsersResponse listUsersResponse = ListUsersResponse.builder().users(awsUser1, awsUser2).build();

        when(iamClient.listUsers(any(ListUsersRequest.class)))
                .thenReturn(listUsersResponse);
        when(iamClient.listMFADevices(any(ListMfaDevicesRequest.class)))
                .thenReturn(ListMfaDevicesResponse.builder().mfaDevices(List.of()).build());
        when(iamClient.listGroupsForUser(any(ListGroupsForUserRequest.class)))
                .thenReturn(ListGroupsForUserResponse.builder().groups(List.of()).build());

        // When
        List<UserDetails> users = userRepository.getUsers();

        // Then
        assertEquals(2, users.size());
        assertEquals(testUsers.get(0).getUserName(), users.get(0).getUserName());
        assertEquals(testUsers.get(1).getUserName(), users.get(1).getUserName());
    }
}
