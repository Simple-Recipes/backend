package com.recipes.interceptor;


import com.recipes.constant.RedisConstants;
import com.recipes.dto.UserDTO;
import com.recipes.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${recipes.jwt.userSecretKey}")
    private String userSecretKey;

    @Value("${recipes.jwt.userTokenName}")
    private String userTokenName;

    @Value("${recipes.jwt.userTtl}")
    private long userTtl;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String token = request.getHeaders().getFirst(userTokenName);
        if (token == null || token.isEmpty()) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }

        try {
            Claims claims = JwtUtil.parseJWT(userSecretKey, token);
            Long userId = Long.valueOf(claims.get("user_id").toString());

            // 从 Redis 中获取用户信息
            String userKey = RedisConstants.LOGIN_USER_KEY + token;
            Map<Object, Object> userMap = redisTemplate.opsForHash().entries(userKey);
            if (userMap.isEmpty()) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return false;
            }

            UserDTO userDTO = new UserDTO();
            userDTO.setId(userId);
            userDTO.setUsername((String) userMap.get("username"));
            userDTO.setEmail((String) userMap.get("email"));
            userDTO.setAvatar((String) userMap.get("avatar"));

            // 将用户信息存储到 WebSocket 会话中
            attributes.put("user", userDTO);

            // 刷新 token 有效期
            redisTemplate.expire(userKey, userTtl, TimeUnit.MILLISECONDS);

            return true;
        } catch (Exception ex) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        // 这里可以选择不做任何操作
    }
}
