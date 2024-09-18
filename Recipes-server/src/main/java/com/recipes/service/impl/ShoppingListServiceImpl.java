package com.recipes.service.impl;

import com.recipes.dao.ShoppingListDAO;
import com.recipes.dao.UserDAO;
import com.recipes.dto.ShoppingListDTO;
import com.recipes.entity.ShoppingList;
import com.recipes.entity.User;
import com.recipes.mapper.ShoppingListMapper;
import com.recipes.result.Result;
import com.recipes.service.ShoppingListService;
import java.util.List;
import java.util.stream.Collectors;
import com.recipes.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ShoppingListServiceImpl implements ShoppingListService {

    @Autowired
    private ShoppingListDAO shoppingListDAO;

    @Autowired
    private ShoppingListMapper shoppingListMapper;

    @Autowired
    private UserDAO userDAO;
    @Override
    public Result<ShoppingListDTO> addList(ShoppingListDTO shoppingListDTO) {
        Long userId = UserHolder.getUser().getId();
        if (userId == null) {
            return Result.error("User is not logged in");
        }

        User user = userDAO.findUserById(userId);
        if (user == null) {
            return Result.error("User not found");
        }
        ShoppingList shoppingList = shoppingListMapper.toEntity(shoppingListDTO);
        shoppingList.setUser(user);


        shoppingListDAO.saveShoppingList(shoppingList);
        ShoppingListDTO savedShoppingListDTO = shoppingListMapper.toDto(shoppingList);
        return Result.success(savedShoppingListDTO);
    }

    @Override
    public Result<List<ShoppingListDTO>> getUserList(Long userId) {
        log.info(String.valueOf(userId));
        List<ShoppingList> shoppingLists = shoppingListDAO.findShoppingListByUserId(userId);
        List<ShoppingListDTO> shoppingListDTOs = shoppingLists.stream()
                .map(shoppingListMapper::toDto)
                .collect(Collectors.toList());
        return Result.success(shoppingListDTOs);
    }

    @Override
    public Result<Void> deleteList(Long userId, Long shoppingListId) {
        if (!shoppingListDAO.existsByIdAndUserId(shoppingListId, userId)) {
            return Result.error("Shopping list item not found or you do not have permission to delete this item");
        }
        shoppingListDAO.deleteShoppingList(shoppingListId);
        return Result.success();
    }

    @Override
    public Result<ShoppingListDTO> updateList(Long userId, ShoppingListDTO shoppingListDTO) {
        ShoppingList shoppingList = shoppingListDAO.findShoppingListById(shoppingListDTO.getId());

        if (shoppingList == null || !shoppingList.getUser().getId().equals(userId)) {
            return Result.error("Shopping list item not found or you do not have permission to edit this item");
        }

        if (shoppingListDTO.getItemName() != null) {
            shoppingList.setItemName(shoppingListDTO.getItemName());
        }
        if (shoppingListDTO.getQuantity() != null) {
            shoppingList.setQuantity(shoppingListDTO.getQuantity());
        }
        if (shoppingListDTO.getUnit() != null) {
            shoppingList.setUnit(shoppingListDTO.getUnit());
        }

        shoppingListDAO.saveShoppingList(shoppingList);

        ShoppingListDTO updatedShoppingListDTO = shoppingListMapper.toDto(shoppingList);
        return Result.success(updatedShoppingListDTO);
    }

    @Override
    public Result<ShoppingListDTO> getListDetails(Long id) {
        ShoppingList shoppingList = shoppingListDAO.findShoppingListById(id);
        if (shoppingList == null) {
            return Result.error("Shopping list item not found");
        }
        ShoppingListDTO shoppingListDTO = shoppingListMapper.toDto(shoppingList);
        return Result.success(shoppingListDTO);
    }

    @Override
    public boolean isListOwner(Long userId, Long shoppingListId) {
        ShoppingList shoppingList = shoppingListDAO.findShoppingListById(shoppingListId);
        return shoppingList != null && shoppingList.getUser().getId().equals(userId);
    }
}
