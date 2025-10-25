package core;

import java.util.Objects;

import enums.Quality;
import enums.Rarity;

public class Item implements Comparable<Item> {
	private Rarity rarity;
	private int rare, qlvl;
	private String name, fixedName;
	private Quality quality;
	private String code;

	public Item(String name, String code, Rarity rarity, int rare, int qlvl, Quality quality) {
		this.rarity = rarity;
		this.code = code;
		this.rare = rare;
		this.qlvl = qlvl;
		this.name = name;
		this.quality = quality;
	}

	public void fixName() {
		switch (this.name) {
		case "Bonesob":
			this.fixedName = "Bonesnap";
			break;
		default:
			this.fixedName = name;
		}
	}

	public Rarity getRarity() {
		return rarity;
	}

	public int getRare() {
		return rare;
	}

	public int getQlvl() {
		return qlvl;
	}

	public String getName() {
		return name;
	}

	public Quality getQuality() {
		return quality;
	}

	@Override
	public String toString() {
		return fixedName;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, qlvl, quality, rare, rarity);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Item))
			return false;
		Item other = (Item) obj;
		return name.equals(other.name);
	}

	public String getCode() {
		return this.code;
	}

	public void debugPrint() {
		System.out.printf("Name: %s, qlvl: %d, quality: %s, rare: %d, rarity %s\n", name, qlvl, quality, rare, rarity);

	}
	@Override
	public int compareTo(Item o) {
		return fixedName.compareTo(o.fixedName);
	}
}
