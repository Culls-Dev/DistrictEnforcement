package xyz.districtrp.districtEnforcement.gui;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.util.List;

public class PoliceRecordsDialog {

    public static void open(Player player) {
        Dialog dialog = Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(Component.text("Police Records Search", NamedTextColor.AQUA))
                        .canCloseWithEscape(true)
                        .body(List.of(
                                DialogBody.plainMessage(Component.text("Enter a citizen's name to search:", NamedTextColor.GRAY)),
                                DialogBody.plainMessage(Component.text("Awaiting search...", NamedTextColor.DARK_GRAY))
                        ))
                        .inputs(List.of(
                                // Added .build() here to resolve the Builder type mismatch
                                DialogInput.text("record_query", Component.text("Citizen Name")).build()
                        ))
                        .build()
                )
                .type(DialogType.notice(
                        ActionButton.builder(Component.text("Search Database", NamedTextColor.GREEN))
                                .action(DialogAction.customClick(Key.key("district", "pcp_submit_records"), null))
                                .build()
                ))
        );

        player.showDialog(dialog);
    }
}