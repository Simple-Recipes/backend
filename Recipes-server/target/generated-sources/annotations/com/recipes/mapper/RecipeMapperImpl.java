package com.recipes.mapper;

import com.recipes.dto.RecipeDTO;
import com.recipes.entity.Recipe;
import com.recipes.entity.User;
import com.recipes.vo.RecipeVO;
import java.time.format.DateTimeFormatter;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-24T21:59:12+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
public class RecipeMapperImpl implements RecipeMapper {

    private final DateTimeFormatter dateTimeFormatter_yyyy_MM_dd_T_HH_mm_ss_11798231098 = DateTimeFormatter.ofPattern( "yyyy-MM-dd'T'HH:mm:ss" );

    @Override
    public RecipeDTO toDto(Recipe recipe) {
        if ( recipe == null ) {
            return null;
        }

        RecipeDTO.RecipeDTOBuilder recipeDTO = RecipeDTO.builder();

        recipeDTO.userId( recipeUserId( recipe ) );
        if ( recipe.getCreateTime() != null ) {
            recipeDTO.createTime( dateTimeFormatter_yyyy_MM_dd_T_HH_mm_ss_11798231098.format( recipe.getCreateTime() ) );
        }
        if ( recipe.getUpdateTime() != null ) {
            recipeDTO.updateTime( dateTimeFormatter_yyyy_MM_dd_T_HH_mm_ss_11798231098.format( recipe.getUpdateTime() ) );
        }
        recipeDTO.id( recipe.getId() );
        recipeDTO.title( recipe.getTitle() );
        recipeDTO.link( recipe.getLink() );
        recipeDTO.source( recipe.getSource() );

        recipeDTO.ingredients( com.recipes.mapper.RecipeMapper.convertJsonToArray(recipe.getIngredients()) );
        recipeDTO.directions( com.recipes.mapper.RecipeMapper.convertJsonToArray(recipe.getDirections()) );
        recipeDTO.ner( com.recipes.mapper.RecipeMapper.convertJsonToArray(recipe.getNer()) );

        return recipeDTO.build();
    }

    @Override
    public Recipe toEntity(RecipeDTO recipeDTO) {
        if ( recipeDTO == null ) {
            return null;
        }

        Recipe.RecipeBuilder recipe = Recipe.builder();

        recipe.id( recipeDTO.getId() );
        recipe.title( recipeDTO.getTitle() );
        recipe.link( recipeDTO.getLink() );
        recipe.source( recipeDTO.getSource() );

        recipe.ingredients( com.recipes.mapper.RecipeMapper.convertArrayToJson(recipeDTO.getIngredients()) );
        recipe.directions( com.recipes.mapper.RecipeMapper.convertArrayToJson(recipeDTO.getDirections()) );
        recipe.ner( com.recipes.mapper.RecipeMapper.convertArrayToJson(recipeDTO.getNer()) );

        return recipe.build();
    }

    @Override
    public RecipeVO toVo(Recipe recipe) {
        if ( recipe == null ) {
            return null;
        }

        RecipeVO.RecipeVOBuilder recipeVO = RecipeVO.builder();

        recipeVO.userId( recipeUserId( recipe ) );
        if ( recipe.getCreateTime() != null ) {
            recipeVO.createTime( dateTimeFormatter_yyyy_MM_dd_T_HH_mm_ss_11798231098.format( recipe.getCreateTime() ) );
        }
        if ( recipe.getUpdateTime() != null ) {
            recipeVO.updateTime( dateTimeFormatter_yyyy_MM_dd_T_HH_mm_ss_11798231098.format( recipe.getUpdateTime() ) );
        }
        recipeVO.id( recipe.getId() );
        recipeVO.title( recipe.getTitle() );
        recipeVO.link( recipe.getLink() );
        recipeVO.source( recipe.getSource() );

        recipeVO.ingredients( com.recipes.mapper.RecipeMapper.convertJsonToArray(recipe.getIngredients()) );
        recipeVO.directions( com.recipes.mapper.RecipeMapper.convertJsonToArray(recipe.getDirections()) );
        recipeVO.ner( com.recipes.mapper.RecipeMapper.convertJsonToArray(recipe.getNer()) );

        return recipeVO.build();
    }

    @Override
    public Recipe toEntity(RecipeVO recipeVO) {
        if ( recipeVO == null ) {
            return null;
        }

        Recipe.RecipeBuilder recipe = Recipe.builder();

        recipe.id( recipeVO.getId() );
        recipe.title( recipeVO.getTitle() );
        recipe.link( recipeVO.getLink() );
        recipe.source( recipeVO.getSource() );

        recipe.ingredients( com.recipes.mapper.RecipeMapper.convertArrayToJson(recipeVO.getIngredients()) );
        recipe.directions( com.recipes.mapper.RecipeMapper.convertArrayToJson(recipeVO.getDirections()) );
        recipe.ner( com.recipes.mapper.RecipeMapper.convertArrayToJson(recipeVO.getNer()) );

        return recipe.build();
    }

    private Long recipeUserId(Recipe recipe) {
        if ( recipe == null ) {
            return null;
        }
        User user = recipe.getUser();
        if ( user == null ) {
            return null;
        }
        Long id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
