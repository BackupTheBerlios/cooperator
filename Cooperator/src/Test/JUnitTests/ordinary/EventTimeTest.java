package Test.JUnitTests.ordinary;
import de.tr1.cooperator.model.mainframe.EventTime;
import java.util.*;
import junit.framework.TestCase;
import de.tr1.cooperator.model.mainframe.*;
/**
 * Will test EventTime.class
 *
 * @author Sven Fischer
 * @version 05-06-04@23:00
 *
 */

public class EventTimeTest extends TestCase
{
	EventTime eventTime1;

	protected void setUp()
	{
		eventTime1 = new EventTime("Freitag", "09:15", 2," ");
	}

	public void testSetAndGetEventDayName()
	{
		String expected = "Donnerstag";
		eventTime1.setDayName("Donnerstag");
		assertEquals(expected, eventTime1.getDayName( ) );
	}

		public void testSetAndGetEventClockTime()
	{
		String expected = "00:00";
		eventTime1.setClockTime("00:00");
		assertEquals(expected, eventTime1.getClockTime( ) );
	}

		public void testSetAndGetEventRythm()
	{
		int expected = 1 ;
		eventTime1.setRhythm(1);
		assertEquals(expected, eventTime1.getRhythm( ) );
	}
}