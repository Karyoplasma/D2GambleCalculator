package core;

import java.util.Map;
import java.util.Set;

public final class ItemData {
	public final Map<String, BaseItem> baseItems;
	public final Set<String> gambleItems;
	public final Map<String, Set<Item>> setItems, uniqueItems;

	public ItemData(Map<String, BaseItem> baseItems, Set<String> gambleItems, Map<String, Set<Item>> setItems,
			Map<String, Set<Item>> uniqueItems) {
		this.baseItems = baseItems;
		this.gambleItems = gambleItems;
		this.setItems = setItems;
		this.uniqueItems = uniqueItems;
	}
}
