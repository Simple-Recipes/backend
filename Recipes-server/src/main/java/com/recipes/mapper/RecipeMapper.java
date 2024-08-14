package com.recipes.mapper;

import com.recipes.dao.LikeDAO;
import com.recipes.dto.RecipeDTO;
import com.recipes.entity.Recipe;
import com.recipes.utils.JsonConversionUtil;
import com.recipes.vo.RecipeSimpleVO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RecipeMapper {

    // Recipe to RecipeDTO mapping
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "createTime", target = "createTime", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(source = "updateTime", target = "updateTime", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(target = "ingredients", qualifiedByName = "convertJsonToArray")
    @Mapping(target = "directions", qualifiedByName = "convertJsonToArray")
    @Mapping(target = "nutrition", qualifiedByName = "convertJsonToArray")
    RecipeDTO toDto(Recipe recipe);

    // RecipeDTO to Recipe mapping
    @Mapping(target = "user", ignore = true) // Assume user will be set separately
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "ingredients", qualifiedByName = "convertArrayToJson")
    @Mapping(target = "directions", qualifiedByName = "convertArrayToJson")
    @Mapping(target = "nutrition", qualifiedByName = "convertArrayToJson")
    Recipe toEntity(RecipeDTO recipeDTO);


    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "link", target = "link")
    @Mapping(target = "likes", expression = "java(likeDAO.countLikesByRecipeId(recipe.getId()))")
    RecipeSimpleVO toSimpleVO(Recipe recipe, @Context LikeDAO likeDAO);


    @Named("convertJsonToArray")
    default String[] convertJsonToArray(String jsonString) {
        return JsonConversionUtil.convertJsonToArray(jsonString);
    }

    @Named("convertArrayToJson")
    default String convertArrayToJson(String[] array) {
        return JsonConversionUtil.convertArrayToJson(array);
    }
}
