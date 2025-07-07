package com.weather.weather_report.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PincodeLocation {
    @Id
    private String pincode;
    private double latitude;
    private double longitude;
}
