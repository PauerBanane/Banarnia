package de.banarnia.api.smartInventory.content;


import de.banarnia.api.smartInventory.ClickableItem;

public interface Pagination {

    public ClickableItem[] getPageItems();

    int getPage();
    public Pagination page(int page);

    boolean isFirst();
    boolean isLast();

    public Pagination first();
    public Pagination previous();
    public Pagination next();
    public Pagination last();

    public Pagination addToIterator(SlotIterator iterator);

    public Pagination setItems(ClickableItem... items);
    public Pagination addItem(ClickableItem item);
    public Pagination setItemsPerPage(int itemsPerPage);
    public Pagination setItemsPerRow(int itemsPerRow);
}
