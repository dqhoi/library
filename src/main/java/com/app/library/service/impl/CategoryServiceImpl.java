package com.app.library.service.impl;

import com.app.library.dto.CategoryDto;
import com.app.library.exception.object.*;
import com.app.library.model.Book;
import com.app.library.model.Category;
import com.app.library.model.Publisher;
import com.app.library.model.User;
import com.app.library.model.enum_class.RoleName;
import com.app.library.payload.PagedResponse;
import com.app.library.repository.BookRepository;
import com.app.library.repository.CategoryRepository;
import com.app.library.repository.UserRepository;
import com.app.library.service.ICategoryService;
import com.app.library.utils.AppUtils;
import com.app.library.utils.SecurityUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements ICategoryService {
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Override
    public PagedResponse<Category> getAllCategories(int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

        Page<Category> categories = categoryRepository.findAll(pageable);

        List<Category> content = categories.getNumberOfElements() == 0 ? Collections.emptyList() : categories.getContent();

        return new PagedResponse<>(content, categories.getNumber(), categories.getSize(), categories.getTotalElements(),
                categories.getTotalPages(), categories.isLast());
    }


    @Override
    public ResponseEntity<?> getCategory(int id) {
        try {
            Category category = categoryRepository.findById(id).orElseThrow();
            return new ResponseEntity<>(category, HttpStatus.OK);
        } catch (Exception e) {
            throw new ObjectException("No get detail category" );
        }
    }

    @Override
    public ResponseEntity<?> createCategory(CategoryDto dto) {
        if (SecurityUtil.hasCurrentUserAnyOfAuthorities("ADMIN_PERMISSION") ){
            Category category = new Category();

            BeanUtils.copyProperties(dto, category);
            category = save(category);

            dto.setCategoryId(category.getCategoryId());
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } else {
            throw new ForbiddenException("You don't have permission to access this resource.");
        }

    }
    @Override
    public Category updateCategory(int id, CategoryDto dto) {
        if (SecurityUtil.hasCurrentUserAnyOfAuthorities("ADMIN_PERMISSION") ){
            try {
                Optional<Category> existingCategory = categoryRepository.findById(id);

                if (existingCategory.isPresent()) {
                    Category categoryToUpdate = existingCategory.get();

                    // Cập nhật các thông tin từ dto
                    categoryToUpdate.setCategoryName(dto.getCategoryName());
                    categoryToUpdate.setCategoryDescription(dto.getCategoryDescription());
                    categoryToUpdate = save(categoryToUpdate);
                    return categoryToUpdate;
                } else {
                    throw new ObjectException("Category not found");
                }

            } catch (Exception e) {
                e.printStackTrace();
                throw new ObjectException("Category update failed");
            }
        } else {
            throw new ForbiddenException("You don't have permission to access this resource.");
        }
    }
    @Override
    public ResponseEntity<?> deleteCategory(int id) {
        if (SecurityUtil.hasCurrentUserAnyOfAuthorities("ADMIN_PERMISSION") ){
            try {
                Category existed = findById(id);
                categoryRepository.delete(existed);
                return new ResponseEntity<>("Category with Id " +id+" was deleted", HttpStatus.OK);
            } catch (Exception e) {
                throw new ObjectException("deleted category failed ");
            }
        }else {
            throw new ForbiddenException("You don't have permission to access this resource.");
        }
    }
    @Override
    public ResponseEntity<?> searchCategory(String keyword) {
        try {
            List<Category> categories = categoryRepository.findAll();
            List<Category> results =  categories.stream()
                    .filter(category -> category.getCategoryName().toLowerCase().contains(keyword.toLowerCase()))
                    .collect(Collectors.toList());
            if (results.size() == 0) {
                return new ResponseEntity<>("No result for " + keyword ,HttpStatus.OK);
            }
            return new ResponseEntity<List<Category>>(results, HttpStatus.OK);
        } catch (Exception e) {
            throw new ObjectException("Can't find category ");
        }
    }

    @Override
    public PagedResponse<Book> getBooksByCategoryId(int categoryId, int page, int size) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ObjectException("Publisher not found"));

        AppUtils.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

        // Lấy danh sách sách thuộc publisher theo phân trang
        Page<Book> books = bookRepository.findByCategory(category, pageable);

        List<Book> content = books.getNumberOfElements() == 0 ? Collections.emptyList() : books.getContent();

        return new PagedResponse<>(content, books.getNumber(), books.getSize(), books.getTotalElements(),
                books.getTotalPages(), books.isLast());
    }


    public Category findById(int id) {

        Optional<Category> found = categoryRepository.findById(id);
        if(found.isEmpty()) {
            throw new ObjectException("Category with id " + id + " doesn't exist");
        }

        return found.get();
    }
}
