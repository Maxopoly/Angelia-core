package com.github.maxopoly.angeliacore.model.entity;

import java.util.Map;
import java.util.TreeMap;

/**
 * On packet level minecraft differentiates between living entities and
 * 'objects' like boats or llama spit. This enum includes all of these objects
 * that can be spawned, together with their id. Note that these ids are
 * completly separate from living entity ids, even though the gaps might make
 * you think otherwise
 *
 */
public enum SpawnableObjectType {

	BOAT(1), // includes all wood types
	ITEMSTACK(2), AREA_EFFECT_CLOUD(3), // the clouds spawned by lingering potions
	MINECART(10), // includes all minecart variations (tnt/chest/furnace)
	ACTIVATED_TNT(50), // tnt in it's block form is deleted when primed and turns into one of these
						// pseduo entities
	ENDER_CRYSTAL(51), // the floating crystal from the end. No clue why these are entities
	ARROW(60), // includes both tipped and normal, but NOT SPECTRAL
	SNOW_BALL(61), EGG(62), // thrown chicken eggs
	FIRE_BALL(63), // ghast projectile
	FIRE_CHARGE(64), // blaze projectile
	ENDER_PEARL(65), // flying ender pearl
	WITHER_SKULL(66), // the projectile shot by a wither, not the item drop
	SHULKER_BULLET(67), // projectile shot by a shulker
	LLAMA_SPIT(68), // why is this a thing
	FALLING_OBJECT(70), // anvils, sand, gravel etc.
	ITEM_FRAME(71), // another thing that should be a block, but isn't
	EYE_OF_ENDER(72), // thrown eye of ender
	POTION(73), // splash or lingering potion, which is midair
	XP_BOTTLE(75), // xp bottle while flying
	FIREWORK(76), LEASH_KNOT(77), // the thing that appear when tying a horse to a fence
	ARMOR_STAND(78), EVOCATION_FANG(79), FISHING_HOOK(90), // the bopper at the end of the line, which is floating in
															// the water
	SPECTRAL_ARROW(91), // if someone gets the chance, please ask jeb what he was thinking when making
						// these a separate thing
	DRAGON_FIREBALL(93); // thing shot by the end dragon

	private static Map<Integer, SpawnableObjectType> types;

	static {
		types = new TreeMap<Integer, SpawnableObjectType>();
		for (SpawnableObjectType type : SpawnableObjectType.values()) {
			types.put(type.getID(), type);
		}
	}

	public static SpawnableObjectType getById(int id) {
		return types.get(id);
	}

	private int id;

	private SpawnableObjectType(int id) {
		this.id = id;
	}

	public int getID() {
		return id;
	}

}
