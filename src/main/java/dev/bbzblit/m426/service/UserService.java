package dev.bbzblit.m426.service;

import dev.bbzblit.m426.entity.Session;
import dev.bbzblit.m426.entity.User;
import dev.bbzblit.m426.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.PushBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.yaml.snakeyaml.events.Event;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;


    public UserService(final UserRepository userRepository){
        this.userRepository = userRepository;
    }


    public User registerUser(User user){

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "You have to provide a User when you want to create a Account");
        }

        if (this.userRepository.findUserByUsername(user.getUsername()).isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "There is already a User with the username " + user.getUsername());
        }

        user = HashService.hashPassword(user);

        return this.userRepository.save(user);
    }


    public User findUserByUsername(String username) {
        return this.userRepository.findUserByUsername(username).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User not found by Username " + username)
        );
    }

    public List<User> getAllUser(){
        return this.userRepository.findAll();
    }

    public User getUserById(long id){
        return this.userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Wasn't be able to find a user with the id " + id)
        );
    }

    public User updateUser(User user, long id){
        this.getUserById(id);
        return this.userRepository.save(user);
    }

    public void deleteUser(long id){
        this.getUserById(id);
        this.userRepository.deleteById(id);
    }
}
