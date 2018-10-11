package com.github.maxopoly.angeliacore.model.item;

public enum Material {
	EMPTY_SLOT(-1), AIR(0, 0), STONE(1), GRASS(2), DIRT(3), COBBLESTONE(4), WOOD(5), SAPLING(6), BEDROCK(7), WATER(8), STATIONARY_WATER(
			9), LAVA(10), STATIONARY_LAVA(11), SAND(12), GRAVEL(13), GOLD_ORE(14), IRON_ORE(15), COAL_ORE(16), LOG(17), LEAVES(
			18), SPONGE(19), GLASS(20), LAPIS_ORE(21), LAPIS_BLOCK(22), DISPENSER(23), SANDSTONE(24), NOTE_BLOCK(25), BED_BLOCK(
			26), POWERED_RAIL(27), DETECTOR_RAIL(28), PISTON_STICKY_BASE(29), WEB(30), LONG_GRASS(31), DEAD_BUSH(32), PISTON_BASE(
			33), PISTON_EXTENSION(34), WOOL(35), PISTON_MOVING_PIECE(36), YELLOW_FLOWER(37), RED_ROSE(38), BROWN_MUSHROOM(39), RED_MUSHROOM(
			40), GOLD_BLOCK(41), IRON_BLOCK(42), DOUBLE_STEP(43), STEP(44), BRICK(45), TNT(46), BOOKSHELF(47), MOSSY_COBBLESTONE(
			48), OBSIDIAN(49), TORCH(50), FIRE(51), MOB_SPAWNER(52), WOOD_STAIRS(53), CHEST(54), REDSTONE_WIRE(55), DIAMOND_ORE(
			56), DIAMOND_BLOCK(57), WORKBENCH(58), CROPS(59), SOIL(60), FURNACE(61), BURNING_FURNACE(62), SIGN_POST(63, 64), WOODEN_DOOR(
			64), LADDER(65), RAILS(66), COBBLESTONE_STAIRS(67), WALL_SIGN(68, 64), LEVER(69), STONE_PLATE(70), IRON_DOOR_BLOCK(
			71), WOOD_PLATE(72), REDSTONE_ORE(73), GLOWING_REDSTONE_ORE(74), REDSTONE_TORCH_OFF(75), REDSTONE_TORCH_ON(76), STONE_BUTTON(
			77), SNOW(78), ICE(79), SNOW_BLOCK(80), CACTUS(81), CLAY(82), SUGAR_CANE_BLOCK(83), JUKEBOX(84), FENCE(85), PUMPKIN(
			86), NETHERRACK(87), SOUL_SAND(88), GLOWSTONE(89), PORTAL(90), JACK_O_LANTERN(91), CAKE_BLOCK(92, 64), DIODE_BLOCK_OFF(
			93), DIODE_BLOCK_ON(94), STAINED_GLASS(95), TRAP_DOOR(96), MONSTER_EGGS(97), SMOOTH_BRICK(98), HUGE_MUSHROOM_1(99), HUGE_MUSHROOM_2(
			100), IRON_FENCE(101), THIN_GLASS(102), MELON_BLOCK(103), PUMPKIN_STEM(104), MELON_STEM(105), VINE(106), FENCE_GATE(
			107), BRICK_STAIRS(108), SMOOTH_STAIRS(109), MYCEL(110), WATER_LILY(111), NETHER_BRICK(112), NETHER_FENCE(113), NETHER_BRICK_STAIRS(
			114), NETHER_WARTS(115), ENCHANTMENT_TABLE(116), BREWING_STAND(117), CAULDRON(118), ENDER_PORTAL(119), ENDER_PORTAL_FRAME(
			120), ENDER_STONE(121), DRAGON_EGG(122), REDSTONE_LAMP_OFF(123), REDSTONE_LAMP_ON(124), WOOD_DOUBLE_STEP(125), WOOD_STEP(
			126), COCOA(127), SANDSTONE_STAIRS(128), EMERALD_ORE(129), ENDER_CHEST(130), TRIPWIRE_HOOK(131), TRIPWIRE(132), EMERALD_BLOCK(
			133), SPRUCE_WOOD_STAIRS(134), BIRCH_WOOD_STAIRS(135), JUNGLE_WOOD_STAIRS(136), COMMAND(137), BEACON(138), COBBLE_WALL(
			139), FLOWER_POT(140), CARROT(141), POTATO(142), WOOD_BUTTON(143), SKULL(144), ANVIL(145), TRAPPED_CHEST(146), GOLD_PLATE(
			147), IRON_PLATE(148), REDSTONE_COMPARATOR_OFF(149), REDSTONE_COMPARATOR_ON(150), DAYLIGHT_DETECTOR(151), REDSTONE_BLOCK(
			152), QUARTZ_ORE(153), HOPPER(154), QUARTZ_BLOCK(155), QUARTZ_STAIRS(156), ACTIVATOR_RAIL(157), DROPPER(158), STAINED_CLAY(
			159), STAINED_GLASS_PANE(160), LEAVES_2(161), LOG_2(162), ACACIA_STAIRS(163), DARK_OAK_STAIRS(164), SLIME_BLOCK(
			165), BARRIER(166), IRON_TRAPDOOR(167), PRISMARINE(168), SEA_LANTERN(169), HAY_BLOCK(170), CARPET(171), HARD_CLAY(
			172), COAL_BLOCK(173), PACKED_ICE(174), DOUBLE_PLANT(175), STANDING_BANNER(176), WALL_BANNER(177), DAYLIGHT_DETECTOR_INVERTED(
			178), RED_SANDSTONE(179), RED_SANDSTONE_STAIRS(180), DOUBLE_STONE_SLAB2(181), STONE_SLAB2(182), SPRUCE_FENCE_GATE(
			183), BIRCH_FENCE_GATE(184), JUNGLE_FENCE_GATE(185), DARK_OAK_FENCE_GATE(186), ACACIA_FENCE_GATE(187), SPRUCE_FENCE(
			188), BIRCH_FENCE(189), JUNGLE_FENCE(190), DARK_OAK_FENCE(191), ACACIA_FENCE(192), SPRUCE_DOOR(193), BIRCH_DOOR(
			194), JUNGLE_DOOR(195), ACACIA_DOOR(196), DARK_OAK_DOOR(197), END_ROD(198), CHORUS_PLANT(199), CHORUS_FLOWER(200), PURPUR_BLOCK(
			201), PURPUR_PILLAR(202), PURPUR_STAIRS(203), PURPUR_DOUBLE_SLAB(204), PURPUR_SLAB(205), END_BRICKS(206), BEETROOT_BLOCK(
			207), GRASS_PATH(208), END_GATEWAY(209), COMMAND_REPEATING(210), COMMAND_CHAIN(211), FROSTED_ICE(212), MAGMA(213), NETHER_WART_BLOCK(
			214), RED_NETHER_BRICK(215), BONE_BLOCK(216), STRUCTURE_VOID(217), OBSERVER(218), WHITE_SHULKER_BOX(219, 1), ORANGE_SHULKER_BOX(
			220, 1), MAGENTA_SHULKER_BOX(221, 1), LIGHT_BLUE_SHULKER_BOX(222, 1), YELLOW_SHULKER_BOX(223, 1), LIME_SHULKER_BOX(
			224, 1), PINK_SHULKER_BOX(225, 1), GRAY_SHULKER_BOX(226, 1), SILVER_SHULKER_BOX(227, 1), CYAN_SHULKER_BOX(228, 1), PURPLE_SHULKER_BOX(
			229, 1), BLUE_SHULKER_BOX(230, 1), BROWN_SHULKER_BOX(231, 1), GREEN_SHULKER_BOX(232, 1), RED_SHULKER_BOX(233, 1), BLACK_SHULKER_BOX(
			234, 1), WHITE_GLAZED_TERRACOTTA(235), ORANGE_GLAZED_TERRACOTTA(236), MAGENTA_GLAZED_TERRACOTTA(237), LIGHT_BLUE_GLAZED_TERRACOTTA(
			238), YELLOW_GLAZED_TERRACOTTA(239), LIME_GLAZED_TERRACOTTA(240), PINK_GLAZED_TERRACOTTA(241), GRAY_GLAZED_TERRACOTTA(
			242), SILVER_GLAZED_TERRACOTTA(243), CYAN_GLAZED_TERRACOTTA(244), PURPLE_GLAZED_TERRACOTTA(245), BLUE_GLAZED_TERRACOTTA(
			246), BROWN_GLAZED_TERRACOTTA(247), GREEN_GLAZED_TERRACOTTA(248), RED_GLAZED_TERRACOTTA(249), BLACK_GLAZED_TERRACOTTA(
			250), CONCRETE(251), CONCRETE_POWDER(252), STRUCTURE_BLOCK(255),
	// ----- Item Separator -----
	IRON_SPADE(256, 1, 250), IRON_PICKAXE(257, 1, 250), IRON_AXE(258, 1, 250), FLINT_AND_STEEL(259, 1, 64), APPLE(260), BOW(
			261, 1, 384), ARROW(262), COAL(263), DIAMOND(264), IRON_INGOT(265), GOLD_INGOT(266), IRON_SWORD(267, 1, 250), WOOD_SWORD(
			268, 1, 59), WOOD_SPADE(269, 1, 59), WOOD_PICKAXE(270, 1, 59), WOOD_AXE(271, 1, 59), STONE_SWORD(272, 1, 131), STONE_SPADE(
			273, 1, 131), STONE_PICKAXE(274, 1, 131), STONE_AXE(275, 1, 131), DIAMOND_SWORD(276, 1, 1561), DIAMOND_SPADE(277,
			1, 1561), DIAMOND_PICKAXE(278, 1, 1561), DIAMOND_AXE(279, 1, 1561), STICK(280), BOWL(281), MUSHROOM_SOUP(282, 1), GOLD_SWORD(
			283, 1, 32), GOLD_SPADE(284, 1, 32), GOLD_PICKAXE(285, 1, 32), GOLD_AXE(286, 1, 32), STRING(287), FEATHER(288), SULPHUR(
			289), WOOD_HOE(290, 1, 59), STONE_HOE(291, 1, 131), IRON_HOE(292, 1, 250), DIAMOND_HOE(293, 1, 1561), GOLD_HOE(
			294, 1, 32), SEEDS(295), WHEAT(296), BREAD(297), LEATHER_HELMET(298, 1, 55), LEATHER_CHESTPLATE(299, 1, 80), LEATHER_LEGGINGS(
			300, 1, 75), LEATHER_BOOTS(301, 1, 65), CHAINMAIL_HELMET(302, 1, 165), CHAINMAIL_CHESTPLATE(303, 1, 240), CHAINMAIL_LEGGINGS(
			304, 1, 225), CHAINMAIL_BOOTS(305, 1, 195), IRON_HELMET(306, 1, 165), IRON_CHESTPLATE(307, 1, 240), IRON_LEGGINGS(
			308, 1, 225), IRON_BOOTS(309, 1, 195), DIAMOND_HELMET(310, 1, 363), DIAMOND_CHESTPLATE(311, 1, 528), DIAMOND_LEGGINGS(
			312, 1, 495), DIAMOND_BOOTS(313, 1, 429), GOLD_HELMET(314, 1, 77), GOLD_CHESTPLATE(315, 1, 112), GOLD_LEGGINGS(
			316, 1, 105), GOLD_BOOTS(317, 1, 91), FLINT(318), PORK(319), GRILLED_PORK(320), PAINTING(321), GOLDEN_APPLE(322), SIGN(
			323, 16), WOOD_DOOR(324, 64), BUCKET(325, 16), WATER_BUCKET(326, 1), LAVA_BUCKET(327, 1), MINECART(328, 1), SADDLE(
			329, 1), IRON_DOOR(330, 64), REDSTONE(331), SNOW_BALL(332, 16), BOAT(333, 1), LEATHER(334), MILK_BUCKET(335, 1), CLAY_BRICK(
			336), CLAY_BALL(337), SUGAR_CANE(338), PAPER(339), BOOK(340), SLIME_BALL(341), STORAGE_MINECART(342, 1), POWERED_MINECART(
			343, 1), EGG(344, 16), COMPASS(345), FISHING_ROD(346, 1, 64), WATCH(347), GLOWSTONE_DUST(348), RAW_FISH(349), COOKED_FISH(
			350), INK_SACK(351), BONE(352), SUGAR(353), CAKE(354, 1), BED(355, 1), DIODE(356), COOKIE(357), MAP(358), SHEARS(
			359, 1, 238), MELON(360), PUMPKIN_SEEDS(361), MELON_SEEDS(362), RAW_BEEF(363), COOKED_BEEF(364), RAW_CHICKEN(365), COOKED_CHICKEN(
			366), ROTTEN_FLESH(367), ENDER_PEARL(368, 16), BLAZE_ROD(369), GHAST_TEAR(370), GOLD_NUGGET(371), NETHER_STALK(
			372), POTION(373, 1), GLASS_BOTTLE(374), SPIDER_EYE(375), FERMENTED_SPIDER_EYE(376), BLAZE_POWDER(377), MAGMA_CREAM(
			378), BREWING_STAND_ITEM(379), CAULDRON_ITEM(380), EYE_OF_ENDER(381), SPECKLED_MELON(382), MONSTER_EGG(383, 64), EXP_BOTTLE(
			384, 64), FIREBALL(385, 64), BOOK_AND_QUILL(386, 1), WRITTEN_BOOK(387, 16), EMERALD(388, 64), ITEM_FRAME(389), FLOWER_POT_ITEM(
			390), CARROT_ITEM(391), POTATO_ITEM(392), BAKED_POTATO(393), POISONOUS_POTATO(394), EMPTY_MAP(395), GOLDEN_CARROT(
			396), SKULL_ITEM(397), CARROT_STICK(398, 1, 25), NETHER_STAR(399), PUMPKIN_PIE(400), FIREWORK(401), FIREWORK_CHARGE(
			402), ENCHANTED_BOOK(403, 1), REDSTONE_COMPARATOR(404), NETHER_BRICK_ITEM(405), QUARTZ(406), EXPLOSIVE_MINECART(
			407, 1), HOPPER_MINECART(408, 1), PRISMARINE_SHARD(409), PRISMARINE_CRYSTALS(410), RABBIT(411), COOKED_RABBIT(412), RABBIT_STEW(
			413, 1), RABBIT_FOOT(414), RABBIT_HIDE(415), ARMOR_STAND(416, 16), IRON_BARDING(417, 1), GOLD_BARDING(418, 1), DIAMOND_BARDING(
			419, 1), LEASH(420), NAME_TAG(421), COMMAND_MINECART(422, 1), MUTTON(423), COOKED_MUTTON(424), BANNER(425, 16), END_CRYSTAL(
			426), SPRUCE_DOOR_ITEM(427), BIRCH_DOOR_ITEM(428), JUNGLE_DOOR_ITEM(429), ACACIA_DOOR_ITEM(430), DARK_OAK_DOOR_ITEM(
			431), CHORUS_FRUIT(432), CHORUS_FRUIT_POPPED(433), BEETROOT(434), BEETROOT_SEEDS(435), BEETROOT_SOUP(436, 1), DRAGONS_BREATH(
			437), SPLASH_POTION(438, 1), SPECTRAL_ARROW(439), TIPPED_ARROW(440), LINGERING_POTION(441, 1), SHIELD(442, 1, 336), ELYTRA(
			443, 1, 431), BOAT_SPRUCE(444, 1), BOAT_BIRCH(445, 1), BOAT_JUNGLE(446, 1), BOAT_ACACIA(447, 1), BOAT_DARK_OAK(
			448, 1), TOTEM(449, 1), SHULKER_SHELL(450), IRON_NUGGET(452), KNOWLEDGE_BOOK(453, 1), GOLD_RECORD(2256, 1), GREEN_RECORD(
			2257, 1), RECORD_3(2258, 1), RECORD_4(2259, 1), RECORD_5(2260, 1), RECORD_6(2261, 1), RECORD_7(2262, 1), RECORD_8(
			2263, 1), RECORD_9(2264, 1), RECORD_10(2265, 1), RECORD_11(2266, 1), RECORD_12(2267, 1), ;

