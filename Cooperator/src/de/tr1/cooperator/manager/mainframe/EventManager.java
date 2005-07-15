/*
File:		EventManager.java
Created:	05-05-31@16:00
Task:		Will be a singleton and manage a global file for Events
Author:		Sebastian Kohl, Carsten Lüdecke

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
import de.tr1.cooperator.manager.system.*;

import de.tr1.cooperator.model.mainframe.*;

/**
 * Will be a singleton and manage a global file for Events
 *
 * @author Sebastian Kohl, Carsten Lüdecke
 * @version 05-06-04@08:30
 *
 */

public class EventManager
{
	public  static	final String	XML_EVENTS						=	"events";
	public  static	final String	XML_EVENT						=	"event";
	public  static	final String	XML_EVENT_MAX_SUBSCRIPTIONS		=	"max_sub";
	public  static	final String	XML_EVENT_SUBSCRIPTION_START	=	"sub_start";
	public  static	final String	XML_EVENT_SUBSCRIPTION_END		=	"sub_end";
	public  static	final String	XML_EVENT_UNSUBSCRIPTION_END	=	"unsub_end";
	public  static	final String	XML_EVENT_PARENT				=	"parent";
	public  static	final String	XML_EVENT_GROUP					=	"group";
	public  static	final String	XML_EVENT_TYPE					=	"type";
	public  static	final String	XML_EVENT_NAME					=	"name";
	public  static	final String	XML_EVENT_ID					=	"id";
	public  static	final String	XML_EVENT_LECTURER				=	"lecturer";
	public  static	final String	XML_EVENT_START					=	"start";
	public  static	final String	XML_EVENT_INFO					=	"info";
	public  static	final String	XML_TIMES						=	"times";
	public  static	final String	XML_TIME						=	"time";
	public  static	final String	XML_TIME_RHYTHM					=	"rhythm";
	public  static	final String	XML_TIME_DAY					=	"day";
	public  static	final String	XML_TIME_LOCATION				=	"location";
	public  static	final String	XML_SUBSCRIPTION				=	"subs";
	public  static	final String	XML_SUBSCRIPTION_USER			=	"user";
	public	static	final String	XML_REQUIREMENTS				=	"allowed";
	public	static	final String	XML_REQUIREMENT					=	"allow";
	public	static	final String	XML_REQUIREMENT_NUMBER			=	"number";

	private static	EventManager	hInstance		= null;
	private			XMLCore			hCore			= null;
	private			String			sBasePath		= null;

	/**
	 * Will create the needed instance, but throw a SingletonException, if already instanciated
	 */
	protected EventManager( ) throws SingletonException
	{
		if ( this.hInstance != null )
			throw new SingletonException( );
		hInstance 	= this;
	}

	/**
	 * Will return the valid singleton-instance and set the name of the file, if not already done.
	 * @param FilePath
	 * @return Instance of the EventManager
	 */
	synchronized public static EventManager getInstance( String BasePath ) throws IllegalArgumentException,
																					  XMLException,
																					  IOException
	{
		if ( hInstance == null )
		{
			hInstance = new EventManager( );
			if ( (BasePath == null) || (BasePath.length()<1) )
				throw new IllegalArgumentException("FilePath was null and can not be set!");

			hInstance.sBasePath = BasePath;

			String filename = BasePath+"Events.xml";
			File f = new File( filename );
			if( (f.exists() && (!f.canWrite()) && (f.isFile())) )
				throw new IOException();

			if ( !f.exists( ) )
			{
				hInstance.hCore = new XMLCore( filename );
				hInstance.hCore.createDomFromScratch( EventManager.XML_EVENTS );
				if ( !hInstance.hCore.saveDom( ) )
				{
					Log.addLog( "Could not create XML-File: " + filename );
					throw new IOException( );
				}
			}
			else
			{
				try
				{
					hInstance.hCore	= new XMLCore( filename );
					hInstance.hCore.createDOM( );
				}
				catch ( IOException e )
				{
					Log.addLog( "Bad XML-File: " + filename );
					throw new IOException( );
				}
			}
		}
		return hInstance;
	}

