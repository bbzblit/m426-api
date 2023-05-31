package dev.bbzblit.m426.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.print.DocFlavor;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @GeneratedValue
    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;


    @NotNull
    @NotEmpty
    private String firstname;


    @NotNull
    @NotEmpty
    private String lastname;

    @NotNull
    @NotEmpty
    @Email
    private String email;

    @JsonIgnore
    private byte[] saltValue;

    private String password;

    private boolean employee;

    @JsonProperty
    public boolean isEmployee() {
        return employee;
    }
    @JsonIgnore
    public void setEmployee(boolean employee) {
        this.employee = employee;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(final String password) {
        this.password = password;
    }
}
