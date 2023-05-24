package dev.bbzblit.m426.service;

import dev.bbzblit.m426.entity.Car;
import dev.bbzblit.m426.repository.CarRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CarService {
    private final CarRepository repository;

    public CarService(CarRepository carRepository){
        this.repository = carRepository;
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
}
