package core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DataBase {
	private final ItemData itemData;
	private static DataBase INSTANCE;
	private final Map<String, Item> uniqueItemByName, setItemByName;

	private DataBase() {
		try {
			this.itemData = TxtFileReader.loadAll();
		} catch (IOException e) {
			throw new IllegalStateException("Failed to load item data files", e);
		}
		uniqueItemByName = new HashMap<String, Item>();
		for (Set<Item> items : itemData.uniqueItems.values()) {
			for (Item item : items) {
				item.fixUniqueName();
				uniqueItemByName.put(item.getName(), item);
			}
		}
		setItemByName = new HashMap<String, Item>();
		for (Set<Item> items : itemData.setItems.values()) {
			for (Item item : items) {
				item.fixSetName();
				setItemByName.put(item.getName(), item);
			}
		}
	}

	public static DataBase getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new DataBase();
		}
		return INSTANCE;
	}

	public ItemData getItemData() {
		return itemData;
	}

	public List<Item> getAllUniqueItems() {
		List<Item> ret = new ArrayList<Item>(uniqueItemByName.values());
		Collections.sort(ret);
		return ret;
	}

	public List<Item> getAllSetItems() {
		List<Item> ret = new ArrayList<Item>(setItemByName.values());
		Collections.sort(ret);
		return ret;
	}

	public static DataBase getINSTANCE() {
		return INSTANCE;
	}

	public int getBaseItemQlvl(String code) {
		return itemData.baseItems.get(code).getQlvl();
	}

	public Item getUniqueItemByName(String name) {
		return uniqueItemByName.get(name);
	}

	public Item getSetItemByName(String name) {
		return setItemByName.get(name);
	}

	public Set<Item> getSetItemsOfType(String type) {
		return itemData.setItems.get(type);
	}

	public Set<Item> getUniqueItemsOfType(String type) {
		return itemData.uniqueItems.get(type);
	}
}
