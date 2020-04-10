package com.github.maxopoly.angeliacore.actions.actions.inventory;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.model.inventory.PlayerInventory;
import com.github.maxopoly.angeliacore.model.item.ItemStack;
import com.github.maxopoly.angeliacore.model.item.Material;

public class MoveItem extends InventoryAction {

	private int originSlot;
	private int targetSlot;
	private byte windowID;
	private boolean done;
	private ClickInventory pickUp;
	private ClickInventory layDown;
	private ClickInventory revert;
	private ItemStack toMove;

	public MoveItem(ServerConnection connection, byte windowID, int originSlot, int targetSlot) {
		super(connection);
		this.windowID = windowID;
		this.originSlot = originSlot;
		this.targetSlot = targetSlot;
	}

	@Override
	public void execute() {
		if (done) {
			return;
		}
		if (pickUp == null) {
			// nothing was done so far, so we create the actions and begin the pickUp;
			toMove = connection.getPlayerStatus().getInventory(windowID).getSlot(originSlot);
			if (toMove.isEmpty()) {
				done = true;
				successful = false;
				return;
			}
			this.pickUp = new ClickInventory(connection, windowID, (short) originSlot, (byte) 0, 0, toMove);
			ItemStack target = connection.getPlayerStatus().getInventory(windowID).getSlot(targetSlot);
			if (!target.isEmpty()) {
				done = true;
				successful = false;
				return;
			}
			this.layDown = new ClickInventory(connection, windowID, (short) targetSlot, (byte) 0, 0,
					new ItemStack(Material.EMPTY_SLOT));
			this.pickUp.execute();
		}
		if (!pickUp.isDone()) {
			// still working on pickup
			pickUp.execute();
			return;
		}
		if (!pickUp.wasSuccessfull()) {
			// pickup failed
			done = true;
			successful = false;
			return;
		}
		if (!layDown.isDone()) {
			// still working on laydown
			layDown.execute();
			return;
		}
		if (!layDown.wasSuccessfull()) {
			if (revert == null) {
				// couldnt put it back down, lets put the item back where we got it from
				revert = new ClickInventory(connection, windowID, (short) originSlot, (byte) 0, 0,
						new ItemStack(Material.EMPTY_SLOT));
			}
			if (revert.isDone()) {
				if (!revert.wasSuccessfull()) {
					// we couldnt put the item back where it was from
					// TODO error handling
				}
				done = true;
				successful = false;
			} else {
				revert.execute();
			}
			return;
		}
		// laydown was successfull, everything worked
		done = true;
		successful = true;
		// update clientside model
		PlayerInventory inv = connection.getPlayerStatus().getPlayerInventory();
		inv.updateSlot(originSlot, new ItemStack(Material.EMPTY_SLOT));
		inv.updateSlot(targetSlot, toMove);
	}

	@Override
	public boolean isDone() {
		return done;
	}

}
