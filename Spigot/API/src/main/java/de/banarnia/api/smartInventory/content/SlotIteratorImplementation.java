package de.banarnia.api.smartInventory.content;

import de.banarnia.api.smartInventory.ClickableItem;
import de.banarnia.api.smartInventory.SmartInventory;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class SlotIteratorImplementation implements SlotIterator {

    private InventoryContents contents;
    private SmartInventory inv;

    private Type type;
    private boolean started = false;
    private boolean allowOverride = true;
    private int row, column;
    private int startColumn, startRow;

    private Set<SlotPos> blacklisted = new HashSet<>();

    public SlotIteratorImplementation(InventoryContents contents, SmartInventory inv, Type type, int startRow, int startColumn) {

        this.contents = contents;
        this.inv = inv;

        this.type = type;

        this.row = startRow;
        this.column = startColumn;

        this.startRow = startRow;
        this.startColumn = startColumn;
    }

    public SlotIteratorImplementation(InventoryContents contents, SmartInventory inv, Type type) {

        this(contents, inv, type, 0, 0);
    }

    @Override
    public Optional<ClickableItem> get() {
        return contents.get(row, column);
    }

    @Override
    public SlotIterator set(ClickableItem item) {
        if (canPlace())
            contents.set(row, column, item);

        return this;
    }

    @Override
    public SlotIterator previous() {
        if (row == 0 && column == 0) {
            this.started = true;
            return this;
        }

        do {
            if (!this.started) {
                this.started = true;
            } else {
                switch (type) {
                    case HORIZONTAL :
                        column--;

                        if (column == 0) {
                            column = inv.getColumns() - 2;
                            row--;
                        }
                        break;
                    case VERTICAL :
                        row--;

                        if (row == 0) {
                            row = inv.getRows() - 1;
                            column--;
                        }
                        break;
                    case CENTERED:
                        column --;

                        if (column == 1) {
                            column = inv.getColumns() - 1;
                            row--;
                        }
                        break;
                    case ROPE:
                        boolean lastRow = row == inv.getRows() - 2;
                        boolean firstRow = row == startRow;
                        boolean up = lastRow && (column == startColumn ||
                                column == startColumn + 4 ||
                                column == startColumn + 8);
                        boolean down = firstRow && (column == startColumn + 2 ||
                                column == startColumn + 6);
                        boolean changeColumn = !up && !down;

                        if (changeColumn)
                            column--;
                        if (up)
                            row--;
                        if (down)
                            row++;

                        break;
                    case UP_DOWN:
                        if (row == startRow) {
                            row++;
                            column--;
                        } else {
                            row--;
                        }
                }
            }
        } while (!canPlace() && (row != 0 || column != 0));

        return this;
    }

    @Override
    public SlotIterator next() {
        if (ended()) {
            this.started = true;
            return this;
        }

        do {
            if (!this.started) {
                this.started = true;
            } else {
                switch (type) {
                    case HORIZONTAL :
                        column = ++column % inv.getColumns();

                        if (column == 0)
                            row++;
                        break;
                    case VERTICAL :
                        row = ++row % inv.getRows();

                        if (row == 0)
                            column++;
                        break;
                    case CENTERED:
                        column = ++column % inv.getColumns();

                        if (column == 0 || column == 8) {
                            row ++;
                            column = 1;
                        }
                        break;
                    case ROPE:
                        boolean lastRow = row == inv.getRows() - 2;
                        boolean firstRow = row == startRow;
                        boolean down = !lastRow && (column == startColumn ||
                                column == startColumn + 4 ||
                                column == startColumn + 8);
                        boolean up = !firstRow && (column == startColumn + 2 ||
                                column == startColumn + 6);
                        boolean changeColumn = !up && !down;

                        if (changeColumn)
                            column++;
                        if (up)
                            row--;
                        if (down)
                            row++;

                        break;
                    case UP_DOWN:
                        if (row == startRow) {
                            row++;
                        } else {
                            row--;
                            column++;
                        }
                }
            }
        } while (!canPlace() && !ended());

        return this;
    }

    @Override
    public SlotIterator blacklist(int row, int column) {
        this.blacklisted.add(SlotPos.of(row, column));
        return this;
    }

    @Override
    public SlotIterator blacklist(SlotPos slotPos) {
        return blacklist(slotPos.getRow(), slotPos.getColumn());
    }

    @Override
    public int row() {
        return row;
    }

    @Override
    public SlotIterator row(int row) {
        this.row = row;
        return this;
    }

    @Override
    public int column() {
        return column;
    }

    @Override
    public SlotIterator column(int column) {
        this.column = column;
        return this;
    }

    @Override
    public boolean started() {
        return this.started;
    }

    @Override
    public boolean ended() {
        return row == inv.getRows() - 1 && column == inv.getColumns() - 1;
    }

    @Override
    public boolean doesAllowOverride() {
        return allowOverride;
    }

    @Override
    public SlotIterator allowOverride(boolean override) {
        this.allowOverride = override;
        return this;
    }

    private boolean canPlace() {
        return !blacklisted.contains(SlotPos.of(row, column)) && (allowOverride || !this.get().isPresent());
    }

}
