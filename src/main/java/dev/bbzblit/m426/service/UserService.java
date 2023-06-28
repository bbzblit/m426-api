package dev.bbzblit.m426.service;

import dev.bbzblit.m426.entity.User;
import dev.bbzblit.m426.repository.ReservationRepository;
import dev.bbzblit.m426.repository.SessionRepository;
import dev.bbzblit.m426.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;

    private final SessionRepository sessionRepository;

    public UserService(final UserRepository userRepository, final ReservationRepository reservationRepository,
                       final SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
        this.sessionRepository = sessionRepository;
    }


    /**
     * Method to create a new User
     * @param user the new user
     * @return the created user
     */
    public User registerUser(User user) {

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "You have to provide a User when you want to create a Account");
        }

        if (this.userRepository.findUserByUsername(user.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "There is already a User with the username " + user.getUsername());
        }

        user = HashService.hashPassword(user);

        return this.userRepository.save(user);
    }


    /**
     * Method to find a user by its username
     * @param username Searched user
     * @return user if it exists
     */
    public User findUserByUsername(String username) {
        return this.userRepository.findUserByUsername(username).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User not found by Username " + username)
        );
    }

    /**
     * Method to return all Users that exist in the DB
     * @return iterable of all Users
     */
    public List<User> getAllUser() {
        return this.userRepository.findAll();
    }

    /**
     * Method to find a User by its id
     * @param id the searched id
     * @return the found user
     */
    public User getUserById(long id) {
        return this.userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Wasn't be able to find a user with the id " + id)
        );
    }

    /**
     * Method to update a User
     * @param user the new User model
     * @param id the User id
     * @return the Updatet user
     */
    public User updateUser(User user, long id) {
        this.getUserById(id);
        return this.userRepository.save(user);
    }

    /**
     * Method to delete a User by its id
     * @param id
     */
    public void deleteUser(long id) {
        long userId = this.getUserById(id).getId();
        this.reservationRepository.deleteByUserId(userId);
        this.sessionRepository.deleteByUserId(userId);
        this.userRepository.deleteById(id);
    }
}
