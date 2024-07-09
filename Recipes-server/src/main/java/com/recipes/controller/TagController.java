package com.recipes.controller;

import com.recipes.dto.*;
import com.recipes.result.Result;
import com.recipes.service.TagService;
import com.recipes.utils.UserHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@Tag(name = "Tag API", description = "Operations related to tags")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/getAllMyTags")
    @Operation(summary = "Get all my tags", description = "Get a list of all tags added by the user")
    public Result<List<TagDTO>> getAllMyTags() {
        Long userId = UserHolder.getUser().getId();
        log.info("Getting all tags for user with id={}", userId);
        return tagService.getAllMyTags(userId);
    }

    @PostMapping("/addNewTag")
    @Operation(summary = "Add a new tag", description = "Add a new tag")
    public Result<TagDTO> addTag(@RequestBody TagDTO tagDTO) {
        log.info("Adding new tag: {}", tagDTO);
        return tagService.addTag(tagDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a tag", description = "Delete a tag")
    public Result<Void> deleteTag(@PathVariable Long id) {
        log.info("Deleting tag with id={}", id);
        return tagService.deleteTag(id);
    }
}
