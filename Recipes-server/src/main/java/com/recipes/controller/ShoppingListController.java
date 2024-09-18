package com.recipes.controller;
import com.recipes.dto.ShoppingListDTO;
import com.recipes.service.ShoppingListService;
import com.recipes.utils.UserHolder;
import io.swagger.v3.oas.annotations.Operation;
import com.recipes.result.Result;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@RestController
@RequestMapping("/list")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@Tag(name = "ShoppingList API", description = "Operations related to ShoppingList management")
public class ShoppingListController {

    @Autowired
    private ShoppingListService shoppingListService;

    @PostMapping("/add")
    @Operation(summary = "Add a shopping list item", description = "Add a new shopping list item for the logged-in user")
    public Result<ShoppingListDTO> addShoppingListItem(@RequestBody ShoppingListDTO shoppingListDTO) {
        Long userId = UserHolder.getUser().getId();
        shoppingListDTO.setUserId(userId);
        log.info("Adding shopping list item: {}", shoppingListDTO);
        return shoppingListService.addList(shoppingListDTO);
    }


    @GetMapping("/getAllMyShoppingList")
    @Operation(summary = "Get user shopping list", description = "Get a list of shopping list items for a specific user")
    public Result<List<ShoppingListDTO>> getUserShoppingList() {
        Long userId = UserHolder.getUser().getId();
        log.info("Getting shopping list items for user with id={}", userId);
        return shoppingListService.getUserList(userId);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete a shopping list item", description = "Delete a specific shopping list item by its ID")
    public Result<Void> deleteShoppingListItem(@Parameter(description = "ID of the shopping list item to be deleted", required = true, in = ParameterIn.QUERY)
                                               @RequestParam Long shoppingListId)  {
        Long userId = UserHolder.getUser().getId();
        log.info("Deleting shopping list item with id={}, userId={}", shoppingListId, userId);
        return shoppingListService.deleteList(userId, shoppingListId);
    }

    @GetMapping("/edit/{id}")
    @Operation(summary = "Update a shopping list item", description = "Update a specific shopping list item by its ID")
    public Result<ShoppingListDTO> getShoppingListItemForEdit(@PathVariable Long id) {
        log.info("Updating shopping list item with id={}", id);
        return shoppingListService.getListDetails(id);
    }

    @PutMapping("/edit")
    @Operation(summary = "Edit a shopping list item", description = "Edit an existing shopping list item")
    public ResponseEntity<Result<ShoppingListDTO>> updateShoppingListItem(@RequestBody ShoppingListDTO shoppingListDTO) {
        Long userId = UserHolder.getUser().getId();
        if (userId == null) {
            log.error("User is not logged in");
            return new ResponseEntity<>(Result.error("User is not logged in"), HttpStatus.UNAUTHORIZED);
        }

        try {
            if (!shoppingListService.isListOwner(userId, shoppingListDTO.getId())) {
                log.error("User {} is not the owner of shopping list item {}", userId, shoppingListDTO.getId());
                return new ResponseEntity<>(Result.error("You do not have permission to edit this shopping list item"), HttpStatus.FORBIDDEN);
            }

            Result<ShoppingListDTO> result = shoppingListService.updateList(userId, shoppingListDTO);

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error editing shopping list item", e);
            return new ResponseEntity<>(Result.error("Error editing shopping list item"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

}





}
