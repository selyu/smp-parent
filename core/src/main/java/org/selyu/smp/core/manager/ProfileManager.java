package org.selyu.smp.core.manager;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import org.selyu.smp.core.Core;
import org.selyu.smp.core.Errors;
import org.selyu.smp.core.data.Repository;
import org.selyu.smp.core.profile.Profile;
import org.selyu.smp.core.profile.ProfileFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.ea.async.Async.await;

public final class ProfileManager {
    private final Repository repository = Core.getInstance().getRepository();
    private final Map<UUID, Profile> cache = new HashMap<>();

    public void login(@NotNull AsyncPlayerPreLoginEvent event) {
        var optionalProfile = await(repository.getProfileStore().getByKey(event.getUniqueId()));
        var profile = optionalProfile.orElseGet(() -> ProfileFactory.create(event.getUniqueId(), event.getName()));

        if (optionalProfile.isEmpty())
            await(repository.getProfileStore().save(profile));

        profile.setUsername(event.getName());
        cache.put(event.getUniqueId(), profile);
    }

    public void quit(@NotNull PlayerQuitEvent event) {
        var profile = cache.get(event.getPlayer().getUniqueId());
        if (profile == null)
            return;

        await(repository.getProfileStore().save(profile));
        cache.remove(event.getPlayer().getUniqueId());
    }

    public Optional<Profile> getByUUID(@NotNull UUID uuid) {
        Profile profile = cache.get(uuid);
        Player player = Core.getInstance().getServer().getPlayer(uuid);
        if (profile == null && player != null)
            player.kickPlayer(Errors.ERROR_FETCHING_PROFILE);

        return Optional.ofNullable(profile);
    }

    public Optional<Profile> getByUsername(@NotNull String username) {
        Player player = Core.getInstance().getServer().getPlayer(username);
        if (player == null)
            return Optional.empty();
        return getByUUID(player.getUniqueId());
    }
}
