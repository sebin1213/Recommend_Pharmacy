package com.project.SNS.service;

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
        Optional<UserEntity> userEntity = userEntityRepository.findByUserName(userName);
        userEntityRepository.save(new UserEntity());
        return new User();
    }

    public String login(String userName, String password) {
        return "";
    }
}
