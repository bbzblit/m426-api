package dev.bbzblit.m426.controller;

import dev.bbzblit.m426.entity.User;
import dev.bbzblit.m426.service.SessionService;
import dev.bbzblit.m426.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    private final SessionService sessionService;

    public UserController(final UserService userService, final SessionService sessionService) {
        this.userService = userService;
        this.sessionService = sessionService;
    }

    /**
     * register a new user
     * @param user
     * @return user
     */
    @PostMapping("/api/v1/register")
    public User register(@RequestBody @Valid User user) {
        return this.userService.registerUser(user);
    }

    /**
     * returns a logged in user
     * @param session
     * @return
     */
    @GetMapping("/api/v1/user")
    public User getLoggedInUser(@CookieValue("session") String session) {
        return this.sessionService.getSessionByToken(session).getUser();
    }

    @GetMapping("/api/v1/users")
    public List<User> getUsers(@CookieValue("session") String session) {
        this.sessionService.isAdministrator(session);
        return this.userService.getAllUser();
    }

    @PutMapping("/api/v1/user/{id}")
    public User updateUser(@CookieValue("session") String session, @PathVariable("id") Long id,
                           @RequestBody @Valid User user){
        this.sessionService.isAdministrator(session);
        return this.userService.updateUser(user, id);
    }

    @DeleteMapping("/api/v1/user/{id}")
    @Transactional
    public void deleteUser(@CookieValue("session") String session, @PathVariable("id") Long id){
        this.sessionService.isAdministrator(session);
        this.userService.deleteUser(id);
    }
}
