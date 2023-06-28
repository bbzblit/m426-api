package dev.bbzblit.m426.repository;

import dev.bbzblit.m426.entity.Car;
import dev.bbzblit.m426.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Repository to access the database table for the {@link Session} model
 */
@Repository
public interface SessionRepository extends JpaRepository<Session, String> {
    public Optional<Session> findSessionByTokenAndExpirationDateIsAfter(String token, LocalDateTime expirationDate);

    public void deleteByUserId(Long id);
}
