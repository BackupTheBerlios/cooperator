package Test.JUnitTests.mockStruts;

import servletunit.struts.MockStrutsTestCase;

public class TestViewEventsAction extends MockStrutsTestCase {

    public TestViewEventsAction(String testName) {
        super(testName);
    }


	public void setUp() {
		super.setUp();
    }


	
	
	//Hier kommen die Testmethoden hin

	public static void main(String[] args) {
        junit.textui.TestRunner.run(TestViewEventsAction.class);
    }
}