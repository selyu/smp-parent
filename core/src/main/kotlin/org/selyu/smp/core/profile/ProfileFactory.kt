package org.selyu.smp.core.profile

import java.util.*

object ProfileFactory {
    fun create(uuid: UUID, username: String): Profile = Profile(uuid, username, 0.0)
}