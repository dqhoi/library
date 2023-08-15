package com.app.library.service;

import com.app.library.dto.AuthorDto;
import com.app.library.model.Author;
import com.app.library.model.Book;
import com.app.library.payload.PagedResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IAuthorService {
    public ResponseEntity<?> getAuthor(int id);

    public PagedResponse<Author> getAllAuthors(int page, int size);

    public Author updateAuthor(int id, AuthorDto dto);

    public Author updateAuthorUrl(int id , MultipartFile authorImage) throws IOException;

    public ResponseEntity<?> deleteAuthor(int id);

    PagedResponse<Book> getBooksByAuthorId(int authorId, int page, int size);
}
