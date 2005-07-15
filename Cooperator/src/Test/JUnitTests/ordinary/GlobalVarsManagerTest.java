/*
 * Created on 12.06.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package Test.JUnitTests.ordinary;
import de.tr1.cooperator.manager.system.GlobalVarsManager;
import junit.framework.TestCase;
import java.io.File;
/**
 * @author Osman Ibrahim
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GlobalVarsManagerTest extends TestCase {
	String filename="./GlobalVars.xml";
	GlobalVarsManager EmptyglobalVarsManager=null;
	
	public static void main(String[] args) {
		junit.swingui.TestRunner.run(GlobalVarsManagerTest.class);
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
		//if(new  File(filename).exists())new File(filename).delete();
		}

	/*
	 * Class under test for GlobalVarsManager getInstance()
	 */
	public void testGetInstance() {
		try{
			GlobalVarsManager.getInstance();// test for get empty Instanc
			assertTrue(false);
		}catch(Exception e)
		{
		assertTrue(true);
		}
		try{
			assertEquals(GlobalVarsManager.getInstance(filename),GlobalVarsManager.getInstance());  //test for make instance  and  to get it 
		}catch(Exception e)
		{
			assertTrue(false);
		}
		

	
	}

	public void testAddGetRemoveUserMail() {
		//System.out.println(GlobalVarsManager.getInstance().toString());
		assertFalse(GlobalVarsManager.getInstance().removeUserMail("web.de"));
		assertEquals(GlobalVarsManager.getInstance().getUserMails().size(),0);
		assertTrue(GlobalVarsManager.getInstance().addUserMail( "web.de" ));
		assertFalse(GlobalVarsManager.getInstance().addUserMail("web.de"));
		assertEquals(GlobalVarsManager.getInstance().getUserMails().size(),1);
		assertTrue(GlobalVarsManager.getInstance().removeUserMail("web.de"));
		assertEquals(GlobalVarsManager.getInstance().getUserMails().size(),0);		
	}
	public void testAddGetRemoveLecturerMail() {
		assertFalse(GlobalVarsManager.getInstance().removeLecturerMail("uni-leipzig.de"));
		assertEquals(GlobalVarsManager.getInstance().getLecturerMails().size(),0);
		assertTrue(GlobalVarsManager.getInstance().addLecturerMail( "uni-leipzig.de" ));
		assertFalse(GlobalVarsManager.getInstance().addLecturerMail("uni-leipzig.de"));
		assertEquals(GlobalVarsManager.getInstance().getLecturerMails().size(),1);
		assertTrue(GlobalVarsManager.getInstance().removeLecturerMail("uni-leipzig.de"));
		assertEquals(GlobalVarsManager.getInstance().getLecturerMails().size(),0);
	}

	

	/*
	 * Class under test for boolean forceUserMails()
	 */
	public void testForceUserMails() {
		assertTrue(GlobalVarsManager.getInstance().forceUserMails());
		GlobalVarsManager.getInstance().forceUserMails(false);
		assertFalse(GlobalVarsManager.getInstance().forceUserMails());
		}
	/*
	 * Class under test for boolean forceLecturerMail()
	 */
	public void testForceLecturerMail() {
		assertTrue(GlobalVarsManager.getInstance().forceLecturerMails());
		GlobalVarsManager.getInstance().forceLecturerMails(false);
		assertFalse(GlobalVarsManager.getInstance().forceLecturerMails());
	}

	public void testGetandSetSystemMail() {
		assertEquals(GlobalVarsManager.getInstance().getSystemMail(),"");
		GlobalVarsManager.getInstance().setSystemMail("mail");
		assertEquals(GlobalVarsManager.getInstance().getSystemMail(),"mail");
	}

	public void testGetAndGetRegisterSubject() {
		assertEquals(GlobalVarsManager.getInstance().getRegisterSubject(),"");
		GlobalVarsManager.getInstance().setRegisterSubject("RegisterSubject");
		assertEquals(GlobalVarsManager.getInstance().getRegisterSubject(),"RegisterSubject");
	}
	
}
	


