package com.app.library.dto;

import com.app.library.model.enum_class.RoleName;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.app.library.model.User}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Serializable {
    int userId;
    String username;
    private String firstName;
    private String lastName;
    String address;
    String email;
    RoleName roleName;
    private String avatarUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}