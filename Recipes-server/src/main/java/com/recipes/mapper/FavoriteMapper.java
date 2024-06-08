package com.recipes.mapper;

import com.recipes.dto.FavoriteDTO;
import com.recipes.entity.Favorite;
import com.recipes.vo.FavoriteVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FavoriteMapper {

    // Favorite to FavoriteDTO mapping
    @Mapping(source = "id.userId", target = "userId")
    @Mapping(source = "id.recipeId", target = "recipeId")
    @Mapping(source = "createTime", target = "createTime", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    FavoriteDTO toDto(Favorite favorite);

    // FavoriteDTO to Favorite mapping
    @Mapping(target = "user", ignore = true) // Assume user will be set separately
    @Mapping(target = "recipe", ignore = true) // Assume recipe will be set separately
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "id.userId", source = "userId")
    @Mapping(target = "id.recipeId", source = "recipeId")
    Favorite toEntity(FavoriteDTO favoriteDTO);

    // Favorite to FavoriteVO mapping
    @Mapping(source = "id.userId", target = "userId")
    @Mapping(source = "id.recipeId", target = "recipeId")
    @Mapping(source = "createTime", target = "createTime", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    FavoriteVO toVo(Favorite favorite);

    // FavoriteVO to Favorite mapping
    @Mapping(target = "user", ignore = true) // Assume user will be set separately
    @Mapping(target = "recipe", ignore = true) // Assume recipe will be set separately
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "id.userId", source = "userId")
    @Mapping(target = "id.recipeId", source = "recipeId")
    Favorite toEntity(FavoriteVO favoriteVO);
}
