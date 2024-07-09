package com.recipes.service.impl;


import com.recipes.dao.TagDAO;
import com.recipes.dto.TagDTO;
import com.recipes.entity.Tag;
import com.recipes.mapper.RecipeMapper;
import com.recipes.result.Result;
import com.recipes.service.TagService;
import com.recipes.mapper.TagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagDAO tagDAO;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private RecipeMapper recipeMapper;

    @Override
    public Result<List<TagDTO>> getAllTags() {
        List<Tag> tags = tagDAO.findAllTags();
        List<TagDTO> tagDTOs = tags.stream()
                                   .map(tagMapper::toDto)
                                   .collect(Collectors.toList());
        return Result.success(tagDTOs);
    }

    @Override
    public Result<TagDTO> addTag(TagDTO tagDTO) {
        Tag tag = tagMapper.toEntity(tagDTO);
        tagDAO.saveTag(tag);
        return Result.success(tagMapper.toDto(tag));
    }

    @Override
    public Result<Void> deleteTag(Long id) {
        tagDAO.deleteTag(id);
        return Result.success();
    }
    @Override
    public Result<List<TagDTO>> getAllMyTags(Long userId) {
        List<Tag> userTags = tagDAO.findAllByUserId(userId);
        List<TagDTO> userTagDTOs = userTags.stream()
                .map(tagMapper::toDto)
                .collect(Collectors.toList());
        return Result.success(userTagDTOs);
    }

}
