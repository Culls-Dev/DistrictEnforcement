package xyz.districtrp.districtEnforcement.gui;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.util.List;

public class PoliceControlPanel {

    public static void open(Player player) {
        Dialog dialog = Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(Component.text("Police Control Panel", NamedTextColor.DARK_BLUE))
                        .canCloseWithEscape(true)
                        .body(List.of(
                                DialogBody.plainMessage(Component.text("Welcome, Officer " + player.getName() + ".", NamedTextColor.GRAY)),
                                DialogBody.plainMessage(Component.text("Select a database to access:", NamedTextColor.WHITE))
                        ))
                        .build()
                )
                .type(DialogType.multiAction(List.of(
                        // Wanted Criminals Button
                        ActionButton.builder(Component.text("Wanted Criminals", NamedTextColor.RED))
                                .tooltip(Component.text("View and manage active warrants."))
                                .action(DialogAction.customClick(Key.key("district", "pcp_wanted"), null))
                                .build(),

                        // Active Cases Button
                        ActionButton.builder(Component.text("Active Cases", NamedTextColor.GOLD))
                                .tooltip(Component.text("Review ongoing investigations."))
                                .action(DialogAction.customClick(Key.key("district", "pcp_cases"), null))
                                .build(),

                        // Police Records Button
                        ActionButton.builder(Component.text("Police Records", NamedTextColor.AQUA))
                                .tooltip(Component.text("Search citizen arrest records."))
                                .action(DialogAction.customClick(Key.key("district", "pcp_records"), null))
                                .build()
                )).build())
        );

        player.showDialog(dialog);
    }
}