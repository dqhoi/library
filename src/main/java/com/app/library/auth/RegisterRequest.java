package com.app.library.auth;

import com.app.library.model.enum_class.RoleName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String address;
    private String email;
    private String firstName;
    private String lastName;
    private String username;

    private String password;
    private RoleName roleName;
    private MultipartFile avatarFile;
}
