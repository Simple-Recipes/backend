package com.recipes.service;

import com.recipes.dto.UserDTO;
import com.recipes.dto.UserLoginDTO;
import com.recipes.dto.UserRegisterDTO;
import com.recipes.dto.UserProfileUpdateDTO;

public interface UserService {
    UserDTO register(UserRegisterDTO userRegisterDTO);
    UserDTO login(UserLoginDTO userLoginDTO);
    UserDTO getCurrentUser(Long id);
    UserDTO getUserById(Long id);
    UserDTO updateProfile(Long userId, UserProfileUpdateDTO userProfileUpdateDTO);
    void requestPasswordReset(String email);
    void resetPassword(String token, String newPassword);
}
