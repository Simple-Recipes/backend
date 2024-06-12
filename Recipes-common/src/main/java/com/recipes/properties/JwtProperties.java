package com.recipes.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "recipes.jwt")
@Data
public class JwtProperties {

    /**
     * Configuration related to jwt token generation for employees on the management side
     */
    private String adminSecretKey;
    private long adminTtl;
    private String adminTokenName;

    /**
     * Configuration related to generation of jwt tokens on the user side
     */
    private String userSecretKey;
    private long userTtl;
    private String userTokenName;

}
