package com.github.maxopoly.angeliacore.gamelogic.physics;

import java.io.IOException;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.packets.out.PlayerPositionPacket;
import com.github.maxopoly.angeliacore.event.events.angelia.ClientPhysicsEvent;
import com.github.maxopoly.angeliacore.libs.yaml.config.GlobalConfig;
import com.github.maxopoly.angeliacore.model.ThePlayer;
import com.github.maxopoly.angeliacore.model.block.states.BlockState;
import com.github.maxopoly.angeliacore.model.entity.AABB;
import com.github.maxopoly.angeliacore.model.location.Location;
import com.github.maxopoly.angeliacore.model.location.Vector;

/**
 * Some inspiration for this class has been taken from mineflayers physics
 * implementation
 * https://github.com/PrismarineJS/mineflayer/blob/master/lib/plugins/physics.js
 *
 */
public class PlayerPhysicsManager {

	private double delta;
	private double gravity;
	private double terminalVelocity;
	private double walkingSpeed;
	private double sprintingMultiplier;
	private double playerHeight;
	private double drag;
	private boolean debug = false;

	private ThePlayer player;
	private ServerConnection connection;
	private Vector queuedMovement;

	public PlayerPhysicsManager(ServerConnection connection, ThePlayer player) {
		this.player = player;
		this.connection = connection;
		GlobalConfig config = connection.getConfig();
		this.delta = config.getPhysicsDelta();
		this.gravity = config.getGravity();
		this.terminalVelocity = config.getTerminalVelocity();
		this.walkingSpeed = config.getPhysicsWalkingSpeed();
		this.sprintingMultiplier = config.getPhysicsSprintingMultiplier();
		this.playerHeight = config.getPhysicsPlayerHeight();
		this.drag = config.getPhysicsDrag();
	}

	/**
	 * A single acceleration value may be queued, which is added to the players
	 * current velocity upon the next tick. After each physics tick this value is
	 * set to null This value is in m/s^2. Calls to this method will overwrite any
	 * preexisting value
	 * 
	 * @param acceleration New acceleration value to apply to player on next physics
	 *                     tick
	 */
	public void queueAcceleration(Vector acceleration) {
		this.queuedMovement = acceleration;
	}

	/**
	 * A single acceleration value may be queued, which is added to the players
	 * current velocity upon the next tick. After each physics tick this value is
	 * set to null This value is in m/s^2. Values passed through this method will be
	 * added to any preexisting acceleration
	 * 
	 * @param acceleration New acceleration value to apply to player on next physics
	 *                     tick
	 */
	public void addAcceleration(Vector acceleration) {
		if (this.queuedMovement == null) {
			this.queuedMovement = acceleration;
		} else {
			this.queuedMovement.add(acceleration);
		}
	}

	/**
	 * @return Acceleration applied to the player on next physics tick in m/s^2
	 */
	public Vector getQueuedAcceleration() {
		return queuedMovement;
	}

	/**
	 * Makes a physic tick for the player and sends appropriate packets out
	 */
	public void updateState() {
		tickPhysics();
		try {
			Location loc = player.getLocation();
			connection.sendPacket(new PlayerPositionPacket(loc.getX(), loc.getY(), loc.getZ(), player.isOnGround()));
		} catch (IOException e) {
			connection.getLogger().error("Failed to update location", e);
		}
	}

