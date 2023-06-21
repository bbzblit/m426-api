package dev.bbzblit.m426.service;

import dev.bbzblit.m426.entity.Car;
import dev.bbzblit.m426.entity.Reservation;
import dev.bbzblit.m426.repository.CarRepository;
import dev.bbzblit.m426.repository.ReservationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CarService {
    private final CarRepository repository;

    private final ReservationService reservationService;

    public CarService(final CarRepository carRepository, final ReservationService reservationService){
        this.repository = carRepository;
        this.reservationService = reservationService;
    }

    public List<Car> getCars() {
        return repository.findCarsByOrderByIdAsc();
    }

    public Car getCar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found with id " + id));
    }

    public Car insertCar(Car car) {
        return repository.save(car);
    }

    public Car updateCar(Car car, Long id) {
        this.getCar(id); // throws 404 if not found
        car.setId(id);
        return repository.save(car);
    }

    public void deleteAddress(Long id) {
        this.getCar(id); // throws 404 if not found
        repository.deleteById(id);
    }

    public List<Car> getAvailableCars(LocalDateTime startDate, LocalDateTime endDate){

        List< Reservation> reservations = this.reservationService.getReservationsBetween(startDate, endDate);

        Set<Long> reservedCarIds = new HashSet<Long>();
        reservations.forEach(reservation -> reservedCarIds.add(reservation.getId()));

        return this.repository.findCarsByIdNot(reservedCarIds);
    }
}
