package com.example.lab6_banking_system;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserSettingsService {

    private final UserSettingsRepository userSettingsRepository;

    public UserSettingsService(UserSettingsRepository userSettingsRepository) {
        this.userSettingsRepository = userSettingsRepository;
    }

    public UserSettings save(UserSettings settings) {
        userSettingsRepository.save(settings);
        return settings;
    }

    public Optional<UserSettings> findById(String userId) {
        return userSettingsRepository.findById(userId);
    }
}
