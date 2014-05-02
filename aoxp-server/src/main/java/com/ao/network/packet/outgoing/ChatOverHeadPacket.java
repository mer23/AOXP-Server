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

import java.awt.Color;
import java.io.UnsupportedEncodingException;

import com.ao.model.character.Character;
import com.ao.network.DataBuffer;
import com.ao.network.packet.OutgoingPacket;

/**
 * Packet that sends to client a message to display over a talking character's head.
 */
public class ChatOverHeadPacket implements OutgoingPacket{

    private Character character;
    private String text;
    private Color color;


    /**
     * Creates the packet.
     * @param character the character that spoke
     * @param text what the character said
     * @param color font color used to display the message
     * */
    public ChatOverHeadPacket (Character character, String text, Color color) {

        this.character= character;
        this.text= text;
        this.color= color;
    }

    @Override
    public void write(DataBuffer buffer) throws UnsupportedEncodingException {

        buffer.putASCIIString(text);
        buffer.putShort((short) character.getCharIndex());

        buffer.put( (byte) color.getRed() );
        buffer.put( (byte) color.getGreen() );
        buffer.put( (byte) color.getBlue() );
    }

}