	/**
	 * Does a physic tick for the player. Applies gravity and queued movement while
	 * taking terminal velocities and friction into account to emulate normal
	 * minecraft physics
	 */
	private void tickPhysics() {
		if (player.getHealth() <= 0) {
			// don't move when dead
			physicsDebug("No physics applied, player is dead");
			return;
		}
		if (connection.getChunkHolder().getChunkFor(player.getLocation()) == null) {
			physicsDebug("Chunk the player is in was not loaded yet, no physics applied");
			return;
		}
		double tickInverse = 1.0 / connection.getTicksPerSecond();
		// in m/s
		Vector velocity = player.getVelocity();
		Vector startingVelocity = velocity;
		boolean onGroundBefore = player.isOnGround();
		// convert velocity to m/tick
		velocity.multiply(tickInverse);
		Vector acceleration = new Vector();

		Location playerLoc = player.getLocation();
		physicsDebug("Pre tick: vel(m/s): %s, vel(m/t): %s, loc: %s, ground: %b", startingVelocity, velocity, playerLoc,
				player.isOnGround());

		// converting gravity from meter/s^2 to meter/tick^2 required dividing by
		// tick/second twice
		double meterPerTickGravity = gravity * tickInverse * tickInverse;
		boolean onGround;

		// apply gravity
		onGround = standingOnBlock(playerLoc);

		if (onGround) {
			// remove y component if facing downwards, effectively stopping an ongoing fall
			if (velocity.getY() < 0) {
				velocity = velocity.add(0, -velocity.getY(), 0);
			}
		} else {
			// accelerate downwards
			acceleration = acceleration.add(0, -meterPerTickGravity, 0);
		}

		physicsDebug("Recalculated on ground: %b", onGround);

		// apply ground friction as inverse walking acceleration, even when midair
		// remove Y component
		Vector slowDownVec = new Vector(velocity.getX(), 0, velocity.getZ());
		double speedVectorLength = slowDownVec.getLength();
		slowDownVec = slowDownVec.getOpposite();
		slowDownVec = slowDownVec.normalize();
		double tickWalkingSpeed = walkingSpeed * tickInverse;
		double walkSpeedMultiplier = player.isSprinting() ? tickWalkingSpeed * sprintingMultiplier : tickWalkingSpeed;
		// prevent negative overshooting, so our friction doesn't accelerate us
		// backwards
		walkSpeedMultiplier = Math.min(speedVectorLength, walkSpeedMultiplier);
		slowDownVec = slowDownVec.multiply(walkSpeedMultiplier);
		velocity = velocity.add(slowDownVec);

		// movement generated clientside
		if (queuedMovement != null) {
			velocity = velocity.add(queuedMovement.multiply(tickInverse));
			physicsDebug("Adding queued velocity of %s (m/s) as %s (m/t) for a total velocity of %s", queuedMovement,
					queuedMovement.multiply(tickInverse), velocity);
			queuedMovement = null;
		}

		// acceleration and velocity are both in m/ticks
		velocity = velocity.add(acceleration);
		physicsDebug("Velocity post gravity is %s", velocity);

		// apply drag
		velocity = velocity.multiply(1.0 - drag);
		physicsDebug("Velocity post drag is %s", velocity);

		// bound terminal velocity, which is a negative number
		if (velocity.getY() < terminalVelocity) {
			velocity = new Vector(velocity.getX(), terminalVelocity, velocity.getZ());
		}
		physicsDebug("Velocity post terminal velocity is %s", velocity);

		// collision check with blocks at target location
		Location target = playerLoc.add(velocity);
		boolean wouldMoveIntoBlock = false;
		physicsDebug("Pre physics target loc is %s", target);
		if (velocity.getLength() > delta) {
			// only do collision checks if we actually moved
			double multiplier = 1.0;
			BlockState lowerTargetBlock = target.getBlockAt(connection);
			if (lowerTargetBlock != null && lowerTargetBlock.hasCollision()) {
				physicsDebug("Found initial collision with lower block %s at %s, attempting raytrace", lowerTargetBlock,
						target);
				// would move into block, so attempt to do a shorter step and find how far we
				// can go based on ray tracing
				AABB offsetLowerAABB = lowerTargetBlock.getBoundingBox().move(target.toBlockLocation());
				physicsDebug("Lower block AABB is %s", offsetLowerAABB);
				double distMultiplier = offsetLowerAABB.intersectRay(velocity, playerLoc);
				if (distMultiplier > 0) {
					multiplier = Math.min(multiplier, distMultiplier - delta);
					Location updatedTargetLoc = playerLoc.add(velocity.multiply(multiplier));
					physicsDebug("Found valid ray %f, setting movement multiplier to %f, updated target loc is %s",
							distMultiplier, multiplier, updatedTargetLoc);
					if (offsetLowerAABB.intersects(player.getBoundingBox().move(updatedTargetLoc))) {
						physicsDebug("Ray trace1 updated location was still in block, no movement possible");
						wouldMoveIntoBlock = true;
					} else {
						// recheck whether we're standing on a block now
						onGround = standingOnBlock(updatedTargetLoc);
						physicsDebug("Raytraced location1 was good, on ground check: %b", onGround);
					}
				} else {
					physicsDebug("Ray trace1 returned negative value, could not fix collision");
					wouldMoveIntoBlock = true;
				}
			}
			// only do second collision check if first one passed
			if (!wouldMoveIntoBlock) {
				Location upperTarget = target.add(0, playerHeight, 0);
				BlockState upperTargetBlock = upperTarget.getBlockAt(connection);
				if (upperTargetBlock != null && upperTargetBlock.hasCollision()) {
					physicsDebug("Found initial collision with upper block %s at %s, attempting raytrace",
							lowerTargetBlock, target);
					AABB offsetUpperAABB = upperTargetBlock.getBoundingBox().move(upperTarget.toBlockLocation());
					physicsDebug("Upper block AABB is %s", offsetUpperAABB);
					double distMultiplier = offsetUpperAABB.intersectRay(velocity, playerLoc);
					if (distMultiplier > 0) {
						multiplier = Math.min(multiplier, distMultiplier - delta);
						Location updatedTargetLoc = playerLoc.add(velocity.multiply(multiplier));
						physicsDebug("Found valid ray %f, setting movement multiplier to %f, updated target loc is %s",
								distMultiplier, multiplier, updatedTargetLoc);
						if (offsetUpperAABB.intersects(player.getBoundingBox().move(updatedTargetLoc))) {
							physicsDebug("Ray trace2 updated location was still in block, no movement possible");
							wouldMoveIntoBlock = true;
						} else {
							physicsDebug("Raytraced location2 was good");
						}
					} else {
						physicsDebug("Ray trace2 returned negative value, could not fix collision");
						wouldMoveIntoBlock = true;
					}
				}
			}
			if (multiplier < delta) {
				wouldMoveIntoBlock = true;
			} else {
				target = playerLoc.add(velocity.multiply(multiplier));
			}
		} else {
			physicsDebug("Velocity was less than our physics delta, no collision checks done");
		}
		Location result;
		if (wouldMoveIntoBlock) {
			result = playerLoc;
			// reset velocity, so it doesn't build up infinitely while running into a wall
			velocity = new Vector();
			physicsDebug("No movement applied because moving into a block could not be avoided");
		} else {
			physicsDebug("Found collision free target location %s", target.toString());
			result = target;
		}

		// revert velocity to m/s
		velocity.multiply(1 / tickInverse);

		connection.getEventHandler().broadcast(
				new ClientPhysicsEvent(playerLoc, result, startingVelocity, velocity, onGroundBefore, onGround));

		// write out finished values
		player.updateLocation(result);
		player.setVelocity(velocity);
		player.setOnGround(onGround);
		physicsDebug("Post tick: vel(m/s): %s, vel(m/t): %s, loc: %s, ground: %b", velocity.toString(),
				velocity.multiply(tickInverse).toString(), result.toString(), onGround);
		physicsDebug("-------");
	}

