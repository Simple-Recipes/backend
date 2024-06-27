package com.recipes.service;

import com.recipes.dto.UserDTO;
import com.recipes.dto.UserLoginWithCodeDTO;
import com.recipes.dto.UserLoginWithPasswordDTO;
import com.recipes.dto.UserRegisterDTO;
import com.recipes.dto.UserProfileUpdateDTO;
import jakarta.servlet.http.HttpSession;

public interface UserService {
    UserDTO register(UserRegisterDTO userRegisterDTO);
    UserDTO loginWithPassword(UserLoginWithPasswordDTO userLoginDTO);
    UserDTO loginWithCode(UserLoginWithCodeDTO userLoginDTO);

    void sendCode(String email);
    UserDTO getCurrentUser(Long id);
    UserDTO getUserById(Long id);
    UserDTO updateProfile(Long userId, UserProfileUpdateDTO userProfileUpdateDTO);
    void requestPasswordReset(String email);
    void resetPassword(String token, String newPassword);
}
