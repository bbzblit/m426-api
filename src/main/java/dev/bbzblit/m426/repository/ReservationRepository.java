package dev.bbzblit.m426.repository;

import dev.bbzblit.m426.entity.Car;
import dev.bbzblit.m426.entity.Reservation;
import jakarta.transaction.Transactional;
import org.aspectj.apache.bcel.generic.LineNumberGen;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository to access the database table for the {@link Reservation} model
 */
public interface ReservationRepository extends CrudRepository<Reservation, Long> {

    public List<Reservation> findReservationsByCarIdAndStartIsBetweenOrEndIsBetween(
            Long carId, LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2);

    public List<Reservation> findReservationsByCarIdAndStartIsGreaterThanEqualAndEndIsLessThanEqual(Long carId,
                                                                                                    LocalDate start,
                                                                                                    LocalDate end);

    public List<Reservation> findReservationsByStartBetweenOrEndBetween(LocalDate start1,
                                                                        LocalDate end1,
                                                                        LocalDate start2, LocalDate end2);

    public List<Reservation> findReservationsByStartIsGreaterThanEqualAndEndIsLessThanEqual(LocalDate start,
                                                                                            LocalDate end);

    public List<Reservation> findReservationsByUserIdAndStartGreaterThanEqual(Long id, LocalDate start);

    public List<Reservation> findReservationsByStart(LocalDate start);

    public void deleteByUserId(Long userId);
}
