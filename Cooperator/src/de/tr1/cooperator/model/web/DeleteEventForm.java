/*
File:		DeleteEventForm.java
Created:	05-06-12@13:00
Task:		This is the Form-Bean for the deleteEvent.jsp-site
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


package de.tr1.cooperator.model.web;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import java.util.*;
import java.util.regex.*;
import java.text.SimpleDateFormat;

import de.tr1.cooperator.model.mainframe.*;
import de.tr1.cooperator.manager.web.*;
import de.tr1.cooperator.model.web.*;
import de.tr1.cooperator.exceptions.*;
import de.tr1.cooperator.manager.ManagerInitializer;
import de.tr1.cooperator.manager.mainframe.UserManager;
import de.tr1.cooperator.manager.mainframe.EventTypeManager;
import de.tr1.cooperator.manager.mainframe.EventManager;
import de.tr1.cooperator.manager.system.*;

/**
 * This is the Form-Bean for the deleteEvent.jsp-site
 * This stores all the date of the formular and verifys for correct input
 *
 * @author Sebastian Kohl
 * @version 05-06-12@13:00
 *
 */

public class DeleteEventForm extends Accessable
{
	protected	Event		hEvent;
	protected	Collection	cAllEvents;
	protected	int			iEventID = -1;
	protected	String		sMessage;
	protected	boolean		bDeleted;

//======= get-Methods ===============

	public	boolean getAllowedToEdit( )
	{
		if ( (this.hEvent == null) || (this.hUser==null) )
			return false;
		else
			return ( this.hasAdminRights()
					|| this.hEvent.getLecturer().equals(this.hUser.getLogin()) );
	}

	public	Collection	getAllEvents( )
	{
		if ( this.hEvent == null )
			return new ArrayList();
		else
			return this.cAllEvents;
	}

	public	int	getEventID( )
	{
		if ( this.hEvent == null )
			return -1;
		else
			return this.hEvent.getID();
	}

	public	String	getEventName( )
	{
		if ( this.hEvent == null )
			return "";
		else
			return this.hEvent.getName();
	}

	public	Event	getEvent( )
	{
		return this.hEvent;
	}

	public	String	getMessage( )
	{
		return this.sMessage;
	}

	public	boolean	getDeleted( )
	{
		return this.bDeleted;
	}

//======= set-Methods ===============


	public	void setMessage( String val )
	{
		this.sMessage = val;
	}

	public	void setEventID( int id )
	{
		this.iEventID = id;
	}

	public	void setDeleted( boolean val )
	{
		this.bDeleted = val;
	}

//======== non-set-get-Methods ===============

	synchronized public void initValues( HttpServletRequest request )
	{
		super.initValues(request);
		this.initValuesWithParam( request, true );
	}

