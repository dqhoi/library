package com.app.library.controller;

import com.app.library.auth.RegisterRequest;
import com.app.library.dto.UserDto;
import com.app.library.exception.object.LibraryException;
import com.app.library.model.Category;
import com.app.library.model.User;
import com.app.library.payload.PagedResponse;
import com.app.library.service.impl.UserServiceImpl;
import com.app.library.utils.AppConstants;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    UserServiceImpl userService;
    @GetMapping("{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") int id){
        return userService.getUser(id);
    }



    @GetMapping
    public PagedResponse<User> listUsers(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {
        return userService.listUsers(page,size);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable(name = "id") int id,
            @Valid @RequestBody RegisterRequest updateUserRequest
    ) {
        return userService.updateUser(id, updateUserRequest);

    }
    @GetMapping("/user-info")
    public ResponseEntity<?> getUserInfo() {
        return userService.getUserInformation();
    }

    @PostMapping("/update-avatar/{id}")
    public ResponseEntity<User> updateAvatar(@PathVariable int id, @RequestParam("avatarFile") MultipartFile avatarFile) throws IOException {
        User updatedUser = userService.updateAvatar(id, avatarFile);
        return ResponseEntity.ok(updatedUser);
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUser(@PathVariable int id){
        return userService.deleteUser(id);
    }
}
