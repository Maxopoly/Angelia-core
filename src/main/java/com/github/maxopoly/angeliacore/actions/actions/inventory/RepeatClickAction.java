package com.github.maxopoly.angeliacore.actions.actions.inventory;

import com.github.maxopoly.angeliacore.model.item.ItemStack;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.model.inventory.Inventory;

public class RepeatClickAction extends InventoryAction {

	private byte windowID;
	private short slot;
	private byte button;
	private int mode;
	private ClickInventory currentClick;
	private int howOftenTotal;
	private int howOftenLeft;
	private boolean done;

	public RepeatClickAction(ServerConnection connection, byte windowID, short slot, byte button, int mode,
			ItemStack clickedSlot, int howOften) {
		super(connection);
		this.windowID = windowID;
		this.slot = slot;
		this.button = button;
		this.mode = mode;
		this.howOftenLeft = howOften;
		this.howOftenTotal = howOften;
	}

	@Override
	public void execute() {
		Inventory inv = connection.getPlayerStatus().getInventory(windowID);
		if (inv == null) {
			done = true;
			successfull = false;
		}
		if (currentClick != null) {
			if (currentClick.isDone()) {
				if (--howOftenLeft <= 0) {
					done = true;
					successfull = true;
					return;
				}
			} else {
				currentClick.execute();
				return;
			}
		}
		currentClick = new ClickInventory(connection, windowID, slot, button, mode, inv.getSlot(slot));
		currentClick.execute();
	}

	@Override
	public boolean isDone() {
		return done;
	}

}
