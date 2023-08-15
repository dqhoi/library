package com.app.library.dto;

import com.app.library.model.Author;
import com.app.library.model.Book;
import com.app.library.model.Category;
import com.app.library.model.Publisher;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link Book}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto implements Serializable {
    int bookId;
    String bookTitle;
    int bookPublishedYear;
    int bookQuantity;
    String bookDescription;
    private MultipartFile bookImageLink;

    List<Author> authors;
    Category category;
    Publisher publisher;

}