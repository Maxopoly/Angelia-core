package com.github.maxopoly.angeliacore.model.potion;

public enum PotionType {

	SPEED(1), SLOWNESS(2), HASTE(3), MINING_FATIGUE(4), STRENGTH(5), INSTANT_HEALTH(6), INSTANT_DAMAGE(7),
	JUMP_BOOST(8), NAUSEA(9), REGENERATION(10), RESISTANCE(11), FIRE_RESISTANCE(12), WATER_BREATHING(13),
	INVISIBILITY(14), BLINDNESS(15), NIGHT_VISION(16), HUNGER(17), WEAKNESS(18), POISON(19), WITHER(20),
	HEALTH_BOOST(21), ABSORPTION(22), SATURATION(23), GLOWING(24), LEVITATION(25), LUCK(26), BAD_LUCK(27);

	public static PotionType getById(int id) {
		switch (id) {
		case 1:
			return SPEED;
		case 2:
			return SLOWNESS;
		case 3:
			return HASTE;
		case 4:
			return MINING_FATIGUE;
		case 5:
			return STRENGTH;
		case 6:
			return INSTANT_HEALTH;
		case 7:
			return INSTANT_DAMAGE;
		case 8:
			return JUMP_BOOST;
		case 9:
			return NAUSEA;
		case 10:
			return REGENERATION;
		case 11:
			return RESISTANCE;
		case 12:
			return FIRE_RESISTANCE;
		case 13:
			return WATER_BREATHING;
		case 14:
			return INVISIBILITY;
		case 15:
			return BLINDNESS;
		case 16:
			return NIGHT_VISION;
		case 17:
			return HUNGER;
		case 18:
			return WEAKNESS;
		case 19:
			return POISON;
		case 20:
			return WITHER;
		case 21:
			return HEALTH_BOOST;
		case 22:
			return ABSORPTION;
		case 23:
			return SATURATION;
		case 24:
			return GLOWING;
		case 25:
			return LEVITATION;
		case 26:
			return LUCK;
		case 27:
			return BAD_LUCK;
		default:
			throw new IllegalArgumentException(id + " is not a valid potion type id");
		}
	}

	private int id;

	private PotionType(int id) {
		this.id = id;
	}

	public int getID() {
		return id;
	}

}
