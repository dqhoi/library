package com.app.library.service.impl;

import com.app.library.dto.PublisherDto;
import com.app.library.exception.object.ForbiddenException;
import com.app.library.exception.object.LibraryException;
import com.app.library.exception.object.ObjectException;
import com.app.library.exception.object.UserNotFoundException;
import com.app.library.model.Book;
import com.app.library.model.Publisher;
import com.app.library.payload.PagedResponse;
import com.app.library.repository.BookRepository;
import com.app.library.repository.PublisherRepository;
import com.app.library.service.IPublisherService;
import com.app.library.utils.AppUtils;
import com.app.library.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PublisherServiceImpl implements IPublisherService {

    @Autowired
    private PublisherRepository publisherRepository;
    @Autowired
    private AmazonS3Service amazonS3Service;
    @Autowired
    private BookRepository bookRepository;
    @Override
    public ResponseEntity<?> getPublisher(int id) {
        try {
            Publisher publisher = publisherRepository.findById(id).orElseThrow(() -> new RuntimeException("can't find loan id:" + id));
            return new ResponseEntity<>(publisher, HttpStatus.OK);
        } catch (Exception e) {
            throw  new ObjectException("No get detail publisher");
        }
    }

    @Override
    public PagedResponse<Publisher> getAllPublishers(int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

        Page<Publisher> publishers = publisherRepository.findAll(pageable);

        List<Publisher> content = publishers.getNumberOfElements() == 0 ? Collections.emptyList() : publishers.getContent();

        return new PagedResponse<>(content, publishers.getNumber(), publishers.getSize(), publishers.getTotalElements(),
                publishers.getTotalPages(), publishers.isLast());
    }

    @Override
    public Publisher updatePublisher(int id, PublisherDto dto) {
        if (SecurityUtil.hasCurrentUserAnyOfAuthorities("ADMIN_PERMISSION") ){
            try {
                Optional<Publisher> existingPublisher = publisherRepository.findById(id);

                if (existingPublisher.isPresent()) {
                    Publisher publisherToUpdate = existingPublisher.get();

                    // Cập nhật các thông tin từ dto
                    publisherToUpdate.setPublisherName(dto.getPublisherName());
                    publisherToUpdate.setPublisherIntroduce(dto.getPublisherIntroduce());
                    publisherToUpdate.setPublisherWebsiteUrl(dto.getPublisherWebsiteUrl());

                    publisherToUpdate = publisherRepository.save(publisherToUpdate);
                    return  publisherToUpdate;
                } else {
                    throw new ObjectException("Publisher not found");
                }

            } catch (Exception e) {
                e.printStackTrace();
                throw new ObjectException("Publisher update failed");
            }
        } else {
            throw new ForbiddenException("You don't have permission to access this resource.");
        }

    }

    @Override
    public Publisher updateImagePublisher(int id, MultipartFile publisherImage) {
        if (SecurityUtil.hasCurrentUserAnyOfAuthorities("ADMIN_PERMISSION")){
            Publisher publisher = publisherRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("Author not found with id: " + id));
            try {
                String imageUrl = amazonS3Service.uploadFile(publisherImage, "publishers");
                publisher.setPublisherImageUrl(imageUrl);
                Publisher updatedPublisher = publisherRepository.save(publisher);

                return updatedPublisher;
            } catch (IOException e) {
                throw new LibraryException(HttpStatus.BAD_REQUEST, "Failed to update image");
            }
        } else {
            throw new ForbiddenException("You don't have permission to access this resource.");
        }
    }

    @Override
    public PagedResponse<Book> getBooksByPublisherId(int publisherId, int page, int size) {
        Publisher publisher = publisherRepository.findById(publisherId)
                .orElseThrow(() -> new ObjectException("Publisher not found"));

        AppUtils.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

        // Lấy danh sách sách thuộc publisher theo phân trang
        Page<Book> books = bookRepository.findByPublisher(publisher, pageable);

        List<Book> content = books.getNumberOfElements() == 0 ? Collections.emptyList() : books.getContent();

        return new PagedResponse<>(content, books.getNumber(), books.getSize(), books.getTotalElements(),
                books.getTotalPages(), books.isLast());
    }

}
