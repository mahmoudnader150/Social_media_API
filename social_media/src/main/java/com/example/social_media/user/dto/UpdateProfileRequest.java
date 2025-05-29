package com.example.social_media.user.dto;

import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public class UpdateProfileRequest {
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;
    private LocalDateTime birthDate;
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    public UpdateProfileRequest() {}

    public UpdateProfileRequest(String name, LocalDateTime birthDate, String password) {
        this.name = name;
        this.birthDate = birthDate;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
