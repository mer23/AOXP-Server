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

package com.ao.network.packet.outgoing;

import java.io.UnsupportedEncodingException;
import com.ao.network.DataBuffer;
import com.ao.network.packet.OutgoingPacket;
import com.ao.model.character.Skill;
import com.ao.model.character.UserCharacter;

/**
 * Packet to send user skill points and progress to the client
 */
public class SendSkillsPacket implements OutgoingPacket {

	private UserCharacter user;


	public SendSkillsPacket(UserCharacter user) {

		super();
		this.user= user;	
	}

	@Override
	public void write(DataBuffer buffer) throws UnsupportedEncodingException {

		for (Skill skill : Skill.VALUES) {

			buffer.put((byte)user.getSkill(skill));

			if (user.getSkill(skill) < Skill.MAX_SKILL_POINT)
				buffer.put( (byte) ( user.getSkillExp(skill) * 100 / user.getSkillELU(skill) ) );
			else
				buffer.put((byte)0);
		}
	}

}
