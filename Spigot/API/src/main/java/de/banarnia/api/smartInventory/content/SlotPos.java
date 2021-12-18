package de.banarnia.api.smartInventory.content;


import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class SlotPos {

    private final int row;
    private final int column;

    public SlotPos(int row, int column) {
        this.row = row;
        this.column = column;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null || getClass() != object.getClass())
            return false;

        SlotPos slotPos = (SlotPos) object;

        return new EqualsBuilder()
                .append(row, slotPos.row)
                .append(column, slotPos.column)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(row)
                .append(column)
                .toHashCode();
    }

    public int getRow() { return row; }
    public int getColumn() { return column; }

    public static SlotPos of(int row, int column) {
        return new SlotPos(row, column);
    }

    public static SlotPos of(int slot) {
        String radixSlot = Integer.toString(slot, 9);
        int row = radixSlot.length() == 1 ? 0 : Integer.valueOf("" + radixSlot.charAt(0));
        int column = Integer.valueOf("" + radixSlot.charAt(radixSlot.length() == 1 ? 0 : 1));
        return new SlotPos(row, column);
    }

}
