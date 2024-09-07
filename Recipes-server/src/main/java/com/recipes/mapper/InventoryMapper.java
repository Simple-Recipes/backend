package com.recipes.mapper;


import com.recipes.dto.InventoryDTO;
import com.recipes.entity.Inventory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(source = "updatedAt", target = "updatedAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    InventoryDTO toDto(Inventory inventory);

    @Mapping(source = "userId", target = "user.id")
    Inventory toEntity(InventoryDTO inventoryDTO);

    List<InventoryDTO> toDtoList(List<Inventory> inventories);

    List<Inventory> toEntityList(List<InventoryDTO> inventoryDTOs);
}

