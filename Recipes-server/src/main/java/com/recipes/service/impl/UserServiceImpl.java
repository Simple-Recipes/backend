package com.recipes.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.recipes.constant.RedisConstants;
import com.recipes.dao.UserDAO;
import com.recipes.dto.UserDTO;
import com.recipes.dto.UserLoginWithCodeDTO;
import com.recipes.dto.UserLoginWithPasswordDTO;
import com.recipes.dto.UserProfileUpdateDTO;
import com.recipes.dto.UserRegisterDTO;
import com.recipes.entity.User;
import com.recipes.exception.AccountNotFoundException;
import com.recipes.exception.PasswordErrorException;
import com.recipes.mapper.UserMapper;
import com.recipes.properties.JwtProperties;
import com.recipes.result.Result;
import com.recipes.service.UserService;
import com.recipes.utils.JwtUtil;
import com.recipes.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private UserMapper userMapper;

    private static final String DEFAULT_AVATAR_URL ="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ8KBogEsVJytTynCC0znYh07_aw_ylaOLd_g&usqp=CAU";

    @Override
    public UserDTO register(UserRegisterDTO userRegisterDTO) {
        User user = User.builder()
                .username(userRegisterDTO.getUsername())
                .password(userRegisterDTO.getPassword())  // Plain text password, should be hashed in real-world applications
                .email(userRegisterDTO.getEmail())
                .avatar(DEFAULT_AVATAR_URL)
                .createTime(LocalDateTime.now())
                .build();
        userDAO.saveUser(user);
        return userMapper.toDto(user);
    }

    @Transactional
    @Override
    public UserDTO loginWithPassword(UserLoginWithPasswordDTO userLoginDTO) {
        String username = userLoginDTO.getUsername();
        String password = userLoginDTO.getPassword();

        User user = userDAO.findUserByUsername(username);
        log.info("User login with username and password:{}", user);
        if (user == null) {
            throw new AccountNotFoundException("Account not found");
        }

        if (!password.equals(user.getPassword())) {
            throw new PasswordErrorException("Password error");
        }

        UserDTO userDTO = userMapper.toDto(user);

        Map<String, Object> claims = new HashMap<>();
        claims.put("user_id", userDTO.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);
        log.info("JWT token: {}", token);

        // 将用户信息存储到 Redis 中，使用 token 作为键
        String userKey = RedisConstants.LOGIN_USER_KEY + token;
        redisTemplate.opsForHash().put(userKey, "id", userDTO.getId().toString());
        redisTemplate.opsForHash().put(userKey, "username", userDTO.getUsername());
        redisTemplate.opsForHash().put(userKey, "email", userDTO.getEmail());
        redisTemplate.opsForHash().put(userKey, "avatar", userDTO.getAvatar());
        redisTemplate.expire(userKey, jwtProperties.getUserTtl(), TimeUnit.MILLISECONDS);
        log.info("User info stored in Redis: {}", userKey);

        // 保存 token 和 userId 的映射关系
        redisTemplate.opsForValue().set(RedisConstants.LOGIN_USER_TOKEN_KEY + token, userDTO.getId().toString(), jwtProperties.getUserTtl(), TimeUnit.MILLISECONDS);

        // 保存用户信息到 ThreadLocal
        UserHolder.saveUser(userDTO);
        log.info("User info stored in ThreadLocal: {}", userDTO);

        // 设置 token 到 UserDTO 中
        userDTO.setToken(token);
        log.info("UserDTO with token: {}", userDTO);

        return userDTO;
    }

    @Transactional
    @Override
    public UserDTO loginWithCode(UserLoginWithCodeDTO userLoginDTO) {
        String email = userLoginDTO.getEmail();
        String code = userLoginDTO.getCode();

        String codeKey = RedisConstants.LOGIN_CODE_KEY + email;
        String storedCode = redisTemplate.opsForValue().get(codeKey);
        if (storedCode == null || !storedCode.equals(code)) {
            throw new PasswordErrorException("Verification code is incorrect");
        }

        User user = userDAO.findUserByEmail(email);
        if (user == null) {
            throw new AccountNotFoundException("Account not found");
        }

        UserDTO userDTO = userMapper.toDto(user);

        Map<String, Object> claims = new HashMap<>();
        claims.put("user_id", userDTO.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);

        // 将用户信息存储到 Redis 中，使用 token 作为键
        String userKey = RedisConstants.LOGIN_USER_KEY + token;
        redisTemplate.opsForHash().put(userKey, "id", userDTO.getId().toString());
        redisTemplate.opsForHash().put(userKey, "username", userDTO.getUsername());
        redisTemplate.opsForHash().put(userKey, "email", userDTO.getEmail());
        redisTemplate.opsForHash().put(userKey, "avatar", userDTO.getAvatar());
        redisTemplate.expire(userKey, jwtProperties.getUserTtl(), TimeUnit.MILLISECONDS);

        // 保存 token 和 userId 的映射关系
        redisTemplate.opsForValue().set(RedisConstants.LOGIN_USER_TOKEN_KEY + token, userDTO.getId().toString(), jwtProperties.getUserTtl(), TimeUnit.MILLISECONDS);

        // 保存用户信息到 ThreadLocal
        UserHolder.saveUser(userDTO);

        // 设置 token 到 UserDTO 中
        userDTO.setToken(token);

        return userDTO;
    }

    @Override
    public void sendCode(String email) {
        //验证邮箱是否存在
        User user = userDAO.findUserByEmail(email);
        if (user == null) {
            throw new AccountNotFoundException("Account not found");
        }

        // 生成验证码
        String code = RandomUtil.randomNumbers(6);
        log.info("Verification code: {}", code);

        // 保存验证码到 Redis
        String codeKey = RedisConstants.LOGIN_CODE_KEY + email;
        redisTemplate.opsForValue().set(codeKey, code, RedisConstants.LOGIN_CODE_TTL, TimeUnit.MINUTES);
        log.info("Verification code key: {}", codeKey);

        // 输出验证码到日志
        log.info("Verification code sent to {}: {}", email, code);
    }

    @Override
    public UserDTO getCurrentUser(Long userId) {
        if (userId != null) {
            User user = userDAO.findUserById(userId);
            if (user != null) {
                return userMapper.toDto(user);
            } else {
                throw new AccountNotFoundException("User not found");
            }
        }
        throw new AccountNotFoundException("User not logged in");
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userDAO.findUserById(id);
        return user != null ? userMapper.toDto(user) : null;
    }

    @Override
    public UserDTO updateProfile(Long userId, UserProfileUpdateDTO userProfileUpdateDTO) {
        User existingUser = userDAO.findUserById(userId);
        if (existingUser == null) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }

        existingUser.setUsername(userProfileUpdateDTO.getUsername());
        existingUser.setEmail(userProfileUpdateDTO.getEmail());
        existingUser.setAvatar(userProfileUpdateDTO.getAvatar());
        // Assume password should be updated too if provided
        if (userProfileUpdateDTO.getPassword() != null && !userProfileUpdateDTO.getPassword().isEmpty()) {
            existingUser.setPassword(userProfileUpdateDTO.getPassword());
        }

        userDAO.updateUser(existingUser);

        return userMapper.toDto(existingUser);
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
}
