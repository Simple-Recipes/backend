package com.recipes.controller;

import com.recipes.dto.UserLoginWithCodeDTO;
import com.recipes.dto.UserLoginWithPasswordDTO;
import com.recipes.dto.UserRegisterDTO;
import com.recipes.dto.UserDTO;
import com.recipes.properties.JwtProperties;
import com.recipes.result.Result;
import com.recipes.service.UserService;
import com.recipes.utils.JwtUtil;
import com.recipes.dto.UserProfileUpdateDTO;
import com.recipes.utils.UserHolder;
import com.recipes.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = {"http://localhost:8082", "http://localhost:3000"})
@Slf4j
@Tag(name = "User API", description = "User related operations")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProperties jwtProperties;

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
    @PostMapping("/sendCode")
    @Operation(summary = "Send verification code to email")
    public Result<Void> sendCode(@RequestParam String email) {
        userService.sendCode(email);
        return Result.success();
    }

    @PostMapping("/loginWithCode")
    @Operation(summary = "User login with email and code")
    public Result<UserDTO> loginWithCode(@RequestBody UserLoginWithCodeDTO userLoginDTO) {
        log.info("User login with email and code:{}", userLoginDTO);

        UserDTO userDTO = userService.loginWithCode(userLoginDTO);

        return Result.success(userDTO);
    }

    @PostMapping("/loginWithPassword")
    @Operation(summary = "User login with username and password")
    public Result<UserDTO> loginWithPassword(@RequestBody UserLoginWithPasswordDTO userLoginDTO) {
        log.info("User login with username and password:{}", userLoginDTO);

        UserDTO userDTO = userService.loginWithPassword(userLoginDTO);

        return Result.success(userDTO);
    }



@GetMapping("/profile")
    @Operation(summary = "Get user profile")
    public Result<UserDTO> getUserProfile() {
        Long userId = UserHolder.getUser().getId();
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
        Long userId = UserHolder.getUser().getId();
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
