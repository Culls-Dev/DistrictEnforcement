package xyz.districtrp.districtEnforcement.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.districtrp.districtEnforcement.DistrictEnforcement;
import xyz.districtrp.districtEnforcement.gui.PoliceControlPanel;

import java.util.ArrayList;
import java.util.List;

public class EnforceCommand implements CommandExecutor, TabCompleter {

    private final DistrictEnforcement plugin;

    public EnforceCommand(DistrictEnforcement plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("districtEnforcement.enforce")) {
            sender.sendMessage(Component.text("You do not have permission.").color(NamedTextColor.RED));
            return true;
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
            if (!sender.hasPermission("districtEnforcement.enforce.give")) {
                sender.sendMessage(Component.text("You do not have permission to give equipment.").color(NamedTextColor.RED));
                return true;
            }

            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(Component.text("Player not found.").color(NamedTextColor.RED));
                return true;
            }

            if (args[2].equalsIgnoreCase("handcuffs")) {
                target.getInventory().addItem(plugin.getPoliceItems().getHandcuffs());
                sender.sendMessage(Component.text("Gave handcuffs to " + target.getName() + ".").color(NamedTextColor.GREEN));
                return true;
            } else if (args[2].equalsIgnoreCase("taser")) {
                target.getInventory().addItem(plugin.getPoliceItems().getTaser());
                sender.sendMessage(Component.text("Gave a taser to " + target.getName() + ".").color(NamedTextColor.GREEN));
                return true;
            }
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("menu")) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(Component.text("Only players can open the menu.").color(NamedTextColor.RED));
                return true;
            }


            PoliceControlPanel.open(player);
            return true;
        }

        sender.sendMessage(Component.text("Usage: /enforce give <player> handcuffs").color(NamedTextColor.YELLOW));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("give");
            completions.add("menu");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                completions.add(p.getName());
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
            completions.add("handcuffs");
            completions.add("equipment");
            completions.add("taser");
        }
        return completions;
    }
}