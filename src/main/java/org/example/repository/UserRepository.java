package org.example.repository;

import org.example.exception.AwsServiceException;
import org.example.model.UserDetails;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.iam.IamClient;
import software.amazon.awssdk.services.iam.model.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Repository layer for handling IAM user operations.
 */
@Repository
public class UserRepository {

    private final IamClient iamClient;

    public UserRepository(IamClient iamClient) {
        this.iamClient = iamClient;
    }

    /**
     * Retrieves all IAM users.
     *
     * @return List of IAM user details (username, ID, MFA status, groups).
     */
    public List<UserDetails> getUsers() {
        try {
            ListUsersResponse response = iamClient.listUsers(ListUsersRequest.builder().build());
            return response.users().stream()
                .map(awsUser -> {
                    boolean mfaEnabled = isMfaEnabled(awsUser.userName());
                    List<String> groups = getUserGroups(awsUser.userName());
                    return new UserDetails(awsUser.userName(), awsUser.userId(), mfaEnabled, groups);
                })
                .collect(Collectors.toList());
        } catch (IamException e) {
            throw new AwsServiceException("Failed to retrieve users from AWS", e);
        }
    }

    /**
     * Checks if a given IAM user has Multi-Factor Authentication (MFA) enabled.
     *
     * @param username IAM username
     * @return true if MFA is enabled, false otherwise
     */
    private boolean isMfaEnabled(String userName) {
        try {
            ListMfaDevicesResponse mfaResponse = iamClient.listMFADevices(ListMfaDevicesRequest.builder().userName(userName).build());
            return !mfaResponse.mfaDevices().isEmpty();
        } catch (IamException e) {
            throw new AwsServiceException("Failed to check MFA status for user: " + userName, e);
        }

    }

    /**
     * Retrieves the groups that a given IAM user belongs to.
     *
     * @param username IAM username
     * @return List of group names
     */
    private List<String> getUserGroups(String userName) {
        try {
            ListGroupsForUserResponse groupsResponse = iamClient.listGroupsForUser(ListGroupsForUserRequest.builder().userName(userName).build());
            return groupsResponse.groups().stream()
                    .map(Group::groupName)
                    .collect(Collectors.toList());
        } catch (IamException e) {
            throw new AwsServiceException("Failed to retrieve groups for user: " + userName, e);
        }
    }
}
