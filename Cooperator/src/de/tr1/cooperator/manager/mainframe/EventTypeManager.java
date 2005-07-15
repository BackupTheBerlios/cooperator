/*
File:		EventTypeManager.java
Created:	05-05-31@12:00
Task:		Will be a singleton and manage a global file for EventTypes
Author:		Sebastian Kohl

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

import java.util.*;
import java.text.SimpleDateFormat;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import org.jdom.Element;

import de.tr1.cooperator.exceptions.*;
import de.tr1.cooperator.model.mainframe.*;
import de.tr1.cooperator.manager.system.*;
/**
 * Will be a singleton and manage a global file for EventTypes
 *
 * @author Sebastian Kohl
 * @version 05-05-31@14:00
 *
 */

public class EventTypeManager
{
	public  static	final String		XML_TYPES	=	"eventTypes";
	public  static	final String		XML_TYPE	=	"eventType";
	public  static	final String		XML_NAME	=	"name";
	public  static	final String		XML_ID		=	"id";

	private static	EventTypeManager	hInstance	= null;
	private			XMLCore				hCore		= null;

	/**
	 * Will create the needed instance, but throw a SingletonException, if already instanciated
	 */
	protected EventTypeManager( ) throws SingletonException
	{
		if ( this.hInstance != null )
			throw new SingletonException( );
		hInstance 	= this;
	}

	/**
	 * Will return the valid singleton-instance and set the name of the type-file, if not already done.
	 * @param FilePath
	 * @return EventTypeManager
	 */
	synchronized public static EventTypeManager getInstance( String FilePath ) throws IllegalArgumentException,
																					  XMLException,
																					  IOException
	{
		if ( hInstance == null )
		{
			hInstance = new EventTypeManager( );
			if ( (FilePath == null) || (FilePath.length()<1) )
				throw new IllegalArgumentException("FilePath was null and can not be set!");

			File f = new File( FilePath );
			if( (f.exists() && (!f.canWrite()) && (f.isFile())) )
				throw new IOException();

			if ( !f.exists( ) )
			{
				hInstance.hCore = new XMLCore( FilePath );
				hInstance.hCore.createDomFromScratch( EventTypeManager.XML_TYPES );
				if ( !hInstance.hCore.saveDom( ) )
				{
					Log.addLog( "Could not create XML-File: " + FilePath );
					throw new IOException( );
				}
			}
			else
			{
				try
				{
					hInstance.hCore	= new XMLCore( FilePath );
					hInstance.hCore.createDOM( );
				}
				catch ( IOException e )
				{
					Log.addLog( "Bad XML-File: " + FilePath );
					throw new IOException( );
				}
			}
		}
		return hInstance;
	}

	/**
	 * Returns a valid singleton-instance, if it was initalized already. If not, it will throw an IllegalArgumentException!
	 * @return EventTypeManager
	 */
	synchronized public static EventTypeManager getInstance( ) throws IllegalArgumentException
	{
		if ( hInstance == null )
			throw new IllegalArgumentException("Class EventTypeManager was not already initialized. Call getInstance(FilePath) first!");
		return hInstance;
	}

	/**
	 * Will search for an EventTypeID and update the Name of this event, if found.<br>Else it will return false.
	 * @param EventName
	 * @param EventTypeID
	 * @return boolean
	 */
	synchronized public boolean update( String EventName, int EventTypeID )
	{
		if ( (EventName==null) || (EventName.length()<1) )
			return false;
		Element found = this.hCore.find( EventTypeManager.XML_TYPE,
										 EventTypeManager.XML_ID,
										 ""+EventTypeID );
		if ( found == null )
			return false;
		found.setAttribute( EventTypeManager.XML_NAME, EventName );
		return this.hCore.saveDom( );
	}

	/**
	 * Will add an EventType with a name, if this ID is not already used. Else it will return false.
	 * @param EventName
	 * @param EventTypeID
	 * @return boolean
	 */
	synchronized public boolean add( String EventName, int EventTypeID )
	{
		if ( (EventName==null) || (EventName.length()<1) )
			return false;
		Element found = this.hCore.find( this.XML_TYPE,
										 this.XML_ID,
										 ""+EventTypeID );
		// if found, it already exists
		if ( found != null )
			return false;

		// create a new entry
		Element newType = new Element( this.XML_TYPE );
		newType.setAttribute(	this.XML_NAME, 	EventName		);
		newType.setAttribute(	this.XML_ID,	""+EventTypeID	);

		// pass it to the Core
		this.hCore.getHRoot( ).addContent( newType );
		// and save the file
		return this.hCore.saveDom( );
	}

	/**
	 * Will search for an EventTypeID and return the name of this event, if found.<br>Else it will return null.
	 * @param EventTypeID
	 * @return boolean
	 */
	public String getEventName( int EventTypeID )
	{
		Element found = this.hCore.find( this.XML_TYPE,
										 this.XML_ID,
										 ""+EventTypeID );
		// not found, so return null
		if ( found == null )
			return null;

		return found.getAttributeValue( this.XML_NAME );
	}

	/**
	 * Will search for an EventTypeID and delete this one from the database, if found.<br>Else it will return false.
	 * @param EventTypeID
	 * @return boolean
	 */
	synchronized public boolean delete( int EventTypeID )
	{
		Element found = this.hCore.find( this.XML_TYPE,
										 this.XML_ID,
										 ""+EventTypeID );
		// not found, so return null
		if ( found == null )
			return false;
		// found, so remove it
		this.hCore.getHRoot().removeContent( found );
		// save the file
		return this.hCore.saveDom( );
	}

	/**
	 * Will search for an ID not used by the manager.
	 * @return free ID as int
	 */
	public int getFreeID( )
	{
		for ( int i=0; i<1000000; i++)	// a million event-types should be enough for hole Germany ;-)
		{
			Element found = this.hCore.find( this.XML_TYPE,
											 this.XML_ID,
											 ""+i );
			if ( found == null )
				return i;
		}
		return -1;						// -1 may be a valid ID, but should neither be used nor ever be returned
	}

	/**
	 * Will create a Map, where key=EventTypeID and value=EventTypeName
	 * @return Map the created Map of all pairs
	 */
	public Collection getAllEventTypes( )
	{
		Iterator it = this.hCore.getHRoot().getChildren().iterator();
		Collection res		= Collections.synchronizedList(new ArrayList( ));
		while ( it.hasNext() )
		{
			Element elem = (Element)it.next();
			String name  = elem.getAttributeValue( this.XML_NAME );
			String sid	 = elem.getAttributeValue( this.XML_ID   );
			if ( (name==null) || (name.length()<1) )
			{
				Log.addLog( "ERROR: EventType-Database contains an invalid entry for an event-type-name!");
				return res;
			}
			if ( (sid==null) || (sid.length()<1) )
			{
				Log.addLog( "ERROR: EventType-Database contains an invalid entry for an event-type-id!");
				return res;
			}
			int id;
			try
			{
				id = Integer.parseInt( sid );
			}
			catch (Exception e)
			{
				Log.addLog( "ERROR: EventType-Database contains an invalid entry for an event-type-id!");
				return res;
			}
			res.add( new EventType( name, id ) );
		}
		return res;
	}
}