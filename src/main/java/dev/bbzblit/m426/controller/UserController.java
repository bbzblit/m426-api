package dev.bbzblit.m426.controller;

import dev.bbzblit.m426.entity.Session;
import dev.bbzblit.m426.entity.User;
import dev.bbzblit.m426.entity.dto.LoginModel;
import dev.bbzblit.m426.service.SessionService;
import dev.bbzblit.m426.service.UserService;
import org.hibernate.query.NativeQuery;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService userService;

    private final SessionService sessionService;

    public UserController(final UserService userService, final SessionService sessionService){
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @PostMapping("/api/v1/register")
    public User register(@RequestBody @Validated User user){
        return this.userService.registerUser(user);
    }


    @GetMapping("/api/v1/user")
    public User getLoggedInUser(@CookieValue("session") String session){
        return this.sessionService.getSessionByToken(session).getUser();
    }
}
