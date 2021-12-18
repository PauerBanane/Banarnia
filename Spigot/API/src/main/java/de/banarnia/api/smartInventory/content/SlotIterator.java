package de.banarnia.api.smartInventory.content;


import de.banarnia.api.smartInventory.ClickableItem;

import java.util.Optional;

public interface SlotIterator {

    enum Type {
        HORIZONTAL,
        VERTICAL,
        CENTERED,
        ROPE,
        UP_DOWN
    }

    public Optional<ClickableItem> get();
    public SlotIterator set(ClickableItem item);

    public SlotIterator previous();
    public SlotIterator next();

    public SlotIterator blacklist(int row, int column);
    public SlotIterator blacklist(SlotPos slotPos);

    public int row();
    public SlotIterator row(int row);

    public int column();
    public SlotIterator column(int column);

    public boolean started();
    public boolean ended();

    public boolean doesAllowOverride();
    public SlotIterator allowOverride(boolean override);
}
