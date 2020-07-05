package org.selyu.smp.core.data.store

import org.selyu.smp.core.data.Store
import org.selyu.smp.core.profile.Profile
import java.util.*
import java.util.concurrent.CompletableFuture

interface ProfileStore : Store<UUID, Profile> {
    fun getByUsername(username: String): CompletableFuture<Optional<Profile>>
}