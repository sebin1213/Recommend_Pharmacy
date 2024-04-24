package com.project.SNS.service;

import com.project.SNS.exception.ErrorCode;
import com.project.SNS.exception.SimpleSnsApplicationException;
import com.project.SNS.fixture.TestUserEntity;
import com.project.SNS.model.entity.UserEntity;
import com.project.SNS.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserEntityRepository userEntityRepository;

    @MockBean
    BCryptPasswordEncoder encoder;



    @Test
    void 회원가입_정상동작() {
        UserEntity userEntity= TestUserEntity.get("name","password");
        String userName = userEntity.getUserName();
        String password = userEntity.getPassword();
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
        when(encoder.encode(password)).thenReturn("암호화된 패스워드");
        when(userEntityRepository.save(any())).thenReturn(mock(UserEntity.class));
        Assertions.assertDoesNotThrow(() -> userService.join(userName,password));
    }


    @Test
    void 회원가입할때_아이디가_중복되면_다르면_에러발생() {
        UserEntity userEntity= TestUserEntity.get("name","password");
        String userName = userEntity.getUserName();
        String password = userEntity.getPassword();
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(encoder.encode(password)).thenReturn("암호화된 패스워드");
        when(userEntityRepository.save(any())).thenReturn(userEntity);

        SimpleSnsApplicationException exception = Assertions.assertThrows(SimpleSnsApplicationException.class,
                () -> userService.join(userName, password));
        Assertions.assertEquals(ErrorCode.DUPLICATED_USER_NAME, exception.getErrorCode());
    }

    @Test
    void 로그인_정상동작() {
        String userName = "name";
        String password = "password";
        UserEntity userEntity= TestUserEntity.get(userName,password);
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(encoder.matches(password,userEntity.getPassword())).thenReturn(true);

        Assertions.assertDoesNotThrow(() -> userService.login(userName,password));
    }

    @Test
    void 로그인할때_유저가_존재하지_않는경우_에러발생() {
        String userName = "name";
        String password = "password";
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());

        SimpleSnsApplicationException exception = Assertions.assertThrows(SimpleSnsApplicationException.class,
                () -> userService.login(userName, password));
        Assertions.assertEquals(ErrorCode.DUPLICATED_USER_NAME, exception.getErrorCode());
    }


    @Test
    void 로그인할때_패스워드_다를경우_에러발생() {
        String userName = "name";
        String password = "password";
        String failPassword = "fail";

        UserEntity userEntity= TestUserEntity.get(userName,password);
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));

        SimpleSnsApplicationException exception = Assertions.assertThrows(SimpleSnsApplicationException.class,
                () -> userService.login(userName, failPassword));
        Assertions.assertEquals(ErrorCode.INVALID_PASSWORD, exception.getErrorCode());
    }
}