	/**
	 * Returns a valid singleton-instance, if it was initalized already. If not, it will throw an IllegalArgumentException!
	 * @return Instance of the EventManager
	 */
	synchronized public static EventManager getInstance( ) throws IllegalArgumentException
	{
		if ( hInstance == null )
			throw new IllegalArgumentException("Class EventManager was not already initialized. Call getInstance(FilePath) first!");
		return hInstance;
	}

	/**
	 * Will add an event to the database, if possible and not existant. Else it will return false.
	 * @param Event to add
	 * @return Success as boolean
	 */
	public synchronized boolean addEvent( Event TheEvent )
	{
		// is the event valid?
		if ( TheEvent == null )
			return false;

		// try to find the event and if, return, because it can not be inserted
		Element event = this.hCore.find( this.XML_EVENT, this.XML_EVENT_ID, ""+TheEvent.getID( ) );
		if ( event != null )
			return false;

		// does it have a valid Group-ID? [-1 is invalid]
		if ( TheEvent.getGroup()==-1 )
			TheEvent.setGroup( this.getFreeGroupID() );

		Element newEvent = new Element( this.XML_EVENT );
		newEvent.setAttribute( this.XML_EVENT_MAX_SUBSCRIPTIONS,  ""+TheEvent.getMaxSubscriptions() );
		newEvent.setAttribute( this.XML_EVENT_SUBSCRIPTION_START, ""+TheEvent.getSubscriptionStart() );
		newEvent.setAttribute( this.XML_EVENT_SUBSCRIPTION_END,   ""+TheEvent.getSubscriptionEnd() );
		newEvent.setAttribute( this.XML_EVENT_UNSUBSCRIPTION_END, ""+TheEvent.getUnsubscriptionEnd() );
		newEvent.setAttribute( this.XML_EVENT_PARENT,   		  ""+TheEvent.getParent() );
		newEvent.setAttribute( this.XML_EVENT_GROUP,	   		  ""+TheEvent.getGroup() );
		newEvent.setAttribute( this.XML_EVENT_TYPE,   			  ""+TheEvent.getType() );
		newEvent.setAttribute( this.XML_EVENT_NAME,   			  TheEvent.getName() );
		newEvent.setAttribute( this.XML_EVENT_ID,   			  ""+TheEvent.getID() );
		newEvent.setAttribute( this.XML_EVENT_LECTURER,   		  TheEvent.getLecturer() );
		newEvent.setAttribute( this.XML_EVENT_START, 	  		  ""+TheEvent.getStart() );
		newEvent.setAttribute( this.XML_EVENT_INFO, 	  		  TheEvent.getInfoText() );
		this.hCore.getHRoot().addContent( newEvent );
		Collection cTimes = TheEvent.getTimes( );
		Iterator i = cTimes.iterator( );
		while ( i.hasNext( ) )
		{
			Element times	  = new Element( this.XML_TIMES );
			EventTime eTime	  = (EventTime)i.next( );
			times.setAttribute( this.XML_TIME,			eTime.getClockTime( ) );
			times.setAttribute( this.XML_TIME_DAY,		eTime.getDayName( )   );
			times.setAttribute( this.XML_TIME_RHYTHM,	""+eTime.getRhythm( ) );
			times.setAttribute( this.XML_TIME_LOCATION,	eTime.getLocation( )       );
			newEvent.addContent( times );
		}
		Collection cUsers = TheEvent.getSubscriberList( );
		i = cUsers.iterator( );
		while ( i.hasNext( ) )
		{
			Element subsc	= new Element( this.XML_SUBSCRIPTION );
			String name		= (String)i.next( );
			subsc.setAttribute( this.XML_SUBSCRIPTION_USER,	name );
			newEvent.addContent( subsc );
		}
		return this.hCore.saveDom( );
	}

