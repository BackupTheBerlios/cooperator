
import de.tr1.cooperator.manager.system.*;
import de.tr1.cooperator.manager.mainframe.*;
import de.tr1.cooperator.model.mainframe.*;
import org.jdom.*;

public class EventTypeManagerTest
{
	public static void main( String[] args )
	{
		System.out.println( "\r\n===============================================================\r\n");
		System.out.println( "Trying direct get of an instance (should cause an exception)");
		int errors = 0;
		try
		{
			EventTypeManager.getInstance();
			errors++;
			System.out.println( "--> [ERROR]");
		}
		catch (Exception e)
		{
			System.out.println( "--> Error: " + e);
			System.out.println( "--> [OK]");
		}

		System.out.println( "Trying get of an instance (with-file-name-init - should work properly) ");
		try
		{
			EventTypeManager.getInstance("./Types.xml");
			System.out.println( "--> Got an Instance" );
			System.out.println( "--> [OK]");
		}
		catch (Exception e)
		{
			System.out.println( "--> Error: " + e);
			errors++;
			System.out.println( "--> [ERROR]");
		}

		System.out.println( "Trying to find a type (should find nothing)");
		String name = EventTypeManager.getInstance().getEventName(13);
		if (name==null)
		{
			System.out.println( "--> Did not found that event!");
			System.out.println( "--> [OK]");
		}
		else
		{
			errors++;
			System.out.println( "--> Found that event: "+name);
			System.out.println( "--> [ERROR]");
		}

		System.out.println( "Trying to enter a type (should work properly)");
		if (!EventTypeManager.getInstance().add( "Test-Event", 13) )
		{
			errors++;
			System.out.println( "--> Could not enter that event!");
			System.out.println( "--> [ERROR]");
		}
		else
		{
			System.out.println( "--> Entered that event!");
			System.out.println( "--> [OK]");
		}

		System.out.println( "Trying to enter a the same type again (should not work)");
		if (EventTypeManager.getInstance().add( "Test-Event", 13) )
		{
			errors++;
			System.out.println( "--> Could enter that event without an error!");
			System.out.println( "--> [ERROR]");
		}
		else
		{
			System.out.println( "--> Entering the event was not possible");
			System.out.println( "--> [OK]");
		}

		System.out.println( "Trying to update the event (should work)");
		if (EventTypeManager.getInstance().update( "Test-Event-update", 13) )
		{
			System.out.println( "--> Could update that event without an error!");
			System.out.println( "--> [OK]");
		}
		else
		{
			errors++;
			System.out.println( "--> Updating the event was not possible");
			System.out.println( "--> [ERROR]");
		}

		System.out.println( "Trying to find a free ID");
		int id = EventTypeManager.getInstance().getFreeID( );
		if ( id != -1 )
		{
			System.out.println( "--> Found free id: "+id);
			System.out.println( "--> [OK]");
		}
		else
		{
			errors++;
			System.out.println( "--> Entering the event was not possible");
			System.out.println( "--> [ERROR]");
		}

		System.out.println( "Trying to remove that (should work)");
		if (EventTypeManager.getInstance().delete( 13) )
		{
			System.out.println( "--> Could remove that event without an error!");
			System.out.println( "--> [OK]");
		}
		else
		{
			errors++;
			System.out.println( "--> Removing the event was not possible");
			System.out.println( "--> [ERROR]");
		}

		System.out.println( "Trying to remove that type again (should not work)");
		if (!EventTypeManager.getInstance().delete( 13) )
		{
			System.out.println( "--> Could not remove that event!");
			System.out.println( "--> [OK]");
		}
		else
		{
			errors++;
			System.out.println( "--> Removing the event was possible");
			System.out.println( "--> [ERROR]");
		}
		System.out.println( "\r\nErrors:"+errors+"\r\n===============================================================\r\n");
	}
}