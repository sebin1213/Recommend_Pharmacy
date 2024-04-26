package com.project.SNS.model.entity;

import com.project.SNS.model.NotificationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@Getter
@Entity
@Table(name = "\"notification\"")
@SQLDelete(sql = "UPDATE \"notification\" SET removed_at = NOW() WHERE id=?")
@Where(clause = "removed_at is NULL")
@NoArgsConstructor
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id = null;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column(name = "from_user_id")
    private Integer fromUserId;

    @Column(name = "target_id")
    private Integer targetId;

    @Column(name = "registered_at")
    private Timestamp registeredAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "removed_at")
    private Timestamp removedAt;


    @PrePersist
    void registeredAt() {
        this.registeredAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    void updatedAt() {
        this.updatedAt = Timestamp.from(Instant.now());
    }

    public static NotificationEntity of(NotificationType type, Integer fromUserId, Integer targetId, UserEntity user) {
        NotificationEntity entity = new NotificationEntity();
        entity.type = type;
        entity.fromUserId = fromUserId;
        entity.targetId = targetId;
        entity.user = user;
        return entity;
    }
}
