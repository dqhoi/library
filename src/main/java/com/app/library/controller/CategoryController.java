package com.app.library.controller;

import com.app.library.dto.CategoryDto;
import com.app.library.model.Book;
import com.app.library.model.Category;
import com.app.library.payload.PagedResponse;
import com.app.library.service.impl.CategoryServiceImpl;
import com.app.library.service.impl.MapValidationErrorService;
import com.app.library.utils.AppConstants;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categories")
@CrossOrigin(origins = "*")
public class CategoryController {
    @Autowired
    CategoryServiceImpl categoryServiceImpl;
    @Autowired
    MapValidationErrorService mapValidationErrorService;
    @PostMapping
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryDto dto,
                                            BindingResult result){
        ResponseEntity<?> responseEntity = mapValidationErrorService.mapValidationFields(result);
        if(responseEntity != null) {
            return responseEntity;
        }
        return categoryServiceImpl.createCategory(dto);
    }
    @GetMapping
    public PagedResponse<Category> getAllCategories(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {
        return categoryServiceImpl.getAllCategories(page, size);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchCategory(@RequestParam("keyword") String keyword) {
        return categoryServiceImpl.searchCategory(keyword);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategory(@PathVariable(name = "id") int id) {
        return categoryServiceImpl.getCategory(id);
    }

    @GetMapping("/books/{id}")
    public PagedResponse<Book> getBooksByCategoryId(
            @Valid @PathVariable(name = "id") int id,
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size
    ) {
        PagedResponse<Book> pagedResponse = categoryServiceImpl.getBooksByCategoryId(id, page, size);
        return pagedResponse;
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(
            @Valid  @PathVariable("id") int id,
            @Valid  @RequestBody CategoryDto dto,
            BindingResult result) {
        ResponseEntity<?> responseEntity = mapValidationErrorService.mapValidationFields(result);
        if(responseEntity != null) {
            return responseEntity;
        }
        Category updated = categoryServiceImpl.updateCategory(id,dto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") int id) {
        return categoryServiceImpl.deleteCategory(id);
    }

}
