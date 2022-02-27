package de.banarnia.api.skulls.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import de.banarnia.api.skulls.gui.SkullCategoryOverviewGUI;
import de.banarnia.api.skulls.gui.SkullOverviewGUI;
import org.bukkit.entity.Player;

@CommandAlias("head | heads")
@CommandPermission("skulls.view")
public class SkullCommand extends BaseCommand {

    // GUI öffnen
    @Default
    public void open(Player sender) {
        SkullOverviewGUI.open(sender);
    }

}
