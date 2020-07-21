package org.selyu.smp.core.data.store;

import org.jetbrains.annotations.NotNull;
import org.selyu.smp.core.data.Store;
import org.selyu.smp.core.profile.Profile;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface ProfileStore extends Store<UUID, Profile> {
    @NotNull
    CompletableFuture<Optional<Profile>> getByUsername(@NotNull String username);
}
