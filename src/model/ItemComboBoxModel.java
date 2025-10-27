package model;

import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import core.Item;

public class ItemComboBoxModel extends AbstractListModel<Item> implements ComboBoxModel<Item> {

	private static final long serialVersionUID = 5064984481379821274L;
	private final List<Item> allItems;
	private List<Item> viewItems;
	private Item selected;

	public ItemComboBoxModel(List<Item> items) {
		this.allItems = items;
		this.viewItems = new ArrayList<Item>(items);
	}

	public void setView(List<Item> newView) {
		this.viewItems = newView;
		if (viewItems.isEmpty()) {
			selected = null;
			fireContentsChanged(this, 0, 0);
		} else {
			selected = viewItems.get(0);
			fireContentsChanged(this, 0, viewItems.size() - 1);
		}
	}

	public List<Item> getView() {
		return viewItems;
	}

	public void resetView() {
		this.viewItems = new ArrayList<>(allItems);
		selected = viewItems.get(0);
		fireContentsChanged(this, 0, getSize() - 1);
	}

	public List<Item> getOriginal() {
		return allItems;
	}

	@Override
	public int getSize() {
		return viewItems.size();
	}

	@Override
	public Item getElementAt(int index) {
		if (index >= 0 && index < viewItems.size()) {
			return viewItems.get(index);
		}
		return null;
	}

	@Override
	public void setSelectedItem(Object item) {
		if ((selected != null && !selected.equals(item)) || selected == null && item != null) {
			selected = (Item) item;
			fireContentsChanged(this, -1, -1);
		}
	}

	@Override
	public Object getSelectedItem() {
		return selected;
	}

	public void selectPreviousItem() {
		if (selected == null || viewItems.isEmpty()) {
			return;
		}
		int previousIndex = viewItems.indexOf(selected) - 1;
		if (previousIndex >= 0) {
			setSelectedItem(getElementAt(previousIndex));
		}
	}

	public void selectNextItem() {
		if (selected == null || viewItems.isEmpty()) {
			return;
		}
		int nextIndex = viewItems.indexOf(selected) + 1;
		if (nextIndex < viewItems.size()) {
			setSelectedItem(getElementAt(nextIndex));
		}
	}
}
