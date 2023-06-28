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

/**
 * Service to manage the @{@link Session} object.
 */
@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final UserService userService;

    public SessionService(final SessionRepository sessionRepository, final UserService userService) {
        this.sessionRepository = sessionRepository;
        this.userService = userService;
    }


    /**
     * Method to log into the application
     * @param loginModel The Username and Password
     * @return a new Session
     */
    public Session login(LoginModel loginModel) {
        try {
            User user = userService.findUserByUsername(loginModel.getUsername());
            byte[] salt = user.getSaltValue();
            if (HashService.verifyPassword(loginModel.getPassword(), salt, user.getPassword())) {

                RandomString randomString = new RandomString();
                randomString.nextString();

                Session session = new Session(randomString.nextString(), user, LocalDateTime.now().plusHours(2));
                return this.sessionRepository.save(session);
            }
        } catch (ResponseStatusException ex) { /*NotFound exception should be wrong password or username */}
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong Password or Username");
    }

    /**
     * Method to get a session by its session key
     * @param token the providet session key
     * @return session if it exists
     */
    public Session getSessionByToken(String token) {
        return this.sessionRepository.findSessionByTokenAndExpirationDateIsAfter(token,
                LocalDateTime.now()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        "Your currently not logged in or your session is expired")
        );
    }


    /**
     * Method to log out of a session
     * @param token the session cookie
     */
    public void logout(String token) {
        getSessionByToken(token);
        this.sessionRepository.deleteById(token);
    }

    /**
     * Method to evaluate if the requesting user has a valid session
     * @param token the session token
     */
    public void isLoggedIn(final String token) {
        if (getSessionByToken(token).getUser() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "You need to be logged in to use this app");
        }
    }

    /**
     * Method to evaluate if the current user has admin priviliges
     * @param token the session id of the current user
     */
    public void isAdministrator(final String token) {
        User user = getSessionByToken(token).getUser();
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "You need to be logged in to use this app");
        }

        if (!user.isEmployee()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You need to be employee to access this Endpoint");
        }
    }


}