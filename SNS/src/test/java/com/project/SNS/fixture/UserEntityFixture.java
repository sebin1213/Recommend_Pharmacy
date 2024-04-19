package com.project.SNS.fixture;

import com.project.SNS.model.entity.UserEntity;

import java.sql.Timestamp;
import java.time.Instant;

public class UserEntityFixture {

    public static UserEntity get(String userName, String password) {
        UserEntity entity = UserEntity.of(userName, password);
        return entity;
    }
}
