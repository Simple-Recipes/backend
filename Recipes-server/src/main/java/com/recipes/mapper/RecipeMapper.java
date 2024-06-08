package com.recipes.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipes.dto.RecipeDTO;
import com.recipes.entity.Recipe;
import com.recipes.vo.RecipeVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.io.IOException;

@Mapper(componentModel = "spring")
public interface RecipeMapper {

    // Recipe to RecipeDTO mapping
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "createTime", target = "createTime", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(source = "updateTime", target = "updateTime", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(target = "ingredients", expression = "java(convertJsonToArray(recipe.getIngredients()))")
    @Mapping(target = "directions", expression = "java(convertJsonToArray(recipe.getDirections()))")
    @Mapping(target = "ner", expression = "java(convertJsonToArray(recipe.getNer()))")
    RecipeDTO toDto(Recipe recipe);

    // RecipeDTO to Recipe mapping
    @Mapping(target = "user", ignore = true) // Assume user will be set separately
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "ingredients", expression = "java(convertArrayToJson(recipeDTO.getIngredients()))")
    @Mapping(target = "directions", expression = "java(convertArrayToJson(recipeDTO.getDirections()))")
    @Mapping(target = "ner", expression = "java(convertArrayToJson(recipeDTO.getNer()))")
    Recipe toEntity(RecipeDTO recipeDTO);

    // Recipe to RecipeVO mapping
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "createTime", target = "createTime", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(source = "updateTime", target = "updateTime", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(target = "ingredients", expression = "java(convertJsonToArray(recipe.getIngredients()))")
    @Mapping(target = "directions", expression = "java(convertJsonToArray(recipe.getDirections()))")
    @Mapping(target = "ner", expression = "java(convertJsonToArray(recipe.getNer()))")
    RecipeVO toVo(Recipe recipe);

    // RecipeVO to Recipe mapping
    @Mapping(target = "user", ignore = true) // Assume user will be set separately
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "ingredients", expression = "java(convertArrayToJson(recipeVO.getIngredients()))")
    @Mapping(target = "directions", expression = "java(convertArrayToJson(recipeVO.getDirections()))")
    @Mapping(target = "ner", expression = "java(convertArrayToJson(recipeVO.getNer()))")
    Recipe toEntity(RecipeVO recipeVO);

    default String convertArrayToJson(String[] array) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(array);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert array to JSON string", e);
        }
    }

    default String[] convertJsonToArray(String jsonString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonString, String[].class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert JSON string to array", e);
        }
    }
}