	public static Material getByID(int id) {
		for (Material material : values()) {
			if (material.getID() == id) {
				return material;
			}
		}
		return Material.AIR;
	}

	private final int id;
	private final int maxStacking;
	private final short maxDurability;

	Material(int id) {
		this(id, 64);
	}

	Material(int id, int maxStacking) {
		this(id, maxStacking, 0);
	}

	Material(int id, int maxStacking, int durability) {
		this.id = (short) id;
		this.maxStacking = maxStacking;
		this.maxDurability = (short)durability;
	}

	public int getID() {
		return id;
	}

	public int getMaximumStackSize() {
		return maxStacking;
	}

	public short getMaximumDurability() {
		return maxDurability;
	}

	public boolean isBlock() {
		return this.id < 256;
	}

	public boolean isItem() {
		switch(this.id) {
			case 9:
			case 10:
			case 11:
			case 12:
			case 27:
			case 35:
			case 37:
			case 44:
			case 52:
			case 56:
			case 60:
			case 63:
			case 64:
			case 65:
			case 69:
			case 72:
			case 75:
			case 76:
			case 84:
			case 91:
			case 93:
			case 94:
			case 95:
			case 105:
			case 106:
			case 116:
			case 118:
			case 119:
			case 120:
			case 125:
			case 126:
			case 128:
			case 133:
			case 141:
			case 142:
			case 143:
			case 145:
			case 150:
			case 151:
			case 177:
			case 178:
			case 179:
			case 182:
			case 194:
			case 195:
			case 196:
			case 197:
			case 198:
			case 205:
			case 208:
			case 210:
			case 213:
				return false;
			default:
				return true;
		}
	}

