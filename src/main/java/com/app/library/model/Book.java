package com.app.library.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data

@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bo_id", nullable = false)
    private int bookId;

    @Column(name = "bo_title", nullable = false)
    private String bookTitle;

    @Column(name = "bo_publishedYear", nullable = false)
    private int bookPublishedYear;

    @Column(name = "bo_quantity", nullable = false)
    private int bookQuantity;

    @Column(name = "bo_description")
    private String bookDescription;

    @Column(name = "bo_image")
    private String bookImageLink;

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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ca_id")
    private Category category;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pu_id")
    private Publisher publisher;


    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "book_author", joinColumns = @JoinColumn(name = "bo_id", referencedColumnName = "bo_id"), inverseJoinColumns = @JoinColumn(name = "au_id", referencedColumnName = "au_id"))
    private List<Author> authors;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    public Book(String bookTitle, int bookPublishedYear, int bookQuantity, String bookDescription, String bookImageLink
                ) {
        this.bookTitle = bookTitle;
        this.bookPublishedYear = bookPublishedYear;
        this.bookQuantity = bookQuantity;
        this.bookDescription = bookDescription;
        this.bookImageLink = bookImageLink;
    }

}
