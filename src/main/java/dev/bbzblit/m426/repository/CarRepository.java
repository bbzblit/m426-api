package dev.bbzblit.m426.repository;

import dev.bbzblit.m426.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findCarsByOrderByIdAsc();
    List<Car> findByUserAndDate(String user, LocalDate date);
}
