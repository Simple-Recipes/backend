package com.recipes.controller;


import com.recipes.dto.AdminDTO;
import com.recipes.dto.AdminLoginDTO;
import com.recipes.properties.JwtProperties;
import com.recipes.result.Result;
import com.recipes.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = {"http://localhost:8085", "http://localhost:3000","http://localhost:8080"})
@Slf4j
@Tag(name = "Admin API", description = "Admin related operations")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("/login")
    @Operation(summary = "Admin login with username and password")
    public Result<AdminDTO> login(@RequestBody AdminLoginDTO adminLoginDTO) {
        log.info("Admin login with username and password:{}", adminLoginDTO);
        AdminDTO adminDTO = adminService.login(adminLoginDTO);
        return Result.success(adminDTO);
    }





}
