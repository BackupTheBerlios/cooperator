/*
File:		UserManager.java
Created:	05-06-01@15:00
Task:		Manager that Handles all EventResult...
Author:		Carsten Lüdecke, Sebastian Kohl

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package de.tr1.cooperator.manager.mainframe;

import de.tr1.cooperator.model.mainframe.User;
import de.tr1.cooperator.model.mainframe.ExamResult;
import de.tr1.cooperator.exceptions.*;
import de.tr1.cooperator.manager.system.Log;
import de.tr1.cooperator.manager.mainframe.XMLCore;

import java.io.File;
import java.io.IOException;

import org.jdom.Element;
import java.util.*;


/**
 * This class handles everything for an XML-based user-database
 *
 * @author Carsten Lüdecke, Sebastian Kohl
 * @version 05-06-02@15:00
 */
public class EventResultManager
{

	private static EventResultManager	hInstance = null;
	private String sBasePath = null;

	//constants for the XML-Files
	public static final String XML_RESULTS			= "results";
	public static final String XML_RESULT	 		= "result";
	public static final String XML_RESULT_USER		= "user";
	public static final String XML_RESULT_RESULT	= "value";

	/**
	 * Will create the needed instance, but throw a SingletonException, if already instanciated
	 */
	protected EventResultManager( ) throws SingletonException
	{
		if ( this.hInstance != null )
			throw new SingletonException( );
		hInstance 	= this;
	}

	/**
	 * Will return the valid singleton-instance and set the name of the file, if not already done.
	 * @param FilePath
	 * @return Instance of the EventResultManager
	 */
	synchronized public static EventResultManager getInstance( String BasePath ) throws IllegalArgumentException,
																					  XMLException,
																					  IOException
	{
		if ( hInstance == null )
		{
			hInstance = new EventResultManager( );
			if ( (BasePath == null) || (BasePath.length()<1) )
				throw new IllegalArgumentException("BasePath was null and can not be set!");
			hInstance.sBasePath = BasePath;
		}
		return hInstance;
	}

	/**
	 * Returns a valid singleton-instance, if it was initalized already. If not, it will throw an IllegalArgumentException!
	 * @return Instance of the EventManager
	 */
	synchronized public static EventResultManager getInstance( ) throws IllegalArgumentException
	{
		if ( hInstance == null )
			throw new IllegalArgumentException("Class EventResultManager was not already initialized. Call getInstance(FilePath) first!");
		return hInstance;
	}

	/**
	 * Returns the Results from a Event
	 * @param EventID identify the Event
	 * @return ExamResult a list of the Results
	 */
	public Collection getResults(int EventID)
	{
		Collection Results = new ArrayList();
		// Name of the file is based on the event-id it lists results for
		String name = this.sBasePath + "Result_" + EventID + ".xml";
		XMLCore Core = null;
		try
		{
			Core = XMLCore.loadFile(name, false, "" );
		}
		catch (IOException ie)
		{
			return Results;
		}

		if (  Core == null )
			return Results;
		else
		{
			Iterator it;
			it = Core.getHRoot().getChildren(XML_RESULT).iterator( );

			while ( it.hasNext( ) )
			{
				Results.add( this.makeResult((Element)it.next(), EventID) );
			}
			return Results;
		}
	}

	/**
	 * Make a ExamResult out of an jdom.Element
	 * @param TheElement to parse
	 * @param EventID of the Event the result should be connected to
	 * @return ExamResult the resulting ExamResult
	 */
	protected ExamResult makeResult( Element TheElement, int EventID )
	{
		if ( TheElement == null )
			return null;

		// grab all three needed values
		String user		= TheElement.getAttributeValue( this.XML_RESULT_USER	);
		String sresult	= TheElement.getAttributeValue( this.XML_RESULT_RESULT	);
		// check resulting strings for the attributes
		if ( (user==null) || (user.length()<1) )
		{
			Log.addLog( "ERROR: Database for Results is invalid. (missing user-login in entry - EventID="+EventID+")!");
			return null;
		}
		if ( (sresult==null) || (sresult.length()<1) )
		{
			Log.addLog( "ERROR: Database for Results is invalid. (missing result in entry - EventID="+EventID+")!");
			return null;
		}
		// parse numeric values
		double result;
		int id;
		try
		{
			result	= Double.parseDouble( sresult );
		}
		catch (Exception e)
		{
			Log.addLog( "ERROR: Database for Results is invalid. (invalid numeric format - EventID="+EventID+")!");
			return null;
		}
		ExamResult res = new ExamResult( user, EventID, result );
		return res;
	}

