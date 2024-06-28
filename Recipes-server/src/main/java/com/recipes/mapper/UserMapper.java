package com.recipes.mapper;

import com.recipes.dto.UserDTO;
import com.recipes.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDto(User user);

    User toEntity(UserDTO userDTO);
}
