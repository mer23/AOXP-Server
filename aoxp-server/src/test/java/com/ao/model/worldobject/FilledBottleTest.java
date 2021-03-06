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

package com.ao.model.worldobject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import junit.framework.Assert;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ao.model.character.Character;
import com.ao.model.inventory.Inventory;
import com.ao.model.worldobject.properties.RefillableStatModifyingItemProperties;

public class FilledBottleTest extends AbstractItemTest {

	private static final int THIRST = 5;

	private FilledBottle bottle1;
	private FilledBottle bottle2;


	@Before
	public void setUp() throws Exception {
		RefillableStatModifyingItemProperties emptyProps = new RefillableStatModifyingItemProperties(WorldObjectType.EMPTY_BOTTLE, 1, "Water Bottle", 1, 1, null, null, false, false, false, false, 0, 0, false, null);
		RefillableStatModifyingItemProperties props = new RefillableStatModifyingItemProperties(WorldObjectType.FILLED_BOTTLE, 1, "Water Bottle", 1, 1, null, null, false,false, false, false, THIRST, THIRST, true, emptyProps);
		bottle1 = new FilledBottle(props, 5);

		bottle2 = new FilledBottle(props, 1);

		object = bottle2;
		ammount = 1;
		objectProps = props;
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testUseWithCleanup() {

		Inventory inventory = EasyMock.createMock(Inventory.class);
		Capture<Item> addedItem = new Capture<Item>();
		EasyMock.expect(inventory.addItem(EasyMock.capture(addedItem))).andReturn(1);

		Character character = EasyMock.createMock(Character.class);
		EasyMock.expect(character.getInventory()).andReturn(inventory).anyTimes();

		// Consumption of bottle2 requires these 2 calls.
		inventory.cleanup();
		character.addToThirstiness(THIRST);

		EasyMock.replay(inventory, character);

		bottle2.use(character);

		EasyMock.verify(inventory, character);

		Assert.assertTrue(addedItem.getValue() instanceof EmptyBottle);
		EmptyBottle emptyBottle = (EmptyBottle) addedItem.getValue();
		Assert.assertEquals(((RefillableStatModifyingItemProperties) bottle2.properties).getOtherStateProperties(), emptyBottle.properties);
		Assert.assertEquals(1, emptyBottle.amount);
	}

	@Test
	public void testUseWithoutCleanup() {

		Inventory inventory = EasyMock.createMock(Inventory.class);
		Capture<Item> addedItem = new Capture<Item>();
		EasyMock.expect(inventory.addItem(EasyMock.capture(addedItem))).andReturn(1);

		Character character = EasyMock.createMock(Character.class);
		EasyMock.expect(character.getInventory()).andReturn(inventory).anyTimes();

		// Consumption of bottle1 requires just a call to addToThirstiness.
		character.addToThirstiness(THIRST);

		EasyMock.replay(inventory, character);

		bottle1.use(character);

		EasyMock.verify(inventory, character);

		Assert.assertTrue(addedItem.getValue() instanceof EmptyBottle);
		EmptyBottle emptyBottle = (EmptyBottle) addedItem.getValue();
		Assert.assertEquals(((RefillableStatModifyingItemProperties) bottle1.properties).getOtherStateProperties(), emptyBottle.properties);
		Assert.assertEquals(1, emptyBottle.amount);
	}

	@Test
	public void testClone() {

		FilledBottle clone = (FilledBottle) bottle1.clone();

		// Make sure all fields match
		assertEquals(bottle1.amount, clone.amount);
		assertEquals(bottle1.properties, clone.properties);

		// Make sure the object itself is different
		assertFalse(bottle1 == clone);


		clone = (FilledBottle) bottle2.clone();

		// Make sure all fields match
		assertEquals(bottle2.amount, clone.amount);
		assertEquals(bottle2.properties, clone.properties);

		// Make sure the object itself is different
		assertFalse(bottle2 == clone);
	}

	@Test
	public void testGetThirst() {
		assertEquals(THIRST, bottle1.getThirst());
		assertEquals(THIRST, bottle2.getThirst());
	}
}
