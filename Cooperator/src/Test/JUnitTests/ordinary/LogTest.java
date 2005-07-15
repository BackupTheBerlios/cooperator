/*
 * Created on 11.06.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package Test.JUnitTests.ordinary;

import de.tr1.cooperator.manager.mainframe.EventManager;
import de.tr1.cooperator.manager.system.Log;
import junit.framework.TestCase;

/**
 * @author Osman Ibrahim
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LogTest extends TestCase {
	String empty=null;
	Log log;
	public static void main(String[] args) {
		junit.textui.TestRunner.run(LogTest.class);
		junit.swingui.TestRunner.run(LogTest.class);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		
	}

	/*
	 * Class under test for Log getInstance(String)
	 * Class under test for Log getInstance()
	 */
	 
	public void testGetInstance() {
		try{
			Log.getInstance(); // test for get empty Instanc
		}catch(Exception e)
		{
		assertTrue(true);
		}
		assertEquals(Log.getInstance("/Log.txt)"),Log.getInstance());  //test for make instance  and  to get it 
		
		
	}
	/*
	 * Calss under test for LOg addLog()
	 */
	

	public void testAddLog() {
		assertTrue(	Log.addLog("valid test (directly)"));// test  for Log 
		assertFalse(Log.addLog(empty));     //test for add Empty
		
	}

}
