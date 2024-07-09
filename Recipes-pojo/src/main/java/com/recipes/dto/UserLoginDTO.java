package com.recipes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NoArgsConstructor;

/**
 * UserLoginDTO represents the data transfer object for transferring user login information.
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User login data transfer object")
public class UserLoginDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description ="Username")
    private String username;

    @Schema(description ="Password")
    private String password;

    @Schema(description ="Remember me")
    private String code;
}
