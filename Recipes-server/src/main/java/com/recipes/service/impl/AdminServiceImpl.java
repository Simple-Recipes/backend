package com.recipes.service.impl;


import com.recipes.dao.AdminDAO;
import com.recipes.dto.AdminDTO;
import com.recipes.dto.AdminLoginDTO;
import com.recipes.entity.Admin;
import com.recipes.exception.AccountNotFoundException;
import com.recipes.exception.PasswordErrorException;
import com.recipes.mapper.AdminMapper;
import com.recipes.properties.JwtProperties;
import com.recipes.service.AdminService;
import com.recipes.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.Map;


@Service
@Slf4j
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminDAO adminDAO;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private AdminMapper adminMapper;

    @Transactional
    @Override
    public AdminDTO login(AdminLoginDTO adminLoginDTO){
        String username = adminLoginDTO.getUsername();
        String password = adminLoginDTO.getPassword();

        log.info("Trying to find admin by username: {}", username);
        Admin admin = adminDAO.findAdminByName(username);
        log.info("Admin login with username and password:{}", admin);
        if (admin == null) {
            throw new AccountNotFoundException("Account not found");
        }

        if (!password.equals(admin.getPassword())) {
            throw new PasswordErrorException("Password error");
        }

        AdminDTO adminDTO = adminMapper.toDto(admin);

        Map<String, Object> claims = new HashMap<>();
        claims.put("admin_id", adminDTO.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);
        log.info("JWT token: {}", token);


        // 设置 token 到 UserDTO 中
        adminDTO.setToken(token);
        log.info("AdminDTO with token: {}", adminDTO);

        return adminDTO;

    }
}
