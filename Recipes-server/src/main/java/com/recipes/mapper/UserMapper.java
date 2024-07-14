package com.recipes.mapper;

import com.recipes.dto.UserDTO;
import com.recipes.entity.User;
import org.mapstruct.*;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "createTime", target = "createTime", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    UserDTO toDto(User user);

    @Mapping(target = "createTime", ignore = true)
    User toEntity(UserDTO userDTO);

    default String formatDate(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.toString() : null;
    }
}
