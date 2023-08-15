package com.app.library.repository;

import com.app.library.exception.object.ResourceNotFoundException;
import com.app.library.model.Book;
import com.app.library.model.Category;
import com.app.library.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PublisherRepository extends JpaRepository<Publisher, Integer> {
    Optional<Publisher> findByPublisherName(String publisherName);
    default Publisher getByPublisherName(String publisherName) {
        return findByPublisherName(publisherName)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher", "Publisher name", publisherName));
    }
    @Query("SELECT p FROM Publisher p WHERE p.publisherName LIKE %?1%")
    List<Publisher> searchPublishersByKeyword(String keyword);
}
