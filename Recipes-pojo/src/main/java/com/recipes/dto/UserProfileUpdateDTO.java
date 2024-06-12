package com.recipes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;

/**
 * UserProfileUpdateDTO represents the data transfer object for updating user profile information.
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User profile update data transfer object")
public class UserProfileUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Username")
    private String username;

    @Schema(description = "Email")
    private String email;

    @Schema(description = "Avatar URL")
    private String avatar;

    @Schema(description = "Password")
    private String password;
}