	/**
	 * Checks if this Material is edible.
	 *
	 * @return true if this Material is edible.
	 */
	public boolean isEdible() {
		switch (this) {
			case BREAD:
			case CARROT_ITEM:
			case BAKED_POTATO:
			case POTATO_ITEM:
			case POISONOUS_POTATO:
			case GOLDEN_CARROT:
			case PUMPKIN_PIE:
			case COOKIE:
			case MELON:
			case MUSHROOM_SOUP:
			case RAW_CHICKEN:
			case COOKED_CHICKEN:
			case RAW_BEEF:
			case COOKED_BEEF:
			case RAW_FISH:
			case COOKED_FISH:
			case PORK:
			case GRILLED_PORK:
			case APPLE:
			case GOLDEN_APPLE:
			case ROTTEN_FLESH:
			case SPIDER_EYE:
			case RABBIT:
			case COOKED_RABBIT:
			case RABBIT_STEW:
			case MUTTON:
			case COOKED_MUTTON:
			case BEETROOT:
			case CHORUS_FRUIT:
			case BEETROOT_SOUP:
				return true;
			default:
				return false;
		}
	}

	public boolean isSolid() {
		if (this.isBlock() && this.id != 0) {
			switch(this.id) {
				case 2:
				case 3:
				case 4:
				case 5:
				case 6:
				case 8:
				case 13:
				case 14:
				case 15:
				case 16:
				case 17:
				case 18:
				case 19:
				case 20:
				case 21:
				case 22:
				case 23:
				case 24:
				case 25:
				case 26:
				case 27:
				case 30:
				case 34:
				case 35:
				case 36:
				case 37:
				case 42:
				case 43:
				case 44:
				case 45:
				case 46:
				case 47:
				case 48:
				case 49:
				case 50:
				case 53:
				case 54:
				case 55:
				case 57:
				case 58:
				case 59:
				case 61:
				case 62:
				case 63:
				case 64:
				case 65:
				case 68:
				case 69:
				case 71:
				case 72:
				case 73:
				case 74:
				case 75:
				case 80:
				case 81:
				case 82:
				case 83:
				case 85:
				case 86:
				case 87:
				case 88:
				case 89:
				case 90:
				case 92:
				case 93:
				case 96:
				case 97:
				case 98:
				case 99:
				case 100:
				case 101:
				case 102:
				case 103:
				case 104:
				case 108:
				case 109:
				case 110:
				case 111:
				case 113:
				case 114:
				case 115:
				case 117:
				case 118:
				case 119:
				case 121:
				case 122:
				case 123:
				case 124:
				case 125:
				case 126:
				case 127:
				case 129:
				case 130:
				case 131:
				case 134:
				case 135:
				case 136:
				case 137:
				case 138:
				case 139:
				case 140:
				case 146:
				case 147:
				case 148:
				case 149:
				case 152:
				case 153:
				case 154:
				case 155:
				case 156:
				case 157:
				case 159:
				case 160:
				case 161:
				case 162:
				case 163:
				case 164:
				case 165:
				case 166:
				case 167:
				case 168:
				case 169:
				case 170:
				case 171:
				case 173:
				case 174:
				case 175:
				case 177:
				case 178:
				case 179:
				case 180:
				case 181:
				case 182:
				case 183:
				case 184:
				case 185:
				case 186:
				case 187:
				case 188:
				case 189:
				case 190:
				case 191:
				case 192:
				case 193:
				case 194:
				case 195:
				case 196:
				case 197:
				case 198:
				case 202:
				case 203:
				case 204:
				case 205:
				case 206:
				case 207:
				case 209:
				case 211:
				case 212:
				case 213:
				case 214:
				case 215:
				case 216:
				case 217:
				case 219:
				case 220:
				case 221:
				case 222:
				case 223:
				case 224:
				case 225:
				case 226:
				case 227:
				case 228:
				case 229:
				case 230:
				case 231:
				case 232:
				case 233:
				case 234:
				case 235:
				case 236:
				case 237:
				case 238:
				case 239:
				case 240:
				case 241:
				case 242:
				case 243:
				case 244:
				case 245:
				case 246:
				case 247:
				case 248:
				case 249:
				case 250:
				case 251:
				case 252:
				case 253:
				case 254:
					return true;
				case 7:
				case 9:
				case 10:
				case 11:
				case 12:
				case 28:
				case 29:
				case 31:
				case 32:
				case 33:
				case 38:
				case 39:
				case 40:
				case 41:
				case 51:
				case 52:
				case 56:
				case 60:
				case 66:
				case 67:
				case 70:
				case 76:
				case 77:
				case 78:
				case 79:
				case 84:
				case 91:
				case 94:
				case 95:
				case 105:
				case 106:
				case 107:
				case 112:
				case 116:
				case 120:
				case 128:
				case 132:
				case 133:
				case 141:
				case 142:
				case 143:
				case 144:
				case 145:
				case 150:
				case 151:
				case 158:
				case 172:
				case 176:
				case 199:
				case 200:
				case 201:
				case 208:
				case 210:
				case 218:
				default:
					return false;
			}
		} else {
			return false;
		}
	}

