package xyz.districtrp.districtEnforcement.item;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import xyz.districtrp.districtEnforcement.DistrictEnforcement;

public class PoliceItems {

    private final DistrictEnforcement plugin;
    private final NamespacedKey handcuffKey;
    private final NamespacedKey taserKey;
    private final NamespacedKey dartKey;

    public PoliceItems(DistrictEnforcement plugin) {
        this.plugin = plugin;
        this.handcuffKey = new NamespacedKey(plugin, "is_handcuff");
        this.taserKey = new NamespacedKey(plugin, "is_taser");
        this.dartKey = new NamespacedKey(plugin, "is_taser_dart");
    }

    public ItemStack getHandcuffs() {
        ItemStack cuffs = new ItemStack(Material.IRON_NUGGET); // Replace with your texture pack material
        ItemMeta meta = cuffs.getItemMeta();

        if (meta != null) {
            meta.displayName(Component.text("Police Handcuffs").color(NamedTextColor.BLUE));
            meta.lore(java.util.List.of(
                    Component.text("Right-click a player to restrain them.").color(NamedTextColor.GRAY)
            ));

            // Secure Identification using PDC
            meta.getPersistentDataContainer().set(handcuffKey, PersistentDataType.BOOLEAN, true);
            cuffs.setItemMeta(meta);
        }
        return cuffs;
    }
    public ItemStack getTaser() {
        // Shears are commonly used for handguns/tasers in RP texture packs
        ItemStack taser = new ItemStack(Material.STICK);
        ItemMeta meta = taser.getItemMeta();

        if (meta != null) {
            meta.displayName(Component.text("Police Taser").color(NamedTextColor.YELLOW));
            meta.lore(java.util.List.of(
                    Component.text("Right-click to fire a stun dart.").color(NamedTextColor.GRAY)
            ));

            meta.getPersistentDataContainer().set(taserKey, PersistentDataType.BOOLEAN, true);
            taser.setItemMeta(meta);
        }
        return taser;
    }

    public boolean isTaser(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer().has(taserKey, PersistentDataType.BOOLEAN);
    }

    public NamespacedKey getDartKey() {
        return dartKey;
    }

    public boolean isHandcuff(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer().has(handcuffKey, PersistentDataType.BOOLEAN);
    }
}