package com.recipes.mapper;

import com.recipes.dto.CommentDTO;
import com.recipes.dto.LikeDTO;
import com.recipes.dto.TagDTO;
import com.recipes.entity.Comment;
import com.recipes.entity.Like;
import com.recipes.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(componentModel = "spring")
public interface LikeMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "recipe.id", target = "recipeId")
    LikeDTO toDto(Like like);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "recipeId", target = "recipe.id")
    Like toEntity(LikeDTO likeDTO);
}
