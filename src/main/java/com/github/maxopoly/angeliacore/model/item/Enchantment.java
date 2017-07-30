package com.github.maxopoly.angeliacore.model.item;

public enum Enchantment {

	// bow
	POWER((short) 48), FLAME((short) 50), INFINITY((short) 51), PUNCH((short) 49),

	// sword
	BANE_OF_THE_ARTHROPODS((short) 18), SMITE((short) 17), SHARPNESS((short) 16), FIRE_ASPECT((short) 20), KNOCKBACK(
			(short) 19), LOOTING((short) 21),

	// rods
	LUCK_OF_THE_SEA((short) 61), LURE((short) 62),

	// helmet
	RESPIRATION((short) 5), AQUA_AFFINITY((short) 6),

	// boots
	DEPTH_STRIDER((short) 8), FROST_WALKER((short) 9), FEATHER_FALLING((short) 2),

	// armor
	PROTECTION((short) 0), FIRE_PROTECTION((short) 1), BLAST_PROTECTION((short) 3), PROJECTILE_PROJECTION((short) 4), THORNS(
			(short) 7),

	// tools
	EFFICIENCY((short) 32), UNBREAKING((short) 34), FORTUNE((short) 35), SILK_TOUCH((short) 33),

	// random bs
	MENDING((short) 70), VANISHING_CURSE((short) 71), BINDING_CURSE((short) 10), SWEEPING_EDGE((short) 22);

	private short id;

	private Enchantment(short id) {
		this.id = id;
	}

	public short getID() {
		return id;
	}

	public static Enchantment fromID(short id) {
		switch (id) {
			case 48:
				return POWER;
			case 50:
				return FLAME;
			case 51:
				return INFINITY;
			case 49:
				return PUNCH;
			case 18:
				return BANE_OF_THE_ARTHROPODS;
			case 17:
				return SMITE;
			case 16:
				return SHARPNESS;
			case 20:
				return FIRE_ASPECT;
			case 19:
				return KNOCKBACK;
			case 21:
				return LOOTING;
			case 61:
				return LUCK_OF_THE_SEA;
			case 62:
				return LURE;
			case 5:
				return RESPIRATION;
			case 6:
				return AQUA_AFFINITY;
			case 8:
				return DEPTH_STRIDER;
			case 9:
				return FROST_WALKER;
			case 2:
				return FEATHER_FALLING;
			case 0:
				return PROTECTION;
			case 1:
				return FIRE_PROTECTION;
			case 3:
				return BLAST_PROTECTION;
			case 4:
				return PROJECTILE_PROJECTION;
			case 7:
				return THORNS;
			case 32:
				return EFFICIENCY;
			case 34:
				return UNBREAKING;
			case 35:
				return FORTUNE;
			case 33:
				return SILK_TOUCH;
			case 70:
				return MENDING;
			case 71:
				return VANISHING_CURSE;
			case 10:
				return BINDING_CURSE;
			case 22:
				return SWEEPING_EDGE;
			default:
				throw new IllegalArgumentException(id + " is not a valid enchantment id");
		}
	}

}
