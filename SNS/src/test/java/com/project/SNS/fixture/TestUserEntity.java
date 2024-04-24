package com.project.SNS.fixture;

import com.project.SNS.model.entity.UserEntity;

public class TestUserEntity {

    public static UserEntity get(String userName, String password) {
        UserEntity entity = UserEntity.of(userName, password);
        return entity;
    }
}
