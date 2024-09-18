package com.recipes.service;

import com.recipes.dto.InventoryDTO;
import com.recipes.dto.ShoppingListDTO;
import com.recipes.result.Result;

import java.util.List;

public interface ShoppingListService {
    Result<ShoppingListDTO> addList(ShoppingListDTO shoppingListDTO);
    Result<List<ShoppingListDTO>> getUserList(Long userId);

    Result<Void> deleteList(Long userId, Long shoppingListId);

    Result<ShoppingListDTO> updateList(Long id, ShoppingListDTO shoppingListDTO);

    Result<ShoppingListDTO> getListDetails(Long id);

    boolean isListOwner(Long userId, Long shoppingListId);





}
