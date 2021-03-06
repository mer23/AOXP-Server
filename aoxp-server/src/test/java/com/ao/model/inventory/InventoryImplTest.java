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

package com.ao.model.inventory;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ao.model.worldobject.Item;

public class InventoryImplTest {

	private Inventory inventory;
	
	@Before
	public void setUp() throws Exception {
		inventory = new InventoryImpl();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAddItem() {
		//Try to add all the items to the inventory
		Item[] item = new Item[inventory.getCapacity()];
		for (int i = 0; i< inventory.getCapacity(); i++) {
			item[i] = EasyMock.createMock(Item.class);
			EasyMock.expect(item[i].getId()).andReturn(i).anyTimes();
			EasyMock.replay(item[i]);
			
			Assert.assertEquals(0, inventory.addItem(item[i]));
		}	

		//Try to add an item when inventory is full and item not repeated.
		Item newItem = EasyMock.createMock(Item.class);
		EasyMock.expect(newItem.getId()).andReturn(22);
		EasyMock.expect(newItem.getAmount()).andReturn(1);
		EasyMock.replay(newItem);
		Assert.assertEquals(1, inventory.addItem(newItem));
		
		//Try to add an item that is repeated when inv. is full
		EasyMock.reset(newItem);
		EasyMock.expect(newItem.getId()).andReturn(1).anyTimes();
		EasyMock.expect(newItem.getAmount()).andReturn(1);
		
		EasyMock.reset(item[1]);
		EasyMock.expect(item[1].getId()).andReturn(1).anyTimes();
		EasyMock.expect(item[1].getAmount()).andReturn(1);
		EasyMock.expect(item[1].addAmount(1)).andReturn(2);
		EasyMock.expect(item[1].getAmount()).andReturn(2).anyTimes();
		
		EasyMock.expect(newItem.addAmount(-1)).andReturn(1);
		EasyMock.expect(newItem.getAmount()).andReturn(1).anyTimes();
		
		EasyMock.replay(newItem, item[1]);
		Assert.assertEquals(0, inventory.addItem(newItem));
		Assert.assertEquals(2, inventory.getItemAmount(newItem));
		
		//Try to add an item that is repeated when inventory isnt full but item not exceed the limit
		inventory.removeItem(0);
		EasyMock.reset(newItem);
		EasyMock.expect(newItem.getId()).andReturn(1).anyTimes();
		EasyMock.expect(newItem.getAmount()).andReturn(2);
		EasyMock.expect(newItem.getAmount()).andReturn(2);
		
		EasyMock.reset(item[1]);
		EasyMock.expect(item[1].getId()).andReturn(1).anyTimes();
		EasyMock.expect(item[1].getAmount()).andReturn(1);
		EasyMock.expect(item[1].addAmount(2)).andReturn(2); //Set the max amount to 2
		EasyMock.expect(item[1].getAmount()).andReturn(2);
		
		EasyMock.expect(newItem.addAmount(-1)).andReturn(1);
		EasyMock.expect(newItem.getAmount()).andReturn(1);
		
		EasyMock.replay(newItem, item[1]);

		Assert.assertEquals(0, inventory.addItem(newItem));
		Assert.assertEquals(3, inventory.getItemAmount(newItem));
		
		//Try to add an item that is repeated when inventory isnt full and item amount exceeds the limit.
		inventory.removeItem(0);
		inventory.addItem(item[0]);
		
		EasyMock.reset(newItem);
		EasyMock.expect(newItem.getId()).andReturn(1).anyTimes();
		EasyMock.expect(newItem.getAmount()).andReturn(2);
		EasyMock.expect(newItem.getAmount()).andReturn(2);
		
		EasyMock.reset(item[1]);
		EasyMock.expect(item[1].getId()).andReturn(1).anyTimes();
		EasyMock.expect(item[1].getAmount()).andReturn(1);
		EasyMock.expect(item[1].addAmount(2)).andReturn(2); //Set the max amount to 2
		EasyMock.expect(item[1].getAmount()).andReturn(2);
		
		EasyMock.expect(newItem.addAmount(-1)).andReturn(1);
		EasyMock.expect(newItem.getAmount()).andReturn(1);
		
		EasyMock.replay(newItem, item[1]);
		
		Assert.assertEquals(1, inventory.addItem(newItem));
	}

	@Test
	public void testGetItem() {
		Item item = EasyMock.createMock(Item.class);
		EasyMock.expect(item.getId()).andReturn(1).anyTimes();
		EasyMock.replay(item);
		
		inventory.addItem(item);
		int slot = inventory.hasItem(item);
		Assert.assertNotNull(inventory.getItem(slot));
		
		// Test bounds
		inventory.getItem(-1);
		inventory.getItem(inventory.getCapacity());
	}

	@Test
	public void testHasFreeSlots() {
		Item[] item = new Item[inventory.getCapacity()];

		for (int i = 0; i< inventory.getCapacity(); i++) {
			item[i] = EasyMock.createMock(Item.class);
			EasyMock.expect(item[i].getId()).andReturn(i).anyTimes();
			EasyMock.replay(item[i]);
			
			Assert.assertTrue(inventory.hasFreeSlots());
			inventory.addItem(item[i]);
		} 
		
		Assert.assertFalse(inventory.hasFreeSlots());
		
		inventory.removeItem(0);
		Assert.assertTrue(inventory.hasFreeSlots());
	}

	@Test
	public void testHasItem() {
		Item item = EasyMock.createMock(Item.class);
		EasyMock.expect(item.getId()).andReturn(1).anyTimes();
		EasyMock.replay(item);
		
		Assert.assertEquals(-1, inventory.hasItem(item));
		inventory.addItem(item);
		Assert.assertTrue(inventory.hasItem(item) != -1);
	}

	@Test
	public void testRemoveItemInt() {
		Item item = EasyMock.createMock(Item.class);
		EasyMock.expect(item.getId()).andReturn(1).anyTimes();
		EasyMock.replay(item);
		
		inventory.addItem(item);
		int slot = inventory.hasItem(item);
		Assert.assertNotNull(inventory.removeItem(slot));
		Assert.assertEquals(-1, inventory.hasItem(item));
		
		// Test bounds
		Assert.assertNull(inventory.removeItem(-1));
		Assert.assertNull(inventory.removeItem(inventory.getCapacity()));
	}

	@Test
	public void testRemoveItemItem() {
		Item item = EasyMock.createMock(Item.class);
		EasyMock.expect(item.getId()).andReturn(1).anyTimes();
		EasyMock.expect(item.getAmount()).andReturn(10).anyTimes();
		EasyMock.expect(item.addAmount(-10)).andReturn(0);
		Item item2 = EasyMock.createMock(Item.class);
		EasyMock.expect(item2.getId()).andReturn(2).anyTimes();
		EasyMock.expect(item2.getAmount()).andReturn(5).anyTimes();
		EasyMock.expect(item2.addAmount(-10)).andReturn(-5);
		Item itemRemoved = EasyMock.createMock(Item.class);
		EasyMock.expect(itemRemoved.getId()).andReturn(2).anyTimes();
		EasyMock.expect(itemRemoved.getAmount()).andReturn(10).anyTimes();
		Item itemRemovedClone = EasyMock.createMock(Item.class);
		EasyMock.expect(itemRemovedClone.getId()).andReturn(2).anyTimes();
		EasyMock.expect(itemRemovedClone.getAmount()).andReturn(10).anyTimes();
		EasyMock.expect(itemRemovedClone.addAmount(-5)).andReturn(5);
		EasyMock.expect(itemRemoved.clone()).andReturn(itemRemovedClone);
		Item newItem = EasyMock.createMock(Item.class);
		EasyMock.expect(newItem.getId()).andReturn(1).anyTimes();
		EasyMock.expect(newItem.getAmount()).andReturn(10).anyTimes();
		EasyMock.expect(item.clone()).andReturn(newItem);
		EasyMock.replay(item, newItem, item2, itemRemoved, itemRemovedClone);
		
		// Completely remove a item from inventory
		inventory.addItem(item);
		inventory.removeItem(item);
		Assert.assertEquals(-1, inventory.hasItem(item));
		
		// Remove an item, by more than can be removed
		inventory.addItem(item2);
		inventory.removeItem(itemRemoved);
		Assert.assertTrue(inventory.hasItem(item2) != -1);
		
		// Try to remove a item not in inventory
		Assert.assertNull(inventory.removeItem(item));
	}

	@Test
	public void testRemoveItemIntInt() {
		Item item = EasyMock.createMock(Item.class);
		EasyMock.expect(item.getId()).andReturn(1).anyTimes();
		EasyMock.expect(item.getAmount()).andReturn(2);
		EasyMock.expect(item.addAmount(-1)).andReturn(1);
		EasyMock.expect(item.addAmount(-1)).andReturn(0);
		Item newItem = EasyMock.createMock(Item.class);
		EasyMock.expect(newItem.getAmount()).andReturn(2).anyTimes();
		EasyMock.expect(item.clone()).andReturn(newItem);
		Item newItem2 = EasyMock.createMock(Item.class);
		EasyMock.expect(newItem2.getAmount()).andReturn(1).anyTimes();
		EasyMock.expect(item.clone()).andReturn(newItem2);
		EasyMock.expect(newItem.addAmount(-1)).andReturn(1);
		EasyMock.expect(newItem2.addAmount(-0)).andReturn(1);
		EasyMock.replay(item, newItem, newItem2);
		
		inventory.addItem(item);
		int slot = inventory.hasItem(item);
		
		Item removedItem = inventory.removeItem(slot, 1);
		Assert.assertNotNull(removedItem);
		Assert.assertTrue(inventory.hasItem(item) != -1);
		
		removedItem = inventory.removeItem(slot, 1);
		Assert.assertNotNull(removedItem);
		Assert.assertEquals(-1, inventory.hasItem(item));
		
		// Test bounds
		Assert.assertNull(inventory.removeItem(-1, 1));
		Assert.assertNull(inventory.removeItem(inventory.getCapacity(), 1));
	}
	
	@Test
	public void testGetItemAmount() {
		Item item = EasyMock.createMock(Item.class);
		EasyMock.expect(item.getId()).andReturn(1).anyTimes();
		EasyMock.expect(item.getAmount()).andReturn(1).anyTimes();
		
		Item item2 = EasyMock.createMock(Item.class);
		EasyMock.expect(item2.getId()).andReturn(1).anyTimes();
		EasyMock.expect(item2.getAmount()).andReturn(1000).anyTimes();
		
		// This is needed to add the second item
		EasyMock.expect(item.addAmount(1000)).andReturn(1000).once();
		EasyMock.expect(item2.addAmount(-999)).andReturn(1).once();
		
		EasyMock.replay(item, item2);
		
		inventory.addItem(item);
		Assert.assertEquals(1, inventory.getItemAmount(item));
		
		// When adding the second item, amount should stack up
		inventory.addItem(item2);
		Assert.assertEquals(1001, inventory.getItemAmount(item));
	}

	@Test
	public void testSetCapacity() {
		Item item = EasyMock.createMock(Item.class);
		EasyMock.expect(item.getId()).andReturn(1).anyTimes();
		EasyMock.expect(item.getAmount()).andReturn(1).anyTimes();
		EasyMock.replay(item);
		
		inventory.addItem(item);
		inventory.setCapacity(1);
		
		Assert.assertEquals(1, inventory.getCapacity());
		Assert.assertEquals(item, inventory.getItem(0));
		
		// TODO : Test when capacity is trimmed and items are droped
	}

	@Test
	public void testCleanup() {
		Item item = EasyMock.createMock(Item.class);
		EasyMock.expect(item.getId()).andReturn(1).anyTimes();
		EasyMock.expect(item.getAmount()).andReturn(0).anyTimes();
		
		Item item2 = EasyMock.createMock(Item.class);
		EasyMock.expect(item2.getId()).andReturn(2).anyTimes();
		EasyMock.expect(item2.getAmount()).andReturn(5).anyTimes();
		
		EasyMock.replay(item, item2);
		
		inventory.addItem(item);
		inventory.addItem(item2);
		
		inventory.cleanup();
		
		Assert.assertTrue(inventory.hasItem(item) == -1);
		Assert.assertTrue(inventory.hasItem(item2) != -1);
	}
}
