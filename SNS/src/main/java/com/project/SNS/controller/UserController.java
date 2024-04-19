package com.project.SNS.controller;

import com.project.SNS.controller.request.UserJoinRequest;
import com.project.SNS.controller.request.UserLoginRequest;
import com.project.SNS.controller.response.Response;
import com.project.SNS.controller.response.UserJoinResponse;
import com.project.SNS.controller.response.UserLoginResponse;
import com.project.SNS.model.User;
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
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request) {
        User user= userService.join(request.getName(), request.getPassword());
        return Response.success(UserJoinResponse.fromUser(user));
    }

    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        String token = userService.login(request.getName(), request.getPassword());
        return Response.success(new UserLoginResponse(token));
    }



}
