package com.recipes.service.impl;


import com.recipes.dao.LikeDAO;
import com.recipes.dto.LikeDTO;
import com.recipes.entity.Like;
import com.recipes.result.Result;
import com.recipes.service.LikeService;
import com.recipes.mapper.LikeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private LikeDAO likeDAO;

    @Autowired
    private LikeMapper likeMapper;

    @Override
    public Result<LikeDTO> likeRecipe(LikeDTO likeDTO) {
        Like like = likeMapper.toEntity(likeDTO);
        likeDAO.saveLike(like);
        return Result.success(likeMapper.toDto(like));
    }

    @Override
    public Result<LikeDTO> unlikeRecipe(LikeDTO likeDTO) {
        Like like = likeMapper.toEntity(likeDTO);
        likeDAO.deleteLike(like);
        return Result.success(likeMapper.toDto(like));
    }

    @Override
    public Result<Integer> getRecipeLikes(Long recipeId) {
        int likes = likeDAO.countLikesByRecipeId(recipeId);
        return Result.success(likes);
    }
}