	/**
	 * Will add remove an Event from the database, if possible and existant. Else it will return false.
	 * @param Event to remove
	 * @return Success as boolean
	 */
	public synchronized boolean deleteEvent( Event TheEvent )
	{
		Element found = this.hCore.find( this.XML_EVENT,
										 this.XML_EVENT_NAME,
										 TheEvent.getName() );
		// not found, so return false
		if (found == null) return false;

		// found, so remove it
		this.hCore.getHRoot().removeContent(found);

		// save the file
		return this.hCore.saveDom();
	}

	/**
	 * Returns true if the update from the Subscription was successfully
	 * @param newParticipant to update
	 * @return boolean
	 */
	synchronized public boolean updateEvent(Event TheEvent)
	{
		if ( (TheEvent.getName()==null) || (TheEvent.getName().length()<1) ) return false;

		Element found = this.hCore.find( this.XML_EVENT,
										 this.XML_EVENT_ID,
										 ""+ TheEvent.getID());

		if ( found == null ) return false;

		Element newEvent = found;
		newEvent.setAttribute( this.XML_EVENT_MAX_SUBSCRIPTIONS,  ""+TheEvent.getMaxSubscriptions() );
		newEvent.setAttribute( this.XML_EVENT_SUBSCRIPTION_START, ""+TheEvent.getSubscriptionStart() );
		newEvent.setAttribute( this.XML_EVENT_SUBSCRIPTION_END,   ""+TheEvent.getSubscriptionEnd() );
		newEvent.setAttribute( this.XML_EVENT_UNSUBSCRIPTION_END, ""+TheEvent.getUnsubscriptionEnd() );
		newEvent.setAttribute( this.XML_EVENT_PARENT,   		  ""+TheEvent.getParent() );
		newEvent.setAttribute( this.XML_EVENT_GROUP,   			  ""+TheEvent.getGroup() );
		newEvent.setAttribute( this.XML_EVENT_TYPE,   			  ""+TheEvent.getType() );
		newEvent.setAttribute( this.XML_EVENT_NAME,   			  TheEvent.getName() );
		newEvent.setAttribute( this.XML_EVENT_ID,   			  ""+TheEvent.getID() );
		newEvent.setAttribute( this.XML_EVENT_LECTURER,   		  TheEvent.getLecturer() );
		newEvent.setAttribute( this.XML_EVENT_START, 	  		  ""+TheEvent.getStart() );
		newEvent.setAttribute( this.XML_EVENT_INFO, 	  		  TheEvent.getInfoText() );
		newEvent.removeContent( );
		Collection cTimes = TheEvent.getTimes( );
		Iterator i = cTimes.iterator( );
		while ( i.hasNext( ) )
		{
			Element times	  = new Element( this.XML_TIMES );
			EventTime eTime	  = (EventTime)i.next( );
			times.setAttribute( this.XML_TIME,			eTime.getClockTime( ) );
			times.setAttribute( this.XML_TIME_DAY,		eTime.getDayName( )   );
			times.setAttribute( this.XML_TIME_RHYTHM,	""+eTime.getRhythm( ) );
			times.setAttribute( this.XML_TIME_LOCATION,	eTime.getLocation( )       );
			newEvent.addContent( times );
		}
		Collection cUsers = TheEvent.getSubscriberList( );
		i = cUsers.iterator( );
		while ( i.hasNext( ) )
		{
			Element subsc	= new Element( this.XML_SUBSCRIPTION );
			String name		= (String)i.next( );
			subsc.setAttribute( this.XML_SUBSCRIPTION_USER,	name );
			newEvent.addContent( subsc );
		}

		return this.hCore.saveDom( );

	}

