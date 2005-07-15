/**
 * Wird geworfen, falls versucht wird, eine Singleton-Klasse mehrmals zu instanziieren.
 *
 * @author Thorsten Berger
 *
 */

package de.tr1.cooperator.exceptions;

public class SingletonException extends RuntimeException
{
	public SingletonException( )
	{
		super( "There should only be one instance!" );
	}
}
