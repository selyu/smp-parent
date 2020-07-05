package org.selyu.smp.core.profile

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

data class Profile(val uniqueId: UUID, val username: String, var balance: Double) {
    fun toPlayer(): Player? = Bukkit.getPlayer(uniqueId)

    fun getProperUsername(): String {
        return if (username.endsWith('s')) {
            "$username'"
        } else {
            "$username's"
        }
    }
}