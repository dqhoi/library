package com.app.library.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Data

@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "publishers")
public class Publisher {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pu_id")
	private int publisherId;

	@Column(name = "pu_name", nullable = false, length = 45)
	private String publisherName;

	@Column(name = "pu_introduce")
	private String publisherIntroduce;

	@Column(name = "pu_website")
	private String publisherWebsiteUrl;

	@Column(name = "pu_image")
	private String publisherImageUrl;

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
	@OneToMany(mappedBy = "publisher", cascade = CascadeType.ALL)
	private List<Book> books;

	public void removeBook(Book book) {
		// Kiểm tra xem cuốn sách có tồn tại trong danh sách cuốn sách của nhà xuất bản hay không
		if (books.contains(book)) {
			books.remove(book);
			book.setPublisher(null); // set publisher thành null để gỡ bỏ liên kết
		}
	}
}
