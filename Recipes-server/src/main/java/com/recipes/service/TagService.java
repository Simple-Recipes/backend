package com.recipes.service;
import com.recipes.dto.LikeDTO;
import com.recipes.dto.TagDTO;
import com.recipes.result.Result;

import java.util.List;

public interface TagService {
    Result<List<TagDTO>> getAllTags();
    Result<TagDTO> addTag(TagDTO tagDTO);
    Result<Void> deleteTag(Long id);
}
