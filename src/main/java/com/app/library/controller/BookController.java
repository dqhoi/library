package com.app.library.controller;

import com.app.library.dto.BookDto;
import com.app.library.model.Author;
import com.app.library.model.Category;
import com.app.library.payload.PagedResponse;
import com.app.library.service.impl.MapValidationErrorService;
import com.app.library.utils.AppConstants;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.app.library.model.Book;
import com.app.library.service.impl.BookServiceImpl;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@CrossOrigin(origins = "*")
public class BookController {

    @Autowired
    BookServiceImpl bookServiceImpl;
    @Autowired
    MapValidationErrorService mapValidationErrorService;
    @RequestMapping("{id}")
    public ResponseEntity<?> getBookById(@PathVariable(name="id") int id ){

        return bookServiceImpl.getBook(id);
    }

    @PostMapping
    public ResponseEntity<?> addBook(@Valid @RequestBody BookDto dto,
                                     BindingResult result) {
        ResponseEntity<?> responseEntity = mapValidationErrorService.mapValidationFields(result);
        if (responseEntity != null) {
            return responseEntity;
        }
        return bookServiceImpl.addBook(dto);
    }

    @GetMapping
    public PagedResponse<Book> getAllBooks(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {
        return bookServiceImpl.getAllBooks(page, size);
    }

    @GetMapping("/search")
    public PagedResponse<Book> searchBooks(
            @RequestParam("keyword") String keyword,
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {
        return bookServiceImpl.searchBook(keyword,page,size);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(
            @Valid @PathVariable(name="id") int id,
            @Valid @RequestBody BookDto dto ,
            BindingResult result) {

        ResponseEntity<?> responseEntity =  mapValidationErrorService.mapValidationFields(result);
        if(responseEntity != null) {
            return responseEntity;
        }
        return bookServiceImpl.updateBook(id,dto);
    }
    @PostMapping("/update-image/{id}")
    public ResponseEntity<Book> updateImageBook(@PathVariable int id,
                                                @RequestParam("bookImageLink") MultipartFile bookImageLink) throws IOException {
        Book updatedBook = bookServiceImpl.updateImageBook(id,bookImageLink);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable(name="id") int id) {
        return bookServiceImpl.deleteBook(id);
    }


}