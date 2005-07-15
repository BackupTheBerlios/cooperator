package Test;
import de.tr1.cooperator.manager.system.*;
import de.tr1.cooperator.manager.mainframe.*;
import de.tr1.cooperator.model.mainframe.*;
import org.jdom.*;

import java.util.*;

public class EventManagerTest
{
	public static void main( String[] args )
	{
		System.out.println( "\r\n===============================================================\r\n");
		System.out.println( "Trying direct get of an instance (should cause an exception)");
		int errors = 0;
		Log.getInstance("Log.txt");

		try
		{
			EventManager.getInstance();
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
			EventManager.getInstance("./");        // kommt hier ne andere xml-datei?
			System.out.println( "--> Got an Instance" );
			System.out.println( "--> [OK]");
		}
		catch (Exception e)
		{
			System.out.println( "--> Error: " + e);
			errors++;
			System.out.println( "--> [ERROR]");
		}

		System.out.println(  "Trying to find an Event (should find nothing)" );
		Event myEvent = EventManager.getInstance().getEventByName( "Test1" );
		if ( myEvent == null )
		{
			System.out.println( "--> Did not find that event!");
			System.out.println( "--> [OK]");
		}
		else
		{
			errors++;
			System.out.println( "--> Found that event: "+myEvent.getName() );
			System.out.println( "--> [ERROR]");
		}

		System.out.println( "Test how many Events are in the Collection (now there must be 0)" );
		Collection allesdrinn = EventManager.getInstance().getAllEvents( );
		if ( allesdrinn.size() == 0 )
		{
			System.out.println( "--> 0 Events in the Collection");
			System.out.println( "--> [OK]");
		}
		else
		{
			errors++;
			System.out.println( "--> wrong count of Elements");
			System.out.println( "--> [ERROR]");
		}


		// Erzeugung eines Events
		myEvent = new Event(  "Test1", 1, 0, -1, -1,
									"admin", "DAS IST NUR EINE TESTVERANSTALTUNG",
									0, new ArrayList(),
									0,	10,
									5,
									new ArrayList(), 10 );

		System.out.println( "Trying to get a free ID (should by the 0)");
		int a = EventManager.getInstance().getFreeID( );
		if ( a == 0 )
		{
			System.out.println( "--> That´s the right ID");
			System.out.println( "--> [OK]");
		}
		else
		{
			errors++;
			System.out.println( "--> get the wrong ID");
			System.out.println( "--> [ERROR]");
		}

		System.out.println( "Trying to find an Event with the ID = 0 (shouldn´t work)");
		Event kackEvent = EventManager.getInstance().getEventByID(0);
		if ( kackEvent == null )
		{
			System.out.println( "--> Couldn´t find the Event by ID without an error!");
			System.out.println( "--> [OK]");
		}
		else
		{
			errors++;
			System.out.println( "--> Found that event with the ID: 0");
			System.out.println( "--> [ERROR]");
		}

		System.out.println( "Trying to update an Event (shouldn´t work)");
		if ( !EventManager.getInstance().updateEvent(myEvent) )
		{
			System.out.println( "--> Couldn´t update that Event without an error!");
			System.out.println( "--> [OK]");
		}
		else
		{
			errors++;
			System.out.println( "--> update the Event was possible");
			System.out.println( "--> [ERROR]");
		}


		System.out.println( "Trying to delete an Event (shouldn´t work)");
		if ( !EventManager.getInstance().deleteEvent(myEvent) )
		{
			System.out.println( "--> Couldn´t delete that Event without an error!");
			System.out.println( "--> [OK]");
		}
		else
		{
			errors++;
			System.out.println( "--> deleting the Event was possible");
			System.out.println( "--> [ERROR]");
		}


		System.out.println( "Trying to enter an Event (should work properly)");
		if ( !EventManager.getInstance().addEvent(myEvent) )
		{
			errors++;
			System.out.println( "--> Couldn´t enter that event without an error!");
			System.out.println( "--> [ERROR]");
		}
		else
		{
			System.out.println( "--> Entering the event was possible");
			System.out.println( "--> [OK]");
		}

		System.out.println( "Trying to enter the same Event again (shouldn´t work)");
		if ( !EventManager.getInstance().addEvent(myEvent) )
		{
			System.out.println( "--> Couldn´t enter that event!");
			System.out.println( "--> [OK]");
		}
		else
		{
			errors++;
			System.out.println( "--> Entering the event was possible");
			System.out.println( "--> [ERROR]");
		}

		System.out.println(  "Trying to find an Event (should work properly)" );
		Event theEvent = EventManager.getInstance().getEventByName( "Test1" );
		if ( theEvent == null )
		{
			System.out.println( "--> Did not found that event!");
			System.out.println( "--> [ERROR]");
			errors++;
		}
		else
		{
			System.out.println( "--> Found that event:" + myEvent.getName() );
			System.out.println( "--> [OK]");
		}

		System.out.println( "Trying to delete an Event (should work properly)");
		if ( !EventManager.getInstance().deleteEvent(myEvent) )
		{
			errors++;
			System.out.println( "--> Couldn´t delete that Event without an error!");
			System.out.println( "--> [ERROR]");
		}
		else
		{
			System.out.println( "--> deleting the Event was possible");
			System.out.println( "--> [OK]");
		}

		System.out.println( "Trying to delete the Event again (shouldn´t work)");
		if ( !EventManager.getInstance().deleteEvent(myEvent) )
		{
			System.out.println( "--> Couldn´t delete that Event without an error!");
			System.out.println( "--> [OK]");
		}
		else
		{
			System.out.println( "--> deleting the Event was possible");
			System.out.println( "--> [ERROR]");
			errors++;
		}

		System.out.println( "Trying to update an Event (shouldn't work)");
		if ( !EventManager.getInstance().updateEvent(myEvent) )
		{
			System.out.println( "--> Couldn´t update that Event without an error!");
			System.out.println( "--> [OK]");
		}
		else
		{
			System.out.println( "--> update the Event was possible");
			System.out.println( "--> [ERROR]");
			errors++;
		}

		System.out.println( "Trying to add the Event again (should work)");
		if ( !EventManager.getInstance().addEvent(myEvent) )
		{
			System.out.println( "--> Couldn´t add that Event without an error!");
			System.out.println( "--> [ERROR]");
			errors++;
		}
		else
		{
			System.out.println( "--> adding the Event was possible");
			System.out.println( "--> [OK]");
		}

		System.out.println( "Trying to find an Event with the ID = 0 (should work properly)");
		Event sauEvent = EventManager.getInstance().getEventByID(0);
		if ( sauEvent == null )
		{
			errors++;
			System.out.println( "--> Couldn´t find the Event by ID without an error!");
			System.out.println( "--> [ERROR]");
		}
		else
		{
			System.out.println( "--> Found that event with the ID: 0");
			System.out.println( "--> [OK]");
		}

		System.out.println( "Test how many Events are in the Collection (now there must be 1)" );
		Collection wasgeht = EventManager.getInstance().getAllEvents( );
		if ( wasgeht.size() == 1 )
		{
			System.out.println( "--> 1 Events in the Collection");
			System.out.println( "--> [OK]");
		}
		else
		{
			errors++;
			System.out.println( "--> wrong count of Elements");
			System.out.println( "--> [ERROR]");
		}

		System.out.println( "Trying to get a free ID (should by the 1)");
		int b = EventManager.getInstance().getFreeID( );
		if ( b == 1 )
		{
			System.out.println( "--> That´s the right ID");
			System.out.println( "--> [OK]");
		}
		else
		{
			errors++;
			System.out.println( "--> get the wrong ID");
			System.out.println( "--> [ERROR]");
		}

		System.out.println( "Trying to delete the Event (should work)");
		if ( EventManager.getInstance().deleteEvent(myEvent) )
		{
			System.out.println( "--> Could delete the Event without an error!");
			System.out.println( "--> [OK]");
		}
		else
		{
			errors++;
			System.out.println( "--> deleting the Event was not possible");
			System.out.println( "--> [ERROR]");
		}
		/*
		Hier folgen nur noch Daten-Parts, die als Test-Daten verwendet werden können:
		Event leipzig = new Event( "Universität Leipzig",		// name
					 			0,					// type
					 0,					// id
					 -1,				// parent
					"-",			// lecturer
					 "Die Universität zu Leipzig, früher die Karl-Marx-Universität",			// info
					0,				// start
						new ArrayList(),		// times
					0,	// substart
					0,		// subend
					0,	// unsub
					new ArrayList(),	//	subs
					0 );		// max
		Event fu = new Event( "Freie Universität Berlin",		// name
					 			0,					// type
					 1,					// id
					 -1,				// parent
					"-",			// lecturer
					 "Die Freie Universität zu Berlin",			// info
					0,				// start
						new ArrayList(),		// times
					0,	// substart
					0,		// subend
					0,	// unsub
					new ArrayList(),	//	subs
					0 );		// max
		Event inf = new Event( "Fakultät für Informatik",		// name
					 			1,					// type
					 2,					// id
					 0,				// parent
					"-",			// lecturer
					 "Die Fakultät für Informatik liegt direkt am Augustusplatz mit Blick auf den Springbrunnen. WLAN vorhanden.",			// info
					0,				// start
						new ArrayList(),		// times
					0,	// substart
					0,		// subend
					0,	// unsub
					new ArrayList(),	//	subs
					0 );		// max
		Event math = new Event( "Fakultät für Mathematik",		// name
					 			1,					// type
					 3,					// id
					 0,				// parent
					"-",			// lecturer
					 "Die Fakultät für Informatik liegt direkt am Augustusplatz mit Blick auf den Springbrunnen. WLAN vorhanden.",			// info
					0,				// start
						new ArrayList(),		// times
					0,	// substart
					0,		// subend
					0,	// unsub
					new ArrayList(),	//	subs
					0 );		// max
		Event ti = new Event( "Abteilung Technische Informatik",		// name
					 			2,					// type
					 4,					// id
					 2,				// parent
					"-",			// lecturer
					 "Hier dreht sich alles um Hardware und sowohl Praxis aus auch Theorie dazu. Treffen sie brillante Exoten wie Herrn Dr. Liekse oder Herrn Dr. Middendorf. ;-)",			// info
					0,				// start
						new ArrayList(),		// times
					new Date().getTime()-15*24*60*60*1000,	// substart
					new Date().getTime()+10*24*60*60*1000,		// subend
					new Date().getTime()+17*24*60*60*1000,	// unsub
					new ArrayList(),	//	subs
					0 );		// max
		Event ai = new Event( "Abteilung Angewandte Informatik",		// name
					 			2,					// type
					 5,					// id
					 2,				// parent
					"-",			// lecturer
					 "Hier lernen sie praktische Anwendung von Programmiersprachen und Prozesse der Software-Erstellung",			// info
					0,				// start
						new ArrayList(),		// times
					new Date().getTime()-15*24*60*60*1000,	// substart
					new Date().getTime()+10*24*60*60*1000,		// subend
					new Date().getTime()+17*24*60*60*1000,	// unsub
					new ArrayList(),	//	subs
					0 );		// max
		Event swt = new Event( "Abteilung Technische Informatik",		// name
					 			2,					// type
					 4,					// id
					 2,				// parent
					"-",			// lecturer
					 "Hier dreht sich alles um Hardware und sowohl Praxis aus auch Theorie dazu. Treffen sie brillante Exoten wie Herrn Dr. Liekse oder Herrn Dr. Middendorf. ;-)",			// info
					0,				// start
						new ArrayList(),		// times
					new Date().getTime()-15*24*60*60*1000,	// substart
					new Date().getTime()+10*24*60*60*1000,		// subend
					new Date().getTime()+17*24*60*60*1000,	// unsub
					new ArrayList(),	//	subs
					0 );		// max
		EventManager.getInstance().addEvent( leipzig );
		EventManager.getInstance().addEvent( fu );
		EventManager.getInstance().addEvent( inf );
		EventManager.getInstance().addEvent( math );
		EventManager.getInstance().addEvent( ti );
		EventManager.getInstance().addEvent( ai );
		/**/
		System.out.println( "\r\nErrors: "+errors+"\r\n===============================================================\r\n");
	}
}