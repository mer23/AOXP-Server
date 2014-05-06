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
import java.util.HashMap;
import java.util.Map;

import com.ao.network.packet.OutgoingPacket;
import com.ao.network.packet.outgoing.BlockPositionPacket;
import com.ao.network.packet.outgoing.ChangeInventorySlotPacket;
import com.ao.network.packet.outgoing.ChangeMapPacket;
import com.ao.network.packet.outgoing.CharacterChangeNickNamePacket;
import com.ao.network.packet.outgoing.CharacterChangePacket;
import com.ao.network.packet.outgoing.CharacterCreatePacket;
import com.ao.network.packet.outgoing.CharacterForceMovePacket;
import com.ao.network.packet.outgoing.CharacterMovePacket;
import com.ao.network.packet.outgoing.CharacterRemovePacket;
import com.ao.network.packet.outgoing.ChatOverHeadPacket;
import com.ao.network.packet.outgoing.CommerceChatPacket;
import com.ao.network.packet.outgoing.CommerceEndPacket;
import com.ao.network.packet.outgoing.ConsoleMessagePacket;
import com.ao.network.packet.outgoing.CreateFXPacket;
import com.ao.network.packet.outgoing.DiceRollPacket;
import com.ao.network.packet.outgoing.DisconnectPacket;
import com.ao.network.packet.outgoing.ErrorMessagePacket;
import com.ao.network.packet.outgoing.GuildChatPacket;
import com.ao.network.packet.outgoing.MeditateTogglePacket;
import com.ao.network.packet.outgoing.NavigateTogglePacket;
import com.ao.network.packet.outgoing.ObjectCreatePacket;
import com.ao.network.packet.outgoing.ObjectDeletePacket;
import com.ao.network.packet.outgoing.PlayMidiPacket;
import com.ao.network.packet.outgoing.PlayWavePacket;
import com.ao.network.packet.outgoing.RemoveAllDialogsPacket;
import com.ao.network.packet.outgoing.RemoveCharDialogPacket;
import com.ao.network.packet.outgoing.SendSkillsPacket;
import com.ao.network.packet.outgoing.ShowMessageBoxPacket;
import com.ao.network.packet.outgoing.UpdateDexterityPacket;
import com.ao.network.packet.outgoing.UpdateExperiencePacket;
import com.ao.network.packet.outgoing.UpdateGoldPacket;
import com.ao.network.packet.outgoing.UpdateHPPacket;
import com.ao.network.packet.outgoing.UpdateManaPacket;
import com.ao.network.packet.outgoing.UpdatePositionPacket;
import com.ao.network.packet.outgoing.UpdateStaminaPacket;
import com.ao.network.packet.outgoing.UpdateStrengthAndDexterityPacket;
import com.ao.network.packet.outgoing.UpdateStrengthPacket;
import com.ao.network.packet.outgoing.UpdateUserStatsPacket;
import com.ao.network.packet.outgoing.UserCharacterIndexInServerPacket;
import com.ao.network.packet.outgoing.UserCommerceInitPacket;
import com.ao.network.packet.outgoing.UserIndexInServer;
import com.ao.network.packet.outgoing.DumbPacket;
import com.ao.network.packet.outgoing.DumbNoMorePacket;
import com.ao.network.packet.outgoing.LoggedPacket;
import com.ao.network.packet.outgoing.WorkRequestTargetPacket;
/**
 * Manager for server-side packets.
 */
public class ServerPacketsManager {

