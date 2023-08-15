package com.app.library.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import java.util.*;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
@Data

@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "au_id", nullable = false)
    private int authorId;

    @Column(name = "au_fullname", nullable = false)
    private String authorFullName;

    @Column(name = "au_introduce")
    private String authorIntroduce;

    @Column(name = "au_image")
    private String authorImageUrl;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;

    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;
    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
    @JsonIgnore
    @ManyToMany(mappedBy = "authors", cascade = CascadeType.ALL)
    private List<Book> books;

    public void removeBook(Book book) {
        if (books.contains(book)) {
            books.remove(book);
            book.setAuthors(null);
        }
    }
}