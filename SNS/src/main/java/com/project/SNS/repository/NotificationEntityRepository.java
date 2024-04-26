package com.project.SNS.repository;

import com.project.SNS.model.entity.NotificationEntity;
import com.project.SNS.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationEntityRepository extends JpaRepository<NotificationEntity, Integer> {
    Page<NotificationEntity> findAllByUser(UserEntity user, Pageable pageable);

}