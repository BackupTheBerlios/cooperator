/*
 * Created on 11.06.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package Test.JUnitTests.ordinary;
import java.util.*;

import de.tr1.cooperator.manager.mainframe.EventTypeManager;

import junit.framework.TestCase;

/**
 * @author Carsten Lüdecke
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EventTypeManagerTest extends TestCase 
{
	
	public static void main(String[] args) 
	{
		junit.swingui.TestRunner.run(EventTypeManagerTest.class);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception 
	{
		super.setUp();
		
		EventTypeManager.getInstance("/");
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception 
	{
		super.tearDown();
		EventTypeManager.getInstance().delete( 0 );
		EventTypeManager.getInstance().delete( 1 );
	}
	
	/*
	 * Class under test for EventTypeManager getInstance(String)
	 * Class under test for EventTypeManager getInstance()
	 */
	public void testEventTypeManagerAndGetInstanceString() {
		try{
			EventTypeManager.getInstance();
			assertTrue(true);
		}catch(Exception e)
		{
			assertTrue(false);		// ich denke zumindest das das so stimmt,
									// denn ich gehe davon aus, dass die setUp() vorher
									// aufgerufen wird und dann schon eine Instanz existiert
									// --> es dürfte zu keiner Exeption kommen !?
		}
	}

	public void testAddAndDeleteAndUpdate() 
	{
		assertFalse(EventTypeManager.getInstance().delete(1));
		assertFalse(EventTypeManager.getInstance().update("Vorbereitung", 1));
		assertTrue(EventTypeManager.getInstance().add("Vorbereitung", 1));
		assertTrue(EventTypeManager.getInstance().update("Nachbereitung", 1));
		assertTrue(EventTypeManager.getInstance().delete(1));
	}


	public void testGetFreeID() 
	{
		assertEquals( EventTypeManager.getInstance().getFreeID(), 0 );
		if ( EventTypeManager.getInstance().add("Vorbereitung", 0) )
		{
			assertEquals( EventTypeManager.getInstance().getFreeID(), 1 );
		}
		if ( EventTypeManager.getInstance().add("Nachbereitung", 1) )
		{
			assertEquals( EventTypeManager.getInstance().getFreeID(), 2 );
		}
		if ( EventTypeManager.getInstance().delete(1) )
		{
			assertEquals( EventTypeManager.getInstance().getFreeID(), 1 );
		}
		EventTypeManager.getInstance().delete(0);	// nur damit wieder alle weg sind
	}
	
	public void testGetAllEventTypes() 
	{
		assertEquals( EventTypeManager.getInstance().getAllEventTypes().size(), 0 );
		EventTypeManager.getInstance().add("Vorbereitung", 0);
		assertEquals( EventTypeManager.getInstance().getAllEventTypes().size(), 1 );
		EventTypeManager.getInstance().add("Nachbereitung", 1);
		assertEquals( EventTypeManager.getInstance().getAllEventTypes().size(), 2 );
		// kann man wenn man will noch ne weile machen
		
		EventTypeManager.getInstance().delete(0);
		EventTypeManager.getInstance().delete(1);	// nur damit wieder alle weg sind
		
	}
	
	public void testGetEventName()
	{
		assertNull( EventTypeManager.getInstance().getEventName(0));
		EventTypeManager.getInstance().add("Vorbereitung", 0);
		assertEquals( EventTypeManager.getInstance().getEventName(0), "Vorbereitung");
		EventTypeManager.getInstance().add("Nachbereitung", 1);
		assertEquals( EventTypeManager.getInstance().getEventName(1), "Nachbereitung");
		
		EventTypeManager.getInstance().delete(0);
		EventTypeManager.getInstance().delete(1);	// nur damit wieder alle weg sind
			
	}

}