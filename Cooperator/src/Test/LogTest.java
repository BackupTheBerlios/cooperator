
import de.tr1.cooperator.manager.system.*;

public class LogTest
{
	public static void main( String[] args )
	{
		System.out.println( "\r\n===============================================================\r\n");
		System.out.println( "Trying false call (directly without init - should cause an Exception)");
		try
		{
			Log.getInstance().addLog("false Test (directly)");
		}
		catch (Exception e)
		{
			System.out.println( "Fehler: " + e);
		}
		System.out.println( "Trying valid call (with-file-name-init - should work properly) ");
		try
		{
			Log.getInstance("./Log.txt").addLog("valid test (width file-name)");
		}
		catch (Exception e)
		{
			System.out.println( "Fehler: " + e);
		}
		System.out.println( "Trying valid call (directly - should work properly, if init was ok)");
		try
		{
			Log.getInstance().addLog("valid test (directly)");
		}
		catch (Exception e)
		{
			System.out.println( "Fehler: " + e);
		}
		System.out.println( "\r\n===============================================================\r\n");
	}
}