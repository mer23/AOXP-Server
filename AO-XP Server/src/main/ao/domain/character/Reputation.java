package ao.domain.character;

public interface Reputation {
	
	Alignment getAlignment();
	
	/**
	 * Retrieves the bandit points.
	 * @return The reputation's bandit points.
	 */
	int getBandit();
	
	/**
	 * Adds the given points to the reputation's bandit points.
	 * @param points The points to add.
	 */
	void addToBandit(int points);
	
	/**
	 * Retrieves the assassin points.
	 * @return The reputation's assassin points.
	 */
	int getAssassin();
	
	/**
	 * Adds the given points to the reputation's assassin points.
	 * @param points The points to add.
	 */
	void addToAssassin(int points);
	
	/**
	 * Retrieves the noble points.
	 * @return The reputation's noble points.
	 */
	int getNoble();
	
	/**
	 * Adds the given points to the reputation's noble points.
	 * @param points The points to add.
	 */
	void addToNoblePoints(int points);
	
	/**
	 * Retrieves the bourgeois points.
	 * @return The reputation's bourgeois points.
	 */
	int getBourgeois();
	
	/**
	 * Adds the given points to the reputation's bourgeois points.
	 * @param points The points to add.
	 */
	void addToBourgeois(int points);
	
	/**
	 * Retrieves the thief points.
	 * @return The thief reputation's points.
	 */
	int getThief();
	
	/**
	 * Adds the given points to the reputation's thief points.
	 * @param points The points to add.
	 */
	void addToThief(int points);
	
	/**
	 * Checks if the reputation belongs to royal army.
	 * @return True if the reputation belongs to royal army, false otherwise.
	 */
	boolean isRoyalArmy();
	
	/**
	 * Sets whether the reputation belongs to royal army, or not.
	 * @param belongs Determines if the reputation belongs to royal army.
	 */
	void setRoyalArmy(boolean belongs);
	
	/**
	 * Checks if the reputation belongs to royal army.
	 * @return True if the reputation belongs to royal army, false otherwise.
	 */
	boolean isChaosLegion();
	
	/**
	 * Sets whether the reputation belongs to royal army, or not.
	 * @param belongs Determines if the reputation belongs to royal army.
	 */
	void setChaosLegion(boolean belongs);
}
