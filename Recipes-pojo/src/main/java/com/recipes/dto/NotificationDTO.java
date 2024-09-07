package com.recipes.dto;

import lombok.Data;

@Data
public class NotificationDTO {
    private Long id;
    private Long recipientId;
    private Long senderId;
    private Long recipeId;
    private Long commentId;
    private String message;
    private Boolean isRead;
    private String createTime;
}
