/*
File:		ViewEventsForm.java
Created:	05-05-30@11:00
Task:		FormBean for viewEvents.jsp
Author:		Peter Matjeschk

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

import de.tr1.cooperator.manager.system.Log;
import de.tr1.cooperator.model.mainframe.Event;
import de.tr1.cooperator.model.mainframe.EventType;
import de.tr1.cooperator.manager.mainframe.EventManager;
import de.tr1.cooperator.manager.mainframe.EventTypeManager;
import de.tr1.cooperator.model.web.EventEntry;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import java.util.*;

/**
 * FormBean for viewEvents.jsp
 *
 * @author Peter Matjeschk
 * @version 05-06-04@08:30
 */
public class ViewEventsForm extends Accessable
{
	private	final	String	DROPDOWNALL				= "alle Veranstaltungen";
	private	final	String	DROPDOWNALLFROM			= "alle ";
	private	final	String	DROPDOWNALLOWN			= "eigene Veranstaltungen";
	private	final	String	DROPDOWNALLOWNFROMM		= "eigene ";

	private	Collection	cSelectEventList;			//this is needed for the Drop-Down-Menu
	private	String		sSelectedEventValue;		//this is needed for the Drop-Down-Menu

	private	Collection	cEventList;					//this stores all the currently displayed events
	private	boolean		hasEvents;					//if no events are present...


	public ArrayList createDropDownArrayList()
	{
		EventTypeManager	myEventTypeManager;
		ArrayList			alReturn;
		int					iEntryCounter = 0;

		Collection			alAllEventTypes;
		Iterator			iAllEventTypes;

		alReturn = new ArrayList();
		alReturn.add( new DropDownEntry( iEntryCounter++, DROPDOWNALL, new Integer( -1 ) ) );

		//get types from EventTypeManager...
		try
		{
			myEventTypeManager = EventTypeManager.getInstance();
		}
		catch( Exception e )		//2do: this should only be a SingletonException (or IllegalArgumentException)
		{
			Log.addLog( "EventTypeManager not initialized or with bad XML-File" );
			return alReturn;
		}

		//get all EventTypes from Manager
		alAllEventTypes = myEventTypeManager.getAllEventTypes();

		//check wether or not it contains some elements..
		if( (alAllEventTypes != null) && (alAllEventTypes.size() == 0) )
			alAllEventTypes = null;

		//... and put it into the Drop-Down-Menü
		if( alAllEventTypes != null )
		{
			iAllEventTypes = alAllEventTypes.iterator();
			while( iAllEventTypes.hasNext() )
			{
				EventType curEventType = (EventType) iAllEventTypes.next();
				alReturn.add( new DropDownEntry( iEntryCounter++, DROPDOWNALLFROM + curEventType.getName(), new Integer( curEventType.getID() ) ) );
			}
		}
		
		//add own events to drop-down...
		alReturn.add( new DropDownEntry( iEntryCounter++, DROPDOWNALLOWN, new Integer( -3 ) ) );

		return alReturn;
	}

