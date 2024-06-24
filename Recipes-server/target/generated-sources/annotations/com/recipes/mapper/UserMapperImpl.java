package com.recipes.mapper;

import com.recipes.dto.UserDTO;
import com.recipes.entity.User;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-24T21:59:12+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO toDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO.UserDTOBuilder userDTO = UserDTO.builder();

        userDTO.id( user.getId() );
        userDTO.username( user.getUsername() );
        userDTO.email( user.getEmail() );
        userDTO.avatar( user.getAvatar() );
        if ( user.getCreateTime() != null ) {
            userDTO.createTime( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( user.getCreateTime() ) );
        }

        return userDTO.build();
    }

    @Override
    public User toEntity(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.id( userDTO.getId() );
        user.username( userDTO.getUsername() );
        user.email( userDTO.getEmail() );
        user.avatar( userDTO.getAvatar() );
        if ( userDTO.getCreateTime() != null ) {
            user.createTime( LocalDateTime.parse( userDTO.getCreateTime() ) );
        }

        return user.build();
    }
}
