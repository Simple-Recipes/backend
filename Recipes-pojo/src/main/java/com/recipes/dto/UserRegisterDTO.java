package com.recipes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


import java.io.Serializable;

/**
 * UserRegisterDTO represents the data transfer object for transferring user registration information.
 */
@Data
@Schema(description = "User registration data transfer object")
public class UserRegisterDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description ="Username")
    private String username;

    @Schema(description ="Password")
    private String password;

    @Schema(description ="Email")
    private String email;
}
