package com.app.library.service;

import com.app.library.dto.PublisherDto;
import com.app.library.model.Book;
import com.app.library.model.Category;
import com.app.library.model.Publisher;
import com.app.library.payload.PagedResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IPublisherService {
    public ResponseEntity<?> getPublisher(int id);
    public PagedResponse<Publisher> getAllPublishers(int page, int size);
    public Publisher updatePublisher(int id, PublisherDto dto);
    public Publisher updateImagePublisher(int id, MultipartFile publisherImage);
    PagedResponse<Book> getBooksByPublisherId(int publisherId, int page, int size);
}
