package com.recipes.service.impl;


import com.recipes.dao.NotificationDAO;
import com.recipes.dto.NotificationDTO;
import com.recipes.entity.Notification;
import com.recipes.mapper.NotificationMapper;
import com.recipes.result.Result;
import com.recipes.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationDAO notificationDAO;

    @Autowired
    private NotificationMapper notificationMapper;

    @Override
    public Result<NotificationDTO> saveNotification(NotificationDTO notificationDTO) {
        Notification notification = notificationMapper.toEntity(notificationDTO);
        notificationDAO.saveNotification(notification);
        return Result.success(notificationMapper.toDto(notification));
    }

    @Override
    public Result<List<NotificationDTO>> getNotifications(Long recipientId) {
        List<Notification> notifications = notificationDAO.findNotificationsByRecipientId(recipientId);
        return Result.success(notificationMapper.toDtoList(notifications));
    }

    @Override
    public Result<Void> markAsRead(Long notificationId) {
        notificationDAO.markAsRead(notificationId);
        return Result.success();
    }
}
