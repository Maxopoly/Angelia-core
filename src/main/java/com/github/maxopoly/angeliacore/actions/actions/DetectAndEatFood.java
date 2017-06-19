package com.github.maxopoly.angeliacore.actions.actions;

import com.github.maxopoly.angeliacore.actions.AbstractAction;
import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.packets.out.HeldItemChangePacket;
import com.github.maxopoly.angeliacore.connection.play.packets.out.PlayerDiggingPacket;
import com.github.maxopoly.angeliacore.connection.play.packets.out.UseItemPacket;
import com.github.maxopoly.angeliacore.model.BlockFace;
import com.github.maxopoly.angeliacore.model.Hand;
import com.github.maxopoly.angeliacore.model.ItemStack;
import com.github.maxopoly.angeliacore.model.Location;
import com.github.maxopoly.angeliacore.model.inventory.Inventory;
import com.github.maxopoly.angeliacore.model.inventory.PlayerInventory;
import java.io.IOException;

public class DetectAndEatFood extends AbstractAction {

	private Hand foodHand;
	private boolean done;
	private int eatingTicksLeft;

	public DetectAndEatFood(ServerConnection connection) {
		super(connection);
		this.eatingTicksLeft = -10;
		this.done = false;
	}

	@Override
	public void execute() {
		if (this.eatingTicksLeft == -10) {
			// first time this is run, so search for right slot
			PlayerInventory inv = connection.getPlayerStatus().getPlayerInventory();
			ItemStack offHand = inv.getOffHand();
			if (offHand != null && offHand.getMaterial().isEdible()) {
				foodHand = Hand.OFFHAND;
			}
			if (foodHand == null) {
				// search on hotbar
				Inventory hotbar = inv.getHotbar();
				int slot = -1;
				for (int i = 0; i < hotbar.getSize(); i++) {
					ItemStack is = hotbar.getSlot(i);
					if (is != null && is.getMaterial().isEdible()) {
						slot = i;
						break;
					}
				}
				if (slot != -1) {
					try {
						connection.sendPacket(new HeldItemChangePacket(slot));
					} catch (IOException e) {
						connection.getLogger().error("Failed to send slot selection packet", e);
						return;
					}
					foodHand = Hand.MAINHAND;
				} else {
					// no food found
					done = true;
					return;
				}
			}
			// 5 seconds should be good
			eatingTicksLeft = (int) connection.getTicksPerSecond() * 5;
		} else {
			try {
				UseItemPacket eatingStarted = new UseItemPacket(foodHand);
				connection.sendPacket(eatingStarted);
			} catch (IOException e) {
				connection.getLogger().error("Failed to send eating start packet", e);
			}
			if (eatingTicksLeft <= 1) {
				try {
					PlayerDiggingPacket finishedEating = new PlayerDiggingPacket(5, new Location(0, 0, 0), BlockFace.SPECIAL);
					connection.sendPacket(finishedEating);
				} catch (IOException e) {
					connection.getLogger().error("Failed to send eating stop packet", e);
				}
			}
			if (eatingTicksLeft-- <= -1) {
				done = true;
			}
		}
	}

	@Override
	public boolean isDone() {
		return done;
	}

}
