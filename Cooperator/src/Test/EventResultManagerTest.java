
import de.tr1.cooperator.manager.system.*;
import de.tr1.cooperator.manager.mainframe.*;
import de.tr1.cooperator.model.mainframe.*;
import org.jdom.*;

import java.util.*;

public class EventResultManagerTest
{
	public static void main( String[] args )
	{
		System.out.println( "\r\n===============================================================\r\n");
		System.out.println( "Trying direct get of an instance (should cause an exception)");
		int errors = 0;
		Log.getInstance("log.txt");
		Log.addLog("Starting Test for EventResultManager");

		try
		{
			EventResultManager.getInstance();
			errors++;								//theoretisch dürfte es garnicht hiezu kommen, oder?
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
			EventResultManager.getInstance("./");        // kommt hier ne andere xml-datei?
			System.out.println( "--> Got an Instance" );
			System.out.println( "--> [OK]");
		}
		catch (Exception e)
		{
			System.out.println( "--> Error: " + e);
			errors++;
			System.out.println( "--> [ERROR]");
		}



		System.out.println(  "Trying to get the Results from a Event with getResults() (shouldn´d work) " );
		Collection Ergebnisse = EventResultManager.getInstance().getResults(5);
		Iterator ergs = Ergebnisse.iterator();
		if ( Ergebnisse.size() == 0 )
		{
			System.out.println( "--> Did not find the Results!");
			System.out.println( "--> [OK]");
		}
		else
		{
			System.out.println( "--> Found the Results:" +  ((ExamResult)ergs.next()).getResult()  );
			System.out.println( "--> [ERROR]");
			errors++;
		}

		ExamResult bestanden = new ExamResult("Klaus", 5, 3.0  );

		System.out.println(  "Trying to add a Result with \"addResult()\" (should work properly) " );
		if ( EventResultManager.getInstance().addResult( bestanden ) )
		{
			System.out.println( "--> Result Successfully added");
			System.out.println( "--> [OK]");
		}
		else
		{
			errors++;
			System.out.println( "--> scheiße hat net gefuntz");
			System.out.println( "--> [ERROR]");
		}

		System.out.println(  "Trying to get the Results from a Event with getResults() (should work properly) " );
		Ergebnisse = EventResultManager.getInstance().getResults(5);
		ergs = Ergebnisse.iterator();
		if ( Ergebnisse.size() == 0 )
		{
			errors++;
			System.out.println( "--> Did not find the Results!");
			System.out.println( "--> [ERROR]");
		}
		else
		{
			System.out.println( "--> Found the Results:" +  ((ExamResult)ergs.next()).getResult()  );
			System.out.println( "--> [OK]");
		}


		System.out.println(  "Trying to get the Results from a Event with getResult() (should work properly) " );
		ExamResult Note = EventResultManager.getInstance().getResult(bestanden.getUserPersonalNumber(), 5);
		if ( Note == null )
 		{
			errors++;
			System.out.println( "--> Did not find the Results!");
			System.out.println( "--> [ERROR]");
		}
		else
		{
			System.out.println( "--> Found the Results:" +  Note.getResult()  );
			System.out.println( "--> [OK]");
		}

		System.out.println(  "Trying to add an other Result with \"addResult()\" (should work properly) " );
		if ( EventResultManager.getInstance().addResult( new ExamResult("phillip", 5, 1.3) ) )
		{
			System.out.println( "--> Result Successfully added");
			System.out.println( "--> [OK]");
		}
		else
		{
			errors++;
			System.out.println( "--> scheiße hat net gefuntz " );
			System.out.println( "--> [ERROR]");
		}

		System.out.println(  "Trying to delete the Results from a Event with deleteAll() (should work properly) " );
		if ( EventResultManager.getInstance().deleteAll(5))
		{
			System.out.println( "--> delete was successfull!");
			System.out.println( "--> [OK]");
		}
		else
		{
			errors++;
			System.out.println( "--> cant delete" );
			System.out.println( "--> [Error]");
		}

		System.out.println(  "Trying to get the Results from a Event with getResults() (shouldn´t work) " );
		Ergebnisse = EventResultManager.getInstance().getResults(5);
		ergs = Ergebnisse.iterator();
		if ( Ergebnisse.size() == 0 )
		{
			System.out.println( "--> Did not find the Results!");
			System.out.println( "--> [OK]");
		}
		else
		{
			errors++;
			System.out.println( "--> Found the Results:" +  ((ExamResult)ergs.next()).getResult()  );
			System.out.println( "--> [ERROR]");
		}

	System.out.println(  "Trying to delete the Result from a Event with deleteResult() (shouldn´t work) " );
		if (EventResultManager.getInstance().deleteResult(bestanden))
		{
			errors++;
			System.out.println( "--> Delete the Results!");
			System.out.println( "--> [ERROR]");
		}
		else
		{
			System.out.println( "--> can´t delete ");
			System.out.println( "--> [OK]");
		}

		System.out.println(  "Trying to add a Result with \"addResult()\" (should work properly) " );
		ExamResult result = new ExamResult( "dieter", 5, 2.0);
		if ( EventResultManager.getInstance().addResult(result) )
		{
			System.out.println( "--> Result Successfully added");
			System.out.println( "--> [OK]");
		}
		else
		{
			errors++;
			System.out.println( "--> Found one, but should not have found one" );
			System.out.println( "--> [ERROR]");
		}

		System.out.println(  "Trying to add the same Result with \"addResult()\" (shouldn´t work) " );
		if ( EventResultManager.getInstance().addResult(result) )
		{
			errors++;
			System.out.println( "--> Result Successfully added");
			System.out.println( "--> [ERROR]");
		}
		else
		{
			System.out.println( "--> can´t add this Result " );
			System.out.println( "--> [OK]");
		}

		System.out.println(  "Trying to delete the Result from a Event with deleteResult() (should work properly) " );
		if (EventResultManager.getInstance().deleteResult(result))
		{
			System.out.println( "--> Delete the Results!");
			System.out.println( "--> [OK]");
		}
		else
		{
			errors++;
			System.out.println( "--> can´t delete" );
			System.out.println( "--> [ERROR]");
		}
	}
}