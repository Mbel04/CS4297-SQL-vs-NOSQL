package com.example.lab6_banking_system;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/nosql")
public class NoSqlController {

    private final UserSettingsService userSettingsService;
    private final SensorReadingService sensorReadingService;

    public NoSqlController(UserSettingsService userSettingsService,
                           SensorReadingService sensorReadingService) {
        this.userSettingsService = userSettingsService;
        this.sensorReadingService = sensorReadingService;
    }


    @PostMapping("/user-settings")
    public ResponseEntity<UserSettings> saveUserSettings(@RequestBody UserSettings settings) {
        UserSettings saved = userSettingsService.save(settings);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/user-settings/{userId}")
    public ResponseEntity<UserSettings> getUserSettings(@PathVariable String userId) {
        Optional<UserSettings> settings = userSettingsService.findById(userId);
        return settings.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping("/sensor-readings")
    public ResponseEntity<SensorReading> saveSensorReading(@RequestBody SensorReading reading) {
        SensorReading saved = sensorReadingService.save(reading);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/sensor-readings/{deviceId}")
    public ResponseEntity<List<SensorReading>> getReadingsByDevice(@PathVariable String deviceId) {
        List<SensorReading> readings = sensorReadingService.findByDeviceId(deviceId);
        return ResponseEntity.ok(readings);
    }
}
