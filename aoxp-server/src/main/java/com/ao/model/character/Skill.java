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

package com.ao.model.character;

/**
 * Defines available skills.
 */
public enum Skill {

    //LUCK //no existe más
    MAGIC,
    STEALING,
    COMBAT_TACTICS,
    HAND_TO_HAND_COMBAT,
    MEDITATION,
    STABBING,
    HIDING,
    SURVIVAL,
    LUMBER,
    TRADE,
    SHIELD_DEFENSE,
    FISHING,
    MINING,
    WOODWORKING,
    SMITHING,
    LEADERSHIP,
    TAMMING,
    RANGED_COMBAT,
    WRESTLING,
    SAILING;

    /**
     * The amount of existing skills.
     * 
     * TODO: I don't really like this...any better alternative?
     */
    public static final int AMOUNT = Skill.values().length;
    private static final Skill[] VALUES = Skill.values();

    /**
     * Maximum amount of points a character can earn for any given skill
     */
    public static final int MAX_SKILL_POINT= 100;

    /**
     * Retrieves the skill for the given index
     * @param index the index
     * @return the skill at the given index
     */
    public Skill get(byte index) {
        //TODO: must ensure index ranges between 0 and AMOUNT(not included)
        return VALUES[index];
    }


}
