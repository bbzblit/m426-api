package dev.bbzblit.m426.service;

import dev.bbzblit.m426.entity.Reservation;
import dev.bbzblit.m426.entity.User;
import dev.bbzblit.m426.repository.ReservationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

/**
 * Service to manage the {@link Reservation} object.
 */
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final SessionService sessionService;

    public ReservationService(final ReservationRepository reservationRepository,
                              final SessionService sessionService) {
        this.reservationRepository = reservationRepository;
        this.sessionService = sessionService;
    }

    /**
     * Methd to check if a sepecific car got already reserved
     * @param carId the id of the car
     * @param start the start date of the new reservation
     * @param end the end date of the new reservation
     */
    private void checkIfCarIsAlreadyReserved(long carId, LocalDate start, LocalDate end) {

        List<Reservation> reservations = this.reservationRepository
                .findReservationsByCarIdAndStartIsBetweenOrEndIsBetween(carId, start, end,start, end);
        reservations.addAll(
                this.reservationRepository.findReservationsByCarIdAndStartIsGreaterThanEqualAndEndIsLessThanEqual(
                        carId, start, end));
        if (reservations.size() > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Car got already reserved for the selected period");
        }
    }

    /**
     * Method to get all reservations that start from today
     * @return iterable of reservations that start today
     */
    public List<Reservation> getReservationOfToday() {
        return this.reservationRepository.findReservationsByStart(LocalDate.now());
    }

    /**
     * Method to get all the reservations that have a "conflict" with the providet datarange.
     * It also returns Reservations that just parcialy match the providet range
     * @param start start date of range
     * @param end  end date of range
     * @return reservations in that range
     */
    public List<Reservation> getReservationsBetween(LocalDate start, LocalDate end) {
        List<Reservation> foundReservation = this.reservationRepository
                .findReservationsByStartBetweenOrEndBetween(start, end ,start, end);
        foundReservation.addAll(
                this.reservationRepository.findReservationsByStartIsGreaterThanEqualAndEndIsLessThanEqual(start, end));
        return foundReservation;
    }

    /**
     * Method to create a new reservations
     * @param reservation the resservation
     * @param sessionToken the currently logged in user
     * @return created reservation
     */
    public Reservation saveReservation(Reservation reservation, String sessionToken) {
        this.checkIfCarIsAlreadyReserved(reservation.getCar().getId(), reservation.getStart(), reservation.getEnd());
        reservation.setUser(sessionService.getSessionByToken(sessionToken).getUser());
        return this.reservationRepository.save(reservation);
    }


    /**
     * Method to get all the reservations of the current user that are in the future
     * @param token the session id
     * @return list of all reservations
     */
    public List<Reservation> getNextReservations(String token) {
        return this.reservationRepository.findReservationsByUserIdAndStartGreaterThanEqual(
                this.sessionService.getSessionByToken(token).getUser().getId(), LocalDate.now());
    }

    /**
     * Method to find a specific reservation by its id
     * @param id the id
     * @return the found reservation
     */
    public Reservation getReservationById(long id) {
        return this.reservationRepository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No Reservation found by id " + id)
                );
    }


    /**
     * Method to delete a Reservation
     * @param token the session id of the currently logged in user
     * @param id id of the reservation
     */
    public void deleteReservation(String token, long id) {
        Reservation reservation = this.getReservationById(id);
        User user = this.sessionService.getSessionByToken(token).getUser();

        if (reservation.getUser().equals(user)) {
            this.reservationRepository.deleteById(id);
        }
    }

}
