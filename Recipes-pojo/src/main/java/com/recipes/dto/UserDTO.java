package com.recipes.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * UserDTO represents the data transfer object for transferring user information.
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User data transfer object")
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description ="User ID")
    private Long id;

    @Schema(description ="Username")
    private String username;

    @Schema(description ="Email")
    private String email;

    @Schema(description ="Avatar URL")
    private String avatar;

    @Schema(description ="Creation timestamp")
    private String createTime;
}
