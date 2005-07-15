/**
 * Wird geworfen, wenn Fehler in XML-Handler-Klassen auftauchen.
 *
 * @author Sebastian Kohl
 *
 */

package de.tr1.cooperator.exceptions;

public class XMLException extends RuntimeException
{
	public XMLException( String what )
	{
		super( what );
	}
}