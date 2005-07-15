package Test.JUnitTests.mockStruts;

import servletunit.struts.MockStrutsTestCase;

/**
 * Will Test ProfileAction.class
 *
 * @author Sven Fischer
 * @version 05-06-05@16:00
 *
 */

public class TestProfileAction extends MockStrutsTestCase
{

    public TestProfileAction(String testName)
    {
        super(testName);
    }


	public void setUp()
	{
		super.setUp();
		setInitParameter("sFirstName","Franz");
		setInitParameter("sSurname","Mustermann");
		setInitParameter("sPersonalNumber","0815");
		setInitParameter("sLogin","nick");
		setInitParameter("sEmailAddress","a@b.cd");
		setInitParameter("sPassword","myPW");
		setInitParameter("iRights", 1);
    }

	public void testPWSuccesfullChanged()
	{
		setRequestPathInfo("/profile");
		addRequestParameter("sPassword","myPW");
		addRequestParameter("sNewPassword","favoritePW");
		addRequestParameter("sAgainPassword","favoritePW");
		actionPerform();
		verifyForward("ProfileSuccesful");
		verifyNoActionErrors();
	}

	public void testFailedLoginCauseWrongAgainPW() {
		addRequestParameter("sPassword","myPW");
		addRequestParameter("sNewPassword","favoritePW");
		addRequestParameter("sAgainPassword","wrongtypedPW");
		setRequestPathInfo("/login");
		actionPerform();
		verifyForward("ProfileUnsuccesful");
	//	verifyActionErrors(new String[] {"profile.pwempty"});
}

	public void testFailedLoginCauseWrongPW() {
		addRequestParameter("sPassword","wrongPW");
		addRequestParameter("sNewPassword","favoritePW");
		addRequestParameter("sAgainPassword","favoritePW");
		setRequestPathInfo("/login");
		actionPerform();
		verifyForward("ProfileUnsuccesful");
	//	verifyActionErrors(new String[] {"profile.pwempty"});
}

	public void testFailedLoginCauseWrongPW() {
		addRequestParameter("sFirstName","Franz-Klaus");
		addRequestParameter("sSurname","Mustermann");
		addRequestParameter("sEmailAddress","a@b.cd");
		setRequestPathInfo("/login");
		actionPerform();
		verifyForward("ProfileSuccesful");
		verifyNoActionErrors();
}

	public static void main(String[] args) {
        junit.textui.TestRunner.run(TestProfileAction.class);
    }
}