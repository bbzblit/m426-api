package dev.bbzblit.m426.controller;

import dev.bbzblit.m426.entity.Reservation;
import dev.bbzblit.m426.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReservationController {

    private final ReservationService reservationService;


    public ReservationController(ReservationService reservationService){
        this.reservationService = reservationService;
    }


    @PostMapping("/api/v1/reservation")
    public Reservation insertReservation(@RequestBody @Valid Reservation reservation){
        return this.reservationService.saveReservation(reservation);
    }



}
