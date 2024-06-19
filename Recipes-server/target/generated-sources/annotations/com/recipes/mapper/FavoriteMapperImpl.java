package com.recipes.mapper;

import com.recipes.dto.FavoriteDTO;
import com.recipes.entity.Favorite;
import com.recipes.entity.FavoriteId;
import com.recipes.vo.FavoriteVO;
import java.time.format.DateTimeFormatter;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-19T10:52:11+0100",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
public class FavoriteMapperImpl implements FavoriteMapper {

    private final DateTimeFormatter dateTimeFormatter_yyyy_MM_dd_T_HH_mm_ss_11798231098 = DateTimeFormatter.ofPattern( "yyyy-MM-dd'T'HH:mm:ss" );

    @Override
    public FavoriteDTO toDto(Favorite favorite) {
        if ( favorite == null ) {
            return null;
        }

        FavoriteDTO.FavoriteDTOBuilder favoriteDTO = FavoriteDTO.builder();

        favoriteDTO.userId( favoriteIdUserId( favorite ) );
        favoriteDTO.recipeId( favoriteIdRecipeId( favorite ) );
        if ( favorite.getCreateTime() != null ) {
            favoriteDTO.createTime( dateTimeFormatter_yyyy_MM_dd_T_HH_mm_ss_11798231098.format( favorite.getCreateTime() ) );
        }

        return favoriteDTO.build();
    }

    @Override
    public Favorite toEntity(FavoriteDTO favoriteDTO) {
        if ( favoriteDTO == null ) {
            return null;
        }

        Favorite.FavoriteBuilder favorite = Favorite.builder();

        favorite.id( favoriteDTOToFavoriteId( favoriteDTO ) );

        return favorite.build();
    }

    @Override
    public FavoriteVO toVo(Favorite favorite) {
        if ( favorite == null ) {
            return null;
        }

        FavoriteVO.FavoriteVOBuilder favoriteVO = FavoriteVO.builder();

        favoriteVO.userId( favoriteIdUserId( favorite ) );
        favoriteVO.recipeId( favoriteIdRecipeId( favorite ) );
        if ( favorite.getCreateTime() != null ) {
            favoriteVO.createTime( dateTimeFormatter_yyyy_MM_dd_T_HH_mm_ss_11798231098.format( favorite.getCreateTime() ) );
        }

        return favoriteVO.build();
    }

    @Override
    public Favorite toEntity(FavoriteVO favoriteVO) {
        if ( favoriteVO == null ) {
            return null;
        }

        Favorite.FavoriteBuilder favorite = Favorite.builder();

        favorite.id( favoriteVOToFavoriteId( favoriteVO ) );

        return favorite.build();
    }

    private Long favoriteIdUserId(Favorite favorite) {
        if ( favorite == null ) {
            return null;
        }
        FavoriteId id = favorite.getId();
        if ( id == null ) {
            return null;
        }
        Long userId = id.getUserId();
        if ( userId == null ) {
            return null;
        }
        return userId;
    }

    private Long favoriteIdRecipeId(Favorite favorite) {
        if ( favorite == null ) {
            return null;
        }
        FavoriteId id = favorite.getId();
        if ( id == null ) {
            return null;
        }
        Long recipeId = id.getRecipeId();
        if ( recipeId == null ) {
            return null;
        }
        return recipeId;
    }

    protected FavoriteId favoriteDTOToFavoriteId(FavoriteDTO favoriteDTO) {
        if ( favoriteDTO == null ) {
            return null;
        }

        FavoriteId favoriteId = new FavoriteId();

        favoriteId.setUserId( favoriteDTO.getUserId() );
        favoriteId.setRecipeId( favoriteDTO.getRecipeId() );

        return favoriteId;
    }

    protected FavoriteId favoriteVOToFavoriteId(FavoriteVO favoriteVO) {
        if ( favoriteVO == null ) {
            return null;
        }

        FavoriteId favoriteId = new FavoriteId();

        favoriteId.setUserId( favoriteVO.getUserId() );
        favoriteId.setRecipeId( favoriteVO.getRecipeId() );

        return favoriteId;
    }
}
