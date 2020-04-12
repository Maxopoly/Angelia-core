package com.github.maxopoly.angeliacore.model.world;

public class WorldStatusHandler {

	private int dimension;
	private byte difficulty;
	private byte maxPlayers;
	private String lvlType;
	private boolean debugInfo;
	private long worldAge;
	private long timeOfDay;

	public WorldStatusHandler() {
		// indicate that the values are uninitialized
		this.dimension = -100;
		this.difficulty = -100;
		this.maxPlayers = -100;
	}

	/**
	 * Updates the world time will calculate the time of day by taking the world age
	 * modulo 24000. World age is in seconds, time of day in ticks
	 * 
	 * @param worldAge Age of the world in seconds
	 * @param timeOfDay Time of the day in ticks, usually on a scale from 0 to 23999
	 */
	public void updateWorldTime(long worldAge, long timeOfDay) {
		this.worldAge = worldAge;
		this.timeOfDay = timeOfDay;
	}

	/**
	 * Right after joining a server it will once provide all of this information to
	 * us
	 * 
	 * @param dimension  -1 = nether, 0 = overworld, 1 = end
	 * @param difficulty 0 = peaceful, 1 = easy, 2 = normal, 3 = hard
	 * @param maxPlayers Player amount once used for drawing tab lists, not actually
	 *                   used for anything in a Notchian client
	 * @param lvlType    generation type, examples include: default, flat,
	 *                   largeBiomes, amplified, default_1_1
	 * @param debugInfo  If true, a Notchian client shows reduced information on the
	 *                   debug screen
	 */
	public void updateOnGameJoin(int dimension, byte difficulty, byte maxPlayers, String lvlType, boolean debugInfo) {
		this.dimension = dimension;
		this.difficulty = difficulty;
		this.maxPlayers = maxPlayers;
		this.lvlType = lvlType;
		this.debugInfo = debugInfo;
	}
	
	/**
	 * Time of day is measured in ticks on a scale from 0 to 23999
	 * @return Time of day in ticks
	 */
	public long getTimeOfDay() {
		return timeOfDay;
	}
	
	/**
	 * World age is measured in seconds, servers increment it by one every 20 ticks
	 * @return World age in seconds
	 */
	public long getWorldAge() {
		return worldAge;
	}

	/**
	 * -1 = nether, 0 = overworld, 1 = end in vanilla. May be anything else on
	 * custom servers
	 * 
	 * @return Dimension id of the world the player is in
	 */
	public int getDimension() {
		return dimension;
	}

	/**
	 * 0 = peaceful, 1 = easy, 2 = normal, 3 = hard
	 * 
	 * @return Current difficulty of the server
	 */
	public byte getDifficulty() {
		return difficulty;
	}

	/**
	 * generation type of the world, examples include: default, flat, largeBiomes,
	 * amplified, default_1_1
	 * 
	 * @return Descriptor of the generation type of the world
	 */
	public String getLevelType() {
		return lvlType;
	}

	/**
	 * Was once used by Notchian clients to draw the player list, but now is ignored
	 * 
	 * @return Legacy number for properly drawing tab lists
	 */
	public byte getMaxPlayers() {
		return maxPlayers;
	}

	/**
	 * If true, a Notchian client shows reduced information on the debug screen.
	 * 
	 * @return Does the server want players to see additional F3 debug info
	 */
	public boolean showDebugInfo() {
		return debugInfo;
	}

}
