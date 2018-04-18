package com.github.maxopoly.angeliacore.event.events;

/**
 * Called when the players experience level/progress is updated
 *
 */
public class XPChangeEvent implements AngeliaEvent {

	private final float oldProgress;
	private final int oldLevel;
	private final int oldTotalXP;
	private final float newProgress;
	private final int newLevel;
	private final int newTotalXP;

	public XPChangeEvent(float oldProgress, int oldLevel, int oldTotalXP,
			float newProgress, int newLevel, int newTotalXP) {
		this.oldProgress = oldProgress;
		this.oldLevel = oldLevel;
		this.oldTotalXP = oldTotalXP;
		this.newProgress = newProgress;
		this.newLevel = newLevel;
		this.newTotalXP = newTotalXP;
	}

	/**
	 * @return Progress to the next level in the range[0,1), before the update
	 *         causing this event
	 */
	public float getPreviousLevelProgress() {
		return oldProgress;
	}

	/**
	 * @return Progress to the next level in the range[0,1), after the update
	 *         causing this event
	 */
	public float getUpdatedLevelProgress() {
		return newProgress;
	}

	/**
	 * @return The players experience level, before the update causing this
	 *         event
	 */
	public int getPreviousLevel() {
		return oldLevel;
	}

	/**
	 * @return The players experience level, after the update causing this event
	 */
	public int getUpdatedLevel() {
		return newLevel;
	}

	/**
	 * @return The players total experience points, before the update causing
	 *         this event
	 */
	public int getPreviousTotalXP() {
		return oldTotalXP;
	}

	/**
	 * @return The players total experience points, after the update causing
	 *         this event
	 */
	public int getUpdatedTotalXP() {
		return newTotalXP;
	}

	/**
	 * Calculates how much XP the player gained from this change. Result may be
	 * negative if XP was lost
	 * 
	 * @return Total XP gained
	 */
	public int getTotalXPGained() {
		return newTotalXP - oldTotalXP;
	}

	/**
	 * @return Whether the players experience level (level, not just total xp)
	 *         changed because of this update
	 */
	public boolean hasLevelChanged() {
		return oldLevel != newLevel;
	}

}
