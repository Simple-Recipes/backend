package com.recipes.service;

import com.recipes.dto.AdminDTO;
import com.recipes.dto.AdminLoginDTO;

public interface AdminService {
    AdminDTO login(AdminLoginDTO adminLoginDTO);
}
