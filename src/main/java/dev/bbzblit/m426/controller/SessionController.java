package dev.bbzblit.m426.controller;

import dev.bbzblit.m426.entity.Session;
import dev.bbzblit.m426.entity.User;
import dev.bbzblit.m426.entity.dto.LoginModel;
import dev.bbzblit.m426.service.SessionService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionController {

    private final SessionService sessionService;


    public SessionController(final SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping("/api/v1/login")
    public ResponseEntity<Session> login(@RequestBody @Validated LoginModel loginModel){

        Session session = this.sessionService.login(loginModel);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie","session="+session.getToken());
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(session);
    }

}
