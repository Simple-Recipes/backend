package com.recipes.dto;

import lombok.Data;

import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
/**
 * UserLoginDTO represents the data transfer object for transferring user login information.
 */
@Data
@Schema(description = "User login data transfer object")
public class UserLoginDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description ="Username")
    private String username;

    @Schema(description ="Password")
    private String password;
}
