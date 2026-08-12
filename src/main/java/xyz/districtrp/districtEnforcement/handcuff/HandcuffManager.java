package xyz.districtrp.districtEnforcement.handcuff;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.districtrp.districtEnforcement.DistrictEnforcement;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HandcuffManager {

    private final DistrictEnforcement plugin;
    private final Map<UUID, UUID> cuffedPlayers = new HashMap<>();
    private BukkitRunnable dragTask;

    public HandcuffManager(DistrictEnforcement plugin) {
        this.plugin = plugin;
        startDragTask();
    }

    public boolean isCuffed(UUID playerUUID) {
        return cuffedPlayers.containsKey(playerUUID);
    }

    public UUID getCuffedBy(UUID suspectUUID) {
        return cuffedPlayers.get(suspectUUID);
    }

    public void cuff(Player suspect, Player officer) {
        cuffedPlayers.put(suspect.getUniqueId(), officer.getUniqueId());

        suspect.sendMessage(Component.text("You have been restrained.").color(NamedTextColor.YELLOW));
        officer.sendMessage(Component.text("You restrained " + suspect.getName() + ".").color(NamedTextColor.GREEN));
    }

    public void uncuff(Player suspect, Player officer) {
        cuffedPlayers.remove(suspect.getUniqueId());

        suspect.sendMessage(Component.text("You have been released.").color(NamedTextColor.GREEN));
        if (officer != null) {
            officer.sendMessage(Component.text("You released " + suspect.getName() + ".").color(NamedTextColor.GREEN));
        }
    }

    public void cleanupAll() {
        if (dragTask != null) dragTask.cancel();
        cuffedPlayers.clear();
    }

    private void startDragTask() {
        double maxDistance = plugin.getConfig().getDouble("handcuffs.max-distance", 4.0);

        dragTask = new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, UUID> entry : cuffedPlayers.entrySet()) {
                    Player suspect = Bukkit.getPlayer(entry.getKey());
                    Player officer = Bukkit.getPlayer(entry.getValue());


                    if (suspect == null || officer == null || !suspect.isOnline() || !officer.isOnline()) {
                        if (suspect != null) cuffedPlayers.remove(suspect.getUniqueId());
                        continue;
                    }


                    if (suspect.getWorld().equals(officer.getWorld())) {
                        double distance = suspect.getLocation().distance(officer.getLocation());
                        if (distance > maxDistance) {

                            suspect.teleport(officer.getLocation().subtract(officer.getLocation().getDirection().multiply(1.5)));
                        }
                    } else {

                        suspect.teleport(officer.getLocation());
                    }
                }
            }
        };

        dragTask.runTaskTimer(plugin, 10L, 10L);
    }
}