	private Event makeEvent( Element TheElement )
	{
		// check for validity
		if ( TheElement==null )
			return null;
		// grab the values from the File
		String name	= TheElement.getAttributeValue( this.XML_EVENT_NAME );
		String lect	= TheElement.getAttributeValue( this.XML_EVENT_LECTURER );
		String info	= TheElement.getAttributeValue( this.XML_EVENT_INFO );
		if ( (name == null) || (name.length()<1) )
		{
			Log.addLog( "ERROR: EventDatabase for one event is incorrect (no name available)!");
			return null;
		}
		if ( (lect == null) || (lect.length()<1)  )
		{
			Log.addLog( "ERROR: EventDatabase for event \""+name+"\" is incorrect (no lecturer available)!");
			return null;
		}
		if ( info == null )
		{
			Log.addLog( "ERROR: EventDatabase for event \""+name+"\" is incorrect (no info available)!");
			return null;
		}
		String ssstart	=  TheElement.getAttributeValue( this.XML_EVENT_SUBSCRIPTION_START	);
		String ssend	=  TheElement.getAttributeValue( this.XML_EVENT_SUBSCRIPTION_END	);
		String suend	=  TheElement.getAttributeValue( this.XML_EVENT_UNSUBSCRIPTION_END	);
		String sstarts	=  TheElement.getAttributeValue( this.XML_EVENT_START				);
		String smax		=  TheElement.getAttributeValue( this.XML_EVENT_MAX_SUBSCRIPTIONS	);
		String sparent	=  TheElement.getAttributeValue( this.XML_EVENT_PARENT				);
		String sgroup	=  TheElement.getAttributeValue( this.XML_EVENT_GROUP				);
		String stype	=  TheElement.getAttributeValue( this.XML_EVENT_TYPE				);
		String sid		=  TheElement.getAttributeValue( this.XML_EVENT_ID					);
		int max, parent, type, id, group;
		long sstart, send, uend, start;
		// try to parse all these values to ints
		try
		{
			max		= Integer.parseInt	( smax		);
			parent	= Integer.parseInt	( sparent	);
			type	= Integer.parseInt	( stype		);
			id		= Integer.parseInt	( sid		);
			group	= Integer.parseInt	( sgroup	);
			sstart	= Long.parseLong	( ssstart	);
			send	= Long.parseLong	( ssend		);
			uend	= Long.parseLong	( suend		);
			start	= Long.parseLong	( sstarts	);
		}
		catch (Exception e)
		{
			Log.addLog( "ERROR: Entry for event \""+name+"\" failed and seems to contain errors!");
			return null;
		}
		Iterator i = TheElement.getChildren( ).iterator( );
		Collection subs = Collections.synchronizedList(new ArrayList());
		Collection tims = Collections.synchronizedList(new ArrayList());
		while ( i.hasNext( ) )
		{
			// trace the children
			Element child = (Element)i.next( );
			// check, if this element contains an event-id
			String val = child.getName( );
			if ( val == null )
				return null;
			if ( val.equals( this.XML_TIMES ) )
			{
				//=========== parse a time for the event =======================================
				String time = child.getAttributeValue( this.XML_TIME		);
				String day  = child.getAttributeValue( this.XML_TIME_DAY	);
				String srhy = child.getAttributeValue( this.XML_TIME_RHYTHM	);
				String loca	= child.getAttributeValue( this.XML_TIME_LOCATION );
				if ( (loca == null) || (loca.length()<1)  )
				{
					Log.addLog( "ERROR: EventDatabase for event \""+name+"\" is incorrect (no location available)!");
					return null;
				}
				if ( (time == null) || (time.length()<1) )
				{
					Log.addLog( "ERROR: EventDatabase for event \""+name+"\" is incorrect (no time available)!");
					return null;
				}
				if ( (day == null) || (day.length()<1) )
				{
					Log.addLog( "ERROR: EventDatabase for event \""+name+"\" is incorrect (no day available)!");
					return null;
				}
				int rhy;
				try
				{
					rhy = Integer.parseInt( srhy );
				}
				catch (Exception e)
				{
					Log.addLog( "ERROR: EventDatabase for event \""+name+"\" has invalid entry on rhythm!");
					return null;
				}
				EventTime et = new EventTime( day, time, rhy, loca );
				tims.add( et );
			}
			else
				if ( val.equals( this.XML_SUBSCRIPTION ) )
				{
					//=========== parse a subscription for the event =======================================
					String login = child.getAttributeValue( this.XML_SUBSCRIPTION_USER	);
					if ( (login == null) || (login.length()<1) )
					{
						Log.addLog( "ERROR: EventDatabase for event \""+name+"\" is incorrect for subscription-entry (no login available)!");
						return null;
					}
					subs.add( login );
				}
				else
				{
					Log.addLog( "ERROR: Database \""+name+"\" contains an unknown Child-Tag for Event!");
					return null;
				}
		}
		Event ev = new Event(	name,
								type,
								id,
								parent,
								group,
								lect,
								info,
								start,
								tims,
								sstart,
								send,
								uend,
								subs,
								max );
		return ev;
	}

