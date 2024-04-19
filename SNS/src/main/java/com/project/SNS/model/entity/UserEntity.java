package com.project.SNS.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Table
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id = null;

    @Column(name = "user_name", unique = true)
    private String userName;

    private String password;

    @Builder
    public UserEntity(Integer id, String userName, String password){
        this.id = id;
        this.userName = userName;
        this.password = password;
    }
}
