package com.recipes.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Admin login with username and password data transfer object")
public class AdminDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description ="Admin ID")
    private Long id;

    @Schema(description ="Username")
    private String username;

    @Schema(description ="Password")
    private String password;

    @Schema(description ="Email")
    private String email;

    @Schema(description ="Avatar URL")
    private String avatar;

    @Schema(description ="Creation timestamp")
    private String createTime;

    @Schema(description = "JWT token")
    private String token;

}