	/**
	 * Will return a true after unsubscribing the user in any event he´s sbscribed
	 * @param UserLogin of the user to unsubscribe
	 * @return boolean if unsubscription was succesful
	 */
	public synchronized boolean unsubscribeEverywhere( String UserLogin )
	{
		// Recoded by Sebastian Kohl, because of the bullshit-code, you can find further down.
		// I hate it to correct such BULLSHIT, because you never can trust anybody but yourself
		// and Peter Matjeschk. Also you can never know, where you can find more of this crappy code.
		if ( (UserLogin==null) || (UserLogin.length()<1) )
			return false;
		Iterator events = this.hCore.getHRoot().getChildren().iterator();
		while ( events.hasNext() )
		{
			Element elem = (Element)events.next();
			Element sub	 = XMLCore.find( elem, XML_SUBSCRIPTION, XML_SUBSCRIPTION_USER, UserLogin );
			if ( sub==null )
				continue;
			// here the login was found as subscription -> remove it
			elem.removeContent( sub );
		}
		return this.hCore.saveDom();
/*		Collection SubscribedEvents = this.getSubscribedEventsByGetAll( UserLogin );	//# wrong, because there is allready a method
																						//# also wrong, because not performant
		//Collection SubscribedEvents = this.getSubscribedEvents( UserLogin );
		User myUser = UserManager.getInstance().getUser( UserLogin );

		Iterator it = SubscribedEvents.iterator();
		while(it.hasNext())
		{
			Event curEvent = (Event) it.next();
			curEvent.unsubscribe( UserLogin, true );
			Log.addLog( "User "+myUser.getFirstName()+" "+myUser.getSurname()+" ("+myUser.getLogin()+") unsubscribed from event \""+curEvent.getName()+"\" (#"+curEvent.getID()+")" );
		}
		//# wrong because nothing was saved to the database
		return true;*/
	}