	public boolean isTransparent() {
		if (!this.isBlock()) {
			return false;
		} else {
			switch(this.id) {
				case 1:
				case 7:
				case 28:
				case 29:
				case 32:
				case 33:
				case 38:
				case 39:
				case 40:
				case 41:
				case 51:
				case 52:
				case 56:
				case 60:
				case 66:
				case 67:
				case 70:
				case 76:
				case 77:
				case 78:
				case 79:
				case 84:
				case 91:
				case 94:
				case 95:
				case 105:
				case 106:
				case 107:
				case 112:
				case 116:
				case 120:
				case 128:
				case 132:
				case 133:
				case 141:
				case 142:
				case 143:
				case 144:
				case 145:
				case 150:
				case 151:
				case 158:
				case 172:
				case 176:
				case 199:
				case 200:
				case 201:
				case 208:
				case 210:
				case 218:
					return true;
				default:
					return false;
			}
		}
	}

	public boolean isFuel() {
		switch(this.id) {
			case 6:
			case 7:
			case 18:
			case 26:
			case 36:
			case 48:
			case 54:
			case 55:
			case 59:
			case 66:
			case 73:
			case 85:
			case 86:
			case 97:
			case 100:
			case 101:
			case 108:
			case 127:
			case 135:
			case 136:
			case 137:
			case 144:
			case 147:
			case 152:
			case 163:
			case 164:
			case 165:
			case 172:
			case 174:
			case 184:
			case 185:
			case 186:
			case 187:
			case 188:
			case 189:
			case 190:
			case 191:
			case 192:
			case 193:
			case 260:
			case 262:
			case 267:
			case 268:
			case 269:
			case 270:
			case 279:
			case 280:
			case 289:
			case 322:
			case 323:
			case 326:
			case 332:
			case 345:
			case 368:
			case 424:
			case 426:
			case 427:
			case 428:
			case 429:
			case 430:
			case 443:
			case 444:
			case 445:
			case 446:
			case 447:
				return true;
			default:
				return false;
		}
	}

