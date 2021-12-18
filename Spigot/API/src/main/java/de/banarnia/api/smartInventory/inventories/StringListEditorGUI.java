package de.banarnia.api.smartInventory.inventories;

import com.google.common.collect.Lists;
import de.banarnia.api.chatinput.ChatInput;
import de.banarnia.api.smartInventory.ClickableItem;
import de.banarnia.api.smartInventory.SmartInventory;
import de.banarnia.api.smartInventory.content.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class StringListEditorGUI implements InventoryProvider {

    public static void open(Player player, String title, Consumer<List<String>> consumer, List<String> currentList) {
        open(player, title, true, consumer, currentList);
    }

    public static void open(Player player, String title, boolean sound, Consumer<List<String>> consumer, List<String> currentList) {
        SmartInventory.builder().title(title).size(4).provider(new StringListEditorGUI(consumer, sound, currentList)).build().open(player);
    }

    private StringListEditorGUI(Consumer<List<String>> consumer, boolean playSound, List<String> currentList) {
        this.consumer = consumer;
        this.sound = playSound;
        this.originalList = currentList != null ? currentList : Lists.newLinkedList();
        this.currentList  = originalList;
    }

    private final boolean sound;
    private final Consumer<List<String>> consumer;
    private final List<String> originalList;
    private List<String> currentList;

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fill(ClickableItem.empty(UtilClickableItem.getFiller()));
        Pagination pagination = contents.pagination();
        ArrayList<ClickableItem> items = Lists.newArrayList();

        for (int i = 0; i < currentList.size(); i++) {
            String entry = UtilPlaceholder.parse(player, currentList.get(i));
            ItemBuilder builder = new ItemBuilder(Material.PAPER)
                                .name("§c§lIndex§r§8: §6" + i)
                                .lore(entry)
                                .lore(" ")
                                .lore("§eLinksklick §7zum Bearbeiten")
                                .lore("§eRechtsklick §7zum §clöschen");

            int index = i;
            items.add(ClickableItem.of(builder.build(), click -> {
                if (click.isLeftClick()) {
                    new ChatInput(player, F.main("Editor", "Geb den neuen Inhalt ein: §8[§cAbbrechen §7zum canceln"), input -> {
                        if (input.length() > 0 && !input.equalsIgnoreCase("Abbrechen")) {
                            currentList.set(index, ChatColor.translateAlternateColorCodes('&', input));
                        }
                        reOpen(player, contents);
                    });
                } else if (click.isRightClick()) {
                    currentList.remove(index);
                    reOpen(player, contents);
                }
            }));
        }

        contents.set(3,3, ClickableItem.of(new ItemBuilder(Material.EMERALD).name("§2§lSpeichern").build(), click -> {
            player.closeInventory();
            consumer.accept(currentList);
        }));

        contents.set(3,4, UtilClickableItem.getAddItem( click -> {
            new ChatInput(player, F.main("Editor", "Gib die neue Zeile ein: §8[§cAbbrechen §7zum canceln]"), input -> {
                if (input != null && input.length() > 0 && !input.equalsIgnoreCase("Abbrechen"))
                    currentList.add(ChatColor.translateAlternateColorCodes('&', input));
                reOpen(player, contents);
            });
        }));

        contents.set(3,5, ClickableItem.of(new ItemBuilder(Material.TNT).name("§4§lAbbrechen").build(), click -> {
            player.closeInventory();
            consumer.accept(originalList);
        }));

        ClickableItem[] c = new ClickableItem[items.size()];
        c = items.<ClickableItem>toArray(c);
        pagination.setItems(c);
        pagination.setItemsPerPage(18);
        if (items.size() > 0 && !pagination.isLast())
            contents.set(3, 7, ClickableItem.of((new ItemBuilder(Material.ARROW)).name("§f§lSeite vor").build(), e -> {
                contents.inventory().open(player, pagination.next().getPage());
            }));
        if (!pagination.isFirst())
            contents.set(3, 1, ClickableItem.of((new ItemBuilder(Material.ARROW)).name("§f§lSeite zurück").build(), e -> {
                contents.inventory().open(player, pagination.previous().getPage());
            }));

        SlotIterator slotIterator = contents.newIterator(SlotIterator.Type.HORIZONTAL, SlotPos.of(1,0));
        slotIterator = slotIterator.allowOverride(true);
        pagination.addToIterator(slotIterator);

    }

}