	/**
	 * Will return a true after renaming lecturer to the new one in any event the old lecturer was lecturer
	 * @param UserLogin of the old lecturer
	 * @param newLecturer UserLogin of the new lecturer
	 * @return boolean if renaming was succesful
	 */
	public synchronized boolean renameLecturer( String UserLogin, String newLogin )
	{
		// Recoded by Sebastian Kohl, because of the bullshit-code, you can find further down.
		// I hate it to correct such BULLSHIT, because you never can trust anybody but yourself
		// and Peter Matjeschk. Also you can never know, where you can find more of this crappy code.
		if ( (UserLogin==null) || (UserLogin.length()<1) || (newLogin==null) || (newLogin.length()<1) )
			return false;
		Iterator events = this.hCore.getHRoot().getChildren().iterator();
		while ( events.hasNext() )
		{
			Element elem = (Element)events.next();
			String	lect = elem.getAttributeValue( this.XML_EVENT_LECTURER );
			String name  = elem.getAttributeValue( this.XML_EVENT_NAME );
			if ( (lect==null) || (lect.length()<1) )
			{
				if ( name==null )
					name="(ERROR, also no name available)";
				Log.addLog( "ERROR: EventDatabase for event \""+name+"\" is incorrect (no lecturer available)!");
				return false;
			}
			elem.setAttribute( this.XML_EVENT_LECTURER, newLogin );
			Log.addLog( "Lecturer of Event "+name+"\" was renamend from "+UserLogin+" to "+newLogin+"." );
		}
		return this.hCore.saveDom();
/*		User oldLecturer = UserManager.getInstance().getUser( UserLogin );
		User newLecturer = UserManager.getInstance().getUser( newLogin );	//# wrong, because can be "-"==none ... no real user!

		Collection lecturedEvents	= new ArrayList();						//# wrong, because getLecturedEvents returns a new Collection!
		lecturedEvents				= this.getLecturedEvents( UserLogin );	//get the lectured events	//# wrong because no performance!
		boolean bEventsLecturerRenamed	= true;
		boolean curSuccess				= false;

		Iterator it = lecturedEvents.iterator();
		while( it.hasNext() )
			{
				Event curEvent = (Event) it.next();
				curEvent.setLecturer( newLogin );
				curSuccess = this.updateEvent( curEvent );
				bEventsLecturerRenamed = bEventsLecturerRenamed & curSuccess;
				Log.addLog( "Lecturer of Event "+curEvent.getName()+"\" (#"+curEvent.getID()+") was renamend from "+oldLecturer.getFirstName()+" "+oldLecturer.getSurname()+" ("+oldLecturer.getLogin()+") in "+newLecturer.getFirstName()+" "+newLecturer.getSurname()+" ("+newLecturer.getLogin()+")" );
			}
		return bEventsLecturerRenamed;
		//# wrong, because here was saved NOTHING to the Database!!!
		/**/
	}

	/**
	 * Will return an event by searching for it's name.<br>If there are two events with the same name, the first found will be returned.
	 * @param EventName of the event to find
	 * @return Event as new Object
	 */
	public Event getEventByName( String EventName )
	{
		// if invalid, return false
		if ( (EventName==null) || (EventName.length()<1) )
			return null;

		// search the event by a name. This will bean, exact match!
		Element event = this.hCore.find( this.XML_EVENT, this.XML_EVENT_NAME, EventName );
		return this.makeEvent( event );
	}

	/**
	 * This returnes a Collection of all Events, that are stored in the Database
	 * @return a Collection of Events is returned
	 */
	public Collection getAllEvents( )
	{
		Collection allEvents = Collections.synchronizedList(new ArrayList());

		Iterator it;
		it = this.hCore.getHRoot().getChildren(this.XML_EVENT).iterator( );


		while ( it.hasNext( ) )
		{
			allEvents.add( this.makeEvent((Element)it.next()) );
		}

		return allEvents;
	}

	/**
	 * Will search for an ID not used by the manager.
	 * @return free ID as int
	 */
	public int getFreeID( )
	{
		for ( int i=0; i<1000000; i++)	// a million events should be enough for hole Germany ;-)
		{
			Element found = this.hCore.find( this.XML_EVENT,
											 this.XML_EVENT_ID,
											 ""+i );
			if ( found == null )
				return i;
		}
		return -1;						// -1 may be a valid ID, but should neither be used nor ever be returned
	}

	/**
	 * Will search for an ID not used by the manager.
	 * @return free ID as int
	 */
	public int getFreeGroupID( )
	{
		for ( int i=0; i<1000000; i++)	// a million events should be enough for hole Germany ;-)
		{
			Element found = this.hCore.find( this.XML_EVENT,
											 this.XML_EVENT_GROUP,
											 ""+i );
			if ( found == null )
				return i;
		}
		return -1;						// -1 may be a valid ID, but should neither be used nor ever be returned
	}

