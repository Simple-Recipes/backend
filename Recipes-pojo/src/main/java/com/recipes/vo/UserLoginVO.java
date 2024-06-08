package com.recipes.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
/**
 * UserLoginVO represents the view object for displaying user login information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User login view object")
public class UserLoginVO implements Serializable {

    private Long id;
    private String username;
    private String token;
}
