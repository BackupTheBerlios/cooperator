package Test.JUnitTests.mockStruts;

import servletunit.struts.MockStrutsTestCase;

/**
 * Pattern for MockStrutsTestCases
 *
 * @author Sven Fischer
 * @version 05-06-05@17:00
 *
 */

public class Vorlage extends MockStrutsTestCase {

    public Vorlage(String testName) {
        super(testName);
    }


	public void setUp()
	{
	super.setUp();
	//setInitParameter("parametername","value");	Funktion nötig wenn Eingangsparameter gesetzt werden sollen die 														sonst aus .xml geholt werden
    }

	//Hier beginnen die Testmethoden

//	Folgende Methode ist nur ein Bsp.; es ist nicht sinvoll anwendbar, es erklärt kurz was die
//	aufgerufenen Methoden machen, es macht keinen Sinn verifyNoActionErrors()
//	und verifyActionErrors(new String[] {"vorlage.error"}) in der gleichen Methode zu benutzen
//
//		public void testBsp()
//	{
//		setRequestPathInfo("/vorlage");							Pfad aus der config-Struts.xml
//		addRequestParameter("parametername","value");			ändert die Werte eines Parameters
//		addRequestParameter("otherParametername","value2");		kann wiederholt werden
//		actionPerform();										die Actionklasse wird ausgeführt
//		verifyForward("ProfileSuccesful");						überprüft ob der richtige Forward ausgeführt wurde
//		verifyNoActionErrors();									kein Fehler aufgetreten ist
//		verifyActionErrors(new String[] {"vorlage.error"});		ob die Fehler aus dem Array aufgetreten sind
//		assertEquals("value", method("paramter"));				überprüft ob 2 Werte übereinstimmen
//		assertNull(getSession().getAttribute("authentication"));überprüft ob Attribut Null ist
//	}

	// Ende der Testmethoden

	public static void main(String[] args) {
        junit.textui.TestRunner.run(Vorlage.class);
    }
}