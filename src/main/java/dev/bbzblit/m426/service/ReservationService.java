package dev.bbzblit.m426.service;

import dev.bbzblit.m426.entity.Reservation;
import dev.bbzblit.m426.entity.User;
import dev.bbzblit.m426.repository.ReservationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
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

    private void checkIfCarIsAlreadyReserved(long carId, LocalDate period){

        List<Reservation> reservations = this.reservationRepository
                .findReservationsByCarIdAndStartIsLessThanEqualAndEndIsGreaterThanEqual(carId,period,period);

        if (reservations.size() > 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Car got already reserved for the selected period");
        }
    }

    public List<Reservation> getReservationOfToday(){
        return this.reservationRepository.findReservationsByStart(LocalDate.now());
    }

    public List<Reservation> getReservationsBetween(LocalDate start, LocalDate end){
        List<Reservation> foundReservation = this.reservationRepository
                .findReservationsByStartBetween(start, start);
        foundReservation.addAll(this.reservationRepository
                .findReservationsByStartBetween(end, end));
        foundReservation.addAll(
                this.reservationRepository.findReservationsByStartIsGreaterThanEqualAndEndIsLessThanEqual(start, end));
        return foundReservation;
    }

    public Reservation saveReservation(Reservation reservation, String sessionToken){
        this.checkIfCarIsAlreadyReserved(reservation.getCar().getId(),reservation.getStart());
        this.checkIfCarIsAlreadyReserved(reservation.getCar().getId(),reservation.getEnd());
        reservation.setUser(sessionService.getSessionByToken(sessionToken).getUser());
        return this.reservationRepository.save(reservation);
    }


    public List<Reservation> getNextReservations(String token){
        return this.reservationRepository.findReservationsByUserIdAndStartGreaterThanEqual(
                this.sessionService.getSessionByToken(token).getUser().getId(), LocalDate.now());
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

}
