package com.example.lab6_banking_system;

import java.util.Map;

public class UserSettings {
    private String userId;
    private Map<String, String> preferences;

    public UserSettings() {
        this.preferences = new java.util.HashMap<>();
    }

    public UserSettings(String userId, Map<String, String> preferences) {
        this.userId = userId;
        this.preferences = preferences;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map<String, String> getPreferences() {
        return preferences;
    }

    public void setPreferences(Map<String, String> preferences) {
        this.preferences = preferences;
    }

    @Override
    public String toString() {
        return "UserSettings{" +
                "userId='" + userId + '\'' +
                ", preferences=" + preferences +
                '}';
    }
}
