/**
 * 
 */
package com.perceivedev.perceivetest;

import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.perceivedev.perceivecore.config.ConfigSerializable;

/**
 * @author Rayzr
 *
 */
public class PlayerData implements ConfigSerializable {

    private UUID   id;

    private double money;

    PlayerData() {
    }

    public PlayerData(Player player) {
        this.id = player.getUniqueId();
    }

    /**
     * @return An optional player
     */
    public Optional<Player> getPlayer() {
        return Optional.ofNullable(Bukkit.getPlayer(id));
    }

    /**
     * @return The UUID of the player
     */
    public UUID getId() {
        return id;
    }

    /**
     * @return The amount of money this player has
     */
    public double getMoney() {
        return money;
    }

    /**
     * Adds money to a player's balance
     * 
     * @param amount The amount of money to add to this player's account
     */
    public double addMoney(double amount) {
        return money += amount;
    }

}
