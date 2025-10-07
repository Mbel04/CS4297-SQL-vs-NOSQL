package com.example.lab6_banking_system;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SensorReadingService {

    private final SensorReadingRepository sensorReadingRepository;

    public SensorReadingService(SensorReadingRepository sensorReadingRepository) {
        this.sensorReadingRepository = sensorReadingRepository;
    }

    public SensorReading save(SensorReading reading) {
        sensorReadingRepository.save(reading);
        return reading;
    }

    public List<SensorReading> findByDeviceId(String deviceId) {
        return sensorReadingRepository.findByDeviceId(deviceId);
    }
}
