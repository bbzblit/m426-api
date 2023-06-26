package dev.bbzblit.m426.controller;

import dev.bbzblit.m426.entity.Reservation;
import dev.bbzblit.m426.service.ReservationService;
import dev.bbzblit.m426.service.SessionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
public class ReservationController {
    public  final SessionService sessionService;
    private final ReservationService reservationService;


    public ReservationController(SessionService sessionService, ReservationService reservationService){
        this.sessionService = sessionService;
        this.reservationService = reservationService;
    }

    /**
     * creates a new reservation
     * @param reservation
     * @param token
     * @return new reservation
     */
    @PostMapping("/api/v1/reservation")
    public Reservation insertReservation(@RequestBody @Valid Reservation reservation, @CookieValue("session") String session){
        sessionService.isLoggedIn(session);
        return this.reservationService.saveReservation(reservation, session);
    }

    /**
     * returns a list of all reservations
     * @param token
     * @return list of all reservations
     */
    @GetMapping("/api/v1/reservation")
    public List<Reservation> getReservationsInFuture(@CookieValue("session") String session){
        sessionService.isLoggedIn(session);
        return this.reservationService.getNextReservations(session);
    }

    /**
     * deletes a reservation
     * @param token
     * @param id
     */
    @DeleteMapping("/api/v1/reservation/{id}")
    public void deleteReservationById(@CookieValue("session") String session, @PathVariable("id") Long id){
        sessionService.isLoggedIn(session);
        this.reservationService.revokeReservation(session, id);
    }

    @GetMapping("/api/v1/reservation/today")
    public List<Reservation> getCurrentReservations(@CookieValue("session") String session){
        sessionService.isAdministrator(session);
        return this.reservationService.getReservationOfToday();
    }
}
