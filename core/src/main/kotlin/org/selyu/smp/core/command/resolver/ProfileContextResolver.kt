package org.selyu.smp.core.command.resolver

import co.aikar.commands.BukkitCommandExecutionContext
import co.aikar.commands.InvalidCommandArgument
import co.aikar.commands.contexts.ContextResolver
import com.ea.async.Async.await
import org.selyu.smp.core.Core
import org.selyu.smp.core.profile.Profile

class ProfileContextResolver : ContextResolver<Profile, BukkitCommandExecutionContext> {
    private val profileManager = Core.instance.profileManager
    private val repository = Core.instance.repository

    override fun getContext(c: BukkitCommandExecutionContext): Profile {
        val username = c.popFirstArg()
        val fromCache = profileManager.getProfileByUsername(username)
        return if (fromCache.isPresent) {
            fromCache.get()
        } else {
            val fromStore = await(repository.profileStore.getByUsername(username))
            if (fromStore.isPresent) {
                fromStore.get()
            } else {
                throw InvalidCommandArgument("Invalid profile! Has '$username' logged on before?")
            }
        }
    }
}