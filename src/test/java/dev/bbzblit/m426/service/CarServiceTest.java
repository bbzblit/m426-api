package dev.bbzblit.m426.service;

import dev.bbzblit.m426.ParentTest;
import dev.bbzblit.m426.entity.Car;
import dev.bbzblit.m426.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CarServiceTest extends ParentTest {
    private CarService carService;

    @Mock
    private CarRepository carRepository;

    @BeforeEach
    void setUp() {
        carRepository = mock(CarRepository.class);
        carService = new CarService(carRepository);
    }

    @Test
    void getCars() {
        // Arrange
        List<Car> expectedCars = new ArrayList<>();
        when(carRepository.findCarsByOrderByIdAsc()).thenReturn(expectedCars);

        // Act
        List<Car> actualCars = carService.getCars();

        // Assert
        assertEquals(expectedCars, actualCars);
        verify(carRepository, times(1)).findCarsByOrderByIdAsc();
    }

    @Test
    void getCar_WithValidId_ShouldReturnCar() {
        // Arrange
        Long carId = 1L;
        Car expectedCar = new Car();
        when(carRepository.findById(carId)).thenReturn(Optional.of(expectedCar));

        // Act
        Car actualCar = carService.getCar(carId);

        // Assert
        assertEquals(expectedCar, actualCar);
        verify(carRepository, times(1)).findById(carId);
    }

    @Test
    void getCar_WithInvalidId_ShouldThrowException() {
        // Arrange
        Long carId = 1L;
        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResponseStatusException.class, () -> carService.getCar(carId));
        verify(carRepository, times(1)).findById(carId);
    }

    @Test
    void insertCar() {
        // Arrange
        Car car = new Car();
        Car savedCar = new Car();
        when(carRepository.save(car)).thenReturn(savedCar);

        // Act
        Car insertedCar = carService.insertCar(car);

        // Assert
        assertEquals(savedCar, insertedCar);
        verify(carRepository, times(1)).save(car);
    }

    @Test
    void updateCar_WithValidId_ShouldReturnUpdatedCar() {
        // Arrange
        Long carId = 1L;
        Car carToUpdate = new Car();
        Car updatedCar = new Car();
        when(carRepository.findById(carId)).thenReturn(Optional.of(carToUpdate));
        when(carRepository.save(carToUpdate)).thenReturn(updatedCar);

        // Act
        Car actualCar = carService.updateCar(carToUpdate, carId);

        // Assert
        assertEquals(updatedCar, actualCar);
        verify(carRepository, times(1)).findById(carId);
        verify(carRepository, times(1)).save(carToUpdate);
    }

    @Test
    void updateCar_WithInvalidId_ShouldThrowException() {
        // Arrange
        Long carId = 1L;
        Car carToUpdate = new Car();
        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResponseStatusException.class, () -> carService.updateCar(carToUpdate, carId));
        verify(carRepository, times(1)).findById(carId);
        verify(carRepository, never()).save(any(Car.class));
    }

    @Test
    void deleteAddress_WithValidId_ShouldDeleteCar() {
        // Arrange
        Long carId = 1L;
        Car carToDelete = new Car();
        when(carRepository.findById(carId)).thenReturn(Optional.of(carToDelete));

        // Act
        carService.deleteAddress(carId);

        // Assert
        verify(carRepository, times(1)).findById(carId);
        verify(carRepository, times(1)).deleteById(carId);
    }

    @Test
    void deleteAddress_WithInvalidId_ShouldThrowException() {
        // Arrange
        Long carId = 1L;
        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResponseStatusException.class, () -> carService.deleteAddress(carId));
        verify(carRepository, times(1)).findById(carId);
        verify(carRepository, never()).deleteById(anyLong());
    }

    @Test
    void filterCarsByUserAndDate() {
        // Arrange
        String user = "John";
        String date = "2023-05-24";
        LocalDate filterDate = LocalDate.parse(date);
        List<Car> expectedCars = new ArrayList<>();
        when(carRepository.findByUserAndDate(user, filterDate)).thenReturn(expectedCars);

        // Act
        List<Car> actualCars = carService.filterCarsByUserAndDate(user, date);

        // Assert
        assertEquals(expectedCars, actualCars);
        verify(carRepository, times(1)).findByUserAndDate(user, filterDate);
    }
}
