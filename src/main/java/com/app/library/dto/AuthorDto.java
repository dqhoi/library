package com.app.library.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * DTO for {@link com.app.library.model.Author}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDto implements Serializable {
    int authorId;
    String authorFullName;
    String authorIntroduce;
    private MultipartFile authorImageUrl;
}