package core;

import enums.Quality;

public class BaseItem {
	private String code, normCode, uberCode, ultraCode;
	private int qlvl;
	private Quality quality;

	public BaseItem(String code, String normCode, String uberCode, String ultraCode, int qlvl) {
		this.code = code;
		this.normCode = normCode;
		this.uberCode = uberCode;
		this.ultraCode = ultraCode;
		this.qlvl = qlvl;
		this.quality = determineQuality();
	}

	public int getQlvl() {
		return this.qlvl;
	}

	public Quality getQuality() {
		return this.quality;
	}

	private Quality determineQuality() {
		if (code.equals(this.normCode)) {
			return Quality.NORMAL;
		}
		if (code.equals(this.ultraCode)) {
			return Quality.ELITE;
		}
		return Quality.EXCEPTIONAL;
	}

	public String getCode() {
		return this.code;
	}

	public String getVariantString(Quality quality) {
		switch (quality) {
		case EXCEPTIONAL:
			return uberCode;
		case ELITE:
			return ultraCode;
		default:
			return normCode;
		}
	}

	@Override
	public String toString() {
		return String.format("Code: %s, No: %s, Ex: %s, El: %s", code, normCode, uberCode, ultraCode);
	}
}
