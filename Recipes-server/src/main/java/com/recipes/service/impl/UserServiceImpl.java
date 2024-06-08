package com.recipes.service.impl;

import com.recipes.context.UserContext;
import com.recipes.dao.UserDAO;
import com.recipes.dto.UserLoginDTO;
import com.recipes.dto.UserRegisterDTO;
import com.recipes.dto.UserDTO;
import com.recipes.entity.User;
import com.recipes.exception.AccountNotFoundException;
import com.recipes.exception.PasswordErrorException;
import com.recipes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public UserDTO register(UserRegisterDTO userRegisterDTO) {
        User user = User.builder()
                .username(userRegisterDTO.getUsername())
                .password(userRegisterDTO.getPassword())  // Plain text password, should be hashed in real-world applications
                .email(userRegisterDTO.getEmail())
                .createTime(LocalDateTime.now())
                .build();
        userDAO.saveUser(user);
        return toDTO(user);
    }

    @Override
    public UserDTO login(UserLoginDTO userLoginDTO) {
        String username = userLoginDTO.getUsername();
        String password = userLoginDTO.getPassword();

        User user = userDAO.findUserByUsername(username);

        if (user == null) {
            throw new AccountNotFoundException("Account not found");
        }

        if (!password.equals(user.getPassword())) {
            throw new PasswordErrorException("Password error");
        }

        // 登录成功后设置用户上下文
        UserContext.setCurrentUserId(user.getId());

        return toDTO(user);
    }

    @Override
    public UserDTO getCurrentUser() {
        Long userId = UserContext.getCurrentUserId();
        if (userId == null) {
            throw new AccountNotFoundException("User not logged in");
        }
        User user = userDAO.findUserById(userId);
        return toDTO(user);
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userDAO.findUserById(id);
        return user != null ? toDTO(user) : null;
    }

    @Override
    public UserDTO updateProfile(UserDTO userDTO) {
        User user = userDAO.findUserById(userDTO.getId());
        if (user != null) {
            user.setUsername(userDTO.getUsername());
            user.setEmail(userDTO.getEmail());
            user.setAvatar(userDTO.getAvatar());
            userDAO.updateUser(user);
            return toDTO(user);
        }
        throw new AccountNotFoundException("User not found");
    }

    @Override
    public void requestPasswordReset(String email) {
        User user = userDAO.findUserByEmail(email);
        if (user != null) {
            // Generate a password reset token and send it to the user's email
            // This implementation is a placeholder and should be replaced with actual email sending logic
            String resetToken = "generated-reset-token"; // Replace with actual token generation logic
            // Send resetToken to user's email
        } else {
            throw new AccountNotFoundException("Email not found");
        }
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        // Validate the token and reset the password
        // This implementation is a placeholder and should be replaced with actual token validation logic
        User user = userDAO.findUserByResetToken(token);
        if (user != null) {
            user.setPassword(newPassword); // Plain text password, should be hashed in real-world applications
            userDAO.updateUser(user);
        } else {
            throw new AccountNotFoundException("Invalid reset token");
        }
    }

    private UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .createTime(user.getCreateTime().toString())
                .build();
    }
}
