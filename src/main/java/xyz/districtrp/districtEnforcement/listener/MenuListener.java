package xyz.districtrp.districtEnforcement.listener;

import io.papermc.paper.connection.PlayerGameConnection;
import io.papermc.paper.event.player.PlayerCustomClickEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.districtrp.districtEnforcement.gui.PoliceControlPanel;
import xyz.districtrp.districtEnforcement.gui.PoliceRecordsDialog;

public class MenuListener implements Listener {

    @EventHandler
    public void onDialogClick(PlayerCustomClickEvent event) {
        if (!(event.getCommonConnection() instanceof PlayerGameConnection gameConnection)) {
            return;
        }

        Player player = gameConnection.getPlayer();
        if (player == null) return;

        String actionId = event.getIdentifier().asString();

        switch (actionId) {
            case "district:pcp_wanted" -> {
                player.sendMessage(Component.text("Opening Wanted List... (Coming Soon)").color(NamedTextColor.YELLOW));
            }
            case "district:pcp_cases" -> {
                player.sendMessage(Component.text("Opening Cases... (Coming Soon)").color(NamedTextColor.YELLOW));
            }
            case "district:pcp_records" -> {
                player.sendMessage(Component.text("DEBUG: Records button clicked!").color(NamedTextColor.GOLD));
                PoliceRecordsDialog.open(player);
            }
            case "district:pcp_submit_records" -> {
                var responseView = event.getDialogResponseView();
                if (responseView != null) {
                    // Use getText() instead of getString()
                    String searchQuery = responseView.getText("record_query");

                    player.sendMessage(Component.text("Searching records for: " + (searchQuery != null ? searchQuery : "none")).color(NamedTextColor.YELLOW));
                }
            }
            case "record_prompt_chat" -> {
                player.closeDialog();
                player.sendMessage(Component.text("Please type the citizen's name in chat: /enforce record <name>").color(NamedTextColor.AQUA));
            }
            case "pcp_back" -> {
                PoliceControlPanel.open(player);
            }
        }
    }
}