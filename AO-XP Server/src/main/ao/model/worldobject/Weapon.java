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

package ao.model.worldobject;

import ao.model.character.Character;
import ao.model.worldobject.properties.WeaponProperties;

/**
 * A weapon.
 */
public class Weapon extends AbstractEquipableItem {

	/**
	 * Creates a new Weapon instance.
	 * @param properties The item's properties.
	 * @param amount The item's amount.
	 */
	public Weapon(WeaponProperties properties, int amount) {
		super(properties, amount);
	}
	
	/*
	 * (non-Javadoc)
	 * @see ao.model.worldobject.AbstractItem#clone()
	 */
	@Override
	public Item clone() {
		return new Weapon((WeaponProperties) properties, amount);
	}

	/*
	 * (non-Javadoc)
	 * @see ao.model.worldobject.Item#use(ao.model.character.Character)
	 */
	@Override
	public void use(Character character) {
		// Weapons can'e be used.
	}
	
	/**
	 * @return the true if this item stabs, false otherwise
	 */
	public boolean getStabs() {
		return ((WeaponProperties) properties).getStabs();
	}

	/**
	 * @return the piercingDamage
	 */
	public int getPiercingDamage() {
		return ((WeaponProperties) properties).getPiercingDamage();
	}

	/**
	 * @return the minHit
	 */
	public int getMinHit() {
		return ((WeaponProperties) properties).getMinHit();
	}

	/**
	 * @return the maxHit
	 */
	public int getMaxHit() {
		return ((WeaponProperties) properties).getMaxHit();
	}

	/**
	 * Retrieves the damage to be applied by the item.
	 * @return The damage to be applied by the item.
	 */
	public int getDamage() {
		int minModifier = ((WeaponProperties) properties).getMinHit();
		int maxModifier = ((WeaponProperties) properties).getMaxHit();
		
		return (int) (Math.random() * (maxModifier - minModifier + 1)) + minModifier;
	}
}
