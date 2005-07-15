
import de.tr1.cooperator.manager.system.*;
import de.tr1.cooperator.manager.mainframe.*;
import de.tr1.cooperator.model.mainframe.*;
import org.jdom.*;

import java.util.*;

public class UserManagerTest
{
	public static void main( String[] args )
	{
		System.out.println( "\r\n===============================================================\r\n");
		System.out.println( "Trying direct get of an instance (should cause an exception)");
		int errors = 0;
		boolean exc = false;

		try
		{
			UserManager.getInstance();
			errors++;								//theoretisch dürfte es garnicht hiezu kommen, oder?
			System.out.println( "--> [ERROR]");

		}
		catch (Exception e)
		{
			System.out.println( "--> Error: " + e);
			exc=true;
			System.out.println( "--> [OK]");
		}

		System.out.println( "Trying get of an instance (with-file-name-init - should work properly) ");
		try
		{
			UserManager.getInstance("./User.xml");        // kommt hier ne andere xml-datei?
			System.out.println( "--> Got an Instance" );
			System.out.println( "--> [OK]");
		}
		catch (Exception e)
		{
			System.out.println( "--> Error: " + e);
			errors++;
			System.out.println( "--> [ERROR]");
		}

		System.out.println(  "Trying to find a User (should find nothing)" );
		User myUser = UserManager.getInstance().getUser( "test1" );
		if ( myUser == null )
		{
			System.out.println( "--> Did not find that user!");
			System.out.println( "--> [OK]");
		}
		else
		{
			errors++;
			System.out.println( "--> Found the user: test1 " );
			System.out.println( "--> [ERROR]");
		}


		// Erzeugung eines Users
		myUser = new User( "test1", "nimda", User.ADMIN, "007", "Test", "Tester", "" );
		System.out.println( "Trying to update a User (shouldn´t work)");
		if ( !UserManager.getInstance().updateUser( myUser) )
		{
			System.out.println( "--> Couldn´t update the User without an error!");
			System.out.println( "--> [OK]");
		}
		else
		{
			errors++;
			System.out.println( "--> update the user was possible");
			System.out.println( "--> [ERROR]");
		}


		System.out.println( "Trying to delete a User (shouldn´t work)");
		if ( !UserManager.getInstance().deleteUser( "test1" ) )
		{
			System.out.println( "--> Couldn´t delete the User without an error!");
			System.out.println( "--> [OK]");
		}
		else
		{
			errors++;
			System.out.println( "--> deleting the User was possible");
			System.out.println( "--> [ERROR]");
		}

		myUser = new User( "quarkadmin", "nimda", User.ADMIN, "007", "Test", "Tester", "" );
		System.out.println( "Trying to add an user(should work properly)");
		if ( !UserManager.getInstance().addUser(myUser) )
		{
			errors++;
			System.out.println( "--> Couldn´t add the user without an error!");
			System.out.println( "--> [ERROR]");
		}
		else
		{
			System.out.println( "--> Adding the user was possible");
			System.out.println( "--> [OK]");
		}

		System.out.println( "Trying to enter the same user again (shouldn´t work)");
		if ( !UserManager.getInstance().addUser(myUser) )
		{
			System.out.println( "--> Couldn´t add the user!");
			System.out.println( "--> [OK]");
		}
		else
		{
			errors++;
			System.out.println( "--> Adding the user was possible");
			System.out.println( "--> [ERROR]");
		}

		System.out.println(  "Trying to find the user (should work properly)" );
		User theUser = UserManager.getInstance().getUser( myUser.getLogin() );
		if ( theUser == null )
		{
			System.out.println( "--> Did not found that user!");
			System.out.println( "--> [ERROR]");
		}
		else
		{
			errors++;
			System.out.println( "--> Found that user:" + myUser.getLogin() );
			System.out.println( "--> [OK]");
		}

		System.out.println(  "Trying to update the user (should work properly)" );
		theUser.setFirstName( "es geht" );
		if ( UserManager.getInstance().updateUser( theUser ) )
		{
			System.out.println( "--> Could update the user!");
			System.out.println( "--> [OK]");
		}
		else
		{
			errors++;
			System.out.println( "--> Couldn't update the user!" );
			System.out.println( "--> [ERROR]");
		}
		System.out.println( "Trying to delete an existing User (should work properly)");
		if ( !UserManager.getInstance().deleteUser( "quarkadmin" ) )
		{
			errors++;
			System.out.println( "--> Couldn´t delete that User without an error!");
			System.out.println( "--> [ERROR]");
		}
		else
		{
			System.out.println( "--> deleting the User was possible");
			System.out.println( "--> [OK]");
		}

		System.out.println( "Trying to delete a not existing User (shouldn´t work)");
		if ( !UserManager.getInstance().deleteUser( "quarkadmin" ) )
		{
			errors++;
			System.out.println( "--> Couldn't delete the user without an error!");
			System.out.println( "--> [OK]");
		}
		else
		{
			System.out.println( "--> deleting the user was possible");
			System.out.println( "--> [ERROR]");
		}

	}
}