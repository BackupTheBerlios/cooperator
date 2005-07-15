/*
 * Created on 11.06.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package Test.JUnitTests.ordinary;
// import java.util.*;


import de.tr1.cooperator.manager.mainframe.UserManager;
import junit.framework.TestCase;
import de.tr1.cooperator.model.mainframe.User;

/**
 * @author Carsten Lüdecke
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class UserManagerTest extends TestCase
{

	User Test = null;

	public static void main(String[] args)
	{
		junit.swingui.TestRunner.run(UserManagerTest.class);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		super.setUp();
		
		Test = 			new User( "Test",							//Login
								   User.cryptLikePW( "Test" ),		//PW
								   User.ADMIN,						//Role
								   "007",							//Personal number
								   "Administrator",					//first name
								   "ADMINISTRATOR",					//surname
								   "" );							//email
		
		UserManager.getInstance("/");
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception
	{
		super.tearDown();
		UserManager.getInstance().deleteUser( Test );
	}

	/*
	 * Class under test for UserManager getInstance(String)
	 * Class under test for UserManager getInstance()
	 */
	public void testUserManagerAndGetInstanceString() {
		try{
			UserManager.getInstance();
			assertTrue(true);
		}catch(Exception e)
		{
			assertTrue(false);	// bin ich mir nicht sicher, Begründung im EventTypeManager
								// selbe stelle
		}
	}

	public void testAddUserAndDeleteUser_StringAndDeleteUser_User()
	{
		private String Schtring = "Test";
		assertFalse(UserManager.getInstance().deleteUser(Schtring));
		assertFalse(UserManager.getInstance().deleteUser(Test));
		assertTrue(UserManager.getInstance().addUser(Test));
		assertTrue(UserManager.getInstance().deleteUser(Test));
		assertFalse(UserManager.getInstance().deleteUser(Schtring));
		assertTrue(UserManager.getInstance().addUser(Test));
		assertTrue(UserManager.getInstance().deleteUser(Schtring));
	}

	public void testGetUserAndGetAllUsers()
	{
		assertTrue(UserManager.getInstance().addUser(Test));
		assertEquals( UserManager.getInstance().getUser().getLogin(), Test.getLogin() );
		assertEquals( UserManager.getInstance().getAllUsers().size(), 1 );
		assertTrue(UserManager.getInstance().deleteUser(Test));
		assertEquals( UserManager.getInstance().getAllUsers().size(), 0 );
		assertNull( UserManager.getInstance().getUser() )		
	}

	public void testUpdateUser()
	{
		assertFalse(UserManager.getInstance().updateUser(Test));
		assertTrue(UserManager.getInstance().addUser(Test));
		assertTrue(UserManager.getInstance().updateUser(Test));
		assertTrue(UserManager.getInstance().deleteUser(Test));
		assertFalse(UserManager.getInstance().updateUser(Test));		
	}
	
}