	synchronized public void initValuesWithParam( HttpServletRequest request, boolean ShowEventErrors )
	{
		super.initValues(request);

		// grab the event-ID out of the request
		String EventID		= request.getParameter( "eventID" );

		this.hEvent			= null;
		this.sMessage		= "";
		this.bDeleted		= false;
		if ( this.hUser == null )
			return;
		try
		{
			if ( this.iEventID == -1 )
				this.iEventID = Integer.parseInt( EventID );
		}
		catch ( Exception e )
		{
			this.sErrorMessage += "Falsche oder ungültige Event-ID angegeben!";
			return;
		}
		// grab the event
		try
		{
			this.cAllEvents	= new ArrayList();
			if ( ShowEventErrors )
			{
				this.hEvent		= EventManager.getInstance().getEventByID( this.iEventID );
				if ( this.hEvent == null )
				{
					this.sErrorMessage += "Ungültige Event-ID angegeben, Event existiert nicht!";
					return;
				}
				if ( !this.getAllowedToEdit() )
				{
					this.sErrorMessage += "Sie haben keine Rechte zum Löschen dieser Veranstaltung.";
					return;
				}

				// grab all children of this event
				Collection all	= EventManager.getInstance().getAllEvents();
				this.cAllEvents.add( this.hEvent );
				Collection subs	= createSortedSubEventList( this.hEvent.getID(), all );
				if ( subs != null )
					this.cAllEvents.addAll( subs );
			}
		}
		catch ( Exception e )
		{
			this.sErrorMessage += "Ein Fehler ist aufgetreten: \r\n" + e;
			return;
		}
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
 	{
		ActionErrors errors = new ActionErrors();
		return errors;
	}


	/**
	 * This method creates the sorts the ArrayLists from the HashMap and creates the "tree"
	 *
	 * Stuff done by Peter Matjeschk
	 *
	 * @param iParent current SuperEvent
	 * @param hmEvents HashMap: every entry is similar to a node in a tree: it has a value (identifer) and some childs (context)
	 * @return ArrayList <EventEntrys> for this SuperEvent
	 */
	private Collection createSubEventListFromHash( int iParent, HashMap hmEvents )
	{
		Event		eCurrentEvent;
		EventEntry	eeCurrentEntry;

		Collection	cSubEventList;										//this is need for the hash and contains an ArrayList
		Collection	cAddCollection;
		Collection	cReturnCollection;
		Iterator	aIterator;											//this is the iterator for the arrayLists stored in the hashMap

		cSubEventList = (ArrayList) hmEvents.get( ""+iParent );			//get list for SuperEvent given through iParent

		if( cSubEventList == null )										//no events stored in this SuperEvent
			return null;

		Collections.sort( (List)cSubEventList );						//sort list for correct output


		cReturnCollection = new ArrayList();							//this is for the return-list
		aIterator = cSubEventList.iterator();
		while( aIterator.hasNext() )
		{
			eCurrentEvent = (Event) aIterator.next();					//get Event in this node

			//add this Event to the list
			cReturnCollection.add( eCurrentEvent );
			//and add all SubEvents
			cAddCollection = createSubEventListFromHash( eCurrentEvent.getID(), hmEvents );
			if( cAddCollection != null )
				cReturnCollection.addAll( cAddCollection );
		}
		return cReturnCollection;
	}

	/**
	 * This method creates a sortet ArrayList of EventEntrys so that they can easily used...
	 *
	 * Stuff done by Peter Matjeschk
	 *
	 * @param iParent this is the root of the "tree": only SubEvents of this Event will be shown
	 * @param cUnsortetList this is the ArrayList of the Events with no order or other things. Just all the raw events
	 * @return sortet 'ArrayList <EventEntrys>' of SubEvents for the Event with @see iParent
	 */
	public Collection createSortedSubEventList( int iParent, Collection cUnsortetList )
	{
		Event		eCurrentEvent;								//this is current iterated event
		Collection	cSubEventList;								//this is need for the hash

		HashMap		hmEvents;									//this stores collections of SubEventLists
		Iterator	ulIterator;									//this is the iterator for the unsortetList
		Iterator	hmIterator;									//this is the iterator for the hashMap

		//create hash in which all subevents where inserted (so that the hash contains a node which consists of parentID and SubEvents)
		ulIterator = cUnsortetList.iterator();
		hmEvents = new HashMap();
		while( ulIterator.hasNext() )
		{
			eCurrentEvent = (Event)ulIterator.next();
			Event myEvent = eCurrentEvent;

			//check if arraylist of events in hash is already there
			//if not create arraylist
			//add event to this list

			cSubEventList = (ArrayList) hmEvents.get( ""+eCurrentEvent.getParent() );	//get old subEventList
			if( cSubEventList == null )													//if no subEventList is there
				cSubEventList = new ArrayList();										//create an ArrayList

			cSubEventList.add( eCurrentEvent );											//add this Event to the ArrayList

			hmEvents.put( ""+eCurrentEvent.getParent(), cSubEventList );				//save ArrayList back in hashmap
		}

		//create the correct collection from the hash
		return createSubEventListFromHash( iParent, hmEvents );
	}

}