    /**
     * Enumerates server packets.
     */
    private enum ServerPackets {
        LOGGED(LoggedPacket.class),
        REMOVE_ALL_DIALOGS(RemoveAllDialogsPacket.class), //sends no data
        REMOVE_CHAR_DIALOG(RemoveCharDialogPacket.class),
        TOGGLE_NAVIGATE(NavigateTogglePacket.class), //sends no data
        DISCONNECT(DisconnectPacket.class), //sends no data
        COMMERCE_END(CommerceEndPacket.class), //sends no data
        BANKING_END(null), //sends no data
        COMMERCE_INIT(null), //sends no data
        BANK_INIT(null), //CANNOT BE DONE UNTIL VAULT GETTERS ARE DEFINED
        USER_COMMERCE_INIT(UserCommerceInitPacket.class),
        USER_COMMERCE_END(null), //sends no data
        USER_OFFER_CONFIRM(null), //sends no data
        COMMERCE_CHAT(CommerceChatPacket.class),
        SHOW_BLACKSMITH_FORM(null), //sends no data
        SHOW_CARPENTER_FORM(null), //sends no data
        UPDATE_STAMINA(UpdateStaminaPacket.class),
        UPDATE_MANA(UpdateManaPacket.class),
        UPDATE_HP(UpdateHPPacket.class),
        UPDATE_GOLD(UpdateGoldPacket.class),
        UPDATE_BANK_GOLD(null), //CANNOT BE DONE UNTIL A GOLD-IN-BANK GETTER IS DEFINED
        UPDATE_EXP(UpdateExperiencePacket.class),
        CHANGE_MAP(ChangeMapPacket.class),
        POSITION_UPDATE(UpdatePositionPacket.class),
        CHAT_OVER_HEAD(ChatOverHeadPacket.class),
        CONSOLE_MESSAGE(ConsoleMessagePacket.class),
        GUILD_CHAT(GuildChatPacket.class),
        SHOW_MESSAGE_BOX(ShowMessageBoxPacket.class),
        USER_INDEX_IN_SERVER(UserIndexInServer.class),
        USER_CHARACTER_INDEX_IN_SERVER(UserCharacterIndexInServerPacket.class),
        CHARACTER_CREATE(CharacterCreatePacket.class),
        CHARACTER_REMOVE(CharacterRemovePacket.class),
        CHARACTER_CHANGE_NICKNAME(CharacterChangeNickNamePacket.class),
        CHARACTER_MOVE(CharacterMovePacket.class),
        CHARACTER_FORCE_MOVE(CharacterForceMovePacket.class),
        CHARACTER_CHANGE(CharacterChangePacket.class),
        OBJECT_CREATE(ObjectCreatePacket.class),
        OBJECT_DELETE(ObjectDeletePacket.class),
        BLOCK_POSITION(BlockPositionPacket.class),
        PLAY_MIDI(PlayMidiPacket.class),
        PLAY_WAVE(PlayWavePacket.class),
        GUILD_LIST(null), //CAN'T BE DONE YET
        AREA_CHANGED(null),
        TOGGLE_PAUSE(null), //sends no data
        TOGGLE_RAIN(null), //sends no data
        CREATE_FX(CreateFXPacket.class),
        UPDATE_USER_STATS(UpdateUserStatsPacket.class),
        WORK_REQUEST_TARGET(WorkRequestTargetPacket.class),
        CHANGE_INVENTORY_SLOT(ChangeInventorySlotPacket.class),
        CHANGE_BANK_SLOT(null),
        CHANGE_SPELL_SLOT(ChangeSpellSlotPacket.class),
        ATTRIBUTES(null),
        BLACKSMITH_WEAPONS(null),
        BLACKSMITH_ARMORS(null),
        CARPENTER_OBJECTS(null),
        REST_OK(null),
        ERROR_MESSAGE(ErrorMessagePacket.class),
        BLIND(null),
        DUMB(DumbPacket.class),
        SHOW_SIGNAL(null),
        CHANGE_NPC_INVENTORY_SLOT(null),
        UPDATE_HUNGER_AND_THRIST(null),
        FAME(null),
        MINI_STATS(null),
        LEVEL_UP(null),
        ADD_FORUM_MESAGE(null),
        SHOW_FORUM_MESSAGE(null),
        SET_INVISIBLE(null),
        ROLL_DICE(DiceRollPacket.class),
        MEDITATE_TOGGLE(MeditateTogglePacket.class), //sends no data (was needed to continue implementing WalkPacket)
        BLIND_NO_MORE(null),
        DUMB_NO_MORE(DumbNoMorePacket.class),
        SEND_SKILLS(SendSkillsPacket.class),
        TRAINER_CREATURE_LIST(null),
        GUILD_NEWS(null),
        OFFER_DETAILS(null),
        ALIANCE_PROPOSALS_LIST(null),
        PEACE_PROPOSALS_LIST(null),
        CHARACTER_INFO(null),
        GUILD_LEADER_INFO(null),
        GUILD_MEMBER_INFO(null),
        GUILD_DETAILS(null),
        SHOW_GUILD_FUNDATION_FORM(null),
        PARALIZE_OK(null),
        SHOW_USER_REQUEST(null),
        TRADE_OK(null),
        BANK_OK(null),
        CHANGE_USER_TRADE_SLOT(null),
        SEND_NIGHT(null),
        PONG(null),
        UPDATE_TAG_AND_STATUS(null),
        SPAWN_LIST(null),
        SHOW_SOS_FORM(null),
        SHOW_MOTD_EDITION_FORM(null),
        SHOW_GM_PANEL_FORM(null),
        USER_NAME_LIST(null),
        SHOW_DENOUNCES(null),
        RECORD_LIST(null),
        RECORD_DETAILS(null),
        SHOW_GUILD_ALIGN(null),
        SHOW_PARTY_FORM(null),
        UPDATE_STRENGHT_AND_DEXTERITY(UpdateStrengthAndDexterityPacket.class),
        UPDATE_STRENGHT(UpdateStrengthPacket.class),
        UPDATE_DEXTERITY(UpdateDexterityPacket.class),
        ADD_SLOTS(null),
        MULTI_MESSAGE(null),
        STOP_WORKING(null),
        CANCEL_OFFER_ITEM(null);

        protected Class<? extends OutgoingPacket> packetClass;

        private ServerPackets(Class<? extends OutgoingPacket> packet) {
            packetClass = packet;
        }
    }

    /**
     * Maps packets ids to their classes.
     */
    protected static final ServerPackets[] packets = ServerPackets.values();

    /**
     * Maps packets classes to their ids.
     */
    protected static final Map<Class<? extends OutgoingPacket>, Integer> packetsMap = new HashMap<Class<? extends OutgoingPacket>, Integer>();

    static {
        for (ServerPackets packet : packets) {
            packetsMap.put(packet.packetClass, packet.ordinal());
        }
    }

    /**
     * Writes the given packet in the given buffer.
     * @param packet The packet to write.
     * @param buffer The buffer where to write the packet.
     * @throws UnsupportedEncodingException
     */
    public static void write(OutgoingPacket packet, DataBuffer buffer) throws UnsupportedEncodingException {

        // Put the packet id before the packet itself.
        buffer.put(packetsMap.get(packet.getClass()).byteValue());
        packet.write(buffer);
    }
}
