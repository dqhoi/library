package com.app.library.repository;

import com.app.library.model.Author;
import com.app.library.model.Category;
import com.app.library.model.Publisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.app.library.model.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Integer> {
    Optional<Book> findByBookTitle(String bookTitle);
    @Query("SELECT b FROM Book b WHERE LOWER(b.category.categoryName) = LOWER(:categoryName)")
    List<Book> findByCategoryName(String categoryName);

    @Query("SELECT b FROM Book b WHERE LOWER(b.publisher.publisherName) = LOWER(:publisherName)")
    List<Book> findByPublisherName(@Param("publisherName") String publisherName);

    @Query("SELECT DISTINCT b FROM Book b JOIN b.authors a WHERE LOWER(a.authorFullName) = LOWER(:authorFullName)")
    List<Book> findBooksByAuthorNameIgnoreCase(@Param("authorFullName") String authorFullName);

    Page<Book> findByCreatedBy(int id, Pageable pageable);
    Page<Book> findByPublisher(Publisher publisher, Pageable pageable);

    Page<Book> findByCategory(Category category , Pageable pageable);

    @Query("SELECT b FROM Book b JOIN b.authors a WHERE a.authorId = :authorId")
    Page<Book> findAuthorById(int authorId, Pageable pageable);


    @Query("SELECT DISTINCT b FROM Book b " +
            "JOIN b.authors a " +
            "WHERE a.authorFullName LIKE %:authorName%")
    Page<Book> searchBooksByAuthorName(String authorName, Pageable pageable);

    @Query("SELECT b FROM Book b " +
            "WHERE b.category.categoryName LIKE %:categoryName%")
    Page<Book> searchBooksByCategoryName(String categoryName, Pageable pageable);

    @Query("SELECT b FROM Book b " +
            "WHERE b.publisher.publisherName LIKE %:publisherName%")
    Page<Book> searchBooksByPublisherName(String publisherName, Pageable pageable);


    List<Book> findByBookTitleContaining(String keyword);
}
