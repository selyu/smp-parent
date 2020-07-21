package org.selyu.smp.core.profile;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class ProfileFactory {
    private ProfileFactory() {
    }

    @NotNull
    public static Profile create(@NotNull UUID uuid, @NotNull String username, double balance) {
        return new Profile(uuid, username, balance);
    }

    @NotNull
    public static Profile create(@NotNull UUID uuid, @NotNull String username) {
        return create(uuid, username, 1000);
    }
}
