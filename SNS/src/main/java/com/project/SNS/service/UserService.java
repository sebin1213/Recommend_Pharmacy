package com.project.SNS.service;

import com.project.SNS.exception.ErrorCode;
import com.project.SNS.exception.SimpleSnsApplicationException;
import com.project.SNS.model.User;
import com.project.SNS.model.entity.UserEntity;
import com.project.SNS.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userEntityRepository;

    @Transactional
    public User join(String userName, String password) {
        userEntityRepository.findByUserName(userName).ifPresent(i -> {
            throw new SimpleSnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, String.format("%s is duplicated", userName));
        });

        UserEntity entity = userEntityRepository.save(UserEntity.of(userName,password));
        return User.fromEntity(entity);
    }

    public String login(String userName, String password) {
        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(() -> new SimpleSnsApplicationException(ErrorCode.DUPLICATED_USER_NAME));
        if (!userEntity.getPassword().equals(password)){
            throw new SimpleSnsApplicationException(ErrorCode.INVALID_PASSWORD);
        }
        return "";
    }
}
