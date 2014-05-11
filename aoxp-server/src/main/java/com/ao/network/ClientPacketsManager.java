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

package com.ao.network;

import java.io.UnsupportedEncodingException;

import com.ao.network.packet.IncomingPacket;
import com.ao.network.packet.incoming.DropPacket;
import com.ao.network.packet.incoming.LoginExistingCharacterPacket;
import com.ao.network.packet.incoming.LoginNewCharacterPacket;
import com.ao.network.packet.incoming.PickUpPacket;
import com.ao.network.packet.incoming.ThrowDicesPacket;
import com.ao.network.packet.incoming.WalkPacket;

/**
 * Manager for client-side packets.
 */
public class ClientPacketsManager {

	/**
	 * Enumerates client packets.
	 */
	private enum ClientPackets {
		LOGIN_EXISTING_CHARACTER(LoginExistingCharacterPacket.class),
		THROW_DICE(ThrowDicesPacket.class),
		LOGIN_NEW_CHARACTER(LoginNewCharacterPacket.class),
		TALK(null),
		YELL(null),
		WHISPER(null),
		WALK(WalkPacket.class),
		REQUEST_POSITION_UPDATE(null),
		ATTACK(null),
		PICK_UP(PickUpPacket.class), //TODO Is this the right place for it?
		SAFE_TOGGLE(null),
		RESUCITATION_SAFE_TOGGLE(null), //RESURRECTION
		REQUEST_GUILD_LEADER_INFO(null),
		REQUEST_ATRIBUTES(null),
		REQUEST_FAME(null),
		REQUEST_SKILLS(null),
		REQUEST_MINI_STATS(null),
		COMMERCE_END(null),
		USER_COMMERCE_END(null),
		USER_COMMERCE_CONFIRM(null),
		COMMERCE_CHAT(null),
		BANK_END(null),
		USER_COMMERCE_OK(null),
		USER_COMMERCE_REJECT(null),
		DROP(DropPacket.class), //TODO Is this the right place for it?
		CAST_SPELL(null),
		LEFT_CLICK(null),
		DOUBLE_CLICK(null),
		WORK(null),
		USE_SPELL_MACRO(null),
		USE_ITEM(null),
		CRAFT_BLACKSMITH(null),
		CRAFT_CARPENTER(null),
		WORK_LEFT_CLICK(null),
		CREATE_NEW_GUILD(null),
		SPELL_INFO(null),
		EQUIP_ITEM(null), //RIG OUT
		CHANGE_HEADING(null),
		MODIFY_SKILLS(null),
		TRAIN(null),
		COMMERCE_BUY(null),
		BANK_EXTRACT_ITEM(null), //WITHDRAW
		COMMERCE_SELL(null),
		BANK_DEPOSIT(null),
		FORUM_POST(null),
		MOVE_SPELL(null),
		MOVE_BANK(null),
		CLAN_CODEX_UPDATE(null),
		USER_COMMERCE_OFFER(null),
		GUILD_ACCEPT_PEACE(null),
		GUILD_REJECT_ALLIANCE(null),
		GUILD_REJECT_PEACE(null),
		GUILD_ACCEPT_ALLIANCE(null),
		GUILD_OFFER_PEACE(null),
		GUILD_OFFER_ALLIANCE(null),
		GUILD_ALLIANCE_DETAILS(null),
		GUILD_PEACE_DETAILS(null),
		GUILD_REQUEST_JOINER_INFO(null), //APPLICANT
		GUILD_ALLIANCE_PROPOSAL_LIST(null),
		GUILD_PEACE_PROPOSAL_LIST(null),
		GUILD_DECLARE_WAR(null),
		GUILD_NEW_WEBSITE(null),
		GUILD_ACCEPT_NEW_MEMBER(null),
		GUILD_REJECT_NEW_MEMBER(null),
		GUILD_KICK_MEMBER(null),
		GUILD_UPDATE_NEWS(null),
		GUILD_MEMBER_INFO(null),
		GUILD_OPEN_ELECTIONS(null),
		GUILD_REQUEST_MEMBERSHIP(null),
		GUILD_REQUEST_DETAILS(null),
		ONLINE(null),
		QUIT(null),
		GUILD_LEAVE(null),
		REQUEST_ACCOUNT_STATE(null),
		PET_STAND(null),
		PET_FOLLOW(null),
		RELEASE_PET(null),
		TRAIN_LIST(null),
		REST(null),
		MEDITATE(null),
		RESUCITATE(null),
		HEAL(null),
		HELP(null),
		REQUEST_STATS(null),
		COMMERCE_START(null),
		BANK_START(null),
		ENLIST(null),
		INFORMATION(null),
		REWARD(null),
		REQUEST_MOTD(null), //MOTD stands for 'message of the day'
		UPTIME(null),
		PARTY_LEAVE(null),
		PARTY_CREATE(null),
		PARTY_JOIN(null),
		INQUIRY(null),
		GUILD_MESSAGE(null),
		PARTY_MESSAGE(null),
		CENTINEL_REPORT(null),
		GUILD_ONLINE(null),
		PARTY_ONLINE(null),
		COUNCIL_MESSAGE(null),
		ROLEMASTER_REQUEST(null),
		GM_REQUEST(null),
		BUG_REPORT(null),
		CHANGE_DESCRIPTION(null),
		GUILD_VOTE(null),
		PUNISHMENTS(null),
		CHANGE_PASSWORD(null),
		GAMBLE(null), //BET
		INQUIRY_VOTE(null),
		LEAVE_FACTION(null),
		BANK_EXTRACT_GOLD(null), //WITHDRAW
		BANK_DEPOSIT_GOLD(null),
		DENOUNCE(null),
		GUILD_FUNDATE(null),
		GUILD_FUNDATION(null),
		PARTY_KICK(null),
		PARTY_SET_LEADER(null),
		PARTY_ACCEPT_MEMBER(null),
		PING(null),
		REQUEST_PARTY_FORM(null),
		ITEM_UPGRADE(null),
		GM_COMMANDS(null),
		INITIATE_CRAFTING(null),
		HOME(null),
		SHOW_GUILD_NEWS(null),
		SHARE_NPC(null),
		STOP_SHARING_NPC(null),
		CONSULTATION(null),
		MOVE_ITEM(null);
		
		protected IncomingPacket handler;

		private ClientPackets(Class<? extends IncomingPacket> handler) {
			try {
				this.handler = handler.getConstructor().newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

	}

	/**
	 * Maps packets ids to their classes.
	 */
	protected static final ClientPackets[] packets = ClientPackets.values();

	/**
	 * Handles new data in the connection's incoming buffer.
	 * @param connection The connection's container.
	 * @param buffer The buffer from which to read data.
	 * @return True if a packet could be processed, false otherwise.
	 * @throws UnsupportedEncodingException
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public static boolean handle(DataBuffer buffer, Connection connection) throws UnsupportedEncodingException,
		ArrayIndexOutOfBoundsException {

		return packets[buffer.get()].handler.handle(buffer, connection);
	}
}