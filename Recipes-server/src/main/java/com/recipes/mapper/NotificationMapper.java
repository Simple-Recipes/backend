package com.recipes.mapper;



import com.recipes.dto.NotificationDTO;
import com.recipes.entity.Notification;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    NotificationDTO toDto(Notification notification);
    Notification toEntity(NotificationDTO notificationDTO);
    List<NotificationDTO> toDtoList(List<Notification> notifications);

}
