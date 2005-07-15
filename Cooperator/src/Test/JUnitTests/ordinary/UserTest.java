package Test.JUnitTests.ordinary;
import de.tr1.cooperator.model.mainframe.User;
import java.util.*;
import junit.framework.TestCase;

/**
 * Will test User.class
 *
 * @author Sven Fischer
 * @version 05-06-04@17:00
 *
 */

public class UserTest extends TestCase
{
	User user1;
	User user2;
	User user4;

			protected void setUp()
	{
		user1 = new User("Student","S[TPW<",1,"9242316","Franz","Mustermann","mustermann@provider.de");
		user2 = new User("Lecturer","L!ECPW]",2,"9242316","Franz","Mustermann","mustermann@provider.de");
		user4 = new User("Admin","ADMP]W<",4,"9242316","Franz","Mustermann","mustermann@provider.de");
	}

	public void testCheckPasswort( )
	{
	}

	public void testCheckPasswortcryptLikePW( )
	{
	}

	public void testSetAndGetRights()
	{
		int expected1 = 4;
		int expected2 = 2;
		int expected4 = 1;
		user1.setRights( 4 );
		user4.setRights( 1 );
		assertEquals(expected1, user1.getRights( ));
		assertEquals(expected2, user2.getRights( ));
		assertEquals(expected4, user4.getRights( ));
	}

	public void testGetRightsAsString()
	{
		String expectedStud = " Student";
		String expectedLect = " Dozent";
		String expectedUser = " Administrator";
		assertEquals(expectedStud, user1.getRightsAsString( ));
		assertEquals(expectedLect, user2.getRightsAsString( ));
		assertEquals(expectedUser, user4.getRightsAsString( ));
	}

	public void testSetAndGetPasswort()
	{
		String expectedPW = "[STP<W";
		user1.setFirstName( "[STP<W" );
		assertEquals(expectedPW, user1.getPassword( ));
	}

	public void testSetAndGetFirstName()
	{
		String expected = "Franziska";
		user1.setFirstName( "Franziska" );
		assertEquals(expected, user1.getFirstName( ));
	}

		public void testSetAndGetLogin()
	{
		String expected = "Admin";
		user1.setLogin( "Admin" );
		assertEquals(expected, user1.getLogin( ));
	}

		public void testSetAndGetSurname()
	{
		String expected = "Schmidt";
		user1.setSurname( "Schmidt" );
		assertEquals(expected, user1.getSurname( ));
	}

		public void testSetAndGetPesonalNumber()
	{
		String expected = "8241336";
		user1.setPersonalNumber( "8241336" );
		assertEquals(expected, user1.getPersonalNumber( ));
	}

			public void testSetAndGetEmailAddress()
	{
		String expected = "mail@mustermann.com";
		user1.setEmailAddress( "mail@mustermann.com" );
		assertEquals(expected, user1.getEmailAddress( ));
	}
}