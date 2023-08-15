package com.app.library.repository;

import com.app.library.exception.object.ResourceNotFoundException;
import com.app.library.model.Author;
import com.app.library.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
    @Override
    Optional<Author> findById(Integer integer);
    Optional<Author> findByAuthorFullName(String authorFullName);
    default Author getByAuthorName(String authorName) {
        return findByAuthorFullName(authorName)
                .orElseThrow(() -> new ResourceNotFoundException("Author", "Author name", authorName));
    }

    List<Author> findByAuthorFullNameContaining(String keyword);
}
