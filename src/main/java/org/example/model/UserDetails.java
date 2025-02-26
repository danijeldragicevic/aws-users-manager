package org.example.model;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDetails {
    private String userName;
    private String userId;
    private boolean mfaEnabled;
    private List<String> groups;
}
