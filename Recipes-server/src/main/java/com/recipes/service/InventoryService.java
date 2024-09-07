package com.recipes.service;


import com.recipes.dto.InventoryDTO;
import com.recipes.dto.RecipeDTO;
import com.recipes.result.Result;

import java.util.List;

public interface InventoryService {
    Result<InventoryDTO> addInventory(InventoryDTO inventoryDTO);
    Result<List<InventoryDTO>> getUserInventories(Long userId);
    Result<Void> deleteInventory(Long userId,Long inventoryId);
    Result<InventoryDTO> updateInventory(Long id, InventoryDTO inventoryDTO);
    Result<InventoryDTO> getInventoryDetails(Long id);

    boolean isInventoryOwner(Long userId, Long inventoryId);


}

