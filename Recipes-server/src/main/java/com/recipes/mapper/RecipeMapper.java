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
    @Mapping(target = "ingredients", expression = "java(com.recipes.mapper.RecipeMapper.convertJsonToArray(recipe.getIngredients()))")
    @Mapping(target = "directions", expression = "java(com.recipes.mapper.RecipeMapper.convertJsonToArray(recipe.getDirections()))")
    @Mapping(target = "ner", expression = "java(com.recipes.mapper.RecipeMapper.convertJsonToArray(recipe.getNer()))")
    RecipeDTO toDto(Recipe recipe);

    // RecipeDTO to Recipe mapping
    @Mapping(target = "user", ignore = true) // Assume user will be set separately
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "ingredients", expression = "java(com.recipes.mapper.RecipeMapper.convertArrayToJson(recipeDTO.getIngredients()))")
    @Mapping(target = "directions", expression = "java(com.recipes.mapper.RecipeMapper.convertArrayToJson(recipeDTO.getDirections()))")
    @Mapping(target = "ner", expression = "java(com.recipes.mapper.RecipeMapper.convertArrayToJson(recipeDTO.getNer()))")
    Recipe toEntity(RecipeDTO recipeDTO);

    // Recipe to RecipeVO mapping
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "createTime", target = "createTime", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(source = "updateTime", target = "updateTime", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(target = "ingredients", expression = "java(com.recipes.mapper.RecipeMapper.convertJsonToArray(recipe.getIngredients()))")
    @Mapping(target = "directions", expression = "java(com.recipes.mapper.RecipeMapper.convertJsonToArray(recipe.getDirections()))")
    @Mapping(target = "ner", expression = "java(com.recipes.mapper.RecipeMapper.convertJsonToArray(recipe.getNer()))")
    RecipeVO toVo(Recipe recipe);

    // RecipeVO to Recipe mapping
    @Mapping(target = "user", ignore = true) // Assume user will be set separately
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "ingredients", expression = "java(com.recipes.mapper.RecipeMapper.convertArrayToJson(recipeVO.getIngredients()))")
    @Mapping(target = "directions", expression = "java(com.recipes.mapper.RecipeMapper.convertArrayToJson(recipeVO.getDirections()))")
    @Mapping(target = "ner", expression = "java(com.recipes.mapper.RecipeMapper.convertArrayToJson(recipeVO.getNer()))")
    Recipe toEntity(RecipeVO recipeVO);

    static String convertArrayToJson(String[] array) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(array);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert array to JSON string", e);
        }
    }

    static String[] convertJsonToArray(String jsonString) {
        if (jsonString == null || jsonString.isEmpty()) {
            return new String[0];
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonString, String[].class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert JSON string to array", e);
        }
    }

}
