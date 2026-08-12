package xyz.districtrp.districtEnforcement.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import xyz.districtrp.districtEnforcement.DistrictEnforcement;

public class RestrictionListener implements Listener {

    private final DistrictEnforcement plugin;

    public RestrictionListener(DistrictEnforcement plugin) {
        this.plugin = plugin;
    }

    private boolean isRestricted(Player player) {
        return plugin.getHandcuffManager().isCuffed(player.getUniqueId());
    }

    @EventHandler
    public void onSprint(PlayerToggleSprintEvent event) {
        if (event.isSprinting() && plugin.getConfig().getBoolean("restrictions.prevent-sprint", true)) {
            if (isRestricted(event.getPlayer())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (plugin.getConfig().getBoolean("restrictions.prevent-drop", true) && isRestricted(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (plugin.getConfig().getBoolean("restrictions.prevent-container-use", true) && isRestricted(event.getPlayer())) {
            // Can be refined to only cancel container blocks if needed
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            if (plugin.getConfig().getBoolean("restrictions.prevent-attack", true) && isRestricted(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onFlight(PlayerToggleFlightEvent event) {
        if (event.isFlying() && plugin.getConfig().getBoolean("restrictions.prevent-flight", true)) {
            if (isRestricted(event.getPlayer())) {
                event.setCancelled(true);
            }
        }
    }
}