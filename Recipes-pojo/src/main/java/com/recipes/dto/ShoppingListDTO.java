package com.recipes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "ShoppingList data transfer object")
public class ShoppingListDTO  implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "The unique ID of the ShoppingList item")
    private Long id;

    @Schema(description = "The ID of the user", required = true)
    private Long userId;

    @Schema(description = "The name of the item", required = true)
    private String itemName;

    @Schema(description = "The quantity of the item", required = true)
    private Integer quantity;

    @Schema(description = "The unit of the item")
    private String unit;
}
