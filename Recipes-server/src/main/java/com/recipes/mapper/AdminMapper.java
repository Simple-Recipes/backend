package com.recipes.mapper;


import com.recipes.dto.AdminDTO;
import com.recipes.dto.UserDTO;
import com.recipes.entity.Admin;
import com.recipes.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface AdminMapper {

    @Mapping(source = "createTime", target = "createTime", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    UserDTO toDto(User user);
    AdminDTO toDto(Admin admin);

    @Mapping(target = "createTime", ignore = true)
    Admin toEntity(AdminDTO adminDTO);

    default String formatDate(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.toString() : null;
    }
}
