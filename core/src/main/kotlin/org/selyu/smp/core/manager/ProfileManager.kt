package org.selyu.smp.core.manager

import com.ea.async.Async.await
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.selyu.smp.core.Core
import org.selyu.smp.core.Errors
import org.selyu.smp.core.profile.Profile
import org.selyu.smp.core.profile.ProfileFactory
import java.util.*

class ProfileManager {
    private val core = Core.getInstance()
    private val repository = Core.getInstance().repository
    private val cache = mutableMapOf<UUID, Profile>()

    fun login(event: AsyncPlayerPreLoginEvent) {
        val optionalProfile = await(repository.profileStore.getByKey(event.uniqueId))
        val profile = if (optionalProfile.isPresent) {
            optionalProfile.get()
        } else {
            await(repository.profileStore.save(ProfileFactory.create(event.uniqueId, event.name)))
        }

        profile.username = event.name
        cache[event.uniqueId] = profile
    }

    fun quit(event: PlayerQuitEvent) {
        val profile = cache[event.player.uniqueId] ?: return

        repository.profileStore.save(profile).thenRun {
            cache.remove(profile.uniqueId)
        }
    }

    fun getProfile(uuid: UUID): Optional<Profile> {
        if (cache[uuid] == null && core.server.getPlayer(uuid) != null)
            core.server.getPlayer(uuid)!!.kickPlayer(Errors.ERROR_FETCHING_PROFILE)

        return Optional.ofNullable(cache[uuid])
    }

    fun getProfileByUsername(username: String): Optional<Profile> {
        val profile = cache.values.find { it.username.equals(username, true) }
        return Optional.ofNullable(profile)
    }
}
