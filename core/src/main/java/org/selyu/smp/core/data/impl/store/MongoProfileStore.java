package org.selyu.smp.core.data.impl.store;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.selyu.smp.core.data.store.ProfileStore;
import org.selyu.smp.core.profile.Profile;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.regex;

public final class MongoProfileStore implements ProfileStore {
    private final MongoCollection<Document> collection;
    private final ReplaceOptions replaceOptions = new ReplaceOptions().upsert(true);

    public MongoProfileStore(@NotNull MongoCollection<Document> collection) {
        this.collection = collection;
    }

    @Override
    public @NotNull CompletableFuture<Optional<Profile>> getByUsername(@NotNull String username) {
        return CompletableFuture.supplyAsync(() -> {
            var document = collection.find(regex("username", Pattern.compile(Pattern.quote(username), Pattern.CASE_INSENSITIVE))).first();
            return Optional.ofNullable(deserialize(document));
        });
    }

    @Override
    public @NotNull CompletableFuture<Optional<Profile>> getByKey(@NotNull UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            var document = collection.find(eq("_id", uuid)).first();
            return Optional.ofNullable(deserialize(document));
        });
    }

    @Override
    public @NotNull CompletableFuture<Profile> save(@NotNull Profile profile) {
        return CompletableFuture.supplyAsync(() -> {
            var document = new Document("_id", profile.getUuid());
            document.append("username", profile.getUsername());
            document.append("balance", profile.getBalance());

            collection.replaceOne(eq("_id", profile.getUuid()), document, replaceOptions);
            return profile;
        });
    }

    @Override
    public @NotNull CompletableFuture<Void> delete(@NotNull Profile profile) {
        return CompletableFuture.runAsync(() -> collection.deleteOne(eq("_id", profile.getUuid())));
    }

    @Nullable
    private Profile deserialize(@Nullable Document document) {
        if (document == null)
            return null;

        return new Profile(document.get("_id", UUID.class), document.getString("username"), document.getDouble("balance"));
    }
}