	/**
	 * Returns the Event that was searched by ID
	 * @param ID identify the Event
	 * @return Event
	 */
	public Event getEventByID(int ID)
	{
		Element found = this.hCore.find( this.XML_EVENT,
										 this.XML_EVENT_ID,
										 ""+ID );

		// not found, so return false
		if (found == null) return null;

		return this.makeEvent(found);
	}

	/**
	 * This will check, if a personal-number of a user given by his login is in a subscriber-list or not.
	 * @param UserLogin The Login-Name of the user which personal-number should be found
	 * @param EventID The ID of the event which list should be searched for
	 * @return boolean TRUE if the user is in the list or the file does not exist. FALSE if the file exists, but the user is not in or was not found in the user-manager.
	 */
	public boolean isInRequirementList( String UserLogin, int EventID )
	{
		// validity checks
		if ( (UserLogin==null) || (UserLogin.length()<1) )
			return false;
		// grab the User
		User user = null;
		try
		{
			user = UserManager.getInstance().getUser( UserLogin );
		}
		catch ( Exception e )
		{
			return false;
		}
		if ( user==null )
			return false;
		// base-path
		String file	 = this.sBasePath + "Requirements" + File.separator + "Req_"+EventID+".xml";
		XMLCore Core = null;
		// load the file
		try
		{
			Core = XMLCore.loadFile( file, false, this.XML_REQUIREMENTS );
		}
		catch ( Exception e )
		{	// Error occured, so return false
			return false;
		}
		// no such file -> return true
		if ( Core == null )
			return true;
		// search the user
		Element found = Core.find( this.XML_REQUIREMENT, this.XML_REQUIREMENT_NUMBER, user.getPersonalNumber() );
		// return the result
		return ( found == null );
	}

	/**
	 * This will return a Collection of PersonalNumbers (Strings), that are allowed for an event
	 * @param EventID The ID of the event which list should be returned for
	 * @return Colelction A Collection of all PersonalNumbers will be returned. The Collection will be empty, if there is no file for the given Event
	 */
	public Collection getAllowanceList( int EventID )
	{
		Collection res = Collections.synchronizedList(new ArrayList());

		//exit if this event has no id till now
		if( EventID == -1 )
			return res;

		// base-path
		String file	 = this.sBasePath + "Requirements" + File.separator + "Req_"+EventID+".xml";
		XMLCore Core = null;
		// load the file
		try
		{
			Core = XMLCore.loadFile( file, false, this.XML_REQUIREMENTS );
		}
		catch ( Exception e )
		{	// Error occured, so return
			return res;
		}
		// no such file -> return true
		if ( Core == null )
			return res;
		// trace all
		Iterator it = Core.getHRoot().getChildren().iterator();
		while ( it.hasNext() )
		{
			Element next = (Element)it.next();
			String number = next.getAttributeValue( this.XML_REQUIREMENT_NUMBER );
			if ( (number!=null) && (number.length()>0) )
				res.add( number );
		}
		return res;
	}

