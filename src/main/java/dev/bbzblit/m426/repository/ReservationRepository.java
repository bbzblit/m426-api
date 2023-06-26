package dev.bbzblit.m426.repository;

import dev.bbzblit.m426.entity.Reservation;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends CrudRepository<Reservation, Long> {

    public List<Reservation> findReservationsByCarIdAndStartIsLessThanEqualAndEndIsGreaterThanEqual(
            Long carId,LocalDate start, LocalDate end);
    public List<Reservation> findReservationsByStartBetween(LocalDate start,
                                                            LocalDate end);

    public List<Reservation> findReservationsByStartIsGreaterThanEqualAndEndIsLessThanEqual(LocalDate start,
                                                                                            LocalDate end);

    public List<Reservation> findReservationsByUserIdAndStartGreaterThanEqual(Long id, LocalDate start);

    public List<Reservation> findReservationsByStart(LocalDate start);
}
