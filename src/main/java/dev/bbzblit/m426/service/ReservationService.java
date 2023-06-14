package dev.bbzblit.m426.service;

import dev.bbzblit.m426.entity.Reservation;
import dev.bbzblit.m426.entity.User;
import dev.bbzblit.m426.repository.ReservationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final SessionService sessionService;

    public ReservationService(final ReservationRepository reservationRepository,
                              final SessionService sessionService){
        this.reservationRepository = reservationRepository;
        this.sessionService = sessionService;
    }

    private void checkIfCarIsAlreadyReserved(long carId, LocalDateTime period){

        List<Reservation> reservations = this.reservationRepository
                .findReservationsByCarIdAndStartIsBeforeAndEndIsAfter(carId,period,period);

        if (reservations.size() > 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Car got already reserved for the selected period");
        }
    }

    public Reservation saveReservation(Reservation reservation, String sessionToken){
        this.checkIfCarIsAlreadyReserved(reservation.getCar().getId(),reservation.getStart());
        this.checkIfCarIsAlreadyReserved(reservation.getCar().getId(),reservation.getEnd());
        reservation.setUser(sessionService.getSessionByToken(sessionToken).getUser());
        return this.reservationRepository.save(reservation);
    }


    public List<Reservation> getNextReservations(String token){
        return this.reservationRepository.findReservationsByUserIdAndStartAfter(
                this.sessionService.getSessionByToken(token).getUser().getId(), LocalDateTime.now());
    }

    public Reservation findReservationById(long id){
        return this.reservationRepository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No Reservation found by id " + id)
                );
    }


    public void revokeReservation(String token, long id){
        Reservation reservation = this.findReservationById(id);
        User user = this.sessionService.getSessionByToken(token).getUser();

        if (reservation.getUser().equals(user)){
            this.reservationRepository.deleteById(id);
        }
    }

public Reservation getReservationToday() {
    Reservation reservation = new Reservation();
    LocalDateTime today = LocalDateTime.now();
    if (reservation.getStart() == today) {
        return reservation;
    }
    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no reservations today");
}

}
