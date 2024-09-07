package com.recipes.service;




import com.recipes.dto.NotificationDTO;
import com.recipes.result.Result;

import java.util.List;

public interface NotificationService {
    Result<NotificationDTO> saveNotification(NotificationDTO notificationDTO);
    Result<List<NotificationDTO>> getNotifications(Long recipientId);
    Result<Void> markAsRead(Long notificationId);
}



