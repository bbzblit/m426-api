package dev.bbzblit.m426.controller;

import dev.bbzblit.m426.entity.Reservation;
import dev.bbzblit.m426.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReservationController {

    private final ReservationService reservationService;


    public ReservationController(ReservationService reservationService){
        this.reservationService = reservationService;
    }


    @PostMapping("/api/v1/reservation")
    public Reservation insertReservation(@RequestBody @Valid Reservation reservation,
                                         @CookieValue("session") String token){
        return this.reservationService.saveReservation(reservation, token);
    }

    @GetMapping("/api/v1/reservation")
    public List<Reservation> getReservationsInFuture(@CookieValue("session") String token){
        return this.reservationService.getNextReservations(token);
    }



}
