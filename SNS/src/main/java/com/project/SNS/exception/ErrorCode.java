package com.project.SNS.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    DUPLICATED_USER_NAME(HttpStatus.CONFLICT, "중복되는 유저입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "유효하지않은 비밀번호입니다."),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "잘못된 접근입니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 게시물입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid token"),
    ;

    private final HttpStatus status;
    private final String message;
}
