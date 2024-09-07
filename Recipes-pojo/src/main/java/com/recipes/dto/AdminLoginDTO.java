package com.recipes.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Admin login with username and password data transfer object")
public class AdminLoginDTO {

    private static final long serialVersionUID = 1L;

    @Schema(description ="Username")
    private String username;

    @Schema(description ="Password")
    private String password;
}
