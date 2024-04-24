package com.project.SNS.repository;

import com.project.SNS.model.entity.LikeEntity;
import com.project.SNS.model.entity.PostEntity;
import com.project.SNS.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeEntityRepository extends JpaRepository<LikeEntity, Integer> {
    Optional<LikeEntity> findByUserAndPost(UserEntity user, PostEntity post);
}
