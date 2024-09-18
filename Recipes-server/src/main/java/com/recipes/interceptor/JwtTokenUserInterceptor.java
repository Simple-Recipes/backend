package com.recipes.interceptor;

import com.recipes.dto.UserDTO;
import com.recipes.properties.JwtProperties;
import com.recipes.utils.JwtUtil;
import com.recipes.utils.UserHolder;
import com.recipes.constant.RedisConstants;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class JwtTokenUserInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        String token = request.getHeader(jwtProperties.getUserTokenName());
        log.info("Token: {}", token);

        if (token == null || token.isEmpty()) {
            log.error("Token is missing");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        try {
            Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
            Long userId = Long.valueOf(claims.get("user_id").toString());
            log.info("Parsed userId from token: {}", userId);

            // Get user information from Redis
            String userKey = RedisConstants.LOGIN_USER_KEY + token;
            Map<Object, Object> userMap = redisTemplate.opsForHash().entries(userKey);
            if (userMap.isEmpty()) {
                log.error("No user information found in Redis for token: {}", token);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }

            UserDTO userDTO = new UserDTO();
            userDTO.setId(userId);
            userDTO.setUsername((String) userMap.get("username"));
            userDTO.setEmail((String) userMap.get("email"));
            userDTO.setAvatar((String) userMap.get("avatar"));

            // Store user information in the UserHolder
            UserHolder.saveUser(userDTO);

            // Refresh the TTL of the user information in Redis
            redisTemplate.expire(userKey, jwtProperties.getUserTtl(), TimeUnit.MILLISECONDS);

            return true;
        } catch (Exception ex) {
            log.error("JWT parsing or Redis error: {}", ex.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUser();
    }
}
