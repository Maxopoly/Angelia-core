package com.github.maxopoly.angeliacore.actions;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.model.ItemStack;

public class ItemMoveAction extends AbstractAction {

	private int originSlot;
	private int targetSlot;
	private byte windowID;
	private boolean done;
	private boolean successfull;
	private InventoryClickAction pickUp;
	private InventoryClickAction layDown;
	private InventoryClickAction revert;

	public ItemMoveAction(ServerConnection connection, byte windowID, int originSlot, int targetSlot) {
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
			ItemStack toMove = connection.getPlayerStatus().getInventory(windowID).getSlot(originSlot);
			if (toMove.isEmpty()) {
				done = true;
				successfull = false;
				return;
			}
			this.pickUp = new InventoryClickAction(connection, windowID, (short) originSlot, (byte) 0, 0, toMove);
			ItemStack target = connection.getPlayerStatus().getInventory(windowID).getSlot(originSlot);
			if (!target.isEmpty()) {
				done = true;
				successfull = false;
				return;
			}
			this.layDown = new InventoryClickAction(connection, windowID, (short) targetSlot, (byte) 0, 0, new ItemStack(
					(short) -1));
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
			successfull = false;
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
				revert = new InventoryClickAction(connection, windowID, (short) originSlot, (byte) 0, 0, new ItemStack(
						(short) -1));
			}
			if (revert.isDone()) {
				if (!revert.wasSuccessfull()) {
					// we couldnt put the item back where it was from
					// TODO error handling
				}
				done = true;
				successfull = false;
			} else {
				revert.execute();
			}
			return;
		}
		// laydown was successfull, everything worked
		done = true;
		successfull = true;

	}

	/**
	 * An item transaction may be denied by the server or simply not work because the slot was empty. This method can be
	 * used afterwards to determine whether everything worked. Note that this will always return false while the
	 * transaction is unfinished.
	 * 
	 * @return True only if the item was successfully moved, false otherwise
	 */
	public boolean wasSuccessfull() {
		return successfull;
	}

	@Override
	public boolean isDone() {
		return done;
	}

}