	/**
	 * This will set the list of allowed subscribers. This will means, that ONLY they
	 * in the given Collection will be allowed!
	 * The given Collection should be of type String and contain only personal-numbers.
	 * @param List The Collection of Strings with personal-numbers
	 * @param EventID The id of the event the list should be set for
	 * @return boolean success
	 */
	public synchronized boolean setAllowanceList( Collection List, int EventID )
	{
		// first delete the file, that contains all allowances
		// Name of the file is based on the event-id it lists results for
		String name	 = this.sBasePath + "Requirements" + File.separator + "Req_"+EventID+".xml";
		File xml	 = new File(name);
		if ( xml.exists() )
		{
			if ( !xml.isFile() )
				return false;
			if ( !xml.delete( ) )
				return false;
		}

		XMLCore Core = null;
		if ( (List==null) || (List.size()==0) )
			return true;

		// recreate the file
		try
		{
			Core = XMLCore.loadFile( name, true, this.XML_REQUIREMENTS );
		}
		catch ( Exception e )
		{	// Error occured, so return
			return false;
		}
		// no such file -> return true
		if ( Core == null )
			return false;
		// trace all
		Iterator it = List.iterator();
		while ( it.hasNext() )
		{
			String number = (String)it.next();
			Element newe  = new Element( this.XML_REQUIREMENT );
			newe.setAttribute( this.XML_REQUIREMENT_NUMBER, number );
			Core.getHRoot().addContent( newe );
		}
		return Core.saveDom();
	}
	/**
	 * This returns a Collection of events a specified user is subscripted in
	 * @param UserLogin The Login-Name of the User
	 * @return Collection Collection of Events
	 */
	public Collection getSubscribedEvents( String UserLogin )
	{
		Collection res = Collections.synchronizedList(new ArrayList());
		if ( (UserLogin==null) || (UserLogin.length()<1) )
			return res;
		Iterator events = this.hCore.getHRoot().getChildren().iterator();
		while ( events.hasNext() )
		{
			Element elem = (Element)events.next();
			Element sub	 = XMLCore.find( elem, XML_SUBSCRIPTION, XML_SUBSCRIPTION_USER, UserLogin );
			if ( sub==null )
				continue;
			Event event  = this.makeEvent( elem );
			if ( event==null )
				continue;
			res.add( event );
		}
		return res;
	}

	/*
	 * This returns a Collection of events a specified user is subscripted in but uses the getAllEvents() methode.
	 * @param UserLogin The Login-Name of the User
	 * @return Collection Collection of Events
	 */
/*	public Collection getSubscribedEventsByGetAll( String UserLogin )
	{
		Collection res = Collections.synchronizedList(new ArrayList());
		if ( (UserLogin==null) || (UserLogin.length()<1) )
			return res;
		Collection cEventList = new ArrayList();
		cEventList = this.getAllEvents();

		Iterator itEvent = cEventList.iterator();
		while( itEvent.hasNext() )
		{
			Event curEvent = (Event) itEvent.next();
			if( curEvent.isSubscribed( UserLogin ) )
			{
				res.add( curEvent );
			}
		}
		return res;
	}/**/

	/**
	 * This returns a Collection of events a specified user is lecturer of
	 * @param UserLogin The Login-Name of the User
	 * @return Collection Collection of Events
	 */
	public Collection getLecturedEvents( String UserLogin )
	{
		Collection res = Collections.synchronizedList(new ArrayList());
		if ( (UserLogin==null) || (UserLogin.length()<1) )
			return res;
		Collection cEventList = new ArrayList();
		cEventList = this.getAllEvents();

		Iterator itEvent = cEventList.iterator();
		while( itEvent.hasNext() )
		{
			Event curEvent = (Event) itEvent.next();
			if( curEvent.getLecturer().equals( UserLogin ) )
			{
				res.add( curEvent );
			}
		}
		return res;
	}

	/**
	 * Will count the events with the given group-id.
	 * @param int the group-id to count for
	 * @return int the count of the events with that id
	 */
	public int countEventsInGroup( int GroupID )
	{
		int count = 0;
		String sid= ""+GroupID;
		Iterator events = this.hCore.getHRoot().getChildren().iterator();
		while ( events.hasNext() )
		{
			Element elem = (Element)events.next();
			String	sgid = elem.getAttributeValue( this.XML_EVENT_GROUP );
			if ( sgid==null )
			{
				Log.addLog( "ERROR: empty or no value for "+this.XML_EVENT_GROUP+" in event-database!" );
				continue;
			}
			if ( sgid.equals( sid ) )
				count++;
		}
		return count;
	}
}