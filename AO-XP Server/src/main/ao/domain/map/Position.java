package ao.domain.map;

public class Position {

	/**
	 * The position in the X axis.
	 */
	private byte x;
	
	/**
	 * The position in the Y axis.
	 */
	private byte y;
	
	/**
	 * The position's map.
	 */
	private int map;
	
	/**
	 * Creates a new position with the given data
	 * @param x	The position in the X axis.
	 * @param y	The position in the Y axis.
	 * @param map The position's map.
	 */
	Position(byte x, byte y, int map) {
		this.x = x;
		this.y = y;
		this.map = map;
	}
	
	/**
	 * Retrieves the position in the X axis.
	 * @return The position in the X axis.
	 */
	byte getX() {
		return x;
	}
	
	/**
	 * Sets the position in the X axis.
	 * @param x	The new position in the X axis.
	 */
	void setX(byte x) {
		this.x = x;
	}
	
	/**
	 * Retrieves the position in the Y axis.
	 * @return	The position in the Y axis.
	 */
	byte getY() {
		return y;
	}
	
	/**
	 * Sets the position in the Y axis.
	 * @param y	The new position in the Y axis.
	 */
	void setY(byte y) {
		this.y = y;
	}
	
	/**
	 * Retrieves the position's map.
	 * @return The position's map.
	 */
	int getMap() {
		return map;
	}
	
	/**
	 * Sets the position's map.
	 * @param map The new position's map. 
	 */
	void setMap(int map) {
		this.map = map;
	}
	
	/**
	 * Adds (or substract if the given number is negative) positions to the X axis.
	 * @param positions The positions to add.
	 */
	void addToX(int positions) {
		// TODO: Chequear que el número no se vaya fuera de los rangos?
		x += positions;
	}
	
	/**
	 * Adds (or substract if the given number is negative) positions to the Y axis.
	 * @param positions The positions to add.
	 */
	void addToY(int positions) {
		// TODO: Chequear que el número no se vaya fuera de los rangos?
		y += positions;
	}
}
