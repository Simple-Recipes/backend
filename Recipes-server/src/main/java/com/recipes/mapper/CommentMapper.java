package com.recipes.mapper;

import com.recipes.dto.CommentDTO;
import com.recipes.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "recipe.id", target = "recipeId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "createTime", target = "createTime", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    CommentDTO toDto(Comment comment);

    @Mapping(source = "recipeId", target = "recipe.id")
    @Mapping(source = "userId", target = "user.id")
    Comment toEntity(CommentDTO commentDTO);

    List<CommentDTO> toDtoList(List<Comment> comments);

    List<Comment> toEntityList(List<CommentDTO> commentDTOs);
}
