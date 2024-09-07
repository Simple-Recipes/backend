package com.recipes.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Inventory data transfer object")
public class InventoryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "The unique ID of the inventory item")
    private Long id;

    @Schema(description = "The ID of the user", required = true)
    private Long userId;

    @Schema(description = "The name of the item", required = true)
    private String itemName;

    @Schema(description = "The quantity of the item", required = true)
    private Integer quantity;

    @Schema(description = "The unit of the item")
    private String unit;

    @Schema(description = "The creation time of the inventory item")
    private LocalDateTime createdAt;

    @Schema(description = "The last update time of the inventory item")
    private LocalDateTime updatedAt;
}


