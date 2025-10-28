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
	private static final Map<String, String> NAME_FIXES = new HashMap<>();

	static {
		NAME_FIXES.put("Fechmars Axe", "Axe of Fechmar");
		NAME_FIXES.put("The Chieftan", "The Chieftain");
		NAME_FIXES.put("Iros Torch", "Iro's Torch");
		NAME_FIXES.put("Maelstromwrath", "Maelstrom");
		NAME_FIXES.put("Umes Lament", "Ume's Lament");
		NAME_FIXES.put("Rixots Keen", "Rixot's Keen");
		NAME_FIXES.put("The Atlantian", "The Atlantean");
		NAME_FIXES.put("The Generals Tan Do Li Ga", "The General's Tan Do Li Ga");
		NAME_FIXES.put("Blinkbats Form", "Blinkbat's Form");
		NAME_FIXES.put("Verdugo's Hearty Cord", "Verdungo's Hearty Cord");
		NAME_FIXES.put("Victors Silk", "Silks of the Victor");
		NAME_FIXES.put("Wartraveler", "War Traveler");
		NAME_FIXES.put("Venomsward", "Venom Ward");
		NAME_FIXES.put("Thudergod's Vigor", "Thundergod's Vigor");
		NAME_FIXES.put("Bul Katho's Wedding Band", "Bul-Kathos' Wedding Band");
		NAME_FIXES.put("Culvens Point", "Culven's Point");
		NAME_FIXES.put("Doomspittle", "Doomslinger");
		NAME_FIXES.put("War Bonnet", "Biggin's Bonnet");
		NAME_FIXES.put("Griswolds Edge", "Griswold's Edge");
		NAME_FIXES.put("Kinemils Awl", "Kinemil's Awl");
		NAME_FIXES.put("Lenyms Cord", "Lenymo");
		NAME_FIXES.put("Mindrend", "Skull Splitter");
		NAME_FIXES.put("Mosers Blessed Circle", "Moser's Blessed Circle");
		NAME_FIXES.put("Djinnslayer", "Djinn Slayer");
		NAME_FIXES.put("Bonesob", "Bonesnap");
		NAME_FIXES.put("Wisp", "Wisp Projector");
		NAME_FIXES.put("Deaths's Web", "Death's Web");
		NAME_FIXES.put("Cerebus", "Cerebus' Bite");
		NAME_FIXES.put("Cutthroat1", "Bartuc's Cut-Throat");
		NAME_FIXES.put("Kerke's Sanctuary", "Gerke's Sanctuary");
		NAME_FIXES.put("Radimant's Sphere", "Radament's Sphere");
		NAME_FIXES.put("Skin of the Flayerd One", "Skin of the Flayed One");
		NAME_FIXES.put("Valkiry Wing", "Valkyrie Wing");
		NAME_FIXES.put("Peasent Crown", "Peasant Crown");
		NAME_FIXES.put("Pus Spiter", "Pus Spitter");
		NAME_FIXES.put("Que-Hegan's Wisdon", "Que-Hegan's Wisdom");
		NAME_FIXES.put("Steel Carapice", "Steel Carapace");
		NAME_FIXES.put("Steelpillar", "Steel Pillar");
		NAME_FIXES.put("Steelshade", "Steel Shade");
		NAME_FIXES.put("Whichwild String", "Witchwild String");
		NAME_FIXES.put("The Minataur", "The Minotaur");
		NAME_FIXES.put("Pompe's Wrath", "Pompeii's Wrath");
		NAME_FIXES.put("Piercerib", "Rogue's Bow");
		NAME_FIXES.put("Pullspite", "Stormstrike");
		NAME_FIXES.put("Rimeraven", "Raven Claw");
		NAME_FIXES.put("Irices Shard", "Spectral Shard");
		NAME_FIXES.put("Krintizs Skewer", "Skewer of Krintiz");
		NAME_FIXES.put("Spiritual Custodian", "Dark Adherent");
		NAME_FIXES.put("Heaven's Taebaek", "Taebaek's Glory");
		NAME_FIXES.put("Deaths's Web", "Death's Web");
		NAME_FIXES.put("Wihtstan's Guard", "Whitstan's Guard");
		NAME_FIXES.put("McAuley's Paragon", "Sander's Paragon");
		NAME_FIXES.put("McAuley's Riprap", "Sander's Riprap");
		NAME_FIXES.put("McAuley's Taboo", "Sander's Taboo");
		NAME_FIXES.put("Aldur's Gauntlet", "Aldur's Rhythm");
		NAME_FIXES.put("Hwanin's Seal", "Hwanin's Blessing");
		NAME_FIXES.put("Tal Rasha's Howling Wind", "Tal Rasha's Guardianship");
	}

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
			Item item = new Item(NAME_FIXES.getOrDefault(parts[0], parts[0]), parts[2], Rarity.SET,
					Integer.parseInt(parts[4]), Integer.parseInt(parts[5]), baseItem.getQuality());
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
				Item item = new Item(NAME_FIXES.getOrDefault(parts[0], parts[0]), parts[8], Rarity.UNIQUE,
						Integer.parseInt(parts[4]), Integer.parseInt(parts[6]), baseItem.getQuality());
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
