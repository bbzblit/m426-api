package dev.bbzblit.m426.controller;

import dev.bbzblit.m426.entity.Car;
import dev.bbzblit.m426.service.CarService;
import dev.bbzblit.m426.service.SessionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
public class CarController {
    public  final SessionService sessionService;
    public final CarService service;

    public CarController(SessionService sessionService, final CarService carService){
        this.sessionService = sessionService;
        this.service = carService;
    }

    /**
     * returns a list of all cars
     * @return list of all cars
     */
    @GetMapping("/api/v1/car")
    public ResponseEntity<Iterable<Car>> all(@CookieValue("session") String session) {
        sessionService.isAdministrator(session);
        List<Car> cars = service.getCars();
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }

    /**
     * returns one specific car
     * @param id
     * @return one specific car
     */
    @GetMapping("/api/v1/car/{id}")
    public ResponseEntity<Car> one(@PathVariable Long id, @CookieValue("session") String session) {
        sessionService.isAdministrator(session);
        Car car = service.getCar(id);
        return new ResponseEntity<>(car, HttpStatus.OK);
    }

    /**
     * creates a new car
     * @param car
     * @return new car
     */
    @PostMapping("/api/v1/car")
    public ResponseEntity<Car> insert(@RequestBody Car car, @CookieValue("session") String session) {
        sessionService.isAdministrator(session);
        Car newCar = service.insertCar(car);
        return new ResponseEntity<>(newCar, HttpStatus.CREATED);
    }

    /**
     * edit a specific car
     * @param car
     * @param id
     * @return edited car
     */
    @PutMapping("/api/v1/car/{id}")
    public ResponseEntity<Car> update(@RequestBody @Valid Car car, @PathVariable Long id, @CookieValue("session") String session) {
        sessionService.isAdministrator(session);
        Car updatedCar = service.updateCar(car, id);
        return new ResponseEntity<>(updatedCar, HttpStatus.OK);
    }

    /**
     * deletes a specific car
     * @param id
     * @return no content
     */
    @DeleteMapping("/api/v1/car/{id}")
    public void delete(@PathVariable Long id, @CookieValue("session") String session) {
        sessionService.isAdministrator(session);
        service.deleteAddress(id);
    }
}
