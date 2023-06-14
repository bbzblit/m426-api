package dev.bbzblit.m426.controller;

import dev.bbzblit.m426.entity.Reservation;
import dev.bbzblit.m426.service.ReservationService;
import dev.bbzblit.m426.service.SessionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReservationController {
    public  final SessionService sessionService;
    private final ReservationService reservationService;


    public ReservationController(SessionService sessionService, ReservationService reservationService){
        this.sessionService = sessionService;
        this.reservationService = reservationService;
    }


    @PostMapping("/api/v1/reservation")
    public Reservation insertReservation(@RequestBody @Valid Reservation reservation, @CookieValue("session") String session){
        sessionService.isLoggedIn(session);
        return this.reservationService.saveReservation(reservation, session);
    }

    @GetMapping("/api/v1/reservation")
    public List<Reservation> getReservationsInFuture(@CookieValue("session") String session){
        sessionService.isLoggedIn(session);
        return this.reservationService.getNextReservations(session);
    }


    @DeleteMapping("/api/v1/reservation/{id}")
    public void deleteReservationById(@CookieValue("session") String session, @PathVariable("id") Long id){
        sessionService.isLoggedIn(session);
        this.reservationService.revokeReservation(session, id);
    }


}
