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

import org.junit.Test;

import com.ao.model.worldobject.properties.WorldObjectProperties;

public abstract class AbstractWorldObjectTest {

	protected AbstractWorldObject object;
	protected WorldObjectProperties objectProps;
	
	@Test
	public void testGetId() {
		assertEquals(objectProps.getId(), object.getId());
	}

	@Test
	public void testGetGraphic() {
		assertEquals(objectProps.getGraphic(), object.getGraphic());
	}

	@Test
	public void testGetName() {
		assertEquals(objectProps.getName(), object.getName());
	}
}
