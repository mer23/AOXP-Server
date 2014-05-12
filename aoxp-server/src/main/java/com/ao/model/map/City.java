/*
    AO-XP Server (XP stands for Cross Platform) is a Java implementation of Argentum Online's server 
    Copyright (C) 2009 Juan Martín Sotuyo Dodero. <juansotuyo@gmail.com>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.ao.model.map;

/**
 * A city in the game.
 * @author zaxtor
 */
public class City{
	
	private int map;
	private byte x;
	private byte y;
	
	/**
	 * Creates a new city instance.
	 * @param map The city's map id.
	 * @param x   City's x coordinate for users spawn.
	 * @param y   City's y coordinate for users spawn.
	 */
	public City(int map, byte x, byte y){
		this.map = map;
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Retrieves the city's map id.
	 * @return The city's map id.
	 */
	public int getMap() {
		return map;
	}
	
	/**
	 * Retrieves city's X coordinate for users spawn.
	 * @return city's X coordinate for users spawn.
	 */
	public byte getX() {
		return x;
	}
	
	/**
	 * Retrieves city's Y coordinate for users spawn.
	 * @return city's Y coordinate for users spawn.
	 */
	public byte getY() {
		return y;
	}
	
}
