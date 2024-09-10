package com.recipes.controller;


import com.recipes.dto.InventoryDTO;
import com.recipes.dto.RecipeDTO;
import com.recipes.result.Result;
import com.recipes.service.InventoryService;
import com.recipes.utils.UserHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/inventory")
@CrossOrigin(origins = {"http://localhost:8082", "http://localhost:3000"})
@Slf4j
@Tag(name = "Inventory API", description = "Operations related to inventory management")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/add")
    @Operation(summary = "Add an inventory item", description = "Add a new inventory item for the logged-in user")
    public Result<InventoryDTO> addInventory(@RequestBody InventoryDTO inventoryDTO) {
        Long userId = UserHolder.getUser().getId();
        if (userId == null) {
            return Result.error("User is not logged in");
        }
        inventoryDTO.setUserId(userId);
        inventoryDTO.setCreatedAt(LocalDateTime.now());
        inventoryDTO.setUpdatedAt(LocalDateTime.now());
        log.info("Adding inventory item: {}", inventoryDTO);
        return inventoryService.addInventory(inventoryDTO);
    }

    @GetMapping("/getAllMyInventory")
    @Operation(summary = "Get user inventories", description = "Get a list of inventory items for a specific user")
    public Result<List<InventoryDTO>> getUserInventories() {
        Long userId = UserHolder.getUser().getId();
        log.info("Getting inventory items for user with id={}", userId);
        return inventoryService.getUserInventories(userId);
    }



    @DeleteMapping("/delete")
    @Operation(summary = "Delete an inventory item", description = "Delete a specific inventory item by its ID")
    public Result<Void> deleteInventory(@Parameter(description = "ID of the inventory to be deleted", required = true, in = ParameterIn.QUERY)
                                            @RequestParam Long inventoryId)  {
        Long userId = UserHolder.getUser().getId();
        log.info("Deleting inventory item with id={}", userId,inventoryId);
        return inventoryService.deleteInventory(userId,inventoryId);
    }


    @GetMapping("/edit/{id}")
    @Operation(summary = "Update an inventory item", description = "Update a specific inventory item by its ID")
    public Result<InventoryDTO> getInventorForEdit(@PathVariable Long id) {
        log.info("Updating inventory item with id={}", id);
        return inventoryService.getInventoryDetails(id);
    }

    @PutMapping("/edit")
    @Operation(summary = "Edit a inventory", description = "Edit an existing inventory")
    public ResponseEntity<Result<InventoryDTO>> updateInventory(@RequestBody InventoryDTO inventoryDTO) {
        Long userId = UserHolder.getUser().getId();
        if (userId == null) {
            log.error("User is not logged in");
            return new ResponseEntity<>(Result.error("User is not logged in"), HttpStatus.UNAUTHORIZED);
        }
        log.info("Editing recipe: userId={}, inventoryDTO={}", userId, inventoryDTO);

        try {

            if (!inventoryService.isInventoryOwner(userId, inventoryDTO.getId())) {
                log.error("User {} is not the owner of recipe {}", userId, inventoryDTO.getId());
                return new ResponseEntity<>(Result.error("You do not have permission to edit this recipe"), HttpStatus.FORBIDDEN);
            }

            Result<InventoryDTO> result = inventoryService.updateInventory(userId, inventoryDTO);

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error editing recipe", e);
            return new ResponseEntity<>(Result.error("Error editing recipe"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}

