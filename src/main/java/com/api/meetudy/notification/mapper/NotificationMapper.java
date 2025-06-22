package com.api.meetudy.notification.mapper;

import com.api.meetudy.notification.dto.NotificationDto;
import com.api.meetudy.notification.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(target = "notificationId", source = "id")
    NotificationDto toNotificationDto(Notification notification);

    List<NotificationDto> toNotificationDtoList(List<Notification> notifications);

}