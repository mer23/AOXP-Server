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
import com.ao.model.user.ConnectedUser;
import com.ao.model.user.LoggedUser;
import com.ao.model.worldobject.AbstractItem;
import com.ao.model.worldobject.Item;
import com.ao.network.Connection;
import com.ao.network.DataBuffer;
import com.ao.network.packet.IncomingPacket;
import com.ao.network.packet.outgoing.ConsoleMessagePacket;
import com.ao.network.packet.outgoing.ObjectCreatePacket;

public class DropPacket implements IncomingPacket{

    @Override
    public boolean handle(DataBuffer buffer, Connection connection)
            throws IndexOutOfBoundsException, UnsupportedEncodingException {
        
        if (buffer.getReadableBytes() < 3)
            return false;
        
        LoggedUser user= ((ConnectedUser)connection.getUser()).getAccount().getLoggedCharacter();
        
        //TODO should we send a message to a counselor trying to pick up an item?
        if (user.getPrivileges().isCouncelor() || user.isSailing()/*TODO or dead, or trading*/)
            return true;
        
        int amount = buffer.getInt();
        
        if (amount > 0) {
            
            byte slot= buffer.get();                            
            Tile tile= user.getPosition().getMap().getTile(user.getPosition().getX(), user.getPosition().getY());
            
            if (tile.getWorldObject() == null) {
                
                tile.setWorldObject( user.getInventory().removeItem(slot, amount) );              
                connection.send( new ObjectCreatePacket(tile.getWorldObject(), user.getPosition()) );
                
                return true;
            }
            
            if ( tile.getWorldObject().getId() == user.getInventory().getItem(slot).getId() && 
                    ((Item) tile.getWorldObject()).getAmount() + amount <= AbstractItem.MAX_STACKED_ITEMS ) {
            
                user.getInventory().removeItem(slot, amount);
                
                ( (Item) tile.getWorldObject() ).addAmount(amount);
               
                return true;
            }
            
            connection.send (new ConsoleMessagePacket("No hay espacio en el piso", Font.INFO));              
        }    
        
        return true;
    }

}
