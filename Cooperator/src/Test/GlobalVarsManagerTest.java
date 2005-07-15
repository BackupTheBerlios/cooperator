
import de.tr1.cooperator.manager.system.*;
import de.tr1.cooperator.manager.mainframe.*;
import de.tr1.cooperator.model.mainframe.*;
import org.jdom.*;

import java.util.*;
import java.util.regex.*;

public class GlobalVarsManagerTest
{
	public static void main( String[] args )
	{
		System.out.println( "\r\n===============================================================\r\n");
		System.out.println( "Running Test for GobalVarsManager");
		System.out.println( "Trying direct get of an instance (should cause an exception)");
		Log.getInstance("./Log.txt");
		int errors = 0;
		boolean exc = false;

		try
		{
			GlobalVarsManager.getInstance();
			errors++;
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
			GlobalVarsManager.getInstance("./GlobalVars.xml");
			System.out.println( "--> Got an Instance" );
			System.out.println( "--> [OK]");
		}
		catch (Exception e)
		{
			System.out.println( "--> Error: " + e);
			errors++;
			System.out.println( "--> [ERROR]");
		}

		System.out.println(  "Trying to find the global-var-section an grab the force-value for user-mails" );
		boolean force = GlobalVarsManager.getInstance().forceUserMails( );
		if ( force )
		{
			System.out.println( "--> Forcing enabled!");
			System.out.println( "--> [OK]");
		}
		else
		{
			errors++;
			System.out.println( "--> Forcing disabled or not found" );
			System.out.println( "--> [ERROR]");
		}

		System.out.println(  "Trying to set a user-host" );
		if ( GlobalVarsManager.getInstance().addUserMail( "gmx.de" ) )
		{
			System.out.println( "--> Mail-Host added");
			System.out.println( "--> [OK]");
		}
		else
		{
			errors++;
			System.out.println( "--> Could not add host" );
			System.out.println( "--> [ERROR]");
		}

		System.out.println(  "Trying to set the same user-host again (should fail)" );
		if ( GlobalVarsManager.getInstance().addUserMail( "gmx.de" ) )
		{
			System.out.println( "--> Mail-Host added");
			System.out.println( "--> [ERROR]");
			errors++;
		}
		else
		{
			System.out.println( "--> Could not add host" );
			System.out.println( "--> [OK]");
		}

		System.out.println(  "Trying to get all user-hosts" );
		Collection users = GlobalVarsManager.getInstance().getUserMails( );
		Iterator it = users.iterator();
		if ( it.hasNext() )
		{
			System.out.println( "--> Mail-Host found: "+(String)(it.next()) );
			System.out.println( "--> [OK]");
		}
		else
		{
			errors++;
			System.out.println( "--> Did not find any hosts" );
			System.out.println( "--> [ERROR]");
		}

		System.out.println(  "Trying to delete a user-host" );
		if ( GlobalVarsManager.getInstance().removeUserMail( "gmx.de" ) )
		{
			System.out.println( "--> Mail-Host removed");
			System.out.println( "--> [OK]");
		}
		else
		{
			errors++;
			System.out.println( "--> Could not remove host" );
			System.out.println( "--> [ERROR]");
		}

		System.out.println(  "Trying to delete the same user-host again (should fail)" );
		if ( GlobalVarsManager.getInstance().removeUserMail( "gmx.de" ) )
		{
			System.out.println( "--> Mail-Host removed");
			System.out.println( "--> [ERROR]");
			errors++;
		}
		else
		{
			System.out.println( "--> Could not remove host" );
			System.out.println( "--> [OK]");
		}

		System.out.println(  "Trying to get all user-hosts (should be none there now)" );
		users = GlobalVarsManager.getInstance().getUserMails( );
		it = users.iterator();
		if ( it.hasNext() )
		{
			System.out.println( "--> Mail-Host found: "+(String)(it.next()) );
			System.out.println( "--> [ERROR]");
			errors++;
		}
		else
		{
			System.out.println( "--> Did not find any hosts" );
			System.out.println( "--> [OK]");
		}
		System.out.println( "\r\nErrors: "+errors+"\r\n===============================================================\r\n");
	}
}