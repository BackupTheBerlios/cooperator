/*
 * Created on 10.06.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package Test.JUnitTests.ordinary;
import java.util.ArrayList;

import de.tr1.cooperator.manager.mainframe.EventManager;
import de.tr1.cooperator.model.mainframe.Event;
import junit.framework.TestCase;

/**
 * @author Osman Ibrahim
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EventManagerTest extends TestCase {
	Event myEvent= null;
	public static void main(String[] args) {
		junit.swingui.TestRunner.run(EventManagerTest.class);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		myEvent = new Event(  "Test1", 1, 0, -1, -1,
				"admin", "DAS IST NUR EINE TESTVERANSTALTUNG",
				0, new ArrayList(),
				0,	10,
				5,
				new ArrayList(), 10 );
		EventManager.getInstance("/");
		}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		EventManager.getInstance().deleteEvent(myEvent);
	}
	/*
	 * Class under test for EventManager getInstance(String)
	 * Class under test for EventManager getInstance()
	 */
	public void testEventManagerandGetInstanceString() {
		try{
			assertEquals(EventManager.getInstance("anther"),EventManager.getInstance());
		}catch(Exception e)
		{
			assertTrue(false);
		}
		
		
	}


	public void testAddEventandDeleteEventandUpdete() {
		assertEquals(EventManager.getInstance().deleteEvent(myEvent),false);
		assertEquals(EventManager.getInstance().updateEvent(myEvent),false);
		assertEquals(EventManager.getInstance().addEvent(myEvent),true);
		assertEquals(EventManager.getInstance().updateEvent(myEvent),true);
		assertEquals(EventManager.getInstance().deleteEvent(myEvent),true);
		assertEquals(EventManager.getInstance().deleteEvent(myEvent),false);
		assertEquals(EventManager.getInstance().updateEvent(myEvent),false);
		
		
		
	}


	public void testGetEventByName() {
	assertNull(EventManager.getInstance().getEventByName("Test1"));
	if(EventManager.getInstance().addEvent(myEvent))assertEquals(EventManager.getInstance().getEventByName(myEvent.getName()).getName(),myEvent.getName());
	if(EventManager.getInstance().deleteEvent(myEvent))assertNull(EventManager.getInstance().getEventByName(myEvent.getName()));
	}

	public void testGetAllEvents() {
		assertEquals(EventManager.getInstance().getAllEvents().size(),0);
		if(EventManager.getInstance().addEvent(myEvent))assertEquals(EventManager.getInstance().getAllEvents().size(),1);
		if(EventManager.getInstance().deleteEvent(myEvent))assertEquals(EventManager.getInstance().getAllEvents().size(),0);
		
	}

	public void testGetFreeID() {
		assertEquals(EventManager.getInstance().getFreeID(),0);
		if(EventManager.getInstance().addEvent(myEvent))assertEquals(EventManager.getInstance().getFreeID(),1);
		if(EventManager.getInstance().deleteEvent(myEvent))assertEquals(EventManager.getInstance().getFreeID(),0);
		
	}

	public void testGetFreeGroupID() {
	}

	public void testGetEventByID() {
		assertNull(EventManager.getInstance().getEventByID(0));
		if(EventManager.getInstance().addEvent(myEvent))assertEquals(EventManager.getInstance().getEventByID(0).getID(),myEvent.getID());
		if(EventManager.getInstance().deleteEvent(myEvent))assertNull(EventManager.getInstance().getEventByID(0));
	}
	

	public void testIsInRequirementList() {
	}

	public void testGetAllowanceList() {
	}

	public void testGetSubscribedEvents() {
	}

}
