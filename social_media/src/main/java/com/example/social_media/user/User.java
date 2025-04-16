package com.example.social_media.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class User {
    private Integer id;

    @Size(min = 2, max = 50, message = "Name should be at least 2 characters")
    @JsonProperty("user_name")
    private String name;

    @Past(message = "Invalid Birth Date")
    @JsonProperty("birth_date")
    private LocalDateTime birthDate;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password should be at least 6 characters")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String role = "ROLE_USER";

    // Default constructor
    public User() {
    }

    // Constructor for existing code compatibility
    public User(Integer id, String name, LocalDateTime birthDate) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
    }

    // Full constructor
    public User(Integer id, String name, LocalDateTime birthDate, String email, String password) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.email = email;
        this.password = password;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDateTime birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthDate=" + birthDate +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
