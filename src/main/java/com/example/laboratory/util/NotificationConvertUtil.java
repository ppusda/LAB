package com.example.laboratory.util;

import com.example.laboratory.entity.Notification;
import com.example.laboratory.response.NotificationResponse;
import org.springframework.stereotype.Component;

@Component
public class NotificationConvertUtil {

    public NotificationResponse convertToNotificationResponse(Notification notification) {
        return NotificationResponse.builder()
                .notificationId(notification.getId())
                .message(notification.getMessage())
                .redirectUrl(notification.getRedirectUrl())
                .isRead(notification.getIsRead())
                .createDate(notification.getCreateDate())
                .build();
    }


}
