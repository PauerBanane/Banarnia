package de.banarnia.api.smartInventory;

import de.banarnia.api.smartInventory.content.InventoryContentImpl;
import de.banarnia.api.smartInventory.content.InventoryContents;
import de.banarnia.api.smartInventory.content.provider.Closable;
import de.banarnia.api.smartInventory.content.provider.InventoryProvider;
import de.banarnia.api.smartInventory.opener.InventoryOpener;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class SmartInventory {

    private String id;
    private String title;
    private InventoryType type;
    private int rows, columns;
    private boolean closeable;

    private InventoryProvider provider;
    private SmartInventory parent;

    private List<InventoryListener<? extends Event>> listeners;

    private SmartInventory() {
    }

    public SmartOpener openFor(Player player) {
        return new SmartOpener(player, this);
    }

    public Inventory open(Player player) {
        return open(player, 0);
    }

    public Inventory open(Player player, String[] property, Object[] value) {
        return this.open(player, 0, property, value);
    }

    public Inventory open(Player player, int page) {
        return this.open(player, page, null, null);
    }

    public Inventory open(Player player, int page, String[] property, Object[] value) {
        InventoryManager manager = InventoryManager.getInstance();

        Optional<SmartInventory> oldInv = manager.getInventory(player);

        oldInv.ifPresent(inv -> {
            inv.getListeners().stream().filter(listener -> listener.getType() == InventoryCloseEvent.class).forEach(listener -> ((InventoryListener<InventoryCloseEvent>) listener).accept(new InventoryCloseEvent(player.getOpenInventory())));
            manager.setInventory(player, null);
        });

        InventoryContents contents = new InventoryContentImpl(this, player);
        contents.pagination().page(page);
        manager.setContents(player, contents);

        if (property != null && value != null) {
            if (property.length != value.length) {
                throw new IllegalStateException("property does not have a assinged object value. Cannot open inventory.");
            }

            for (int i = 0; i < property.length; i++) {
                contents.setProperty(property[i], value[i]);
            }
        }

        this.provider.preInit(player, contents);
        if (provider instanceof Closable)
            ((Closable) provider).insertCloseItem(player, contents);

        this.provider.init(player, contents);


        InventoryOpener opener = manager.findOpener(type).orElseThrow(() -> new IllegalStateException("No opener found for the inventory type " + type.name()));
        Inventory handle = opener.open(this, player);
        manager.setInventory(player, this);
        return handle;
    }

    public void close(Player player) {
        InventoryManager manager = InventoryManager.getInstance();

        listeners.stream().filter(listener -> listener.getType() == InventoryCloseEvent.class).forEach(listener -> ((InventoryListener<InventoryCloseEvent>) listener).accept(new InventoryCloseEvent(player.getOpenInventory())));

        manager.setInventory(player, null);
        player.closeInventory();

        manager.setContents(player, null);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public InventoryType getType() {
        return type;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public boolean isCloseable() {
        return closeable;
    }

    public InventoryProvider getProvider() {
        return provider;
    }

    public Optional<SmartInventory> getParent() {
        return Optional.ofNullable(parent);
    }

    List<InventoryListener<? extends Event>> getListeners() {
        return listeners;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String id = "unknown";
        private String title = "unknown";
        private InventoryType type = InventoryType.CHEST;
        private int rows = 6, columns = 9;
        private boolean closeable = true;

        private InventoryProvider provider;
        private SmartInventory parent;

        private List<InventoryListener<? extends Event>> listeners = new ArrayList<>();

        private Builder() { }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder type(InventoryType type) {
            this.type = type;
            return this;
        }

        public Builder size(int rows) {
            this.rows = rows;
            this.columns = 9;
            return this;
        }

        public Builder size(int rows, int columns) {
            this.rows = rows;
            this.columns = columns;
            return this;
        }

        public Builder closeable(boolean closeable) {
            this.closeable = closeable;
            return this;
        }

        public Builder provider(InventoryProvider provider) {
            this.provider = provider;
            return this;
        }

        public Builder parent(SmartInventory parent) {
            this.parent = parent;
            return this;
        }

        public Builder listener(InventoryListener<? extends Event> listener) {
            this.listeners.add(listener);
            return this;
        }

        public SmartInventory build() {
            if (this.provider == null)
                throw new IllegalStateException("The provider of the SmartInventory.Builder must be set.");

            SmartInventory inv = new SmartInventory();
            inv.id = this.id;
            inv.title = this.title;
            inv.type = this.type;
            inv.rows = this.rows;
            inv.columns = this.columns;
            inv.closeable = this.closeable;
            inv.provider = this.provider;
            inv.parent = this.parent;
            inv.listeners = this.listeners;

            return inv;
        }
    }

}
