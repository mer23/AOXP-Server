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

import com.ao.model.fonts.Font;
import com.ao.model.map.Tile;
import com.ao.model.user.LoggedUser;
import com.ao.model.worldobject.Item;
import com.ao.model.worldobject.WorldObject;
import com.ao.network.Connection;
import com.ao.network.DataBuffer;
import com.ao.network.packet.IncomingPacket;
import com.ao.network.packet.outgoing.ConsoleMessagePacket;
import com.ao.network.packet.outgoing.ObjectDeletePacket;

public class PickUpPacket implements IncomingPacket{

    
    @Override
    public boolean handle(DataBuffer buffer, Connection connection)
            throws IndexOutOfBoundsException, UnsupportedEncodingException {
        
        LoggedUser user = ((LoggedUser)connection.getUser());
        
        Tile tile = user.getPosition().getMap().getTile(user.getPosition().getX(), user.getPosition().getY());
        
        WorldObject object = tile.getWorldObject();
         
         if(object == null/*TODO or user is dead or user is trading*/)
             return true;

         if (user.getPrivileges().isCouncelor()) {

             connection.send(
                     new ConsoleMessagePacket("No puedes tomar ningún objeto.", Font.INFO) );
             
             return true;
         }
        
        if (object instanceof Item) {
            
            if( user.pickUp((Item)object) ) {
                
                tile.setWorldObject(null);
                
                connection.send( new ObjectDeletePacket(user.getPosition()) );
            }
        }
        return true;
    }

}
