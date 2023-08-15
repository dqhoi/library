package com.app.library.service;

import com.app.library.dto.CategoryDto;
import com.app.library.model.Book;
import com.app.library.model.Category;
import com.app.library.model.User;
import com.app.library.payload.PagedResponse;
import org.springframework.http.ResponseEntity;

public interface ICategoryService {
    PagedResponse<Category> getAllCategories(int page, int size);
    ResponseEntity<?> getCategory(int id);
    ResponseEntity<?> createCategory(CategoryDto dto) ;
    Category updateCategory(int id, CategoryDto dto);
    ResponseEntity<?> deleteCategory(int id);
    ResponseEntity<?> searchCategory(String keyword);
    PagedResponse<Book> getBooksByCategoryId(int categoryId, int page, int size);
}
