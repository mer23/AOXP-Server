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

package com.ao.service.login;

import com.ao.config.ServerConfig;
import com.ao.context.ApplicationContext;
import com.ao.data.dao.AccountDAO;
import com.ao.data.dao.UserCharacterDAO;
import com.ao.data.dao.exception.DAOException;
import com.ao.data.dao.exception.NameAlreadyTakenException;
import com.ao.model.builder.UserCharacterBuilder;
import com.ao.model.character.Attribute;
import com.ao.model.character.Gender;
import com.ao.model.character.Race;
import com.ao.model.character.UserCharacter;
import com.ao.model.character.archetype.UserArchetype;
import com.ao.model.map.City;
import com.ao.model.user.Account;
import com.ao.model.user.ConnectedUser;
import com.ao.model.user.LoggedUser;
import com.ao.security.SecurityManager;
import com.ao.service.CharacterBodyService;
import com.ao.service.LoginService;
import com.ao.service.MapService;
import com.ao.service.UserService;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Default implementation of the login service.
 * An account is the exact same as a character.
 *
 * @author jsotuyod
 */
public class LoginServiceImpl implements LoginService {
	public static final String DAO_ERROR = "Ocurrió un error, intentá de nuevo.";
	public static final String CHARACTER_NOT_FOUND_ERROR = "El personaje no existe.";
	public static final String CLIENT_OUT_OF_DATE_ERROR_FORMAT = "Esta versión del juego es obsoleta, la versión correcta es %s. La misma se encuentra disponible en http://www.argentumonline.com.ar/.";
	public static final String CORRUPTED_CLIENT_ERROR = "El cliente está dañado, por favor descárguelo nuevamente desde http://www.argentumonline.com.ar/";
	public static final String BANNED_CHARACTER_ERROR = "Se te ha prohibido la entrada a Argentum debido a tu mal comportamiento. Puedes consultar el reglamento y el sistema de soporte desde www.argentumonline.com.ar";
	public static final String INCORRECT_PASSWORD_ERROR = "Contraseña incorrecta.";
	public static final String CHARACTER_CREATION_DISABLED_ERROR = "La creación de personajes en este servidor se ha deshabilitado momentaneamente.";
	public static final String ONLY_ADMINS_ERROR = "Servidor restringido a administradores. Consulte la página oficial o el foro oficial para más información.";
	public static final String MUST_THROW_DICES_BEFORE_ERROR = "Debe tirar los dados antes de poder crear un personaje.";
	public static final String INVALID_RACE_ERROR = "La raza seleccionada no es válida.";
	public static final String INVALID_GENDER_ERROR = "El género seleccionado no es válido.";
	public static final String INVALID_ARCHETYPE_ERROR = "La clase seleccionada no es válida.";
	public static final String INVALID_SKILLS_POINTS_ERROR = "Los skills asignados no son válidos.";
	public static final String ACCOUNT_NAME_TAKEN_ERROR = "Ya existe el personaje.";
	public static final String CHARACTER_IS_LOGGED_IN = "El personaje está conectado.";
	public static final String INVALID_HEAD_ERROR = "La cabeza seleccionada no es válida.";
	public static final String INVALID_BODY_ERROR = "No existe un cuerpo para la combinación seleccionada.";
	public static final String INVALID_HOMELAND_ERROR = "El hogar seleccionado no es válido.";

	private String[] clientHashes;

	private final AccountDAO accDAO;
	private final UserCharacterDAO charDAO;
	private final ServerConfig config;
	private String currentClientVersion;
	private final int initialAvailableSkills;
	private final UserService userService;
	private final CharacterBodyService characterBodyService;
	private final MapService mapService;

	@Inject
	public LoginServiceImpl(AccountDAO accDAO, UserCharacterDAO charDAO,
			ServerConfig config, UserService userService,
			CharacterBodyService characterBodyService, MapService mapService,
			@Named("initialAvailableSkills") int initialAvailableSkills) {
		this.accDAO = accDAO;
		this.charDAO = charDAO;
		this.config = config;
		this.initialAvailableSkills = initialAvailableSkills;
		this.userService = userService;
		this.characterBodyService = characterBodyService;
		this.mapService = mapService;
		currentClientVersion = config.getVersion();
	}

