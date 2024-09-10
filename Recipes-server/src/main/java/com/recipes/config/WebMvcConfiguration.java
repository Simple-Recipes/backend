package com.recipes.config;

import com.recipes.interceptor.JwtTokenAdminInterceptor;
import com.recipes.interceptor.JwtTokenUserInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Configuration class to register web layer related components
 */
@Configuration
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;

    @Autowired
    private JwtTokenUserInterceptor jwtTokenUserInterceptor;

    /**
     * Register custom interceptors
     *
     * @param registry
     */
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("Starting to register custom interceptors...");

        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/login");

        registry.addInterceptor(jwtTokenUserInterceptor)
                .addPathPatterns("/user/**", "/recipes/**", "/favorites/**", "/comments/**", "/likes/**", "/tags/**", "/recommendation/**","/inventory/**")
                .excludePathPatterns(
                        "/user/loginWithPassword",
                        "/user/loginWithCode",
                        "/user/sendCode",
                        "/user/register",
                        "/user/forgotPassword",
                        "/user/resetPassword"
                );
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "http://localhost:8082") // 允许两个前端地址
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Authorization")
                .allowCredentials(true);
    }

    /**
     * Generate API documentation using Knife4j
     * @return
     */
    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        String groupName = "2.X Version";
        Docket docket = new Docket(DocumentationType.OAS_30)
                .host("localhost:8085")
                .apiInfo(apiInfo())
                .groupName(groupName)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.recipes.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Recipes API Documentation")
                .version("2.9.2")
                .description("Recipes API Documentation")
                .termsOfServiceUrl("http://localhost:8085/")
                .build();
    }

    /**
     * Set static resource mappings
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");

        /** Configure Knife4j to display documentation */
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        /**
         * Configure Swagger-ui to display documentation
         */
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        /** Common part content */
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
