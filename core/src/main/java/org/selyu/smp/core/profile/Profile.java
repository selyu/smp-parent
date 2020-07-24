package org.selyu.smp.core.profile;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public final class Profile {
    private final UUID uuid;
    private String username;
    private double balance;

    public Profile(@NotNull UUID uuid, @NotNull String username, double balance) {
        this.uuid = uuid;
        this.username = username;
        this.balance = balance;
    }

    @Nullable
    public Player toPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    @NotNull
    public UUID getUuid() {
        return uuid;
    }

    @NotNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NotNull String username) {
        this.username = username;
    }

    @NotNull
    public String getProperUsername() {
        return username + (username.endsWith("s") ? "'" : "'s");
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void addBalance(double amount) {
        balance += amount;
    }

    public boolean removeBalance(double amount) {
        if (balance < amount)
            return false;
        balance -= amount;
        return true;
    }
}
