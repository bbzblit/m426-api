package dev.bbzblit.m426.repository;

import dev.bbzblit.m426.entity.Reservation;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends CrudRepository<Reservation, Long> {

    public List<Reservation> findReservationsByCarIdAndStartIsBeforeAndEndIsAfter(
            Long carId,LocalDateTime start, LocalDateTime end);

    public List<Reservation> findReservationsByStartBefore(LocalDateTime localDateTime);

    public List<Reservation> findReservationsByUserIdAndStartAfter(Long id, LocalDateTime start);
}
