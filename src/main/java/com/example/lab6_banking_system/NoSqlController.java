package com.example.lab6_banking_system;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Profile("nosql")
@RestController
@RequestMapping("/api/nosql")
public class NoSqlController {
    private final UserSettingsRepository userRepo;
    private final SensorReadingRepository sensorRepo;

    public NoSqlController(UserSettingsRepository u, SensorReadingRepository s) {
        this.userRepo = u; this.sensorRepo = s;
    }

    // --- User settings (flexible map) ---
    @PutMapping("/usersettings/{userId}")
    public String saveUserSettings(@PathVariable String userId, @RequestBody Map<String,String> prefs) {
        userRepo.put(userId, prefs);
        return "Saved";
    }

    @GetMapping("/usersettings/{userId}")
    public Map<String,Object> getUserSettings(@PathVariable String userId) {
        return userRepo.get(userId).orElse(null);
    }

    // --- Sensor readings (time-series) ---
    @PostMapping("/sensors/{deviceId}/readings")
    public String addReading(@PathVariable String deviceId,
                             @RequestParam(required=false) String ts,
                             @RequestBody Map<String,String> payload) {
        String when = ts == null ? DateTimeFormatter.ISO_INSTANT.format(Instant.now()) : ts;
        sensorRepo.put(deviceId, when, payload);
        return "Recorded";
    }

    @GetMapping("/sensors/{deviceId}/readings")
    public List<Map<String,Object>> readingsSince(@PathVariable String deviceId,
                                                  @RequestParam String since) {
        return sensorRepo.since(deviceId, since);
    }
}

