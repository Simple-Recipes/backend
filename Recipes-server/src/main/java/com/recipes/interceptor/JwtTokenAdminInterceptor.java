package com.recipes.interceptor;

import com.recipes.constant.JwtClaimsConstant;
import com.recipes.properties.JwtProperties;
import com.recipes.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;


/**
 * Interceptor for JWT token validation
 */
@Component
@Slf4j
public class JwtTokenAdminInterceptor implements HandlerInterceptor {

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
        String token = request.getHeader(jwtProperties.getAdminTokenName());

        // 2. Validate the token
        try {
            log.info("JWT validation: {}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);
            Long empId = Long.valueOf(claims.get(JwtClaimsConstant.EMP_ID).toString());
            log.info("Current employee ID: {}", empId);
            // 3. Pass the validation, allow access
            return true;
        } catch (Exception ex) {
            // 4. Fail validation, respond with 401 status code
            response.setStatus(401);
            return false;
        }
    }
}
