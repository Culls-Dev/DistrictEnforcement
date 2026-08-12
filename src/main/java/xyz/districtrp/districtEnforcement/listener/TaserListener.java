package xyz.districtrp.districtEnforcement.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.districtrp.districtEnforcement.DistrictEnforcement;

public class TaserListener implements Listener {

    private final DistrictEnforcement plugin;

    public TaserListener(DistrictEnforcement plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onTaserFire(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (!event.getAction().isRightClick()) return;

        Player officer = event.getPlayer();
        if (!plugin.getPoliceItems().isTaser(officer.getInventory().getItemInMainHand())) return;

        event.setCancelled(true); // Prevent default shear usage

        if (!officer.hasPermission("districtEnforcement.taser.use")) {
            officer.sendMessage(Component.text("You do not have permission to use a taser.").color(NamedTextColor.RED));
            return;
        }

        // Check Cooldown
        int cooldown = plugin.getConfig().getInt("taser.cooldown", 5);
        if (plugin.getCooldownManager().isOnCooldown(officer.getUniqueId())) {
            long remaining = plugin.getCooldownManager().getRemaining(officer.getUniqueId());
            officer.sendMessage(Component.text("Taser on cooldown for " + remaining + "s.").color(NamedTextColor.RED));
            return;
        }

        // Fire Projectile
        Snowball dart = officer.launchProjectile(Snowball.class);
        dart.setShooter(officer);
        // Tag the projectile so we know it's a taser dart and not a regular snowball
        dart.getPersistentDataContainer().set(plugin.getPoliceItems().getDartKey(), PersistentDataType.BOOLEAN, true);

        // Optional: Increase projectile speed
        dart.setVelocity(dart.getVelocity().multiply(1.5));

        plugin.getCooldownManager().setCooldown(officer.getUniqueId(), cooldown);
    }

    @EventHandler
    public void onTaserHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Snowball dart)) return;

        // Verify it's a taser dart
        if (!dart.getPersistentDataContainer().has(plugin.getPoliceItems().getDartKey(), PersistentDataType.BOOLEAN)) return;

        if (event.getHitEntity() instanceof Player suspect) {
            int durationSeconds = plugin.getConfig().getInt("taser.effects.duration", 4);
            int ticks = durationSeconds * 20;
            int slowLevel = plugin.getConfig().getInt("taser.effects.slowness-level", 4);

            // Apply Stun Effects
            suspect.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, ticks, slowLevel, false, false, true));
            suspect.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, ticks, 1, false, false, true));
            suspect.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, ticks, 1, false, false, true));

            suspect.sendMessage(Component.text("You have been tased!").color(NamedTextColor.RED));

            if (dart.getShooter() instanceof Player officer) {
                officer.sendMessage(Component.text("You tased " + suspect.getName() + ".").color(NamedTextColor.GREEN));
            }
        }
    }
}