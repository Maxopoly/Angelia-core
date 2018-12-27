package com.github.maxopoly.angeliacore.model.chat.hover;

/**
 * Upon hovering components with this hover action, an entity description is shown
 * 
 * Not fully implemented yet, does not parse the entity out, but only offers its serialized version
 *
 */
public class ShowEntityHoverEvent extends HoverEvent {
	
	private String enttiySerialized;
	
	public ShowEntityHoverEvent(String enttiySerialized) {
		super(Action.SHOW_ENTITY);
		this.enttiySerialized = enttiySerialized;
	}
	
	/**
	 * @return Serialized JSON/NBT mix of the entity to display
	 */
	public String getEntitySerialized() {
		return enttiySerialized;
	}
}
