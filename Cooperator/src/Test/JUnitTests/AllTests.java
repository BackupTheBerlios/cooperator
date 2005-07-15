package Test.JUnitTests;

import junit.framework.*;

/**
 * Starts all Tests of path Test.JUnitTests.ordinary
 *
 * @author Sven Fischer
 * @version 05-06-05@1:00
 *
 */

public class AllTests {

	public static void main (String[] args) {
		junit.textui.TestRunner.run (suite());
	}

	public static Test suite ( ) {
		TestSuite suite= new TestSuite("All JUnit Tests");
		suite.addTest(new TestSuite(Test.JUnitTests.ordinary.DropDownEntryTest.class));
		suite.addTest(new TestSuite(Test.JUnitTests.ordinary.EventTimeTest.class));
		suite.addTest(new TestSuite(Test.JUnitTests.ordinary.ExamResultTest.class));
		suite.addTest(new TestSuite(Test.JUnitTests.ordinary.UserTest.class));
	    return suite;
	}
}