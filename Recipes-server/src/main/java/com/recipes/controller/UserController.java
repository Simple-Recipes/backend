package com.recipes.controller;

import com.recipes.dto.UserLoginDTO;
import com.recipes.dto.UserRegisterDTO;
import com.recipes.dto.UserDTO;
import com.recipes.properties.JwtProperties;
import com.recipes.result.Result;
import com.recipes.service.UserService;
import com.recipes.utils.JwtUtil;
import com.recipes.vo.UserLoginVO;
import com.recipes.dto.UserProfileUpdateDTO;
import com.recipes.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@Tag(name = "User API", description = "User related operations")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private HttpSession session;

    @PostMapping("/register")
    @Operation(summary = "User register")
    public Result<UserVO> register(@RequestBody UserRegisterDTO userRegisterDTO) {
        log.info("User register:{}", userRegisterDTO);

        UserDTO userDTO = userService.register(userRegisterDTO);

        UserVO userVO = UserVO.builder()
                .id(userDTO.getId())
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .avatar(userDTO.getAvatar())
                .build();

        return Result.success(userVO);
    }

    @PostMapping("/login")
    @Operation(summary = "User login")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("User login:{}", userLoginDTO);

        UserDTO userDTO = userService.login(userLoginDTO);

        // Generate JWT token
        Map<String, Object> claims = new HashMap<>();
        claims.put("user_id", userDTO.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getUserSecretKey(),
                jwtProperties.getUserTtl(),
                claims);

        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(userDTO.getId())
                .username(userDTO.getUsername())
                .token(token)
                .build();

        return Result.success(userLoginVO);
    }

    @GetMapping("/profile")
    @Operation(summary = "Get user profile")
    public Result<UserDTO> getUserProfile() {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            log.error("User is not logged in");
            return Result.error("User is not logged in");
        }
        log.info("Get user profile.");
        UserDTO userDTO = userService.getCurrentUser(userId);
        return Result.success(userDTO);
    }

    @PutMapping("/profile")
    @Operation(summary = "Update user profile")
    public Result<UserDTO> updateUserProfile(@RequestBody UserProfileUpdateDTO userProfileUpdateDTO) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            log.error("User is not logged in");
            return Result.error("User is not logged in");
        }

        log.info("Updating user profile for userId={}, userProfileUpdateDTO={}", userId, userProfileUpdateDTO);
        UserDTO updatedUserDTO = userService.updateProfile(userId, userProfileUpdateDTO);
        return Result.success(updatedUserDTO);
    }

    @PostMapping("/forgotPassword")
    @Operation(summary = "forgot Password")
    public Result<?> requestPasswordReset(@RequestBody Map<String, String> request) {
        log.info("Request password reset.");
        userService.requestPasswordReset(request.get("email"));
        return Result.success();
    }

    @PostMapping("/resetPassword")
    @Operation(summary = "Reset password")
    @Parameters({
            @Parameter(name = "token", description = "Reset token", required = true, in = ParameterIn.HEADER),
            @Parameter(name = "newPassword", description = "New password", required = true, in = ParameterIn.QUERY)
    })
    public Result<?> resetPassword(@RequestParam("newPassword") String newPassword, @RequestHeader("User-Token") String token) {
        log.info("Reset password.");
        userService.resetPassword(token, newPassword);
        return Result.success();
    }
}
