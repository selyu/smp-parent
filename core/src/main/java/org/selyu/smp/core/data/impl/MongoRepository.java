package org.selyu.smp.core.data.impl;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.UuidRepresentation;
import org.jetbrains.annotations.NotNull;
import org.selyu.smp.core.data.Repository;
import org.selyu.smp.core.data.impl.store.MongoProfileStore;
import org.selyu.smp.core.data.store.ProfileStore;
import org.selyu.smp.core.settings.Settings;

public final class MongoRepository implements Repository {
    private final MongoClient mongoClient;
    private final ProfileStore profileStore;

    public MongoRepository() {
        var settingsBuilder = MongoClientSettings.builder();
        settingsBuilder.uuidRepresentation(UuidRepresentation.STANDARD);
        settingsBuilder.applyConnectionString(new ConnectionString(Settings.CONNECTION_STRING));

        mongoClient = MongoClients.create(settingsBuilder.build());
        var database = mongoClient.getDatabase(Settings.DATABASE_NAME);

        profileStore = new MongoProfileStore(database.getCollection("profiles"));
    }

    @Override
    public @NotNull ProfileStore getProfileStore() {
        return profileStore;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        mongoClient.close();
    }
}
