package xyz.districtrp.districtEnforcement.util;

import xyz.districtrp.districtEnforcement.DistrictEnforcement;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {

    private final Map<UUID, Long> cooldowns = new HashMap<>();
    private final DistrictEnforcement plugin;

    public CooldownManager(DistrictEnforcement plugin) {
        this.plugin = plugin;
    }

    public void setCooldown(UUID player, int seconds) {
        long expireTime = System.currentTimeMillis() + (seconds * 1000L);
        cooldowns.put(player, expireTime);
    }

    public boolean isOnCooldown(UUID player) {
        if (!cooldowns.containsKey(player)) return false;
        if (System.currentTimeMillis() >= cooldowns.get(player)) {
            cooldowns.remove(player); // CLEAN UP - CULLS
            return false;
        }
        return true;
    }

    public long getRemaining(UUID player) {
        if (!isOnCooldown(player)) return 0;
        return (cooldowns.get(player) - System.currentTimeMillis()) / 1000L;
    }
}