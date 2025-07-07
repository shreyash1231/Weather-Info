package com.weather.weather_report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.weather.weather_report.entity.PincodeLocation;

@Repository
public interface PincodeRepository extends JpaRepository<PincodeLocation, String> {
}

