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

package com.ao.data.dao.ini;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ao.data.dao.exception.DAOException;
import com.ao.model.worldobject.WorldObjectType;
import com.ao.model.worldobject.properties.BackpackProperties;
import com.ao.model.worldobject.properties.ForumProperties;
import com.ao.model.worldobject.properties.ItemProperties;
import com.ao.model.worldobject.properties.MineralProperties;
import com.ao.model.worldobject.properties.SignProperties;
import com.ao.model.worldobject.properties.StatModifyingItemProperties;
import com.ao.model.worldobject.properties.TemporalStatModifyingItemProperties;
import com.ao.model.worldobject.properties.WorldObjectProperties;

public class WorldObjectPropertiesDAOIniTest {
	
	private static final int YELLOW_POTION_INDEX = 35;
	private static final int BLUE_POTION_INDEX = 36;
	private static final int RED_POTION_INDEX = 37;
	private static final int GREEN_POTION_INDEX = 38;
	private static final int VIOLET_POTION_INDEX = 165;
	private static final int BLACK_POTION_INDEX = 644;

	private static final int SIGN_INDEX = 12;
	private static final int ULLATHORPE_FORUM_INDEX = 33;
	private static final int BACKPACK_INDEX = 865;
	private static final int MINERAL_INDEX = 191;
	

	private static final String TEST_OBJ_DAT = "src/test/resources/obj.dat";
	
	protected WorldObjectPropertiesDAOIni dao;

	@Before
	public void setUp() throws Exception {
		dao = new WorldObjectPropertiesDAOIni(TEST_OBJ_DAT);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRetrieveAll() {
		WorldObjectProperties[] objectProperties = null;
		try {
			objectProperties = dao.retrieveAll();
		} catch (DAOException e) {
			fail("Loading of objects failed with message " + e.getMessage());
		}
		
		// Check some items to make sure they loaded properly...
		WorldObjectProperties yellowPotion = objectProperties[YELLOW_POTION_INDEX];
		assertTrue(yellowPotion instanceof TemporalStatModifyingItemProperties);
		assertEquals(WorldObjectType.AGILITY_POTION, yellowPotion.getType());
		
		WorldObjectProperties bluePotion = objectProperties[BLUE_POTION_INDEX];
		assertTrue(bluePotion instanceof StatModifyingItemProperties);
		assertEquals(WorldObjectType.MANA_POTION, bluePotion.getType());
		
		WorldObjectProperties redPotion = objectProperties[RED_POTION_INDEX];
		assertTrue(redPotion instanceof StatModifyingItemProperties);
		assertEquals(WorldObjectType.HP_POTION, redPotion.getType());
		
		WorldObjectProperties greenPotion = objectProperties[GREEN_POTION_INDEX];
		assertTrue(greenPotion instanceof TemporalStatModifyingItemProperties);
		assertEquals(WorldObjectType.STRENGTH_POTION, greenPotion.getType());
		
		WorldObjectProperties violetPotion = objectProperties[VIOLET_POTION_INDEX];
		assertTrue(violetPotion instanceof ItemProperties);
		assertEquals(WorldObjectType.POISON_POTION, violetPotion.getType());
		
		WorldObjectProperties blackPotion = objectProperties[BLACK_POTION_INDEX];
		assertTrue(blackPotion instanceof ItemProperties);
		assertEquals(WorldObjectType.DEATH_POTION, blackPotion.getType());

		WorldObjectProperties sign = objectProperties[SIGN_INDEX];
		assertTrue(sign instanceof SignProperties);
		assertEquals(WorldObjectType.SIGN, sign.getType());
		
		WorldObjectProperties ullathorpeForum = objectProperties[ULLATHORPE_FORUM_INDEX];
		assertTrue(ullathorpeForum instanceof ForumProperties);
		assertEquals(WorldObjectType.FORUM, ullathorpeForum.getType());
		
		WorldObjectProperties backpack = objectProperties[BACKPACK_INDEX];
		assertTrue(backpack instanceof BackpackProperties);
		assertEquals(WorldObjectType.BACKPACK, backpack.getType());
		
		WorldObjectProperties mineral = objectProperties[MINERAL_INDEX];		
		assertTrue(mineral instanceof MineralProperties);               		
		assertEquals(WorldObjectType.MINERAL, mineral.getType());
		
		
		// TODO : Keep doing this with other object types. Also check some other attributes are properly loaded...
	}
}