	@Override
	public void connectNewCharacter(ConnectedUser user, String username, String password, byte bRace,
			byte bGender, byte bArchetype, int head, String mail,
			byte bHomeland, String clientHash,
			String version) throws LoginErrorException {

		checkClient(clientHash, version);

		if (!config.isCharacterCreationEnabled()) {
			throw new LoginErrorException(CHARACTER_CREATION_DISABLED_ERROR);
		}

		if (config.isRestrictedToAdmins()) {
			throw new LoginErrorException(ONLY_ADMINS_ERROR);
		}

		//TODO: Check to avoid mass characters creation for this IP


		if (user.getAttribute(Attribute.AGILITY) == null) {
			throw new LoginErrorException(MUST_THROW_DICES_BEFORE_ERROR);
		}

		UserCharacterBuilder userCharacterBuilder = new UserCharacterBuilder();

		Race race;
		try {
			race = Race.get(bRace);
		} catch(ArrayIndexOutOfBoundsException e) {
			throw new LoginErrorException(INVALID_RACE_ERROR);
		}

		Gender gender;
		try {
			gender = Gender.get(bGender);
		} catch(ArrayIndexOutOfBoundsException e) {
			throw new LoginErrorException(INVALID_GENDER_ERROR);
		}

		City homeland = mapService.getCity(bHomeland);
		if (homeland == null){
			throw new LoginErrorException(INVALID_HOMELAND_ERROR);
		}

		UserArchetype archetype;
		try {
			archetype = UserArchetype.get(bArchetype);
		} catch(ArrayIndexOutOfBoundsException e) {
			throw new LoginErrorException(INVALID_ARCHETYPE_ERROR);
		}

		if (!characterBodyService.isValidHead(head, race, gender)) {
			throw new LoginErrorException(INVALID_HEAD_ERROR);
		}

		// Get default body
		int body = characterBodyService.getBody(race, gender);

		if (body == 0) {
			throw new LoginErrorException(INVALID_BODY_ERROR);
		}

		try {
			userCharacterBuilder.withName(username)
				.withEmail(mail)
				.withGender(gender)
				.withCity(homeland)
				.withRace(race)
				.withArchetype(archetype)
				.withHead(head)
				.withBody(body);
		} catch (Exception e) {
			throw new LoginErrorException(e.getMessage());
		}


		// First, we have to create the new account.
		Account acc;
		
		try {
			acc = accDAO.create(username, password, mail);
		} catch(NameAlreadyTakenException e) {
			throw new LoginErrorException(ACCOUNT_NAME_TAKEN_ERROR);

		} catch (DAOException e) {
			accDAO.delete(username);

			throw new LoginErrorException(DAO_ERROR);
		}


		// Once we have the account, lets create the character itself!
		UserCharacter chara;
		
		try {
			chara= charDAO.create(username, race, gender, archetype,
			        head, homeland, user.getAttribute(Attribute.STRENGTH),
					user.getAttribute(Attribute.AGILITY), user.getAttribute(Attribute.INTELLIGENCE),
					user.getAttribute(Attribute.CHARISMA), user.getAttribute(Attribute.CONSTITUTION),
					initialAvailableSkills, body);
			
		} catch (DAOException e) {
			accDAO.delete(username);

			throw new LoginErrorException(e.getMessage());
		}
		
		acc.addCharacter(username);
		user.setAccount(acc);	
		acc.setLoggedCharacter((LoggedUser)chara);

	    userService.logIn(user); //logs the character in.
	    

	    
//	    setPosition(new Position(homeland.getX(), homeland.getY(), mapService.getMap((byte)homeland.getMap())));
	        
	    mapService.putCharacterAtPos(chara, chara.getPosition()); //places character in the world.
	}

	@Override
	public void connectExistingCharacter(ConnectedUser user, String name, String password, String version,
			String clientHash) throws LoginErrorException {

		checkClient(clientHash, version);

		if (config.isRestrictedToAdmins()) {
			throw new LoginErrorException(ONLY_ADMINS_ERROR);
		}

		Account acc;

		try {
			acc = accDAO.retrieve(name);
		} catch (DAOException e) {
			throw new LoginErrorException(DAO_ERROR);
		}

		//TODO : Is the ip in use?

		if (acc == null) {
			throw new LoginErrorException(CHARACTER_NOT_FOUND_ERROR);
		}

		if (!acc.authenticate(password)) {
			throw new LoginErrorException(INCORRECT_PASSWORD_ERROR);
		}

		if (acc.isBanned()) {
			throw new LoginErrorException(BANNED_CHARACTER_ERROR);
		}

		if (userService.isLoggedIn(user)) {
			throw new LoginErrorException(CHARACTER_IS_LOGGED_IN);
		}

		// TODO : Add ip to connected ips

		// TODO : If user is a GM, log it to admin's log with it's ip.

		// TODO : Do something with the account!!!

		UserCharacter chara;

		// TODO : send all data!

		user.setAccount(acc); //associates the account with the user

	    try {
	        chara= (LoggedUser) charDAO.load(name); // loads the character.
	            
	    } catch (DAOException e) {
	          throw new LoginErrorException(e.getMessage());
	    }
	    
	    acc.setLoggedCharacter((LoggedUser)chara);
	       
	    userService.logIn(user); //adds character to list of logged characters.
	    
	    mapService.putCharacterAtPos(chara, chara.getPosition()); //places character in the world.
	}

	/**
	 * Sets the current client's version.
	 * @param version The new client version.
	 */
	public void setCurrentClientVersion(String version) {
		// TODO: Update the config!
		currentClientVersion = version;
	}

	/**
	 * Checks if the given hash matches any of the valid hashes and if the given version is up to date.
	 * @param hash 	The hash to check.
	 * @param version The client's version.
	 * @throws LoginErrorException
	 */
	private void checkClient(String hash, String version) throws LoginErrorException {

		if (!currentClientVersion.equals(version)) {
			throw new LoginErrorException(String.format(CLIENT_OUT_OF_DATE_ERROR_FORMAT, currentClientVersion));
		}

		if (clientHashes == null) {
			clientHashes = ApplicationContext.getInstance(SecurityManager.class).getValidClientHashes();
		}

		if (clientHashes.length < 1) {
			return;
		}

		for (String validHash : clientHashes) {
			if (hash.equals(validHash)) {
				return;
			}
		}

		throw new LoginErrorException(CORRUPTED_CLIENT_ERROR);
	}

}
