package com.recipes.interceptor;

import com.recipes.properties.JwtProperties;
import com.recipes.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class JwtTokenUserInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * Validate JWT
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Determine if the current interception is a Controller method or other resource
        if (!(handler instanceof HandlerMethod)) {
            // If the current interception is not a dynamic method, allow direct access
            return true;
        }

        // 1. Get the token from the request header
        String token = request.getHeader(jwtProperties.getUserTokenName());

        // 2. Validate the token
        try {
            log.info("JWT validation: {}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
            Long userId = Long.valueOf(claims.get("user_id").toString());
            log.info("Current user ID: {}", userId);

            // 3. Set the user ID in the session
            HttpSession session = request.getSession();
            session.setAttribute("userId", userId);

            // 4. Pass the validation, allow access
            return true;
        } catch (Exception ex) {
            // 5. Fail validation, respond with 401 status code
            response.setStatus(401);
            return false;
        }
    }
}
