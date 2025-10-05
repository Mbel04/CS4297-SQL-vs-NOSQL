package com.example.lab6_banking_system;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Profile("nosql")
@Repository
public class UserSettingsRepository {
    private final DynamoDbClient ddb;
    private static final String TABLE = "UserSettings";

    public UserSettingsRepository(DynamoDbClient ddb) { this.ddb = ddb; }

    public void put(String userId, Map<String,String> prefs) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("userId", AttributeValue.builder().s(userId).build());
        Map<String, AttributeValue> prefMap = prefs.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> AttributeValue.builder().s(e.getValue()).build()));
        item.put("preferences", AttributeValue.builder().m(prefMap).build());

        ddb.putItem(PutItemRequest.builder().tableName(TABLE).item(item).build());
    }

    public Optional<Map<String,Object>> get(String userId) {
        Map<String, AttributeValue> key = Map.of("userId", AttributeValue.builder().s(userId).build());
        GetItemResponse res = ddb.getItem(GetItemRequest.builder().tableName(TABLE).key(key).build());
        if (!res.hasItem() || res.item().isEmpty()) return Optional.empty();

        Map<String, AttributeValue> it = res.item();
        Map<String, String> prefs = it.containsKey("preferences") && it.get("preferences").m()!=null
                ? it.get("preferences").m().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().s()))
                : Map.of();

        Map<String,Object> out = new HashMap<>();
        out.put("userId", it.get("userId").s());
        out.put("preferences", prefs);
        return Optional.of(out);
    }
}

