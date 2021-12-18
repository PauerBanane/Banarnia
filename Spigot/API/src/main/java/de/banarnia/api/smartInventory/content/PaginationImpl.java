package de.banarnia.api.smartInventory.content;

import com.google.common.collect.Lists;
import de.banarnia.api.smartInventory.ClickableItem;

import java.util.Arrays;
import java.util.List;

public class PaginationImpl implements Pagination {

    private int currentPage;

    private ClickableItem[] items = new ClickableItem[0];
    private int itemsPerPage = 5;
    private int itemsPerRow = 9;

    @Override
    public ClickableItem[] getPageItems() {
        return Arrays.copyOfRange(items, currentPage * itemsPerPage, (currentPage + 1) * itemsPerPage);
    }

    @Override
    public int getPage() {
        return this.currentPage;
    }

    @Override
    public Pagination page(int page) {
        this.currentPage = page;
        return this;
    }

    @Override
    public boolean isFirst() {
        return this.currentPage == 0;
    }

    @Override
    public boolean isLast() {
        int pageCount = (int) Math.ceil((double) this.items.length / this.itemsPerPage);
        return this.currentPage >= pageCount - 1;
    }

    @Override
    public Pagination first() {
        this.currentPage = 0;
        return this;
    }

    @Override
    public Pagination previous() {
        if (!isFirst())
            this.currentPage--;

        return this;
    }

    @Override
    public Pagination next() {
        if (!isLast())
            this.currentPage++;

        return this;
    }

    @Override
    public Pagination last() {
        this.currentPage = this.items.length / this.itemsPerPage;
        return this;
    }

    @Override
    public Pagination addToIterator(SlotIterator iterator) {
        for (ClickableItem item : getPageItems()) {
            iterator.next().set(item);

            if (iterator.ended())
                break;
        }

        return this;
    }

    @Override
    public Pagination setItems(ClickableItem... items) {
        this.items = items;
        return this;
    }

    @Override
    public Pagination addItem(ClickableItem item) {
        List<ClickableItem> list = Lists.newArrayList(Arrays.asList(this.items));
        list.add(item);
        this.items = list.toArray(new ClickableItem[list.size()]);
        return this;
    }

    @Override
    public Pagination setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
        return this;
    }

    @Override
    public Pagination setItemsPerRow(int itemsPerRow) {
        this.itemsPerRow = itemsPerRow;
        return this;
    }

}
