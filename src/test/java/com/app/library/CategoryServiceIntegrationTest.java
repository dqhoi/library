package com.app.library;

import com.app.library.dto.CategoryDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "testuser", roles = "ADMIN")
    public void testUpdateCategoryWithCurrentUser() throws Exception {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setCategoryName("Test Category");
        categoryDto.setCategoryDescription("Test Description");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(categoryDto))) // Chuyển CategoryDto thành JSON string
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.categoryName").value("Test Category"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.categoryDescription").value("Test Description"));
    }


    private String asJsonString(Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
