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
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (!(event.getRightClicked() instanceof Player suspect)) return;

        Player officer = event.getPlayer();
        ItemStack itemInHand = officer.getInventory().getItemInMainHand();


        if (!plugin.getPoliceItems().isHandcuff(itemInHand)) return;


        if (!officer.hasPermission("districtEnforcement.handcuffs.use")) {
            officer.sendMessage(Component.text("You do not have permission to use handcuffs.").color(NamedTextColor.RED));
            return;
        }

        HandcuffManager manager = plugin.getHandcuffManager();


        if (officer.equals(suspect)) return;


        if (manager.isCuffed(suspect.getUniqueId())) {

            if (manager.getCuffedBy(suspect.getUniqueId()).equals(officer.getUniqueId()) || officer.hasPermission("districtEnforcement.admin")) {
                manager.uncuff(suspect, officer);
            } else {
                officer.sendMessage(Component.text("This player is restrained by another officer.").color(NamedTextColor.RED));
            }
            return;
        }


        int cooldown = plugin.getConfig().getInt("handcuffs.cooldown", 3);
        if (plugin.getCooldownManager().isOnCooldown(officer.getUniqueId())) {
            long remaining = plugin.getCooldownManager().getRemaining(officer.getUniqueId());
            officer.sendMessage(Component.text("Handcuffs on cooldown for " + remaining + "s.").color(NamedTextColor.RED));
            return;
        }


        manager.cuff(suspect, officer);
        plugin.getCooldownManager().setCooldown(officer.getUniqueId(), cooldown);
    }
}