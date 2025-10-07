package com.example.lab6_banking_system;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Map;

@Document(collection = "sensor_readings")
public class SensorReading {

    @Id
    private String id;
    private String deviceId;
    private String timestamp;
    private Map<String, String> payload;

    public SensorReading() {}

    public SensorReading(String deviceId, String timestamp, Map<String, String> payload) {
        this.deviceId = deviceId;
        this.timestamp = timestamp;
        this.payload = payload;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public Map<String, String> getPayload() { return payload; }
    public void setPayload(Map<String, String> payload) { this.payload = payload; }
}
