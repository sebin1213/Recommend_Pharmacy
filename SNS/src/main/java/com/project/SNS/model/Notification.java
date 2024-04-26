package com.project.SNS.model;

import com.project.SNS.model.entity.NotificationEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class Notification {
    private Integer id = null;

    private User user;

    private Integer fromUserId;

    private Integer targetId;

    private NotificationType type;

    private Timestamp registeredAt;

    private Timestamp updatedAt;

    private Timestamp removedAt;

    public String getText() {
        return type.getNotificationText();
    }

    public static Notification fromEntity(NotificationEntity entity) {
        return new Notification(
                entity.getId(),
                User.fromEntity(entity.getUser()),
                entity.getTargetId(),
                entity.getFromUserId(),
                entity.getType(),
                entity.getRegisteredAt(),
                entity.getUpdatedAt(),
                entity.getRemovedAt()
        );
    }
}