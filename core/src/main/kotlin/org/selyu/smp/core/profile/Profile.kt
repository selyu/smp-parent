package org.selyu.smp.core.profile

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

data class Profile(val uniqueId: UUID, var username: String, var balance: Double) {
    fun addBalance(addedBalance: Double) {
        balance += addedBalance
    }

    fun removeBalance(removedBalance: Double): Boolean {
        if (balance < removedBalance)
            return false

        balance -= removedBalance
        return true
    }

    fun toPlayer(): Player? = Bukkit.getPlayer(uniqueId)

    fun getProperUsername(): String {
        return if (username.endsWith('s')) {
            "$username'"
        } else {
            "$username's"
        }
    }
}