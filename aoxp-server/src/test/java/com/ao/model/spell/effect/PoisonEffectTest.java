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

package com.ao.model.spell.effect;

import org.easymock.EasyMock;
import org.junit.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ao.exception.InvalidTargetException;
import com.ao.model.character.Character;
import com.ao.model.spell.effect.Effect;
import com.ao.model.spell.effect.PoisonEffect;
import com.ao.model.worldobject.WorldObject;

public class PoisonEffectTest {

	private Effect poisonEffect;
	
	@Before
	public void setUp() throws Exception {
		this.poisonEffect = new PoisonEffect();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testApplyCharacterCharacter() {
		Character caster = EasyMock.createMock(Character.class);
		Character target = EasyMock.createMock(Character.class);
		
		target.setPoisoned(true);
		
		EasyMock.replay(caster, target);
		
		poisonEffect.apply(caster, target);
		
		EasyMock.verify(caster, target);
	}

	@Test
	public void testAppliesToCharacterCharacter() {
		Character caster = EasyMock.createMock(Character.class);
		Character target = EasyMock.createMock(Character.class);
		
		// Pretend the target is dead first, then alive.
		EasyMock.expect(target.isDead()).andReturn(true).once();
		EasyMock.expect(target.isDead()).andReturn(false).once();
		
		EasyMock.replay(caster, target);
		
		// Paralyzing a dead char is invalid.
		Assert.assertFalse(poisonEffect.appliesTo(caster, target));
		
		/// Paralyzing an alive char is valid.
		Assert.assertTrue(poisonEffect.appliesTo(caster, target));
	}

	@Test
	public void testAppliesToCharacterWorldObject() {
		WorldObject obj = EasyMock.createMock(WorldObject.class);
		Character caster = EasyMock.createMock(Character.class);
		
		EasyMock.replay(obj, caster);
		
		// Should always false, no matter what
		Assert.assertFalse(poisonEffect.appliesTo(caster, obj));
	}

	@Test
	public void testApplyCharacterWorldObject() {
		WorldObject obj = EasyMock.createMock(WorldObject.class);
		Character caster = EasyMock.createMock(Character.class);
		
		EasyMock.replay(obj, caster);
		
		// Should do nothing....
		try {
			poisonEffect.apply(caster, obj);
			Assert.fail("Applying an effect for characters to a world object didn't fail.");
		} catch (InvalidTargetException e) {
			// this is ok
		}
		
		EasyMock.verify(caster, obj);
	}

}
