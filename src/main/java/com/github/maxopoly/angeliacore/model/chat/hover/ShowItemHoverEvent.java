package com.github.maxopoly.angeliacore.model.chat.hover;

/**
 * Upon hovering components with this hover action, an item is shown
 * 
 * Not fully implemented yet, does not parse the item out, but only offers its serialized version
 *
 */
public class ShowItemHoverEvent extends HoverEvent {
	
	private String itemSerialized;
	
	public ShowItemHoverEvent(String itemSerialized) {
		super(Action.SHOW_ITEM);
		this.itemSerialized = itemSerialized;
	}
	
	/**
	 * @return Serialized JSON/NBT mix of the item to display
	 */
	public String getItemSerialized() {
		return itemSerialized;
	}
}
