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

package com.ao.network.packet.incoming;

import java.io.UnsupportedEncodingException;

import com.ao.context.ApplicationContext;
import com.ao.model.fonts.Font;
import com.ao.model.map.Heading;
import com.ao.model.user.ConnectedUser;
import com.ao.model.user.LoggedUser;
import com.ao.network.Connection;
import com.ao.network.DataBuffer;
import com.ao.network.packet.IncomingPacket;
import com.ao.network.packet.outgoing.CharacterChangePacket;
import com.ao.network.packet.outgoing.CharacterMovePacket;
import com.ao.network.packet.outgoing.ConsoleMessagePacket;
import com.ao.network.packet.outgoing.CreateFXPacket;
import com.ao.network.packet.outgoing.MeditateTogglePacket;
import com.ao.network.packet.outgoing.SetInvisiblePacket;
import com.ao.service.MapService;

public class WalkPacket implements IncomingPacket {

    private MapService mapService = ApplicationContext.getInstance(MapService.class);

    @Override
    public boolean handle(DataBuffer buffer, Connection connection) throws IndexOutOfBoundsException, UnsupportedEncodingException {

        if (buffer.getReadableBytes() < 1) {
            return false;
        }

        LoggedUser user = ( (ConnectedUser)connection.getUser() ).getAccount().getLoggedCharacter();

        //TODO check security issues

        Heading heading = Heading.get(buffer.get());

        if (heading != null) {

            if ( !(user.isParalyzed() || user.isImmobilized()) ) {

                if (user.isMeditating()) {
                    // Stop meditating, next action will start movement.
                    user.setMeditate(false);

                    connection.send(
                            new ConsoleMessagePacket("Dejas de meditar.", Font.INFO)
                            );

                    user.getFx().setId(0);
                    user.getFx().setLoops(0);

                    connection.send(
                            new MeditateTogglePacket());

                    connection.send(
                            new CreateFXPacket(user)); // Halts meditation FX with a no-effect FX

                } else if (!user.isHomecoming()) {

                    mapService.moveCharacterTo(user, heading); // Move user

                    //TODO Stop resting if needed

                    connection.send(new CharacterMovePacket(user));
                }
            } else { //if paralyzed
                //TODO set last message flag
                //TODO aplicar seguridad contra SH

                user.setHeading(heading);

                /* TODO client updates character's heading without 
                 * server notification (doesn't it?) so no need to send anything.
                 * */
            }

            // Check if hidden and can move while hidden
            if (user.isHidden() && !user.isAdminHidden() && !user.getArchetype().canWalkHidden()) {
                user.setHidden(false);

                if (user.isSailing()) {
                    //No need to check for pirate since any character hidden while sailing MUST be a pirate.
                    connection.send(
                            new ConsoleMessagePacket("¡Has recuperado tu apariencia normal!", Font.INFO)
                            );
                    
                    connection.send( 
                            new CharacterChangePacket(user)
                            );
                        

                } else if (!user.isInvisible()) {
                    // If not under a spell effect, show character
                    connection.send(
                            new ConsoleMessagePacket("Has vuelto a ser visible.", Font.INFO)
                            );
                    
                    user.setHidden(false);
                    
                    connection.send(
                            new SetInvisiblePacket(user, false)
                            );
                }
            }
        }
        
        return true;
    }
}
