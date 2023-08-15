package com.app.library.service.impl;

import com.app.library.auth.RegisterRequest;
import com.app.library.dto.UserDto;
import com.app.library.exception.object.*;
import com.app.library.model.Category;
import com.app.library.model.User;
import com.app.library.model.enum_class.RoleName;
import com.app.library.payload.PagedResponse;
import com.app.library.repository.UserRepository;
import com.app.library.service.IUserService;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AmazonS3Service amazonS3Service;
    @Autowired
    private  PasswordEncoder passwordEncoder;

    public ResponseEntity<User> updateUser(int id, RegisterRequest updateUserRequest) {
        Optional<String> currentUserLogin = SecurityUtil.getCurrentUserLogin();

        if (!currentUserLogin.isPresent()) {
            throw new ForbiddenException("User unauthorized");
        }

        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            throw new UserNotFoundException("No find user : " + id);
        }

        User existingUser = userOptional.get();
        String existingUserLogin = existingUser.getEmail();

        if (!currentUserLogin.get().equals(existingUserLogin)
                && !SecurityUtil.hasCurrentUserAnyOfAuthorities("ADMIN_PERMISSION")) {
            throw new ForbiddenException("You don't have permission to access this resource. ");
        }

        existingUser.setFirstName(updateUserRequest.getFirstName());
        existingUser.setLastName(updateUserRequest.getLastName());
        existingUser.setUsername(updateUserRequest.getUsername());
        existingUser.setPassword(passwordEncoder.encode(updateUserRequest.getPassword()));
        existingUser.setAddress(updateUserRequest.getAddress());
        existingUser.setEmail(updateUserRequest.getEmail());

        userRepository.save(existingUser);
        return new ResponseEntity<>(existingUser, HttpStatus.OK);
    }


    public ResponseEntity<?> getUserInformation() {
        try {
            String currentUserLogin = SecurityUtil.getCurrentUserLogin().orElse(null);
            if (currentUserLogin == null) {
                throw new ForbiddenException("User unauthorized");
            }

            Optional<User> userOptional = userRepository.findByEmail(currentUserLogin);
            if (!userOptional.isPresent()) {
                throw new UserNotFoundException("No get detail user");
            }

            User user = userOptional.get();

            if (user.getRoleName() == RoleName.ADMIN) {
                throw new ForbiddenException("You don't have permission to access this resource.");
            }

            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(user, userDto);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            throw e; // Re-throw the UserNotFoundException
        } catch (Exception e) {
            throw new UserNotFoundException("No get detail user");
        }
    }
    @Override
    public User updateAvatar(int id, MultipartFile avatarFile) throws IOException {
        Optional<String> currentUserLogin = SecurityUtil.getCurrentUserLogin();

        if (!currentUserLogin.isPresent()) {
            throw new ForbiddenException("User unauthorized");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("No find user : " + id));

        if (!currentUserLogin.get().equals(user.getUsername())
                && !SecurityUtil.hasCurrentUserAnyOfAuthorities("ADMIN_PERMISSION")) {
            throw new ForbiddenException("You don't have permission to access this resource.");
        }

        try {
            String imageUrl = amazonS3Service.uploadFile(avatarFile, "avatars");
            user.setAvatarUrl(imageUrl);
            User updatedUser = userRepository.save(user);

            return updatedUser;
        } catch (IOException e) {
            throw new LibraryException(HttpStatus.BAD_REQUEST, "Failed to update image");
        }
    }

    @Override
    public ResponseEntity<?> deleteUser(int id) {
        if (SecurityUtil.hasCurrentUserAnyOfAuthorities("ADMIN_PERMISSION")){
            Optional<User> user = userRepository.findById(id);
            if(user.isPresent()){
                userRepository.deleteById(id);
                return new ResponseEntity<>(user,HttpStatus.OK);

            } else{
                return new ResponseEntity<>(new User(),HttpStatus.NOT_FOUND);
            }

        } else {
            throw new ForbiddenException("You don't have permission to access this resource.");
        }

    }

    @Override
    public ResponseEntity<?> getUser(int id) {
        try {
            Optional<User> userOptional = userRepository.findById(id);
            if (!userOptional.isPresent()) {
                throw new UserNotFoundException("No get detail user");
            }

            User user = userOptional.get();
            String currentUserLogin = SecurityUtil.getCurrentUserLogin().orElse(null);

            if (currentUserLogin == null) {
                throw new ForbiddenException("User unauthorized");
            }

            if (!currentUserLogin.equals(user.getEmail()) &&
                    !SecurityUtil.hasCurrentUserAnyOfAuthorities("ADMIN_PERMISSION")) {
                throw new ForbiddenException("You don't have permission to access this resource.");
            }

            if (user.getRoleName() == RoleName.ADMIN) {
                throw new ForbiddenException("You don't have permission to access this resource.");
            }

            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(user, userDto);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new UserNotFoundException("No get detail user");
        }
    }



    @Override
    public PagedResponse<User> listUsers(int page, int size) {
        if (SecurityUtil.hasCurrentUserAnyOfAuthorities("ADMIN_PERMISSION")){
            AppUtils.validatePageNumberAndSize(page, size);

            Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

            Page<User> users = userRepository.findAll(pageable);

            List<User> content = users.getNumberOfElements() == 0 ? Collections.emptyList() : users.getContent();

            return new PagedResponse<>(content, users.getNumber(), users.getSize(), users.getTotalElements(),
                    users.getTotalPages(), users.isLast());
        } else {
            throw new ForbiddenException("You don't have permission to access this resource.");
        }
    }

    @Override
    public ResponseEntity<?> listByGroup() {
        return null;
    }


}
