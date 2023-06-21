package dev.bbzblit.m426.repository;

import dev.bbzblit.m426.entity.Reservation;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends CrudRepository<Reservation, Long> {

    public List<Reservation> findReservationsByCarIdAndStartIsLessThanAndEndIsGreaterThan(
            Long carId,LocalDateTime start, LocalDateTime end);
    public List<Reservation> findReservationsByStartIsLessThanAndEndIsGreaterThan(LocalDateTime start,
                                                                                  LocalDateTime end);

    public List<Reservation> findReservationsByStartBefore(LocalDateTime localDateTime);

    public List<Reservation> findReservationsByUserIdAndStartAfter(Long id, LocalDateTime start);
}
