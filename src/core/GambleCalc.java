package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import enums.Quality;
import enums.Rarity;

public class GambleCalc {
	private final DataBase itemDB = DataBase.getInstance();
	private final Map<String, Double> chanceCache = new HashMap<String, Double>();

	public GambleCalc() {

	}

	private Set<Item> getSetItemsOfType(String code) {
		return itemDB.getItemData().setItems.get(code);
	}

	private Set<Item> getUniqueItemsOfType(String code) {
		return itemDB.getItemData().uniqueItems.get(code);
	}

	private double calculateChanceToGambleCached(Item item, int ilvl, int qlvlExceptional, int qlvlElite) {
		String key = item.toString() + ":" + ilvl;
		Double cached = chanceCache.get(key);
		if (cached != null)
			return cached;

		double rarityFactor = this.getRarityFactorAtIlvl(item, ilvl);
		if (rarityFactor == 0.0) {
			chanceCache.put(key, 0.0);

			return 0.0;
		}
		double upgradeChance;
		if (qlvlExceptional == qlvlElite) {
			upgradeChance = 1.0;
		} else {
			upgradeChance = this.calculateUpgradeChanceAtIlvl(qlvlExceptional, qlvlElite, ilvl, item.getQuality());
		}
		double result = rarityFactor * upgradeChance * ((item.getRarity() == Rarity.UNIQUE) ? 0.0005 : 0.001);
		chanceCache.put(key, result);

		return result;
	}

	private double calculateChanceToGamble(Item item, int charLvl) {
		double sumOfProbs = 0.0;

		BaseItem b = itemDB.getItemData().baseItems.get(item.getCode());
		int qlvlExceptional = itemDB.getBaseItemQlvl(b.getVariantString(Quality.EXCEPTIONAL));
		int qlvlElite = itemDB.getBaseItemQlvl(b.getVariantString(Quality.ELITE));
		for (int i = 0; i < 10; i++) {
			int ilvl = Math.max(0, Math.min(charLvl - 5 + i, 99));
			sumOfProbs += this.calculateChanceToGambleCached(item, ilvl, qlvlExceptional, qlvlElite);
		}
		return sumOfProbs / 10.0;
	}

	public double[][] packChancesAndOptimalLevel(Item item) {
		double[][] result = new double[3][];
		double[] x = new double[99];
		double[] y = new double[99];
		double[] summary = new double[3];
		double bestChance = 0.0;
		List<Integer> bestLevels = new ArrayList<Integer>();

		for (int lvl = 1; lvl <= 99; lvl++) {
			double chance = calculateChanceToGamble(item, lvl);

			x[lvl - 1] = lvl;
			y[lvl - 1] = chance;

			if (chance > bestChance) {
				bestChance = chance;
				bestLevels.clear();
				bestLevels.add(lvl);
			} else if (chance == bestChance) {
				bestLevels.add(lvl);
			}
		}

		int minLvl = bestLevels.get(0);
		int maxLvl = bestLevels.get(bestLevels.size() - 1);
		summary[0] = minLvl;
		summary[1] = maxLvl;
		summary[2] = bestChance;

		result[0] = x;
		result[1] = y;
		result[2] = summary;
		return result;
	}

	private double getRarityFactorAtIlvl(Item item, int ilvl) {
		if (item.getQlvl() > ilvl) {
			return 0.0;
		}
		Set<Item> others;
		if (item.getRarity() == Rarity.SET) {
			others = getSetItemsOfType(item.getCode());
		} else {
			others = getUniqueItemsOfType(item.getCode());
		}
		if (others.size() == 1) {
			return 1.0;
		}

		int rarityTotal = 0;
		for (Item i : others) {
			if (i.getQlvl() > ilvl) {
				continue;
			}
			rarityTotal += i.getRarityFactor();
		}

		return (item.getRarityFactor() * 1.0d) / (rarityTotal * 1.0d);
	}

	private double calculateUpgradeChanceAtIlvl(int qlvlExceptional, int qlvlElite, int ilvl, Quality quality) {

		double exceptional = 0.0;
		double elite = 0.0;

		if (ilvl >= qlvlExceptional) {
			exceptional = (ilvl - qlvlExceptional) * 0.009 + 0.01;
		}
		if (ilvl >= qlvlElite) {
			elite = (ilvl - qlvlElite) * 0.0033 + 0.01;
		}
		switch (quality) {
		case ELITE:
			return elite;
		case EXCEPTIONAL:
			return exceptional;
		default:
			return 1.0 - exceptional - elite;
		}
	}
}
