package com.recipes.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;

/**
 * UserLoginWithPasswordDTO represents the data transfer object for transferring user login information with username and password.
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User login with username and password data transfer object")
public class UserLoginWithPasswordDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description ="Username")
    private String username;

    @Schema(description ="Password")
    private String password;
}
