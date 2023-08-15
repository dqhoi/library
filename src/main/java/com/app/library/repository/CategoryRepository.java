package com.app.library.repository;

import com.app.library.exception.object.ResourceNotFoundException;
import com.app.library.model.Book;
import com.app.library.model.Category;
import com.app.library.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findByCategoryNameStartsWith(String categoryName, Pageable pageable);
    Optional<Category> findByCategoryName(String categoryName);

    default Category getByCategoryName(String categoryName) {
        return findByCategoryName(categoryName)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "category name", categoryName));
    }
    @Query("SELECT c FROM Category c WHERE c.categoryName LIKE %?1%")
    List<Category> searchCategoriesByKeyword(@Param("keyword") String keyword);

}