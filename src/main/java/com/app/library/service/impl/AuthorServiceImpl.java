package com.app.library.service.impl;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.app.library.exception.object.ForbiddenException;
import com.app.library.exception.object.LibraryException;
import com.app.library.exception.object.UserNotFoundException;
import com.app.library.model.*;
import com.app.library.payload.PagedResponse;
import com.app.library.repository.BookRepository;
import com.app.library.utils.AppUtils;
import com.app.library.utils.SecurityUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.app.library.dto.AuthorDto;
import com.app.library.exception.object.ObjectException;
import com.app.library.repository.AuthorRepository;
import com.app.library.service.IAuthorService;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AuthorServiceImpl implements IAuthorService {
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private AmazonS3Service amazonS3Service;
    @Autowired
    private BookRepository bookRepository;

    public Author save(Author author) {
        return authorRepository.save(author);
    }

    @Override
    public ResponseEntity<?> getAuthor(int id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("can't find author id:" + id));
        return new ResponseEntity<>(author, HttpStatus.OK);
    }

    @Override
    public PagedResponse<Author> getAllAuthors(int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

        Page<Author> authors = authorRepository.findAll(pageable);

        List<Author> content = authors.getNumberOfElements() == 0 ? Collections.emptyList() : authors.getContent();

        return new PagedResponse<>(content, authors.getNumber(), authors.getSize(), authors.getTotalElements(),
                authors.getTotalPages(), authors.isLast());
    }

    @Override
    public PagedResponse<Book> getBooksByAuthorId(int authorId, int page, int size) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new ObjectException("Author not found"));

        AppUtils.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

        Page<Book> books = bookRepository.findAuthorById(authorId, pageable);

        List<Book> content = books.getNumberOfElements() == 0 ? Collections.emptyList() : books.getContent();

        return new PagedResponse<>(content, books.getNumber(), books.getSize(), books.getTotalElements(),
                books.getTotalPages(), books.isLast());
    }

    @Override
    public Author updateAuthor(int id, AuthorDto dto) {
        if (!SecurityUtil.hasCurrentUserAnyOfAuthorities("ADMIN_PERMISSION") ){
            try {
                Optional<Author> existAuthor = authorRepository.findById(id);

                if (existAuthor.isPresent()) {
                    Author authorUpdate = existAuthor.get();
                    authorUpdate.setAuthorFullName(dto.getAuthorFullName());
                    authorUpdate.setAuthorIntroduce(dto.getAuthorIntroduce());

                    authorUpdate = save(authorUpdate);
                    return authorUpdate;
                } else {
                    throw new ObjectException("Author not found");
                }
            } catch (Exception e) {
                // Log thông tin lỗi
                e.printStackTrace();
                throw new ObjectException("Author creation failed ");
            }
        } else {
            throw new ForbiddenException("You don't have permission to access this resource.");
        }
    }

    @Override
    public Author updateAuthorUrl(int id, MultipartFile authorImage )throws IOException {

        if (!SecurityUtil.hasCurrentUserAnyOfAuthorities("ADMIN_PERMISSION")){
            Author author = authorRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("Author not found with id: " + id));
            try {
                String imageUrl = amazonS3Service.uploadFile(authorImage, "authors");
                author.setAuthorImageUrl(imageUrl);
                Author updatedAuthor = authorRepository.save(author);

                return updatedAuthor;
            } catch (IOException e) {
                throw new LibraryException(HttpStatus.BAD_REQUEST, "Failed to update image");
            }
        } else {
            throw new ForbiddenException("You don't have permission to access this resource.");
        }
    }

    @Override
    public ResponseEntity<?> deleteAuthor(int id) {
        if (SecurityUtil.hasCurrentUserAnyOfAuthorities("ADMIN_PERMISSION") ){
            try {
                Author existed = authorRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("can't find author id:" + id));
                authorRepository.delete(existed);
                return new ResponseEntity<>("Author with Id " + id + " was deleted", HttpStatus.OK);
            } catch (Exception e) {
                throw new ObjectException("deleted author failed ");
            }
        } else {
            throw new ForbiddenException("You don't have permission to access this resource.");
        }
    }


}
