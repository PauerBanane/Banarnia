package de.banarnia.api.smartInventory.content;

import com.google.common.collect.Maps;
import de.banarnia.api.menu.gui.MenuGUI;
import de.banarnia.api.smartInventory.ClickableItem;
import de.banarnia.api.smartInventory.SmartInventory;
import de.banarnia.api.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class InventoryContentImpl implements InventoryContents {

    private final Player holder;
    private SmartInventory inv;
    private ClickableItem[][] contents;

    private Pagination pagination = new PaginationImpl();
    private Map<String, SlotIterator> iterators = Maps.newHashMap();
    private Map<String, Object> properties = Maps.newHashMap();
    private Inventory inventory;

    public InventoryContentImpl(SmartInventory inv, Player player) {
        this.holder = player;
        this.inv = inv;
        if (inv.getType() == InventoryType.CHEST || inv.getType() == InventoryType.ENDER_CHEST) {
            this.inventory = Bukkit.createInventory(player, inv.getColumns() * inv.getRows(), inv.getTitle());
        } else {
            this.inventory = Bukkit.createInventory(player, inv.getType(), inv.getTitle());
        }
        this.contents = new ClickableItem[inv.getRows()][inv.getColumns()];
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public SmartInventory inventory() {
        return inv;
    }

    @Override
    public Pagination pagination() {
        return pagination;
    }

    @Override
    public Pagination pagination(int itemsPerPage, SlotIterator slotIterator) {
        return pagination().setItemsPerPage(itemsPerPage).addToIterator(slotIterator);
    }

    @Override
    public InventoryContents setPaginationButtons(Player player, int row) {
        return setPaginationButtons(player, SlotPos.of(row,1), SlotPos.of(row, 7), Material.ARROW);
    }

    @Override
    public InventoryContents setPaginationButtons(Player player, SlotPos prevButtonSlot, SlotPos nextButtonSlot, Material material) {
        if (!pagination.isLast())
            set(nextButtonSlot, ClickableItem.of((new ItemBuilder(Material.ARROW)).name("§8§lSeite vor").build(), e -> {
                inventory().open(player, pagination.next().getPage());
            }));
        if (!pagination.isFirst())
            set(prevButtonSlot, ClickableItem.of((new ItemBuilder(Material.ARROW)).name("§8§lSeite zurück").build(), e -> {
                inventory().open(player, pagination.previous().getPage());
            }));
        return this;
    }

    @Override
    public Optional<SlotIterator> iterator(String id) {
        return Optional.ofNullable(this.iterators.get(id));
    }

    @Override
    public SlotIterator newIterator(String id, SlotIterator.Type type, int startRow, int startColumn) {
        SlotIterator iterator = new SlotIteratorImplementation(this, inv, type, startRow, startColumn);

        this.iterators.put(id, iterator);
        return iterator;
    }

    @Override
    public SlotIterator newIterator(String id, SlotIterator.Type type, SlotPos startPos) {
        return newIterator(id, type, startPos.getRow(), startPos.getColumn());
    }

    @Override
    public SlotIterator newIterator(SlotIterator.Type type, int startRow, int startColumn) {
        return new SlotIteratorImplementation(this, inv, type, startRow, startColumn);
    }

    @Override
    public SlotIterator newIterator(SlotIterator.Type type, SlotPos startPos) {
        return newIterator(type, startPos.getRow(), startPos.getColumn());
    }

    @Override
    public SlotIterator newIterator(SlotIterator.Type type, SlotPos startPos, boolean allowOverride) {
        return newIterator(type, startPos).allowOverride(allowOverride);
    }

    @Override
    public ClickableItem[][] all() {
        return contents;
    }

    @Override
    public Optional<SlotPos> firstEmpty() {
        for (int column = 0; column < contents[0].length; column++) {
            for (int row = 0; row < contents.length; row++) {
                if (!this.get(row, column).isPresent())
                    return Optional.of(new SlotPos(row, column));
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<ClickableItem> get(int row, int column) {
        if (row >= contents.length)
            return Optional.empty();
        if (column >= contents[row].length)
            return Optional.empty();

        return Optional.ofNullable(contents[row][column]);
    }

    @Override
    public Optional<ClickableItem> get(SlotPos slotPos) {
        return get(slotPos.getRow(), slotPos.getColumn());
    }

    @Override
    public InventoryContents set(int row, int column, ClickableItem item) {
        if (row >= contents.length)
            return this;
        if (column >= contents[row].length)
            return this;

        contents[row][column] = item;
        update(row, column, item != null ? item : null);
        return this;
    }

    @Override
    public InventoryContents set(SlotPos slotPos, ClickableItem item) {
        return set(slotPos.getRow(), slotPos.getColumn(), item);
    }

    @Override
    public InventoryContents set(int slot, ClickableItem item) {
        return set(SlotPos.of(slot), item);
    }

    @Override
    public InventoryContents add(ClickableItem item) {
        for (int row = 0; row < contents.length; row++) {
            for (int column = 0; column < contents[0].length; column++) {
                if (contents[row][column] == null) {
                    set(row, column, item);
                    return this;
                }
            }
        }

        return this;
    }

    @Override
    public InventoryContents fill(ClickableItem item) {
        for (int row = 0; row < contents.length; row++)
            for (int column = 0; column < contents[row].length; column++)
                set(row, column, item);

        return this;
    }

    public InventoryContents fillColumn(int column, Material material) {
        return fillRow(column, ClickableItem.empty(new ItemBuilder(material).name(" ").build()));
    }
    public InventoryContents fillBorders() {
        return fillBorders(Material.GRAY_STAINED_GLASS_PANE);
    }
    public InventoryContents fillRow(int row, Material material) {
        return fillRow(row, ClickableItem.empty(new ItemBuilder(material).name(" ").build()));
    }
    public InventoryContents fill(Material material) {
        return fill(ClickableItem.empty(new ItemBuilder(material).name(" ").build()));
    }
    public InventoryContents fill() {
        return fill(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
    }
    public InventoryContents fillBorders(Material material) {
        return fillBorders(ClickableItem.empty(new ItemBuilder(material).name(" ").build()));
    }

    @Override
    public InventoryContents fillRow(int row, ClickableItem item) {
        if (row >= contents.length)
            return this;

        for (int column = 0; column < contents[row].length; column++)
            set(row, column, item);

        return this;
    }

    @Override
    public InventoryContents fillColumn(int column, ClickableItem item) {
        for (int row = 0; row < contents.length; row++)
            set(row, column, item);

        return this;
    }

    @Override
    public InventoryContents fillBorders(ClickableItem item) {
        fillRect(0, 0, inv.getRows() - 1, inv.getColumns() - 1, item);
        return this;
    }

    @Override
    public InventoryContents setCloseItem(int row, int column, Player player) {
        return set(row,column,ClickableItem.of(ItemBuilder.of(Material.BARRIER).name("§cSchließen").build(), click -> player.closeInventory()));
    }

    @Override
    public InventoryContents setBackItem(int row, int column, Player player, Consumer<InventoryClickEvent> consumer) {
        return set(row, column, ClickableItem.of(ItemBuilder.of(Material.ARROW).name("§cZurück").build(), click -> consumer.accept(click)));
    }

    @Override
    public InventoryContents setMenuItem(int row, int column, Player player) {
        return setBackItem(row, column, player, click -> MenuGUI.open(player));
    }

    @Override
    public InventoryContents fillRect(int fromRow, int fromColumn, int toRow, int toColumn, ClickableItem item) {
        for (int row = fromRow; row <= toRow; row++) {
            for (int column = fromColumn; column <= toColumn; column++) {
                if (row != fromRow && row != toRow && column != fromColumn && column != toColumn)
                    continue;

                set(row, column, item);
            }
        }

        return this;
    }

    @Override
    public InventoryContents fillRect(SlotPos fromPos, SlotPos toPos, ClickableItem item) {
        return fillRect(fromPos.getRow(), fromPos.getColumn(), toPos.getRow(), toPos.getColumn(), item);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T property(String name) {
        return (T) properties.get(name);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T property(String name, T def) {
        return properties.containsKey(name) ? (T) properties.get(name) : def;
    }

    @Override
    public Map<String, Object> properties() {
        return properties;
    }

    @Override
    public InventoryContents setProperty(String name, Object value) {
        properties.put(name, value);
        return this;
    }

    private void update(int row, int column, ClickableItem item) {
        if (item == null || item.getItem() != null)
            return;
        this.inventory.setItem(inv.getColumns() * row + column, item.getItem());
    }

    @Override
    public InventoryContents updateMeta(SlotPos pos, ItemMeta meta) {
        int slot = inv.getColumns() * pos.getRow() + pos.getColumn();
        this.inventory.getItem(slot).setItemMeta(meta);
        return this;
    }

    @Override
    public Player getHolder() {
        return this.holder;
    }
}
