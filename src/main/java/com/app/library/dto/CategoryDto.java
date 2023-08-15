package com.app.library.dto;

import com.app.library.model.Book;
import com.app.library.model.Category;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link Category}
 */
@Data
@AllArgsConstructor
public class CategoryDto implements Serializable {
    int categoryId;
    String categoryName;
    String categoryDescription;
    List<Book> books;

    public CategoryDto(String categoryName) {
    }

    public CategoryDto() {

    }
}