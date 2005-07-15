package Test.JUnitTests.mockStruts;

import servletunit.struts.MockStrutsTestCase;

/**
 * Will Test LoginAction.class
 *
 * @author Sven Fischer
 * @version 05-06-05@3:00
 *
 */

public class TestLoginAction extends MockStrutsTestCase {

    public TestLoginAction(String testName) {
		super(testName);
	}


	public void setUp() {
	super.setUp();
    }

	public void tearDown() {
	super.tearDown();
	}

	public void testSuccessfulLogin() {
		setRequestPathInfo("/login");
		addRequestParameter("loginName","admin");
		addRequestParameter("loginPW", getUser("admin").getPassword( ));
		actionPerform();
		verifyForward("success");
		assertEquals("admin",(String) getSession().getAttribute("login"));
		verifyNoActionErrors();
    }

	public void testFailedLogin() {
		addRequestParameter("username","wrongname");
		addRequestParameter("password","wrongPW");
		setRequestPathInfo("/login");
		actionPerform();
		verifyForward("login");
		verifyActionErrors(new String[] {"login.unempty"});
		assertNull((String) getSession().getAttribute("login"));
}
	//Hier kommen die Testmethoden hin

	public static void main(String[] args) {
        junit.textui.TestRunner.run(TestLoginAction.class);
    }
}