	public boolean isOpaque() {
		if (!this.isBlock()) {
			return false;
		} else {
			switch(this.id) {
				case 2:
				case 3:
				case 4:
				case 5:
				case 6:
				case 8:
				case 13:
				case 14:
				case 15:
				case 16:
				case 17:
				case 18:
				case 20:
				case 22:
				case 23:
				case 24:
				case 25:
				case 26:
				case 36:
				case 42:
				case 43:
				case 44:
				case 46:
				case 48:
				case 49:
				case 50:
				case 53:
				case 57:
				case 58:
				case 59:
				case 62:
				case 63:
				case 74:
				case 75:
				case 81:
				case 83:
				case 85:
				case 87:
				case 88:
				case 89:
				case 92:
				case 98:
				case 99:
				case 100:
				case 101:
				case 104:
				case 111:
				case 113:
				case 122:
				case 124:
				case 125:
				case 126:
				case 130:
				case 134:
				case 138:
				case 154:
				case 156:
				case 159:
				case 160:
				case 163:
				case 166:
				case 167:
				case 169:
				case 171:
				case 173:
				case 174:
				case 175:
				case 180:
				case 182:
				case 202:
				case 203:
				case 205:
				case 207:
				case 211:
				case 212:
				case 214:
				case 215:
				case 216:
				case 217:
				case 236:
				case 237:
				case 238:
				case 239:
				case 240:
				case 241:
				case 242:
				case 243:
				case 244:
				case 245:
				case 246:
				case 247:
				case 248:
				case 249:
				case 250:
				case 251:
				case 252:
				case 253:
				case 254:
					return true;
				default:
					return false;
			}
		}
	}

}
