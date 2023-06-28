package dev.bbzblit.m426.service;

import dev.bbzblit.m426.entity.Car;
import dev.bbzblit.m426.entity.Reservation;
import dev.bbzblit.m426.repository.CarRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Services that is used to manage the Car Model
 */
@Service
public class CarService {
    private final CarRepository repository;

    private final ReservationService reservationService;

    public CarService(final CarRepository carRepository, final ReservationService reservationService){
        this.repository = carRepository;
        this.reservationService = reservationService;
    }

    /**
     * Method to get all the cars in the Database
     * @return all cars
     */
    public List<Car> getCars() {
        return repository.findCarsByOrderByIdAsc();
    }

    /**
     * Method to get a specific car by its id
     * @param id the provdidet id
     * @return the car if it exists
     */
    public Car getCarById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found with id " + id));
    }

    /**
     * Method to create a new car
     * @param car new car
     * @return created car
     */
    public Car saveCar(Car car) {
        return repository.save(car);
    }

    /**
     * Method to update a car
     * @param car the new car
     * @param id the carId of the old car
     * @return updated car
     */
    public Car updateCar(Car car, Long id) {
        this.getCarById(id); // throws 404 if not found
        car.setId(id);
        return repository.save(car);
    }

    /**
     * Method to delete a car
     * @param id the id of the car
     */
    public void deleteCar(Long id) {
        this.getCarById(id); // throws 404 if not found
        repository.deleteById(id);
    }

    /**
     * Method to get all the cars that are available to reserver during the specified period
     * @param startDate start date of the period
     * @param endDate end date of the period
     * @return found cars
     */
    public List<Car> getAvailableCars(LocalDate startDate, LocalDate endDate){

        List< Reservation> reservations = this.reservationService.getReservationsBetween(startDate, endDate);
        Set<Long> reservedCarIds = new HashSet<Long>();
        reservations.forEach(reservation -> reservedCarIds.add(reservation.getCar().getId()));

        if (reservedCarIds.size() == 0){
            return this.repository.findAll();
        }

        return this.repository.findCarsByIdNotIn(reservedCarIds);
    }
}
