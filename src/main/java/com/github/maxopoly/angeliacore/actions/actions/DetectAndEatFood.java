package com.github.maxopoly.angeliacore.actions.actions;

import com.github.maxopoly.angeliacore.actions.AbstractAction;
import com.github.maxopoly.angeliacore.actions.ActionLock;
import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.packets.out.HeldItemChangePacket;
import com.github.maxopoly.angeliacore.connection.play.packets.out.UseItemPacket;
import com.github.maxopoly.angeliacore.model.inventory.Inventory;
import com.github.maxopoly.angeliacore.model.inventory.PlayerInventory;
import com.github.maxopoly.angeliacore.model.item.Hand;
import com.github.maxopoly.angeliacore.model.item.ItemStack;
import java.io.IOException;

public class DetectAndEatFood extends AbstractAction {

	private Hand foodHand;
	private boolean done;
	private int eatingTicksLeft;
	private boolean foundFood;

	public DetectAndEatFood(ServerConnection connection) {
		super(connection);
		this.eatingTicksLeft = -10;
		this.done = false;
		this.foundFood = true;
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
					foundFood = false;
					done = true;
					return;
				}
			}
			eatingTicksLeft = (int) connection.getTicksPerSecond() / 20 * 32;
			try {
				UseItemPacket eatingStarted = new UseItemPacket(foodHand);
				connection.sendPacket(eatingStarted);
			} catch (IOException e) {
				connection.getLogger().error("Failed to send eating start packet", e);
			}
		} else {
			if (eatingTicksLeft-- <= 0) {
				done = true;
			}
		}
	}

	@Override
	public boolean isDone() {
		return done;
	}

	public boolean foundFood() {
		return foundFood;
	}

	@Override
	public ActionLock[] getActionLocks() {
		// movement is slower when eating, so let's stop moving when eating
		return new ActionLock[] { ActionLock.HOTBAR_SLOT, ActionLock.MOVEMENT };
	}

}
