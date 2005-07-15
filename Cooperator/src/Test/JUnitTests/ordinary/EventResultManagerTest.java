/*
 * Created on 11.06.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package Test.JUnitTests.ordinary;
import java.util.*;

import de.tr1.cooperator.manager.mainframe.EventResultManager;
import de.tr1.cooperator.model.mainframe.ExamResult;
import junit.framework.TestCase;

/**
 * @author Carsten Lüdecke
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EventResultManagerTest extends TestCase 
{
	
	ExamResult myResult = null;
	
	public static void main(String[] args) 
	{
		junit.swingui.TestRunner.run(EventResultManagerTest.class);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception 
	{
		super.setUp();
		
		myResult = new ExamResult( "Klaus", 1, 2.0   );
					
		EventResultManager.getInstance("/");
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception 
	{
		super.tearDown();
		EventResultManager.getInstance().deleteAll(1);
	}
	
	/*
	 * Class under test for EventResultManager getInstance(String)
	 * Class under test for EventResultManager getInstance()
	 */
	public void testEventResultManagerAndGetInstanceString() {
		try{
			EventResultManager.getInstance();
			assertTrue(true);
		}catch(Exception e)
		{
			assertTrue(false);	// bin ich mir nicht sicher, Begründung im EventTypeManager
								// selbe stelle
		}
	}

	public void testAddResultAndDeleteResultAndDeleteAll() 
	{
		assertFalse(EventResultManager.getInstance().deleteResult(myResult));
		assertFalse(EventResultManager.getInstance().deleteAll(1));
		assertTrue(EventResultManager.getInstance().addResult(myResult));
		assertTrue(EventResultManager.getInstance().deleteAll(1));
		assertTrue(EventResultManager.getInstance().addResult(myResult));
		assertTrue(EventResultManager.getInstance().deleteResult(myResult));
	}


	


	public void testGetResultsAndGetResult() 
	{
		assertEquals( EventResultManager.getInstance().getResults(1).size(), 0 );
		assertTrue(EventResultManager.getInstance().addResult(myResult));
		assertEquals( EventResultManager.getInstance().getResults(1).size(), 1 );
		assertEquals( EventResultManager.getInstance().getResult("Klaus", 1).getResult(), 2.0 );
		asserNull( EventResultManager.getInstance().getResult("Klaus", 2) );
	}
	
	//Es fehlen die Test´s für makeResult() und getResultAsElement()

}