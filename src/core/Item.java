package core;

import java.util.Objects;
import enums.Quality;
import enums.Rarity;

public class Item implements Comparable<Item> {
	private Rarity rarity;
	private int rarityFactor, qlvl;
	private String name;
	private Quality quality;
	private String code;

	public Item(String name, String code, Rarity rarity, int rarityFactor, int qlvl, Quality quality) {
		this.rarity = rarity;
		this.code = code;
		this.rarityFactor = rarityFactor;
		this.qlvl = qlvl;
		this.name = name;
		this.quality = quality;
	}

	public Rarity getRarity() {
		return rarity;
	}

	public int getRarityFactor() {
		return rarityFactor;
	}

	public int getQlvl() {
		return qlvl;
	}

	public Quality getQuality() {
		return quality;
	}

	public String getCode() {
		return this.code;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, qlvl, quality, rarityFactor, rarity);
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

	@Override
	public int compareTo(Item o) {
		return name.compareTo(o.name);
	}
}
