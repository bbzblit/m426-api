package dev.bbzblit.m426.controller;

import dev.bbzblit.m426.entity.Session;
import dev.bbzblit.m426.entity.dto.LoginModel;
import dev.bbzblit.m426.service.SessionService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionController {

    private final SessionService sessionService;


    public SessionController(final SessionService sessionService) {
        this.sessionService = sessionService;
    }

    /**
     * for login
     * @param loginModel
     * @return Session
     */
    @PostMapping("/api/v1/login")
    public ResponseEntity<Session> login(@RequestBody @Validated LoginModel loginModel) {

        Session session = this.sessionService.login(loginModel);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", "session=" + session.getToken() + ";Path=/api;SameSite=Strict");
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(session);
    }

    /**
     * for logout
     * @param session
     * @return header
     */
    @PostMapping("/api/v1/logout")
    public ResponseEntity<Void> logout(@CookieValue("session") String session) {
        this.sessionService.logout(session);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", "session=; Path=/api;SameSite=Strict; Expires=Thu, 01 Jan 1970 00:00:01 GMT;");
        return ResponseEntity.ok().headers(headers).body(null);
    }

}
