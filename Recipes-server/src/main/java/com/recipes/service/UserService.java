package com.recipes.service;

import com.recipes.dto.UserDTO;
import com.recipes.dto.UserLoginDTO;
import com.recipes.dto.UserRegisterDTO;

public interface UserService {
    UserDTO register(UserRegisterDTO userRegisterDTO);
    UserDTO login(UserLoginDTO userLoginDTO);
    UserDTO getCurrentUser();
    UserDTO getUserById(Long id);
    UserDTO updateProfile(UserDTO userDTO);
    void requestPasswordReset(String email);
    void resetPassword(String token, String newPassword);
}
