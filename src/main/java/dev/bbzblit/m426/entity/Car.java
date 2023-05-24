package dev.bbzblit.m426.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Car {
    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    @NotEmpty
    private String serialNumber;
    @NotNull
    @NotEmpty
    private String licencePlate;
    @NotNull
    @NotEmpty
    private String brand;
    @NotNull
    @NotEmpty
    private String model;
    @NotNull
    @NotEmpty
    private String color;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
