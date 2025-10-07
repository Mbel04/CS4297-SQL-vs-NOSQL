package com.example.lab6_banking_system;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.*;

@Profile("nosql")
@Repository
public class UserSettingsRepository {
    private final DynamoDbClient ddb;
    private static final String TABLE = "UserSettings";

    public UserSettingsRepository(DynamoDbClient ddb) {
        this.ddb = ddb;
    }

    public void save(UserSettings settings) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("userId", AttributeValue.builder().s(settings.getUserId()).build());

        Map<String, AttributeValue> prefs = new HashMap<>();
        settings.getPreferences().forEach((k, v) -> prefs.put(k, AttributeValue.builder().s(v).build()));
        item.put("preferences", AttributeValue.builder().m(prefs).build());

        ddb.putItem(PutItemRequest.builder().tableName(TABLE).item(item).build());
    }

    public Optional<UserSettings> findById(String userId) {
        Map<String, AttributeValue> key = Map.of("userId", AttributeValue.builder().s(userId).build());

        GetItemResponse resp = ddb.getItem(GetItemRequest.builder().tableName(TABLE).key(key).build());
        if (!resp.hasItem()) {
            return Optional.empty();
        }

        Map<String, AttributeValue> item = resp.item();
        Map<String, String> prefs = new HashMap<>();
        if (item.containsKey("preferences")) {
            item.get("preferences").m().forEach((k, v) -> prefs.put(k, v.s()));
        }

        return Optional.of(new UserSettings(userId, prefs));
    }
}
