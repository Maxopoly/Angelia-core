package com.github.maxopoly.angeliacore.event.events.angelia;

import com.github.maxopoly.angeliacore.event.events.AngeliaEvent;
import com.github.maxopoly.angeliacore.model.location.Location;
import com.github.maxopoly.angeliacore.model.location.Vector;

/**
 * Called whenever the client has executed a physics tick, before sending out
 * the finished values to the server. Thus any listeners can modify the final
 * result
 *
 */
public class ClientPhysicsEvent implements AngeliaEvent {

	private Location startingLocation;
	private Location targetLocation;
	private Vector oldVelocity;
	private Vector newVelocity;
	private boolean onGroundBefore;
	private boolean onGroundNow;

	public ClientPhysicsEvent(Location startingLocation, Location targetLocation, Vector oldVelocity,
			Vector newVelocity, boolean onGroundBefore, boolean onGroundNow) {
		this.startingLocation = startingLocation;
		this.targetLocation = targetLocation;
		this.oldVelocity = oldVelocity;
		this.newVelocity = newVelocity;
		this.onGroundBefore = onGroundBefore;
		this.onGroundNow = onGroundNow;
	}
	
	/**
	 * @return The location the player was at before the physics update
	 */
	public Location getStartingLocation() {
		return startingLocation;
	}
	
	/**
	 * @return The location the player will be at after the physics update
	 */
	public Location getTargetLocation() {
		return targetLocation;
	}
	
	/**
	 * @return Velocity the player had before the physics update in m/s
	 */
	public Vector getOldVelocity() {
		return oldVelocity;
	}
	
	/**
	 * @return Velocity the player will have after the physics update in m/s
	 */
	public Vector getNewVelocity() {
		return newVelocity;
	}
	
	/**
	 * @return Whether the player was standing on solid ground before this update
	 */
	public boolean wasOnGroundBefore() {
		return onGroundBefore;
	}
	
	/**
	 * @return Whether the player is standing on solid ground after this update
	 */
	public boolean isOnGroundAfter() {
		return onGroundNow;
	}
}
