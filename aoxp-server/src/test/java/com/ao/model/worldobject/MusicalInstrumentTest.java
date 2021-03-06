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
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ao.model.character.Character;
import com.ao.model.worldobject.properties.MusicalInstrumentProperties;

public class MusicalInstrumentTest extends AbstractItemTest {
	
	private static final int ATTEMPTS = 100;
	
	private MusicalInstrument instrument1;
	private MusicalInstrument instrument2;
	
	private List<Integer> sounds1;
	private List<Integer> sounds2;
	
	@Before
	public void setUp() throws Exception {
		sounds1 = new LinkedList<Integer>();
		sounds1.add(1);
		sounds1.add(3);
		sounds1.add(5);
		
		sounds2 = new LinkedList<Integer>();
		sounds2.add(2);
		
		MusicalInstrumentProperties props1 = new MusicalInstrumentProperties(WorldObjectType.MUSICAL_INSTRUMENT, 1, "Horn", 1, 1, null, null, false, false, false, false, 1, sounds1);
		instrument1 = new MusicalInstrument(props1, 5);
		
		MusicalInstrumentProperties props2 = new MusicalInstrumentProperties(WorldObjectType.MUSICAL_INSTRUMENT, 1, "Drum", 1, 1, null, null, false, false, false, false, 1, sounds2);
		instrument2 = new MusicalInstrument(props2, 1);
		
		object = instrument1;
		objectProps = props1;
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testClone() {
		MusicalInstrument clone = (MusicalInstrument) instrument1.clone();
		
		// Make sure all fields match
		assertEquals(instrument1.amount, clone.amount);
		assertEquals(instrument1.properties, clone.properties);
		
		// Make sure the object itself is different
		assertFalse(instrument1 == clone);
		
		
		clone = (MusicalInstrument) instrument2.clone();
		
		// Make sure all fields match
		assertEquals(instrument2.amount, clone.amount);
		assertEquals(instrument2.properties, clone.properties);
		
		// Make sure the object itself is different
		assertFalse(instrument2 == clone);
	}

	@Test
	public void testUse() {
		Character character = EasyMock.createMock(Character.class);
		EasyMock.replay(character);
		
		// nothing should happen
		instrument1.use(character);
		instrument2.use(character);
		
		EasyMock.verify(character);
	}
	
	@Test
	public void testGetSounds() {
		
		for (int i = 0; i < ATTEMPTS; i++) {
			assertTrue(sounds1.contains(instrument1.getSoundToPlay()));
		}
		
		assertEquals(sounds2.get(0), (Integer) instrument2.getSoundToPlay());
	}
}
