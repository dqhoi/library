package com.app.library.service.impl;
import com.app.library.dto.BookDto;
import com.app.library.exception.object.ForbiddenException;
import com.app.library.exception.object.LibraryException;
import com.app.library.exception.object.ObjectException;
import com.app.library.exception.object.UserNotFoundException;
import com.app.library.model.Author;
import com.app.library.model.Category;
import com.app.library.model.Publisher;
import com.app.library.payload.PagedResponse;
import com.app.library.repository.AuthorRepository;
import com.app.library.repository.CategoryRepository;
import com.app.library.repository.PublisherRepository;
import com.app.library.utils.AppUtils;
import com.app.library.utils.SecurityUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import com.app.library.model.Book;
import com.app.library.repository.BookRepository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements com.app.library.service.IBookService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PublisherRepository publisherRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private AmazonS3Service amazonS3Service;

    public Book save(Book book) {
        return bookRepository.save(book);
    }
    @Override
    public ResponseEntity<?> getBook(int id) {
        try {
            Book book = bookRepository.findById(id).orElseThrow(() -> new ObjectException( " "+ id));
            return new ResponseEntity<>(book, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ObjectException("No get detail book" );
        }
    }

    @Override
    public PagedResponse<Book> getAllBooks(int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

        Page<Book> books = bookRepository.findAll(pageable);

        List<Book> content = books.getNumberOfElements() == 0 ? Collections.emptyList() :books.getContent();

        return new PagedResponse<>(content, books.getNumber(), books.getSize(), books.getTotalElements(),
                books.getTotalPages(), books.isLast());
    }


    public Page<Book> findAll(Pageable pageable)  {
        return bookRepository.findAll(pageable);
    }


    @Override
    public ResponseEntity<?> addBook(BookDto dto) {
        if (SecurityUtil.hasCurrentUserAnyOfAuthorities("ADMIN_PERMISSION") ){
            try {
                Optional<Book> existingBook = bookRepository.findByBookTitle(dto.getBookTitle());

                if (existingBook.isPresent()) {
                    // Nếu sách đã tồn tại, chỉ cập nhật quantity và thông tin liên quan nếu có
                    Book bookToUpdate = existingBook.get();
                    bookToUpdate.setBookQuantity(bookToUpdate.getBookQuantity() + dto.getBookQuantity());

                    checkExistCategory(dto);

                    checkExistPublisher(dto);

                    checkExistAuthors(dto,bookToUpdate);



                    bookToUpdate = save(bookToUpdate);
                    return new ResponseEntity<>(bookToUpdate, HttpStatus.CREATED);

                }

                // Tạo mới sách và lưu vào cơ sở dữ liệu
                Book newBook = new Book();
                checkExistCategory(dto);
                checkExistPublisher(dto);
                checkExistAuthors(dto,newBook);
                BeanUtils.copyProperties(dto, newBook);


                newBook = save(newBook);
                dto.setBookId(newBook.getBookId());


                return new ResponseEntity<>(dto, HttpStatus.CREATED);


            } catch (Exception e) {
                // Log thông tin lỗi
                e.printStackTrace();
                throw new ObjectException("Book creation failed ");
            }
        } else {
            throw new ForbiddenException("You don't have permission to access this resource.");
        }
    }

    private void checkExistCategory(BookDto dto) {
        // Kiểm tra  category nếu tồn tại trong dto
        if (dto.getCategory() != null) {
            Category category = dto.getCategory();
            Optional<Category> existingCategory =categoryRepository.findByCategoryName(dto.getCategory().getCategoryName());
            if (!existingCategory.isPresent()) {
                categoryRepository.save(category);
            } else {
              dto.setCategory(existingCategory.get());
            }
        }
    }

    private void checkExistPublisher(BookDto dto) {
        // Kiểm tra  publisher nếu tồn tại trong dto
       if(dto.getPublisher() != null) {
           Publisher publisher = dto.getPublisher();
           Optional<Publisher> existingPublisher = publisherRepository.findByPublisherName(dto.getPublisher().getPublisherName());
           if (!existingPublisher.isPresent()) {
               publisherRepository.save(publisher);
           } else {
               dto.setPublisher(existingPublisher.get());
           }
       }
    }
    private void checkExistAuthors(BookDto dto, Book book) {
        if (dto.getAuthors() != null && !dto.getAuthors().isEmpty()) {
            List<Author> authors = new ArrayList<>();
            for (Author author : dto.getAuthors()) {
                Optional<Author> existingAuthor = authorRepository.findByAuthorFullName(author.getAuthorFullName());
                if (existingAuthor.isPresent()) {
                    authors.add(existingAuthor.get()); // Sử dụng tác giả đã tồn tại trong cơ sở dữ liệu
                    dto.setAuthors(authors);
                } else {
                    // Tạo một tác giả mới chỉ khi tác giả chưa tồn tại trong cơ sở dữ liệu
                    authorRepository.save(author);
                }
            }
        }
    }
    @Override
    public ResponseEntity<?> updateBook(int id, BookDto dto) {
        if (SecurityUtil.hasCurrentUserAnyOfAuthorities("ADMIN_PERMISSION") ){
            try {
                Optional<Book> existingBook = bookRepository.findById(id);

                if (existingBook.isPresent()) {
                    Book bookToUpdate = existingBook.get();

                    // Cập nhật các thông tin từ dto
                    bookToUpdate.setBookTitle(dto.getBookTitle());
                    bookToUpdate.setBookQuantity(dto.getBookQuantity());
                    bookToUpdate.setBookDescription(dto.getBookDescription());
                    bookToUpdate.setBookPublishedYear(dto.getBookPublishedYear());
                    // Cập nhật các thông tin liên quan khác từ dto
                    checkExistCategory(dto);
                    checkExistPublisher(dto);
                    checkExistAuthors(dto, bookToUpdate);

                    bookToUpdate = save(bookToUpdate);
                    return new ResponseEntity<>(bookToUpdate, HttpStatus.OK);
                } else {
                    throw new ObjectException("Book not found");
                }

            } catch (Exception e) {
                e.printStackTrace();
                throw new ObjectException("Book update failed");
            }
        } else {
            throw new ForbiddenException("You don't have permission to access this resource.");
        }

    }

    @Override
    public Book updateImageBook(int id, MultipartFile bookImageLink) throws IOException {
        if (SecurityUtil.hasCurrentUserAnyOfAuthorities("ADMIN_PERMISSION")){
            Book book = bookRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("Author not found with id: " + id));
            try {
                String imageUrl = amazonS3Service.uploadFile(bookImageLink, "books");
                book.setBookImageLink(imageUrl);
                Book updatedBook = bookRepository.save(book);

                return updatedBook;
            } catch (IOException e) {
                throw new LibraryException(HttpStatus.BAD_REQUEST, "Failed to update image");
            }
        } else {
            throw new ForbiddenException("You don't have permission to access this resource.");
        }
    }


    @Override
    public ResponseEntity<?> deleteBook(int id) {
        if (SecurityUtil.hasCurrentUserAnyOfAuthorities("ADMIN_PERMISSION") ){
            try {
                Book existed = findById(id);
                // Kiểm tra xem cuốn sách có liên kết đến nhà xuất bản hay không
                Publisher publisher = existed.getPublisher();

                if (publisher != null) {
                    publisher.removeBook(existed);
                }
                Category category = existed.getCategory();
                if (category != null) {
                    category.removeBook(existed);
                }
                List<Author> authors = existed.getAuthors();

                for (Author author : authors) {
                    if (author != null) {
                        author.removeBook(existed);
                    }
                }

                bookRepository.delete(existed);
                return new ResponseEntity<>("Book with Id " + id +" was deleted", HttpStatus.OK);
            } catch (Exception e) {
                throw new ObjectException("deleted book failed ");
            }
        } else {
            throw new ForbiddenException("You don't have permission to access this resource.");
        }
    }


    private Book findById(int id) {
        Optional<Book> found = bookRepository.findById(id);
        if(found.isEmpty()) {
            throw new ObjectException("Book with id " + id + " doesn't exist");
        }
        return found.get();
    }

    @Override
    public PagedResponse<Book> searchBook(String keyword, int page, int size) {
        List<Book> result = new ArrayList<>();


        // Tìm kiếm sách theo tiêu đề
        List<Book> booksByTitle = bookRepository.findByBookTitleContaining(keyword);
        result.addAll(booksByTitle);

        // Tìm kiếm sách theo tác giả
        List<Author> authors = authorRepository.findByAuthorFullNameContaining(keyword);
        for (Author author : authors) {
            result.addAll(searchBookByAuthor(author.getAuthorFullName(), page, size).getContent());
        }

        // Tìm kiếm sách theo thể loại
        List<Category> categories = categoryRepository.searchCategoriesByKeyword(keyword);
        for (Category category : categories) {
            result.addAll(searchBookByCategory(category.getCategoryName(), page, size).getContent());
        }

        // Tìm kiếm sách theo nhà xuất bản
        List<Publisher> publishers = publisherRepository.searchPublishersByKeyword(keyword);
        for (Publisher publisher : publishers) {
            result.addAll(searchBookByPublisher(publisher.getPublisherName(), page, size).getContent());
        }

        // Tạo một danh sách sách duy nhất từ kết quả các hàm search riêng
        Set<Book> uniqueBooks = new HashSet<>(result);

        List<Book> content = new ArrayList<>(uniqueBooks);

        // Phân trang cho danh sách kết quả
        int totalItems = content.size();
        int totalPages = (int) Math.ceil((double) totalItems / size);
        int start = page * size;
        int end = Math.min(start + size, totalItems);
        content = content.subList(start, end);

        return new PagedResponse<>(content, page, size, totalItems, totalPages, page == totalPages - 1);
    }

    @Override
    public PagedResponse<Book> searchBookByAuthor(String authorName, int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

        Page<Book> searchedBooks = bookRepository.searchBooksByAuthorName(authorName, pageable);

        List<Book> content = searchedBooks.getNumberOfElements() == 0 ? Collections.emptyList() : searchedBooks.getContent();

        return new PagedResponse<>(content, searchedBooks.getNumber(), searchedBooks.getSize(), searchedBooks.getTotalElements(),
                searchedBooks.getTotalPages(), searchedBooks.isLast());
    }

    @Override
    public PagedResponse<Book> searchBookByCategory(String categoryName, int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

        Page<Book> searchedBooks = bookRepository.searchBooksByCategoryName(categoryName, pageable);

        List<Book> content = searchedBooks.getNumberOfElements() == 0 ? Collections.emptyList() : searchedBooks.getContent();

        return new PagedResponse<>(content, searchedBooks.getNumber(), searchedBooks.getSize(), searchedBooks.getTotalElements(),
                searchedBooks.getTotalPages(), searchedBooks.isLast());
    }

    @Override
    public PagedResponse<Book> searchBookByPublisher(String publisherName, int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

        Page<Book> searchedBooks = bookRepository.searchBooksByPublisherName(publisherName, pageable);

        List<Book> content = searchedBooks.getNumberOfElements() == 0 ? Collections.emptyList() : searchedBooks.getContent();

        return new PagedResponse<>(content, searchedBooks.getNumber(), searchedBooks.getSize(), searchedBooks.getTotalElements(),
                searchedBooks.getTotalPages(), searchedBooks.isLast());
    }
}