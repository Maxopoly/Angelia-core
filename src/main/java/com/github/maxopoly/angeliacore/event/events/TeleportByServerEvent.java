package com.github.maxopoly.angeliacore.event.events;

import com.github.maxopoly.angeliacore.model.Location;

/**
 * Called when the player is teleported by the player. This is useful to dermine when the player is stuck running
 * against a block, as he will be teleported back.
 *
 */
public class TeleportByServerEvent implements AngeliaEvent {

	private Location previousClientsideLocation;
	private Location serverDictatedLocation;

	public TeleportByServerEvent(Location previousClientsideLocation, Location serverDictatedLocation) {
		this.previousClientsideLocation = previousClientsideLocation;
		this.serverDictatedLocation = serverDictatedLocation;
	}

	/**
	 * @return Where the client thought it was before it got teleported
	 */
	public Location getPreviousClientSideLocation() {
		return previousClientsideLocation;
	}

	/**
	 * @return Location teleported (back) to
	 */
	public Location getLocationTeleportedTo() {
		return serverDictatedLocation;
	}

}
