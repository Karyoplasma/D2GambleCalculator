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
	
	public void fixSetName() {
		switch (this.name) {
		case "Spiritual Custodian":
			this.fixedName = "Dark Adherent";
			break;
		case "Heaven's Taebaek":
			this.fixedName = "Taebaek's Glory";
			break;
		case "Deaths's Web":
			this.fixedName = "Death's Web";
			break;
		case "Wihtstan's Guard":
			this.fixedName = "Whitstan's Guard";
			break;
		case "McAuley's Paragon":
			this.fixedName = "Sander's Paragon";
			break;
		case "McAuley's Riprap":
			this.fixedName = "Sander's Riprap";
			break;
		case "McAuley's Taboo":
			this.fixedName = "Sander's Taboo";
			break;
		default:
			this.fixedName = name;
		}
	}
	
	public void fixUniqueName() {
		switch (this.name) {
		case "Bonesob":
			this.fixedName = "Bonesnap";
			break;
		case "Wisp":
			this.fixedName = "Wisp Projector";
			break;
		case "Deaths's Web":
			this.fixedName = "Death's Web";
			break;
		case "Cerebus":
			this.fixedName = "Cerebus' Bite";
			break;
		case "Cutthroat1":
			this.fixedName = "Bartuc's Cut-Throat";
			break;
		case "Kerke's Sanctuary":
			this.fixedName = "Gerke's Sanctuary";
			break;
		case "Radimant's Sphere":
			this.fixedName = "Radament's Sphere";
			break;
		case "Skin of the Flayerd One":
			this.fixedName = "Skin of the Flayed One";
			break;
		case "Valkiry Wing":
			this.fixedName = "Valkyrie Wing";
			break;
		case "Peasent Crown":
			this.fixedName = "Peasant Crown";
			break;
		case "Pus Spiter":
			this.fixedName = "Pus Spiter";
			break;
		case "Whichwild String":
			this.fixedName = "Witchwild String";
			break;
		case "The Minataur":
			this.fixedName = "The Minotaur";
			break;
		case "Pompe's Wrath":
			this.fixedName = "Pompeii's Wrath";
			break;
		case "Piercerib":
			this.fixedName = "Rogue's Bow";
			break;
		case "Pullspite":
			this.fixedName = "Stormstrike";
			break;
		case "Rimeraven":
			this.fixedName = "Raven Claw";
			break;
		case "Irices Shard":
			this.fixedName = "Spectral Shard";
			break;
		case "Krintizs Skewer":
			this.fixedName = "Skewer of Krintiz";
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