	/**
	 * Grabs a single result for a user and an exam
	 * @param UserPersonalNumber for the requested user
	 * @param EventID for the special event
	 * @return the requested ExamResult
	 */
	public ExamResult getResult( String UserPersonalNumber, int EventID )
	{
		if ( (UserPersonalNumber==null) || (UserPersonalNumber.length()<1) )
			return null;
		// Name of the file is based on the event-id it lists results for
		String name		= this.sBasePath + "Result_" + EventID + ".xml";
		XMLCore Core	= null;
		try
		{
			Core = XMLCore.loadFile(name, false, "");
		}
		catch (IOException ie)
		{
			return null;
		}

		if (  Core == null )
			return null;
		else
		{
			Iterator it = Core.getHRoot( ).getChildren( ).iterator( );
			while ( it.hasNext( ) )
			{
				Element found = (Element)it.next( );
				// grab a result out of this
				ExamResult res= this.makeResult( found, EventID );
				if (res==null)
					return null;
				if ( UserPersonalNumber.equals( res.getUserPersonalNumber() ) && (EventID==res.getEvent()) )
					return res;
			}
		}
		return null;
	}

	/**
	 * Grabs a single result for a user and an exam
	 * @param UserPersonalNumber for the requested user
	 * @param EventID for the special event
	 * @param XMLCore to use
	 * @return Element of the XMLFile
	 */
	protected Element getResultAsElement( String UserPersonalNumber, int EventID, XMLCore Core )
	{
		if ( (UserPersonalNumber==null) || (UserPersonalNumber.length()<1) )
			return null;

		if (  Core == null )
			return null;
		else
		{
			Iterator it = Core.getHRoot( ).getChildren( ).iterator( );
			while ( it.hasNext( ) )
			{
				Element found = (Element)it.next( );
				// grab a result out of this
				ExamResult res= this.makeResult( found, EventID );
				if (res==null)
					return null;
				if ( UserPersonalNumber.equals( res.getUserPersonalNumber() ) && (EventID==res.getEvent()) )
					return found;
			}
		}
		return null;
	}

	/**
	 * Will delete a result out of the file, if existent
	 * @param ExamResult to delete from the database
	 * @return boolean Success
	 */
	synchronized public boolean deleteResult( ExamResult Result )
	{
		if ( (Result==null) )
			return false;
		// Name of the file is based on the event-id it lists results for
		String name		= this.sBasePath + "Result_" + Result.getEvent() + ".xml";
		XMLCore Core	= null;
		try
		{
			Core	= XMLCore.loadFile(name, false, "");
		}
		catch (IOException ie)
		{
			return false;
		}

		if (  Core == null )
			return false;
		// try to find it
		Element found = this.getResultAsElement( Result.getUserPersonalNumber(), Result.getEvent(), Core );
		if ( found == null )
			return false;
		if ( !Core.getHRoot().removeContent( found ) )
			return false;
		return Core.saveDom( );
	}

	/**
	 * Will delete all Results for an Exam/Event. This will mean, that the complete XML-File is removed!
	 * @param EventID for the corresponding Event
	 * @return boolean Success
	 */
	synchronized public boolean deleteAll( int EventID )
	{
		// Name of the file is based on the event-id it lists results for
		String name	= this.sBasePath + "Result_" + EventID + ".xml";
		File xml	= new File(name);
		if ( !xml.exists() )
			return true;
		if ( !xml.isFile() )
			return false;
		return xml.delete( );
	}

	/**
	 * Will save a result in a XML-File and create this file, if needed
	 * @param ExamResult to add to the List
	 * @return boolean Success
	 */
	 synchronized public boolean addResult( ExamResult Result )
	 {
	 	if ( Result == null )
	 		return false;
		// Name of the file is based on the event-id it lists results for
		String name	= this.sBasePath + "Result_" + Result.getEvent() + ".xml";
		XMLCore Core= null;
		try
		{
			Core = XMLCore.loadFile( name, true, this.XML_RESULTS );
		}
		catch ( IOException ie )
		{
			return false;
		}
		// is this result already defined?
		Element found = Core.find( this.XML_RESULT, this.XML_RESULT_USER, Result.getUserPersonalNumber( ) );
		if ( found!=null )
     		return false;
		Element newResult = new Element( this.XML_RESULT );
		newResult.setAttribute( this.XML_RESULT_USER,	Result.getUserPersonalNumber( )	);
		newResult.setAttribute( this.XML_RESULT_RESULT,	""+Result.getResult( )	);
		Core.getHRoot().addContent( newResult );
		return Core.saveDom( );
     }
}