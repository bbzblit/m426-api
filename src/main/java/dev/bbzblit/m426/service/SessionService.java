package dev.bbzblit.m426.service;

import dev.bbzblit.m426.entity.Session;
import dev.bbzblit.m426.entity.User;
import dev.bbzblit.m426.entity.dto.LoginModel;
import dev.bbzblit.m426.helper.RandomString;
import dev.bbzblit.m426.repository.SessionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final UserService userService;

    public SessionService(final SessionRepository sessionRepository,final UserService userService){
        this.sessionRepository = sessionRepository;
        this.userService = userService;
    }



    public Session login(LoginModel loginModel){
        try{
            User user = userService.findUserByUsername(loginModel.getUsername());
            byte[] salt = user.getSaltValue();
            if(HashService.verifyPassword(loginModel.getPassword(), salt, user.getPassword())){

                RandomString randomString = new RandomString();
                randomString.nextString();

                Session session = new Session(randomString.nextString(), user, LocalDateTime.now().plusHours(2));
                return this.sessionRepository.save(session);
            }
        } catch (ResponseStatusException ex){ /*NotFound exception should be wrong password or username */}
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong Password or Username");
    }

}
