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

import com.ao.model.character.archetype.Archetype;
import com.ao.model.map.City;

public interface UserCharacter extends Character {

    /**
     * Retrieves the user's requested skill points.
     * @param skill    The skill.
     * @return    The amount of skill points.
     */
    int getSkill(Skill skill);

    /**
     * Retrieves the experience needed to level up the skill.
     * ELU stands for Experience Level Up.
     * @param skill    The skill.
     * @return the experience needed to level up the skill.
     */
    int getSkillELU(Skill skill);

    /**
     * Retrieves the skill's accumulated experience.
     * @param skill    The skill.
     * @return the skill's progress.
     */
    int getSkillExp(Skill skill);

    /**
     * Adds points to the given user's skill. 
     * @param skill        The skill to add points.
     * @param points    The points to add.
     */
    void addToSkill(Skill skill, byte points);

    /**
     * Adds experience points to this skill.
     * @param skill    The skill.
     * @param points the experience points to add.
     */
    void addToSkillExp(Skill skill, byte points);

    /**
     * Checks if the user is working.
     * @return True if the user is working, false otherwise.
     */
    boolean isWorking();

    /**
     * Sets the user to work.
     */
    void work();

    /**
     * Retrieves the user's guild name.
     * @return The user's guild name.
     */
    String getGuildName();

    /**
     * Sets the user's guild name.
     * @param name The guild name to be set.
     */
    void setGuildName(String name);

    /**
     * Retrieves the user's party id.
     * @return The user's party id if the user belongs to one, -1 otherwise.
     */
    int getPartyId();

    /**
     * Sets the user's party id.
     * @param id The party id to be set.
     */
    void setPartyId(int id);

    /**
     * Retrieves the user's race.
     * @return The user's race.
     */
    Race getRace();

    /**
     * Retrieves the user's gender.
     * @return The user's gender.
     */
    Gender getGender();

    /**
     * Retrieves the user's archetype.
     * @return The user's archetype.
     */
    Archetype getArchetype();

    /**
     * Retrieves the user's homeland.
     * @return the user's homeland.
     */
    City getHomeland();
    
    /**
     * Sets user's new homeland.
     * @param homeland the new homeland.
     */
    void setHomeland(City homeland);
    /**
     * Retrieves the user's stamina.
     * @return The user's stamina.
     */
    public int getStamina();

    /**
     * Sets the user's stamina.
     * @param stamina The stamina
     */
    public void setStamina(int stamina);

    /**
     * Retrieves the user's max stamina.
     * @return The user's max stamina.
     */
    public int getMaxStamina();

    /**
     * Sets the user's max stamina.
     * @param maxStamina The max stamina.
     */
    public void setMaxStamina(int maxStamina);

    /**
     * Checks if the user is meditating.
     * @return True if the user is meditating, false otherwise.
     */
    boolean isMeditating();

    /**
     * Sets the user to meditate.
     * @param meditating The boolean value.
     */
    void setMeditate(boolean meditating);
}
