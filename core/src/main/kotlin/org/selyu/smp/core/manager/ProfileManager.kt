package org.selyu.smp.core.manager

import com.ea.async.Async.await
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.selyu.smp.core.data.Repository
import org.selyu.smp.core.profile.Profile
import java.util.*

class ProfileManager(private val repository: Repository) {
    private val cache = mutableMapOf<UUID, Profile>()

    fun login(event: AsyncPlayerPreLoginEvent) {
        val optionalProfile = await(repository.profileStore.getByKey(event.uniqueId))
        val profile = if (optionalProfile.isPresent) {
            optionalProfile.get()
        } else {
            await(repository.profileStore.insert(Profile(event.uniqueId, event.name)))
        }

        cache[event.uniqueId] = profile
    }

    fun quit(event: PlayerQuitEvent) {
        val profile = cache[event.player.uniqueId] ?: return

        repository.profileStore.insert(profile).thenRun {
            cache.remove(profile.uniqueId)
        }
    }

    fun getProfile(uuid: UUID): Optional<Profile> = Optional.ofNullable(cache[uuid])
}
