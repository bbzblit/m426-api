package dev.bbzblit.m426.controller;

import dev.bbzblit.m426.entity.User;
import dev.bbzblit.m426.entity.dto.LoginModel;
import dev.bbzblit.m426.service.SessionService;
import dev.bbzblit.m426.service.UserService;
import org.hibernate.query.NativeQuery;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService){
        this.userService = userService;
    }

    @PostMapping("/api/v1/register")
    public User register(@RequestBody @Validated User user){
        return this.userService.registerUser(user);
    }

}
