package xyz.districtrp.districtEnforcement;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.districtrp.districtEnforcement.command.EnforceCommand;
import xyz.districtrp.districtEnforcement.handcuff.HandcuffListener;
import xyz.districtrp.districtEnforcement.handcuff.HandcuffManager;
import xyz.districtrp.districtEnforcement.listener.RestrictionListener;
import xyz.districtrp.districtEnforcement.listener.TaserListener;
import xyz.districtrp.districtEnforcement.util.CooldownManager;
import xyz.districtrp.districtEnforcement.item.PoliceItems;

public final class DistrictEnforcement extends JavaPlugin {

    private HandcuffManager handcuffManager;
    private CooldownManager cooldownManager;
    private PoliceItems policeItems;

    @Override
    public void onEnable() {
        // Load default config
        saveDefaultConfig();

        getComponentLogger().info("DistrictEnforcement is initializing...");

        // Initialize Managers
        this.cooldownManager = new CooldownManager(this);
        this.handcuffManager = new HandcuffManager(this);
        this.policeItems = new PoliceItems(this);

        // Register Listeners
        getServer().getPluginManager().registerEvents(new HandcuffListener(this), this);
        getServer().getPluginManager().registerEvents(new RestrictionListener(this), this);
        // Inside your onEnable() method:
        getServer().getPluginManager().registerEvents(new xyz.districtrp.districtEnforcement.listener.MenuListener(), this);
        getServer().getPluginManager().registerEvents(new TaserListener(this), this);

        // Register Commands
        getCommand("enforce").setExecutor(new EnforceCommand(this));
        getCommand("enforce").setTabCompleter(new EnforceCommand(this));

        getComponentLogger().info("DistrictEnforcement enabled successfully!");
    }

    @Override
    public void onDisable() {
        if (handcuffManager != null) {
            handcuffManager.cleanupAll();
        }
        getComponentLogger().info("DistrictEnforcement has been disabled.");
    }

    public HandcuffManager getHandcuffManager() { return handcuffManager; }
    public CooldownManager getCooldownManager() { return cooldownManager; }
    public PoliceItems getPoliceItems() { return policeItems; }
}