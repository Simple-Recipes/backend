package com.recipes.controller;


import com.recipes.dto.NotificationDTO;
import com.recipes.result.Result;
import com.recipes.service.NotificationService;
import com.recipes.utils.UserHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notification")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@Tag(name = "Notification API", description = "Operations related to notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/getMyNotifications")
    @Operation(summary = "Get notifications for the logged-in user", description = "Get all notifications for the logged-in user")
    public Result<List<NotificationDTO>> getMyNotifications() {
        Long userId = UserHolder.getUser().getId();
        log.info("Getting notifications for user with id={}", userId);
        return notificationService.getNotifications(userId);
    }


    @Operation(summary = "Mark notification as read", description = "Mark a specific notification as read")
    @PostMapping("/markAsRead")
    public ResponseEntity<Void> markAsRead(@RequestBody Map<String, Long> request) {
        Long notificationId = request.get("notificationId");
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok().build();
    }


}
