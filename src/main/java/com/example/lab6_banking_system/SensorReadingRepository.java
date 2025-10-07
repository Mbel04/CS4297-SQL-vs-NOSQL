package com.example.lab6_banking_system;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.*;
import java.util.stream.Collectors;

@Profile("nosql")
@Repository
public class SensorReadingRepository {
    private final DynamoDbClient ddb;
    private static final String TABLE = "SensorData";

    public SensorReadingRepository(DynamoDbClient ddb) {
        this.ddb = ddb;
    }

    public void save(SensorReading reading) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("deviceId", AttributeValue.builder().s(reading.getDeviceId()).build());
        item.put("ts", AttributeValue.builder().s(reading.getTimestamp()).build());

        Map<String, AttributeValue> pay = reading.getPayload().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> AttributeValue.builder().s(e.getValue()).build()
                ));
        item.put("payload", AttributeValue.builder().m(pay).build());

        ddb.putItem(PutItemRequest.builder().tableName(TABLE).item(item).build());
    }

    public List<SensorReading> findByDeviceId(String deviceId) {
        Map<String, String> names = Map.of("#ts", "ts");
        Map<String, AttributeValue> vals = Map.of(
                ":did", AttributeValue.builder().s(deviceId).build()
        );

        QueryRequest qr = QueryRequest.builder()
                .tableName(TABLE)
                .keyConditionExpression("deviceId = :did")
                .expressionAttributeNames(names)
                .expressionAttributeValues(vals)
                .scanIndexForward(true)
                .build();

        QueryResponse resp = ddb.query(qr);
        if (!resp.hasItems()) return List.of();

        List<SensorReading> out = new ArrayList<>();
        for (Map<String, AttributeValue> it : resp.items()) {
            Map<String, String> payload = it.containsKey("payload") && it.get("payload").m() != null
                    ? it.get("payload").m().entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().s()))
                    : Map.of();

            out.add(new SensorReading(
                    it.get("deviceId").s(),
                    it.get("ts").s(),
                    payload
            ));
        }
        return out;
    }
}
