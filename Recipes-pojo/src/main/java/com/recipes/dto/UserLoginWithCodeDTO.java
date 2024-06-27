package com.recipes.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
/**
 * UserLoginWithCodeDTO represents the data transfer object for transferring user login information with email and code.
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User login with email and code data transfer object")
public class UserLoginWithCodeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description ="Email")
    private String email;

    @Schema(description ="Verification code")
    private String code;
}
