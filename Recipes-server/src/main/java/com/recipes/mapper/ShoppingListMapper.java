package com.recipes.mapper;
import com.recipes.dto.ShoppingListDTO;
import com.recipes.entity.ShoppingList;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShoppingListMapper {
    @Mapping(source = "user.id", target = "userId")
    ShoppingListDTO toDto(ShoppingList shoppingList);

    @Mapping(source = "userId", target = "user.id")
    ShoppingList toEntity(ShoppingListDTO shoppingListDTO);

}
