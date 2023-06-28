package dev.bbzblit.m426.repository;

import dev.bbzblit.m426.entity.Car;
import dev.bbzblit.m426.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository to access the database table for the {@link User} model
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUsername(String username);
}
