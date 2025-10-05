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

    public SensorReadingRepository(DynamoDbClient ddb) { this.ddb = ddb; }

    public void put(String deviceId, String ts, Map<String,String> payload) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("deviceId", AttributeValue.builder().s(deviceId).build());
        item.put("ts", AttributeValue.builder().s(ts).build());
        Map<String, AttributeValue> pay = payload.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> AttributeValue.builder().s(e.getValue()).build()));
        item.put("payload", AttributeValue.builder().m(pay).build());

        ddb.putItem(PutItemRequest.builder().tableName(TABLE).item(item).build());
    }

    public List<Map<String,Object>> since(String deviceId, String tsInclusive) {
        Map<String, String> names = Map.of("#ts","ts");
        Map<String, AttributeValue> vals = Map.of(
                ":did", AttributeValue.builder().s(deviceId).build(),
                ":since", AttributeValue.builder().s(tsInclusive).build()
        );

        QueryRequest qr = QueryRequest.builder()
                .tableName(TABLE)
                .keyConditionExpression("deviceId = :did AND #ts >= :since")
                .expressionAttributeNames(names)
                .expressionAttributeValues(vals)
                .scanIndexForward(true) // ascending time
                .build();

        QueryResponse resp = ddb.query(qr);
        if (!resp.hasItems()) return List.of();

        List<Map<String,Object>> out = new ArrayList<>();
        for (Map<String, AttributeValue> it : resp.items()) {
            Map<String, String> payload = it.containsKey("payload") && it.get("payload").m()!=null
                    ? it.get("payload").m().entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().s()))
                    : Map.of();
            Map<String,Object> row = new LinkedHashMap<>();
            row.put("deviceId", it.get("deviceId").s());
            row.put("ts", it.get("ts").s());
            row.put("payload", payload);
            out.add(row);
        }
        return out;
    }
}

