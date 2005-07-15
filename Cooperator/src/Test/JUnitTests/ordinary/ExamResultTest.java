package Test.JUnitTests.ordinary;
import de.tr1.cooperator.model.mainframe.ExamResult;
import java.util.*;
import junit.framework.TestCase;

/**
 * Will test ExamResult.class
 *
 * @author Sven Fischer
 * @version 05-06-05@00:00
 *
 */

public class ExamResultTest extends TestCase
{
	ExamResult examResult;

		protected void setUp()
	{
		examResult = new ExamResult("Sowollteichschonimmermalheiﬂen",109, 2.3);
	}

	private void testSetAndGetResult()
	{
		double expected = 1.0;
		examResult.setResult(1.0);
		assertEquals(expected, examResult.getResult() ,0.0000000001);
	}

	private void testSetAndGetEvent()
	{
		int expected = 110;
		examResult.setResult(110);
		assertEquals(expected, examResult.getEvent() );
	}

	private void testSetAndGetUserLogin()
	{
		String expected = "Login-name";
		examResult.setUserLogin("Login-name");
		assertEquals(expected, examResult.getUserLogin() );
	}
}