	private boolean standingOnBlock(Location playerLoc) {
		double playerAABBRange = connection.getConfig().getPlayerAABBRadius();
		AABB offsetPlayerAABB = player.getBoundingBox().move(new Vector(0, -delta, 0)).move(player.getLocation());
		physicsDebug("Offset player AABB is %s", offsetPlayerAABB);
		for (int i = 0; i < 4; i++) {
			// cycle through all 4 possible combinations
			double x = i < 2 ? playerAABBRange : -playerAABBRange;
			double z = i % 2 == 0 ? playerAABBRange : -playerAABBRange;
			Location offSetLoc = playerLoc.add(x, -delta, z);
			BlockState block = offSetLoc.getBlockAt(connection);
			if (block != null && block.hasCollision()) {
				AABB offsetBlockAABB = block.getOffsetBoundingBox(offSetLoc);
				physicsDebug("Checking gravity collision with offset block AABB %s", offsetBlockAABB);
				if (offsetBlockAABB.intersects(offsetPlayerAABB)) {
					physicsDebug("Found valid block to stand on at %s", offSetLoc.toBlockLocation());
					return true;
				}
			} else {
				physicsDebug("Did not stand on hard block %s at %s",block, offSetLoc.toBlockLocation());
			}
		}
		return false;
	}

	private void physicsDebug(String formatted, Object... msg) {
		if (debug) {
			if (msg.length == 0) {
				connection.getLogger().info("PhysicDebug: " + formatted);
			} else {
				connection.getLogger().info("PhysicDebug: " + String.format(formatted, msg));
			}
		}
	}

}
