package de.banarnia.api.smartInventory.content;

import de.banarnia.api.smartInventory.ClickableItem;
import de.banarnia.api.smartInventory.SmartInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public interface InventoryContents {

    public SmartInventory inventory();
    public Pagination pagination();
    public Pagination pagination(int itemsPerPage, SlotIterator slotIterator);

    default Pagination pagination(int itemsPerPage) {
        return pagination(itemsPerPage, SlotIterator.Type.CENTERED, 1, 1, true);
    }
    default Pagination pagination(int itemsPerPage, int row, int column, boolean allowOverride) {
        return pagination(itemsPerPage, SlotIterator.Type.CENTERED, row, column, allowOverride);
    }
    default Pagination pagination(int itemsPerPage, SlotIterator.Type type, int row, int column, boolean allowOverride) {
        return pagination(itemsPerPage, type, SlotPos.of(row, column), allowOverride);
    }
    public Pagination pagination(int itemsPerPage, SlotIterator.Type type, SlotPos slotPos, boolean allowOverride);

    public InventoryContents setPaginationButtons(Player player, int row);
    public InventoryContents setPaginationButtons(Player player, SlotPos prevButtonSlot, SlotPos nextButtonSlot, Material material);

    public Player getHolder();

    public Optional<SlotIterator> iterator(String id);

    public SlotIterator newIterator(String id, SlotIterator.Type type, int startRow, int startColumn);
    public SlotIterator newIterator(SlotIterator.Type type, int startRow, int startColumn);

    public SlotIterator newIterator(String id, SlotIterator.Type type, SlotPos startPos);
    public SlotIterator newIterator(SlotIterator.Type type, SlotPos startPos);
    public SlotIterator newIterator(SlotIterator.Type type, SlotPos startPos, boolean allowOverride);

    public ClickableItem[][] all();

    public Optional<SlotPos> firstEmpty();

    public Optional<ClickableItem> get(int row, int column);
    public Optional<ClickableItem> get(SlotPos slotPos);

    public InventoryContents set(int row, int column, ClickableItem item);
    public InventoryContents set(SlotPos slotPos, ClickableItem item);
    public InventoryContents set(int slot, ClickableItem item);

    public InventoryContents add(ClickableItem item);

    public InventoryContents fill(ClickableItem item);

    public InventoryContents fillRow(int row, ClickableItem item);
    public InventoryContents fillColumn(int column, ClickableItem item);
    public InventoryContents fillColumn(int column, Material material);
    public InventoryContents fillBorders();
    public InventoryContents fillRow(int row, Material material);
    public InventoryContents fill(Material material);
    public InventoryContents fill();
    public InventoryContents fillBorders(Material material);
    public InventoryContents fillBorders(ClickableItem item);

    public InventoryContents setCloseItem(int row, int column, Player player);
    public InventoryContents setBackItem(int row, int column, Player player, Consumer<InventoryClickEvent> consumer);
    public InventoryContents setMenuItem(int row, int column, Player player);

    public InventoryContents fillRect(int fromRow, int fromColumn, int toRow, int toColumn, ClickableItem item);
    public InventoryContents fillRect(SlotPos fromPos, SlotPos toPos, ClickableItem item);

    public Inventory getInventory();

    public InventoryContents updateMeta(SlotPos pos, ItemMeta meta);

    public <T> T property(String name);
    public <T> T property(String name, T def);
    public Map<String, Object> properties();

    public InventoryContents setProperty(String name, Object value);

    public int getRows();
    public int getLastRow();

    public int getColumns();
    public int getCenterColumn();



}
