package com.project.SNS.controller;

import com.project.SNS.controller.request.UserJoinRequest;
import com.project.SNS.controller.request.UserLoginRequest;
import com.project.SNS.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

//@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public void join(@RequestBody UserJoinRequest request) {
        userService.join(request.getName(), request.getPassword());
    }

    @PostMapping("/login")
    public void join(@RequestBody UserLoginRequest request) {
        userService.login(request.getName(), request.getPassword());
    }


}