package xyz.districtrp.districtEnforcement.handcuff;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import xyz.districtrp.districtEnforcement.DistrictEnforcement;

public class HandcuffListener implements Listener {

    private final DistrictEnforcement plugin;

    public HandcuffListener(DistrictEnforcement plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEntityEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return; // Prevent double-firing with offhand
        if (!(event.getRightClicked() instanceof Player suspect)) return;

        Player officer = event.getPlayer();
        ItemStack itemInHand = officer.getInventory().getItemInMainHand();

        // Check if item is handcuffs via PDC
        if (!plugin.getPoliceItems().isHandcuff(itemInHand)) return;

        // Permissions
        if (!officer.hasPermission("districtEnforcement.handcuffs.use")) {
            officer.sendMessage(Component.text("You do not have permission to use handcuffs.").color(NamedTextColor.RED));
            return;
        }

        HandcuffManager manager = plugin.getHandcuffManager();

        // Prevent self-cuffing (just in case of weird hitboxes)
        if (officer.equals(suspect)) return;

        // Uncuffing logic
        if (manager.isCuffed(suspect.getUniqueId())) {
            // Only the officer who cuffed them (or admin) can uncuff
            if (manager.getCuffedBy(suspect.getUniqueId()).equals(officer.getUniqueId()) || officer.hasPermission("districtEnforcement.admin")) {
                manager.uncuff(suspect, officer);
            } else {
                officer.sendMessage(Component.text("This player is restrained by another officer.").color(NamedTextColor.RED));
            }
            return;
        }

        // Cuffing logic (with cooldown check)
        int cooldown = plugin.getConfig().getInt("handcuffs.cooldown", 3);
        if (plugin.getCooldownManager().isOnCooldown(officer.getUniqueId())) {
            long remaining = plugin.getCooldownManager().getRemaining(officer.getUniqueId());
            officer.sendMessage(Component.text("Handcuffs on cooldown for " + remaining + "s.").color(NamedTextColor.RED));
            return;
        }

        // Apply state and cooldown
        manager.cuff(suspect, officer);
        plugin.getCooldownManager().setCooldown(officer.getUniqueId(), cooldown);
    }
}