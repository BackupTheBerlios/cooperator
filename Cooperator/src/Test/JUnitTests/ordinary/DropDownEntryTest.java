package Test.JUnitTests.ordinary;
import java.util.*;
import junit.framework.TestCase;
import de.tr1.cooperator.model.web.DropDownEntry;
;
/**
 * Will test DropDownEntry.class
 *
 * @author Sven Fischer
 * @version 05-06-04@12:00
 *
 */

public class DropDownEntryTest extends TestCase
{
	DropDownEntry dropDownEntry;

	protected void setUp()
	{
		dropDownEntry = new DropDownEntry(2, "test"," object ");
	}

	public void testGetLabel()
	{
		String expected = "test";
		assertEquals( expected,  dropDownEntry.getLabel() );
	}

	public void testGetValue()
	{
		int expected = 2;
		assertEquals( expected,  dropDownEntry.getValue() );
	}
}