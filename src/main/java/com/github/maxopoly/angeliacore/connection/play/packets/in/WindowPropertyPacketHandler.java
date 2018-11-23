package com.github.maxopoly.angeliacore.connection.play.packets.in;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.libs.packetEncoding.EndOfPacketException;
import com.github.maxopoly.angeliacore.libs.packetEncoding.ReadOnlyPacket;
import com.github.maxopoly.angeliacore.model.inventory.AnvilInventory;
import com.github.maxopoly.angeliacore.model.inventory.BeaconInventory;
import com.github.maxopoly.angeliacore.model.inventory.BrewingStandInventory;
import com.github.maxopoly.angeliacore.model.inventory.EnchantmentTableInventory;
import com.github.maxopoly.angeliacore.model.inventory.FurnaceInventory;
import com.github.maxopoly.angeliacore.model.inventory.EnchantmentTableInventory.EnchantingSlot;
import com.github.maxopoly.angeliacore.model.inventory.Inventory;
import com.github.maxopoly.angeliacore.model.item.Enchantment;
import com.github.maxopoly.angeliacore.model.potion.PotionType;

public class WindowPropertyPacketHandler extends AbstractIncomingPacketHandler {

	public WindowPropertyPacketHandler(ServerConnection connection) {
		super(connection, 0x15);
	}

	@Override
	public void handlePacket(ReadOnlyPacket packet) {
		try {
			byte windowID = packet.readByte();
			Inventory inv = connection.getPlayerStatus().getInventory(windowID);
			if (inv == null) {
				return;
			}
			short property = packet.readShort();
			short value = packet.readShort();
			// forgive me gods of polymorphism, but this is neccessary to contain the packet
			// handling mess here
			if (inv instanceof EnchantmentTableInventory) {
				updateEnchantmentTable((EnchantmentTableInventory) inv, property, value);
				return;
			}
			if (inv instanceof AnvilInventory) {
				updateAnvil((AnvilInventory) inv, property, value);
				return;
			}
			if (inv instanceof FurnaceInventory) {
				updateFurnace((FurnaceInventory) inv, property, value);
				return;
			}
			if (inv instanceof BeaconInventory) {
				updateBeacon((BeaconInventory) inv, property, value);
				return;
			}
			if (inv instanceof BrewingStandInventory) {
				updateBrewingStand((BrewingStandInventory) inv, property, value);
				return;
			}

		} catch (EndOfPacketException e) {
			connection.getLogger().error("Failed to parse window property packet", e);
		}
	}
	
	private void updateBrewingStand(BrewingStandInventory inv, short property, short value) {
		if (property < 0 || property > 1) {
			throw new IllegalArgumentException("Property " + property + " is not known for brewing stands");
		}
		switch(property) {
		case 0:
			inv.setBrewingTime(value);
			break;
		case 1:
			inv.setFuelTime(value);
		}
	}
	
	private void updateBeacon(BeaconInventory inv, short property, short value) {
		if (property < 0 || property > 2) {
			throw new IllegalArgumentException("Property " + property + " is not known for beacons");
		}
		switch(property) {
		case 0:
			inv.setPowerLevel(value);
			break;
		case 1:
			inv.setPrimaryEffect(PotionType.getById(value));
			break;
		case 2:
			inv.setSecondaryEffect(PotionType.getById(value));
		}
	}

	private void updateFurnace(FurnaceInventory inv, short property, short value) {
		if (property < 0 || property > 3) {
			throw new IllegalArgumentException("Property " + property + " is not known for furnaces");
		}
		switch(property) {
		case 0:
			inv.setFuelTicksLeft(value);
			break;
		case 1:
			inv.setMaximumFuelBurnTime(value);
			break;
		case 2:
			inv.setSmeltingProgress(value);
			break;
		case 3:
			inv.setMaximumSmeltingProgress(value);
		}
	}

	private void updateAnvil(AnvilInventory inv, short property, short value) {
		if (property != 0) {
			throw new IllegalArgumentException("Property " + property + " is not known for anvils");
		}
		inv.setRepairCost(value);
	}

	private void updateEnchantmentTable(EnchantmentTableInventory inv, short property, short value) {
		if (property < 0 || property > 9) {
			throw new IllegalArgumentException("Property " + property + " is not known for enchantment tables");
		}
		if (property == 3) {
			inv.setEnchantingSeed(value);
			return;
		}
		EnchantingSlot slot = EnchantingSlot.parse(property);
		if (property < 3) {
			inv.setEnchantingCost(slot, value);
			return;
		}
		if (property >= 7) {
			inv.setPreviewEnchantLevel(slot, value);
			return;
		}
		Enchantment enchant = Enchantment.fromID(value);
		inv.setPreviewEnchant(slot, enchant);
	}

}