	/**
	 * This method creates the sorts the ArrayLists from the HashMap and creates the "tree"
	 *
	 * @param iParent current SuperEvent
	 * @param hmEvents HashMap: every entry is similar to a node in a tree: it has a value (identifer) and some childs (context)
	 * @param deep this is the deep of the recursion
	 * @return ArrayList <EventEntrys> for this SuperEvent
	 */
	private Collection createEventEntryListFromHash( int iParent, HashMap hmEvents, int deep )
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
			cReturnCollection.add( new EventEntry( this.getUserLogin(), deep, eCurrentEvent ) );
			//and add all SubEvents
			cAddCollection = createEventEntryListFromHash( eCurrentEvent.getID(), hmEvents, deep + 1 );
			if( cAddCollection != null )
				cReturnCollection.addAll( cAddCollection );
		}
		return cReturnCollection;
	}

	/**
	 * This method creates a sortet ArrayList of EventEntrys so that they can easily used...
	 *
	 * @param iParent this is the root of the "tree": only SubEvents of this Event will be shown
	 * @param cUnsortetList this is the ArrayList of the Events with no order or other things. Just all the raw events
	 * @return sortet 'ArrayList <EventEntrys>' of SubEvents for the Event with @see iParent
	 */
	public Collection createSortedEventEntryList( int iParent, Collection cUnsortetList )
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
		return createEventEntryListFromHash( iParent, hmEvents, 0 );
	}
	/**
	 * This method creates a sortet ArrayList of EventEntrys so that they can easily used...
	 *
	 * @param iEventType this is the type of the Events...
	 * @param cUnsortetList this is the ArrayList of the Events with no order or other things. Just all the raw events
	 * @return sortet 'ArrayList <EventEntrys>' of SubEvents for the Event with @see iParent
	 */
	public Collection createSortedEventEntryTypeList( int iEventType, Collection cUnsortetList )
	{
		Event		eCurrentEvent;								//this is current iterated event
		Collection	cSubEventList = null;						//this is need for the hash

		HashMap		hmEvents;									//this stores collections of SubEventLists
		Iterator	ulIterator;									//this is the iterator for the unsortetList
		Iterator	hmIterator;									//this is the iterator for the hashMap

		//create hash in which all subevents where inserted
		ulIterator = cUnsortetList.iterator();
		hmEvents = new HashMap();

		while( ulIterator.hasNext() )
		{
			eCurrentEvent = (Event)ulIterator.next();
			if( eCurrentEvent.getType() == iEventType )
			{
				if( cSubEventList == null )
					cSubEventList = new ArrayList();
				cSubEventList.add( eCurrentEvent );			//add this Event to the ArrayList
			}
		}

		if( cSubEventList == null )
			return null;

		hmEvents.put( "-1", cSubEventList );					//save ArrayList in hashmap
		//create the correct collection from the hash
		return createEventEntryListFromHash( -1, hmEvents, 0 );
	}



	/**
	 * This method creates the sorts the ArrayLists from the HashMap and creates the "tree"
	 *
	 * @param iParent current SuperEvent
	 * @param hmEvents HashMap: every entry is similar to a node in a tree: it has a value (identifer) and some childs (context)
	 * @param deep this is the deep of the recursion
	 * @return ArrayList <EventEntrys> for this SuperEvent
	 */
	private Collection createOwnEventEntryListFromCollection( Collection cEvents )
	{
		Event		eCurrentEvent;
		EventEntry	eeCurrentEntry;
		
		EventTypeManager	myEventTypeManager = null;
		
		try
		{
			myEventTypeManager = EventTypeManager.getInstance();
		}
		catch( IllegalArgumentException iae )
		{
			//so we dont add topics
		}

		Collection	cSubEventList;										//this is need for the hash and contains an ArrayList
		Collection	cAddCollection;
		Collection	cReturnCollection;
		Iterator	aIterator;											//this is the iterator for the arrayLists stored in the hashMap
		Iterator	cIterator;											//this is the iterator for the big Collection (of Collections)

		cReturnCollection = new ArrayList();							//this is for the return-list

		cIterator = cEvents.iterator();
		while( cIterator.hasNext() )
		{
			cSubEventList = (ArrayList) cIterator.next();


			if( cSubEventList == null )										//no events stored in this SuperEvent
				return null;
			Collections.sort( (List)cSubEventList );						//sort list for correct output

			aIterator = cSubEventList.iterator();
			
			if( aIterator.hasNext() )
			{
				eCurrentEvent = (Event) aIterator.next();
				//add Topic
				if( myEventTypeManager != null )
				{
					String sTopicName = myEventTypeManager.getEventName( eCurrentEvent.getType() );
					if( sTopicName == null ) sTopicName = "";
					cReturnCollection.add( new EventEntry( this.getUserLogin(), sTopicName, 0 ) );
				}
				//add Event
				cReturnCollection.add( new EventEntry( this.getUserLogin(), 1, eCurrentEvent ) );
				
			}
			while( aIterator.hasNext() )
			{
				eCurrentEvent = (Event) aIterator.next();					//get Event in this node
				//add this Event to the list (userlogin, deep, event)
				cReturnCollection.add( new EventEntry( this.getUserLogin(), 1, eCurrentEvent ) );
			}
		}
		return cReturnCollection;
	}
	
	/**
	 * This method creates a sortet ArrayList of EventEntrys so that they can easily used...
	 *
	 * @param iEventType this is the type of the Events... (-1 for all)
	 * @param cUnsortetList this is the ArrayList of the Events with no order or other things. Just all the raw events
	 * @return sortet 'ArrayList <EventEntrys>' of SubEvents for the Event with @see iParent
	 */
	public Collection createOwnEventTypeList( int iEventType, Collection cUnsortetList )
	{
		Event		eCurrentEvent;								//this is current iterated event
		Collection	cSubEventList = null;						//this is need for the hash

		HashMap		hmEvents;									//this stores collections of SubEventLists
		Iterator	ulIterator;									//this is the iterator for the unsortetList
		Iterator	hmIterator;									//this is the iterator for the hashMap

		//create hash in which all subevents where inserted
		ulIterator = cUnsortetList.iterator();
		hmEvents = new HashMap();

		while( ulIterator.hasNext() )
		{
			eCurrentEvent = (Event)ulIterator.next();
			if( (eCurrentEvent.getType() == iEventType) || (iEventType == -1) )
			{
				//check if u are subscribed or the owner of this event...
				//...
				if( eCurrentEvent.isSubscribed( this.getUserLogin() ) ||
					eCurrentEvent.getLecturer().equals( this.getUserLogin() ) )
				{
					cSubEventList = (ArrayList) hmEvents.get( ""+eCurrentEvent.getType() );	//get old subEventList
				
					if( cSubEventList == null )
						cSubEventList = new ArrayList();
					cSubEventList.add( eCurrentEvent );			//add this Event to the ArrayList

					hmEvents.put( ""+eCurrentEvent.getType(), cSubEventList );				//save ArrayList back in hashmap
				}
			}
		}

		if( cSubEventList == null )
			return null;

		//create the correct collection from collection form the hash (start with 0-element)
		return createOwnEventEntryListFromCollection( hmEvents.values() );
	}



	/**
	 * This method intialises all the values
	 *
	 * @param request this is need if some values from the session are needed
	 */
	synchronized public void initValues( HttpServletRequest request )
	{
		super.initValues( request );
		Collection cEventCollection;

		//init with standart....
		initValues( request, "-1", "-1" );
	}
	/**
	 * This method intialises all the values
	 *
	 * @param treeRoot 2do: this will be the treeRoot for the init...
	 * @param request this is need if some values from the session are needed
	 */
	synchronized public void initValues( HttpServletRequest request, String treeRoot, String whatEventType )
	{
		super.initValues( request );

		Collection		cEventCollection;
		EventManager	myEventManager;
		int				iEventType;

		//create new drop-down-list and check for old entrys
		{
			//store old List
			Collection		cOldSelectEventList = this.cSelectEventList;
			DropDownEntry	ddeOldEntry = null;

			if( this.cSelectEventList != null )
			{
				//get type which was choosen in old list...
				int iOldEventValue = -1;
				Iterator iOldSelectEventList = cOldSelectEventList.iterator();
				while( iOldSelectEventList.hasNext() && (iOldEventValue == -1) )
				{
					//get next entry in Collection
					DropDownEntry curDropDownEntry = (DropDownEntry) iOldSelectEventList.next();

					//check if this is the selected value and save value
					if( this.sSelectedEventValue.equals( ""+curDropDownEntry.getValue() ) )
					{
						iOldEventValue = curDropDownEntry.getValue();
						ddeOldEntry = curDropDownEntry;
					}
				}
			}

			//create new EventList
			this.cSelectEventList = createDropDownArrayList();	//create standard-list

			if( ddeOldEntry != null )
			{

				//get eventtype from old Entry
				iEventType = ( (Integer) ddeOldEntry.getObject() ).intValue();

				int iEventValue = -2;
				Iterator iNewSelectEventList = this.cSelectEventList.iterator();
				while( iNewSelectEventList.hasNext() && (iEventValue == -2) )
				{
					//get next entry in Collection
					DropDownEntry curDropDownEntry = (DropDownEntry) iNewSelectEventList.next();

					//check if this is the selected EventType
					if( iEventType == ((Integer) curDropDownEntry.getObject()).intValue() )
						iEventValue = curDropDownEntry.getValue();
				}

				if( iEventValue != -2 )
				{
					this.sSelectedEventValue = ""+iEventValue;
				}
				else
				{
					//old event-type was deleted so display all events
					iEventType = -1;
					this.sSelectedEventValue = "0";
				}
			}
			else
			{
				//HOWEVER THIS SHOULD NEVER EVER HAPPEN WE CAN JUST DISPLAY ALL EVENTS...
				//old event-type was deleted so display all events
				iEventType = -1;
				this.sSelectedEventValue = "0";
			}

		}



		//get events from EventManager...
		try
		{
			myEventManager = EventManager.getInstance();
			cEventCollection = myEventManager.getAllEvents();

			//check if there are some elements in this collection
			if( cEventCollection.size() == 0 )
				cEventCollection = null;
		}
		catch( Exception e )		//2do: this should only be a SingletonException (or IllegalArgumentException)
		{
			Log.addLog( "EventManager not initialized or with bad XML-File" );
			cEventCollection = null;
		}

		if( cEventCollection != null )
		{
			if( iEventType == -1 )
			{
				cEventList = createSortedEventEntryList( -1, cEventCollection );

				if( cEventList == null )
					this.hasEvents = false;
				else
					this.hasEvents = true;
			}
			else if( iEventType == -3 )
			{
				cEventList = createOwnEventTypeList( -1, cEventCollection );
				if( cEventList == null )
					this.hasEvents = false;
				else
					this.hasEvents = true;
			}
			else
			{
				cEventList = createSortedEventEntryTypeList( iEventType, cEventCollection );
				if( cEventList == null )
					this.hasEvents = false;
				else
					this.hasEvents = true;
			}
		}
		else
			this.hasEvents = false;

		//check if entry is in the drop-down-list
	}


	/**
	 * This method validates the input
	 *
	 * @param mapping this is the forward (see also your struts-config.xml)
	 * @param request
	 * @return Collection (?) of Errors which occured during validation...
	 */
 	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
 	{
 		return null;
	}



	//GETTER AND SETTER

	/**
	 * This method is called by the struts-tag-library
	 *
	 * @return Collection for the DropDownMenue
	 */
	public Collection getSelectEventList()
	{
		return this.cSelectEventList;
	}

	/**
	 * This method is called by the struts-tag-library
	 *
	 * @return unique ID for the DropDownMenu (for preselect)
	 */
	public String getSelectedEventValue()
	{
		return this.sSelectedEventValue;
	}
	/**
	 * This method is called by the struts-framework
	 *
	 * @param SelectEventType this is the unique ID of the DropDownMenu
	 */
	public void setSelectedEventValue( String SelectEventType )
	{
		this.sSelectedEventValue = SelectEventType;
	}

	/**
	 * This method is called by the struts-tag-library
	 *
	 * @return Collection of EventEntrys
	 */
	public Collection getEventList()
	{
		return this.cEventList;
	}

	public String getHasEvents()
	{
		return ""+this.hasEvents;
	}
}