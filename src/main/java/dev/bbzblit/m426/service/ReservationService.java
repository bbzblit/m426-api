package dev.bbzblit.m426.service;

import dev.bbzblit.m426.entity.Reservation;
import dev.bbzblit.m426.repository.ReservationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.LongStream;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(final ReservationRepository reservationRepository){
        this.reservationRepository = reservationRepository;
    }

    private void checkIfCarIsAlreadyReserved(long carId, LocalDateTime period){

        List<Reservation> reservations = this.reservationRepository
                .findReservationsByCarIdAndStartIsBeforeAndEndIsAfter(carId,period,period);

        if (reservations.size() > 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Car got already reserved for the selected period");
        }
    }

    public Reservation saveReservation(Reservation reservation){
        this.checkIfCarIsAlreadyReserved(reservation.getCar().getId(),reservation.getStart());
        this.checkIfCarIsAlreadyReserved(reservation.getCar().getId(),reservation.getEnd());
        return this.reservationRepository.save(reservation);
    }

}
