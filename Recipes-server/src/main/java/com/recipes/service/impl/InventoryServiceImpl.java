package com.recipes.service.impl;


import com.recipes.dao.InventoryDAO;
import com.recipes.dto.InventoryDTO;
import com.recipes.entity.Inventory;
import com.recipes.dao.UserDAO;
import com.recipes.entity.User;
import com.recipes.mapper.InventoryMapper;
import com.recipes.result.Result;
import com.recipes.service.InventoryService;
import com.recipes.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryDAO inventoryDAO;

    @Autowired
    private InventoryMapper inventoryMapper;

    @Autowired
    private UserDAO userDAO;

    @Override
    public Result<InventoryDTO> addInventory(InventoryDTO inventoryDTO) {
        Long userId = UserHolder.getUser().getId();
        if (userId == null) {
            return Result.error("User is not logged in");
        }

        User user = userDAO.findUserById(userId);
        if (user == null) {
            return Result.error("User not found");
        }

        Inventory inventory = inventoryMapper.toEntity(inventoryDTO);
        inventory.setUser(user);
        inventory.setCreatedAt(LocalDateTime.now());
        inventory.setUpdatedAt(LocalDateTime.now());

        inventoryDAO.saveInventory(inventory);
        InventoryDTO saveInventoryDTO = inventoryMapper.toDto(inventory);
        return Result.success(inventoryMapper.toDto(inventory));
    }

    @Override
    public Result<List<InventoryDTO>> getUserInventories(Long userId) {
        List<Inventory> inventories = inventoryDAO.findInventoriesByUserId(userId);
        List<InventoryDTO> inventoryDTOs = inventories.stream()
                .map(inventoryMapper::toDto)
                .collect(Collectors.toList());
        return Result.success(inventoryDTOs);
    }



    @Override
    public Result<Void> deleteInventory(Long userId,Long inventoryId) {
        if (!inventoryDAO.existsByIdAndUserId(inventoryId, userId)) {
            return Result.error("Inventory not found or you do not have permission to delete this inventory");
        }
        inventoryDAO.deleteInventory(inventoryId);
        return Result.success();
    }


    @Override
    @Transactional
    public Result<InventoryDTO> updateInventory(Long userId, InventoryDTO inventoryDTO) {
        Inventory inventory = inventoryDAO.findInventoryById(inventoryDTO.getId());

        if (inventory == null || !inventory.getUser().getId().equals(userId)) {
            return Result.error("Inventory not found or you do not have permission to edit this inventory item");
        }

        // Update only the fields that are provided in the DTO
        if (inventoryDTO.getItemName() != null) {
            inventory.setItemName(inventoryDTO.getItemName());
        }
        if (inventoryDTO.getQuantity() != null) {
            inventory.setQuantity(inventoryDTO.getQuantity());
        }
        if (inventoryDTO.getUnit() != null) {
            inventory.setUnit(inventoryDTO.getUnit());
        }

        inventory.setUpdatedAt(LocalDateTime.now());
        inventoryDAO.saveInventory(inventory);

        InventoryDTO updatedInventoryDTO = inventoryMapper.toDto(inventory);
        return Result.success(updatedInventoryDTO);
    }

    @Override
    public Result<InventoryDTO> getInventoryDetails(Long id) {
        Inventory inventory = inventoryDAO.findInventoryById(id);
        if (inventory == null) {
            return Result.error("Inventory not found");
        }
        InventoryDTO inventoryDTO = inventoryMapper.toDto(inventory);
        return Result.success(inventoryDTO);
    }

    @Override
    public boolean isInventoryOwner(Long userId, Long inventoryId) {
        Inventory inventory = inventoryDAO.findInventoryById(inventoryId);
        return inventory != null && inventory.getUser().getId().equals(userId);
    }



}

