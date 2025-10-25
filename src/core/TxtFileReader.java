package core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import enums.Quality;
import enums.Rarity;

public class TxtFileReader {

	private TxtFileReader() {
	}

	public static ItemData loadAll() throws IOException {
		Map<String, BaseItem> baseItems = readBaseItems();
		Set<String> gambleItems = readGambleItems(baseItems);
		Map<String, Set<Item>> setItems = readSets(baseItems, gambleItems);
		Map<String, Set<Item>> uniqueItems = readUniques(baseItems, gambleItems);

		return new ItemData(baseItems, gambleItems, setItems, uniqueItems);
	}

	private static void readTxtFile(String path, Consumer<String[]> handler) throws IOException {
		try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
			String line = reader.readLine();
			while ((line = reader.readLine()) != null) {
				if (line.isEmpty())
					continue;
				String[] parts = line.split("\t");
				handler.accept(parts);
			}
		}
	}

	public static Map<String, BaseItem> readBaseItems() throws IOException {
		Map<String, BaseItem> ret = new HashMap<>();

		readTxtFile("resources/Armor.txt", parts -> {
			if (parts.length >= 26 && !parts[17].isEmpty()) {
				ret.put(parts[17],
						new BaseItem(parts[17], parts[23], parts[24], parts[25], Integer.parseInt(parts[13])));
			}
		});

		readTxtFile("resources/Weapons.txt", parts -> {
			if (parts.length >= 37 && !parts[9].isEmpty() && !parts[3].isEmpty()) {
				ret.put(parts[3], new BaseItem(parts[3], parts[34], parts[35], parts[36], Integer.parseInt(parts[27])));
			}
		});

		ret.put("amu", new BaseItem("amu", "amu", "amu", "amu", 1));
		ret.put("rin", new BaseItem("rin", "rin", "rin", "rin", 1));

		return ret;
	}

	public static Map<String, Set<Item>> readSets(Map<String, BaseItem> baseItems, Set<String> gambleItems)
			throws IOException {
		Map<String, Set<Item>> ret = new HashMap<String, Set<Item>>();
		readTxtFile("resources/SetItems.txt", parts -> {
			if (parts[0].startsWith("Cow King")) {
				return;
			}
			if (parts.length < 6) {
				return;
			}
			BaseItem baseItem = baseItems.get(parts[2]);
			if (baseItem == null) {
				return;
			}
			if (!gambleItems.contains(baseItem.getCode())) {
				return;
			}
			Item item = new Item(parts[0], parts[2], Rarity.SET, Integer.parseInt(parts[4]), Integer.parseInt(parts[5]),
					baseItem.getQuality());
			ret.computeIfAbsent(parts[2], k -> new HashSet<>()).add(item);
		});

		return ret;
	}

	public static Map<String, Set<Item>> readUniques(Map<String, BaseItem> baseItems, Set<String> gambleItems)
			throws IOException {
		Map<String, Set<Item>> ret = new HashMap<String, Set<Item>>();

		readTxtFile("resources/UniqueItems.txt", parts -> {
			if (parts.length < 9 || !parts[2].equals("1")) {
				return;
			}
			BaseItem baseItem = baseItems.get(parts[8]);
			if (baseItem != null && gambleItems.contains(baseItem.getCode())) {
				Item item = new Item(parts[0], parts[8], Rarity.UNIQUE, Integer.parseInt(parts[4]),
						Integer.parseInt(parts[6]), baseItem.getQuality());
				ret.computeIfAbsent(parts[8], k -> new HashSet<>()).add(item);
			}
		});
		return ret;
	}

	public static Set<String> readGambleItems(Map<String, BaseItem> baseItems) throws IOException {
		Set<String> ret = new HashSet<String>();

		readTxtFile("resources/Gamble.txt", parts -> {
			if (parts.length >= 2) {
				BaseItem baseItem = baseItems.get(parts[1]);
				ret.add(baseItem.getCode());
				ret.add(baseItem.getVariantString(Quality.EXCEPTIONAL));
				ret.add(baseItem.getVariantString(Quality.ELITE));
			}
		});

		return ret;
	}

//	public static void main(String[] args) throws IOException {
//		GambleCalc calc = new GambleCalc();
//		System.out.println(calc.getUniqueItemsOfType("ci3"));
//		String item1 = "Metalgrid";
//		int clvl1 = 98;
//		double chance1 = calc.calculateChanceToGamble(item1, clvl1, Rarity.UNIQUE);
//		System.out.println("chance1: " + Math.round(1.0d / chance1));
//		String item2 = "Gull";
//		int clvl2 = 39;
//		double chance2 = calc.calculateChanceToGamble(item2, clvl2, Rarity.UNIQUE);
//		System.out.println("chance2: " + Math.round(1.0d / chance2));
//		String item3 = "Gull";
//		int clvl3 = 99;
//		double chance3 = calc.calculateChanceToGamble(item3, clvl3, Rarity.UNIQUE);
//		System.out.println("chance3: " + Math.round(1.0d / chance3));
//	}
}
