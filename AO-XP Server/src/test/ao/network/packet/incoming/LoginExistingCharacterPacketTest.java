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

package ao.network.packet.incoming;

import org.easymock.classextension.EasyMock;
import org.junit.Before;
import org.junit.Test;

import ao.config.ServerConfig;
import ao.context.ApplicationContext;
import ao.mock.MockFactory;
import ao.network.Connection;
import ao.network.DataBuffer;
import ao.network.packet.IncomingPacket;
import ao.security.Hashing;
import ao.service.LoginService;
import ao.service.login.LoginServiceImpl;

public class LoginExistingCharacterPacketTest {

	private static final String BANNED_CHARACTER_NAME = "banned";
	private static final String BANNED_CHARACTER_PASSWORD = "a";
	private static final String CHARACTER_NAME = "test";
	private static final String CHARACTER_PASSWORD = "a";
	private static final byte CLIENT_MAJOR = 0;
	private static final byte CLIENT_MINOR = 12;
	private static final byte CLIENT_VERSION = 2;
	
	Connection connection;
	IncomingPacket packet;
	ServerConfig config = ApplicationContext.getInstance(ServerConfig.class);
	
	@Before
	public void setUp() throws Exception {
		packet = new LoginExistingCharacterPacket();
		connection = MockFactory.mockConnection();
		
		config.setRestrictedToAdmins(false);
	}

	private void writeLogin(String charName, String password, short major, short minor, short version, String hash, String error) throws Exception {
		DataBuffer buffer = connection.getInputBuffer();
		DataBuffer outBuffer = connection.getOutputBuffer();

		EasyMock.expect(buffer.getASCIIString()).andReturn(charName).once();
		EasyMock.expect(buffer.getASCIIString()).andReturn(password).once();
		EasyMock.expect(buffer.getShort()).andReturn(major).once();
		EasyMock.expect(buffer.getShort()).andReturn(minor).once();
		EasyMock.expect(buffer.getShort()).andReturn(version).once();
		
		if (ApplicationContext.SECURITY_ENABLED) {
			EasyMock.expect(buffer.getASCIIStringFixed(Hashing.MD5_BINARY_LENGTH)).andReturn(hash).once();
		}
		
		EasyMock.replay(buffer);
		
		if (error.length() > 0) {
			EasyMock.expect(outBuffer.put(EasyMock.anyByte())).andReturn(outBuffer).once();
			EasyMock.expect(outBuffer.putASCIIString(error)).andReturn(outBuffer).once();
		}
		
		EasyMock.replay(outBuffer);
	}
	
	@Test
	public void testHandleRestrictedToAdminsTest() throws Exception {
		config.setRestrictedToAdmins(true);
		
		writeLogin(CHARACTER_NAME, CHARACTER_PASSWORD,
				CLIENT_MAJOR, CLIENT_MINOR, CLIENT_VERSION, "", LoginServiceImpl.ONLY_ADMINS_ERROR);
		packet.handle(connection);
		
		EasyMock.verify(connection.getOutputBuffer());
	}
	
	@Test
	public void testHandleCharacterNotFound() throws Exception {
		writeLogin("foo", "foo", CLIENT_MAJOR, CLIENT_MINOR, CLIENT_VERSION, "", LoginServiceImpl.CHARACTER_NOT_FOUND_ERROR);
		packet.handle(connection);
		
		EasyMock.verify(connection.getOutputBuffer());
	}
	
	@Test
	public void testHandleIncorrectPassword() throws Exception {
		writeLogin(CHARACTER_NAME, CHARACTER_PASSWORD + "foo", CLIENT_MAJOR, CLIENT_MINOR, CLIENT_VERSION, "", LoginServiceImpl.INCORRECT_PASSWORD_ERROR);
		packet.handle(connection);
		
		EasyMock.verify(connection.getOutputBuffer());
	}
	
	@Test
	public void testHandleOutOfDateClient() throws Exception {
		LoginServiceImpl service = (LoginServiceImpl) ApplicationContext.getInstance(LoginService.class);
		service.setCurrentClientVersion(CLIENT_MAJOR + "." + CLIENT_MINOR + "." + CLIENT_VERSION);
		
		writeLogin(CHARACTER_NAME, CHARACTER_PASSWORD, (short) 0, (short) 0,(short) 0, "", String.format(LoginServiceImpl.CLIENT_OUT_OF_DATE_ERROR_FORMAT, CLIENT_MAJOR + "." + CLIENT_MINOR + "." + CLIENT_VERSION));
		packet.handle(connection);
		
		EasyMock.verify(connection.getOutputBuffer());
	}
	
	@Test
	public void testHandleCorruptedClient() throws Exception {
		if (!ApplicationContext.SECURITY_ENABLED) {
			return;
		}
		
		writeLogin(CHARACTER_NAME, CHARACTER_PASSWORD,
				CLIENT_MAJOR, CLIENT_MINOR, CLIENT_VERSION, "foo", LoginServiceImpl.CORRUPTED_CLIENT_ERROR);
		packet.handle(connection);
		
		EasyMock.verify(connection.getOutputBuffer());
	}
	
	@Test
	public void testHandleBannedCharacter() throws Exception {
		writeLogin(BANNED_CHARACTER_NAME, BANNED_CHARACTER_PASSWORD,
						CLIENT_MAJOR, CLIENT_MINOR, CLIENT_VERSION, "", LoginServiceImpl.BANNED_CHARACTER_ERROR);
		packet.handle(connection);
		
		EasyMock.verify(connection.getOutputBuffer());
	}

}
