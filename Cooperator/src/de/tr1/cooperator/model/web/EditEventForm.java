/*
File:		EditEventForm.java
Created:	05-06-07@11:00
Task:		FormBean for editEvent.jsp
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
import de.tr1.cooperator.model.mainframe.EventTime;
import de.tr1.cooperator.model.mainframe.User;
import de.tr1.cooperator.model.mainframe.RhythmType;
import de.tr1.cooperator.manager.mainframe.EventManager;
import de.tr1.cooperator.manager.mainframe.EventTypeManager;
import de.tr1.cooperator.manager.mainframe.UserManager;
import de.tr1.cooperator.model.web.EventEntry;
import de.tr1.cooperator.model.web.EventTimeEntry;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import java.util.*;
import java.util.regex.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 * FormBean for editEvent.jsp
 *
 * @author Peter Matjeschk
 * @version 05-06-07@11:30
 */
public class EditEventForm extends LookupDispatchAccessable
{
	private	final	String	DROPDOWNCHOOSEONE				= "Bitte auswählen";
	private	final	String	DROPDOWNNONE					= "(ohne)";

	private	final	String	ERRORMSG_NOEVENTMANAGER			= "EventManager nicht bereit";
	private	final	String	ERRORMSG_SUBMITEDEVENTIDINVALID	= "Ungültige EventID";

	private	final	String	ALLOWANCELISTSEPARATOR			= ", ";


	private	final	String	DAYNAME_MONDAY					= "Montag";
	private	final	String	DAYNAME_TUESDAY					= "Dienstag";
	private	final	String	DAYNAME_WEDNESDAY				= "Mittwoch";
	private	final	String	DAYNAME_THURSDAY				= "Donnerstag";
	private	final	String	DAYNAME_FRIDAY					= "Freitag";
	private	final	String	DAYNAME_SATURDAY				= "Samstag";
	private	final	String	DAYNAME_SUNDAY					= "Sonntag";

	private	final	String	TIMESTAMPFORMAT					= "dd.MM.yyyy HH:mm";
	private	final	String	TIMESTAMPFORMATNOCLOCKTIME		= "dd.MM.yyyy";

	private	boolean		bCreate;				//the topic
	private	Event		eEvent;					//the current edited Event...

	private	String		sHideTypeSelection;		//if Type was submitted we cannot change it (it contains only true or false)
	private	Collection	cEventTypeList;			//this stores all known event-types
	private	String		sSelectedEventType;		//currently (pre)selected entry

	private	Collection	cLecturerList;			//this stores all known lecturer names
	private String		sSelectedLecturer;		//currently (pre)selected entry

	private	String		sTimeStampFormat = TIMESTAMPFORMAT;

	private	String		sStart;					//start of this event

	private	String		sSubscriptionStart;		//contains timestamp
	private	String		sSubscriptionEnd;		//..
	private	String		sUnsubscriptionEnd;		//..

	private	String		sMaxSubscriptions;

	private	boolean		bHasEventTimes;
	private Collection	cEventTimeEntryList;	//this stores time-entrys for this event...

	private String		sNewTimeSelectedDayName;//this one is the selected day-name
	private Collection	cNewTimeDayNameList;	//Drop-Down for DayName..
	private String		sNewTimeClockTime;		//string for ClockTime
	private	Collection	cNewTimeRhythmList;		//Drop-Down for Rhythms..
	private	String		sNewTimeSelectedRhythm;	//selected Type
	private	String		sNewTimeLocation;		//string for Location

	private	String		sAllowanceList;			//this stores all the personal-numbers whiche are allowed to subscribe
	private	Collection	cAllowanceList;			//this stores the person-numbers as collection (is initiated during validation-process)

	public ArrayList createDropDownEventTypeArrayList()
	{
		EventTypeManager	myEventTypeManager;
		ArrayList			alReturn;
		int					iEntryCounter = 0;

		Collection			alAllEventTypes;
		Iterator			iAllEventTypes;

		alReturn = new ArrayList();
		alReturn.add( new DropDownEntry( iEntryCounter++, DROPDOWNCHOOSEONE, new Integer( -1 ) ) );

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
			Collections.sort( (List)alAllEventTypes );						//sort list for correct output

			iAllEventTypes = alAllEventTypes.iterator();
			while( iAllEventTypes.hasNext() )
			{
				EventType curEventType = (EventType) iAllEventTypes.next();
				alReturn.add( new DropDownEntry( iEntryCounter++, curEventType.getName(), new Integer( curEventType.getID() ) ) );
			}
		}

		return alReturn;
	}

	public ArrayList createDropDownLecturerNameArrayList()
	{
		UserManager			myUserManager;
		ArrayList			alReturn;
		int					iEntryCounter = 0;

		Collection			alAllUsers;
		Iterator			iAllUsers;

		alReturn = new ArrayList();
		alReturn.add( new DropDownEntry( iEntryCounter++, DROPDOWNCHOOSEONE, new String( "" ) ) );
		alReturn.add( new DropDownEntry( iEntryCounter++, DROPDOWNNONE, new String( "-" ) ) );

		//get users from UserManager...
		try
		{
			myUserManager = UserManager.getInstance();
		}
		catch( Exception e )		//2do: this should only be a SingletonException (or IllegalArgumentException)
		{
			Log.addLog( "UserManager not initialized or with bad XML-File" );
			return alReturn;
		}

		//get all Users from Manager
		alAllUsers = myUserManager.getAllUsers();

		//check wether or not it contains some elements..
		if( (alAllUsers != null) && (alAllUsers.size() == 0) )
			alAllUsers = null;

		//... and put it into the Drop-Down-Menü
		if( alAllUsers != null )
		{
			Collections.sort( (List)alAllUsers );						//sort list for correct output

			iAllUsers = alAllUsers.iterator();
			while( iAllUsers.hasNext() )
			{
				User curUser = (User) iAllUsers.next();

				//check if the user has lecturer-rights
				if( ( ( curUser.getRights() & (User.LECTURER | User.ADMIN) ) > 0 ) )
					alReturn.add( new DropDownEntry( iEntryCounter++, curUser.getSurname() + ", " + curUser.getFirstName(), new String( curUser.getLogin() ) ) );
			}
		}

		return alReturn;
	}

	public ArrayList createDropDownNewTimeDayNameArrayList()
	{
		ArrayList	alReturn;

		alReturn = new ArrayList();
		alReturn.add( new DropDownEntry( 0, DAYNAME_MONDAY, new Integer( 0 ) ) );
		alReturn.add( new DropDownEntry( 1, DAYNAME_TUESDAY, new Integer( 1 ) ) );
		alReturn.add( new DropDownEntry( 2, DAYNAME_WEDNESDAY, new Integer( 2 ) ) );
		alReturn.add( new DropDownEntry( 3, DAYNAME_THURSDAY, new Integer( 3 ) ) );
		alReturn.add( new DropDownEntry( 4, DAYNAME_FRIDAY, new Integer( 4 ) ) );
		alReturn.add( new DropDownEntry( 5, DAYNAME_SATURDAY, new Integer( 5 ) ) );
		alReturn.add( new DropDownEntry( 6, DAYNAME_SUNDAY, new Integer( 6 ) ) );

		return alReturn;
	}

	public ArrayList createDropDownNewTimeRhythmArrayList()
	{
		ArrayList	alReturn;

		alReturn = new ArrayList();

		Collection cRhythmCollection = EventTime.getAllRhythmTypes();
		Iterator iRC = cRhythmCollection.iterator();
		while( iRC.hasNext() )
		{
			RhythmType curRhythmType = (RhythmType) iRC.next();
			alReturn.add( new DropDownEntry( curRhythmType.getID(),  curRhythmType.getName(), null ) );
		}

		return alReturn;
	}



	synchronized public void initValues( HttpServletRequest request )
	{
		super.initValues( request );
		
		Event		eOldEvent;
		boolean		bClearOldEntrys;
		String		sActionPath;
		String		sEventID;
		int			iEventID;
		int			iNewEventGroupID = -1;
		int			iNewEventParentID = -1;
		int			iNewEventTypeID = -1;

		HttpSession	curSession;

		//ok, we're here, so reset the needed values
		eOldEvent = null;
		bClearOldEntrys = false;
		curSession = request.getSession();

		if( curSession == null )
		{
			//if this happens we dont have to initialize anything, because
			//the session was killed so the user is no longer logged in
			return;
		}
		
		//try to get actionpath from parameter or if there's no parameter set
		//from the session-data
		sEventID = request.getParameter( "eventID" );
		if( sEventID == null )
		{
			if( "true".equals( request.getParameter( "create" ) ) ||
				"true".equals((String) curSession.getAttribute( "create" ) ) )
			{
				curSession.removeAttribute( "create" );
				sActionPath = "create";
				bClearOldEntrys = true;
			}
			else
			{
				//if no param or attribute has passed it's possible that we only refreshed this page
				//so check this and if there is no old action-path do a create-event
				if( curSession.getAttribute( "oldEditEventActionPath" ) != null )
					sActionPath = (String) curSession.getAttribute( "oldEditEventActionPath" );
				else
				{
					sActionPath = "create";
					bClearOldEntrys = true;
				}
			}
		}
		else
		{
			sActionPath = "edit";
			bClearOldEntrys = true;
		}

		//set actionpath if this reset is reentered...
		curSession.setAttribute( "oldEditEventActionPath", sActionPath );




		//remove all of the old event-entrys
		if( bClearOldEntrys )
		{
			//clear all sessionentrys...
			curSession.removeAttribute( "oldEditEvent" );
			curSession.removeAttribute( "oldEditEventSelectedEventType" );
			curSession.removeAttribute( "oldEditEventSelectedLecturer" );
			curSession.removeAttribute( "oldEditEventStart" );
			curSession.removeAttribute( "oldEditEventSubscriptionStart" );
			curSession.removeAttribute( "oldEditEventSubscriptionEnd" );
			curSession.removeAttribute( "oldEditEventUnsubscriptionEnd" );
			curSession.removeAttribute( "oldEditEventMaxSubscriptions" );
			curSession.removeAttribute( "oldEditEventHideSelectType" );
			curSession.removeAttribute( "oldEditEventAllowanceList" );
		}

		//get old event if there is one
		eOldEvent = (Event) curSession.getAttribute( "oldEditEvent" );
		curSession.removeAttribute( "oldEditEvent" );

		//ok, set default hideTypeSelection to false;
		this.sHideTypeSelection = "false";
		
		//ok, now get the correct event or create one
		if( sActionPath.equals( "create" ) )
		{
			if( eOldEvent == null )
			{
				//create an empty event
				if ( ! (this.hasAdminRights() || this.hasLecturerRights()) )
				{
					this.sErrorMessage += "Sie haben nicht genug Rechte eine Veranstaltung zu erstellen.";
					return;
				}
				String paramString;

				paramString = request.getParameter( "group" );
				if( paramString != null )
					try
					{
						iNewEventGroupID = Integer.parseInt( paramString );
					}
					catch( NumberFormatException nfe)
					{
					}

				paramString = request.getParameter( "parent" );
				if( paramString != null )
					try
					{
						iNewEventParentID = Integer.parseInt( paramString );
					}
					catch( NumberFormatException nfe)
					{
					}

				paramString = request.getParameter( "type" );
				if( paramString != null )
					try
					{
						iNewEventTypeID = Integer.parseInt( paramString );
						if( iNewEventTypeID > -1 )
							this.sHideTypeSelection = "true";
					}
					catch( NumberFormatException nfe)
					{
					}

				//if this user is admin dont preselect him has owner
				if( this.hasAdminRights() )
					eEvent = new Event(  "", iNewEventTypeID, -1, iNewEventParentID, iNewEventGroupID, "", "", -1, new ArrayList(), -1, -1, -1, new ArrayList(), -1 );
				else
					eEvent = new Event(  "", iNewEventTypeID, -1, iNewEventParentID, iNewEventGroupID, this.getUserLogin(), "", -1, new ArrayList(), -1, -1, -1, new ArrayList(), -1 );
			}
			else
			{
				eEvent = eOldEvent;
			}
			
			this.bCreate = true;
		}
		else
		{
			if( eOldEvent == null )
			{
				//parse eventID to int
				try
				{
					iEventID	= Integer.parseInt	( sEventID );
				}
				catch (Exception e)
				{
					this.sErrorMessage += ERRORMSG_SUBMITEDEVENTIDINVALID;
					return;
				}

				//try to get event from eventmanager
				EventManager myEventManager = EventManager.getInstance();
				if( myEventManager == null )
				{
					this.sErrorMessage += ERRORMSG_NOEVENTMANAGER;
					return;
				}
				this.eEvent = myEventManager.getEventByID( iEventID );
				if( this.eEvent == null )
				{
					this.sErrorMessage += ERRORMSG_SUBMITEDEVENTIDINVALID;
					return;
				}

				//if group != -1 it contains to some sort of group, so hide TypeSelection..
				//if countEventsInGroup == 1, we can change the type
				if( myEventManager.countEventsInGroup( this.eEvent.getGroup() ) > 1 )
					this.sHideTypeSelection = "true";

			}
			else
			{
				eEvent = eOldEvent;
				
				if ( !(this.hasAdminRights() || this.hUser.getLogin().equals( eEvent.getLecturer() ) ) )
				{
					this.sErrorMessage += "Sie haben nicht genug Rechte diese Veranstaltung zu bearbeiten.";
					return;
				}
			}
			this.bCreate = false;
		}
		

		//create collection for dayname etc...
		this.cNewTimeDayNameList = this.createDropDownNewTimeDayNameArrayList();


		//create collection for rhythm etc...
		this.cNewTimeRhythmList = this.createDropDownNewTimeRhythmArrayList();


		//drop-down-entry-collection for event-type
		this.cEventTypeList = this.createDropDownEventTypeArrayList();

		//drop-down-entry-collection for lecturers...
		this.cLecturerList = this.createDropDownLecturerNameArrayList();

		//preselect correct value in event-type
		this.sSelectedEventType = "0";
		Iterator iETL = this.cEventTypeList.iterator();
		while( iETL.hasNext() )
		{
			DropDownEntry curEntry = (DropDownEntry) iETL.next();

			if( ((Integer)curEntry.getObject()).intValue() == this.eEvent.getType() )
			{
				this.sSelectedEventType = ""+curEntry.getValue();
				break;
			}
		}
		
		//preselect correct value for lecturer-list
		this.sSelectedLecturer = "0";
		Iterator iLL = this.cLecturerList.iterator();
		while( iLL.hasNext() )
		{
			DropDownEntry curEntry = (DropDownEntry) iLL.next();
			if( ((String)curEntry.getObject()).equals( this.eEvent.getLecturer() ) )
			{
				this.sSelectedLecturer = ""+curEntry.getValue();
				break;
			}
		}



		//create correct strings for Time-Input-Fields
		SimpleDateFormat myDateFormat = new SimpleDateFormat( TIMESTAMPFORMAT );

		long timeStamp = this.eEvent.getStart();
		if( timeStamp == -1 )
			this.sStart = "";
		else
			this.sStart = myDateFormat.format( new java.util.Date( timeStamp ) );

		timeStamp = this.eEvent.getSubscriptionStart();
		if( timeStamp == -1 )
			this.sSubscriptionStart = "";
		else
			this.sSubscriptionStart = myDateFormat.format( new java.util.Date( timeStamp ) );

		timeStamp = this.eEvent.getSubscriptionEnd();
		if( timeStamp == -1 )
			this.sSubscriptionEnd = "";
		else
			this.sSubscriptionEnd = myDateFormat.format( new java.util.Date( timeStamp ) );

		timeStamp = this.eEvent.getUnsubscriptionEnd();
		if( timeStamp == -1 )
			this.sUnsubscriptionEnd = "";
		else
			this.sUnsubscriptionEnd = myDateFormat.format( new java.util.Date( timeStamp ) );


		if( this.eEvent.getMaxSubscriptions() == -1 )
			this.sMaxSubscriptions = "";
		else
			this.sMaxSubscriptions = ""+this.eEvent.getMaxSubscriptions();



		//reset the addTime-line
		this.sNewTimeSelectedDayName = "0";
		this.sNewTimeClockTime = "";
		this.sNewTimeSelectedRhythm = "1";
		this.sNewTimeLocation = "";

		//get standart event-allowance-list from manager as String...
		this.sAllowanceList = getAllowanceListFromManagerAsString( this.eEvent );

		//give the old entrys if this reset-method is called because of addEventTime
		//then the attributes will exists and can be restored
		String	attributeString;

		attributeString = (String) curSession.getAttribute( "oldEditEventSelectedEventType" );
		if( attributeString != null )
			this.sSelectedEventType = attributeString;
		curSession.removeAttribute( "oldEditEventSelectedEventType" );

		attributeString = (String) curSession.getAttribute( "oldEditEventSelectedLecturer" );
		if( attributeString != null )
			this.sSelectedLecturer = attributeString;
		curSession.removeAttribute( "oldEditEventSelectedLecturer" );

		attributeString = (String) curSession.getAttribute( "oldEditEventStart" );
		if( attributeString != null )
			this.sStart = attributeString;
		curSession.removeAttribute( "oldEditEventStart" );

		attributeString = (String) curSession.getAttribute( "oldEditEventSubscriptionStart" );
		if( attributeString != null )
			this.sSubscriptionStart = attributeString;
		curSession.removeAttribute( "oldEditEventSubscriptionStart" );

		attributeString = (String) curSession.getAttribute( "oldEditEventSubscriptionEnd" );
		if( attributeString != null )
			this.sSubscriptionEnd = attributeString;
		curSession.removeAttribute( "oldEditEventSubscriptionEnd" );

		attributeString = (String) curSession.getAttribute( "oldEditEventUnsubscriptionEnd" );
		if( attributeString != null )
			this.sUnsubscriptionEnd = attributeString;
		curSession.removeAttribute( "oldEditEventUnsubscriptionEnd" );

		attributeString = (String) curSession.getAttribute( "oldEditEventMaxSubscriptions" );
		if( attributeString != null )
			this.sMaxSubscriptions = attributeString;
		curSession.removeAttribute( "oldEditEventMaxSubscriptions" );

		attributeString = (String) curSession.getAttribute( "oldEditEventHideSelectType" );
		if( attributeString != null )
			this.sHideTypeSelection = attributeString;
		curSession.removeAttribute( "oldEditEventHideSelectType" );

		attributeString = (String) curSession.getAttribute( "oldEditEventAllowanceList" );
		if( attributeString != null )
			this.sAllowanceList = attributeString;
		curSession.removeAttribute( "oldEditEventAllowanceList" );


		//and create all of the other collections and update old values (and reset them like add etc.)
		updateTimeCollection();

		//dont ask why - just a very very dirty hack
		//because struts is sooooo stupid!
		//if u click on a submit button it will FIRST reset all of the things (allthough the reset-method was called already)
		//then call all the setters and getters
		//and after that it will validate.... stupid stupid stupid....
		curSession.setAttribute( "oldEditEvent", 					this.getEvent() );
		curSession.setAttribute( "oldEditEventSelectedEventType",	this.getSelectedEventType() );
		curSession.setAttribute( "oldEditEventSelectedLecturer",	this.getSelectedLecturer() );
		curSession.setAttribute( "oldEditEventStart",				this.getEventStart() );
		curSession.setAttribute( "oldEditEventSubscriptionStart",	this.getEventSubscriptionStart() );
		curSession.setAttribute( "oldEditEventSubscriptionEnd",		this.getEventSubscriptionEnd() );
		curSession.setAttribute( "oldEditEventUnsubscriptionEnd",	this.getEventUnsubscriptionEnd() );
		curSession.setAttribute( "oldEditEventMaxSubscriptions",	this.getEventMaxSubscriptions() );
		curSession.setAttribute( "oldEditEventHideSelectType",		this.getHideTypeSelection() );
		curSession.setAttribute( "oldEditEventAllowanceList",		this.getEventAllowanceList() );
	}

	public void updateTimeCollection()
	{
		//create collection for EventTimes..
		if( (this.eEvent.getTimes() == null) ||
			(this.eEvent.getTimes().size() == 0) )
		{
			this.cEventTimeEntryList = null;
			this.bHasEventTimes = false;
		}
		else
		{
			this.cEventTimeEntryList = new ArrayList();
			int iEventTimeEntryID = 0;

			Iterator iET = this.eEvent.getTimes().iterator();
			while( iET.hasNext() )
			{
				EventTime curEventTime = (EventTime) iET.next();

				this.cEventTimeEntryList.add( new EventTimeEntry( iEventTimeEntryID++,  curEventTime ) );
			}
			this.bHasEventTimes = true;
		}

	}

	public String getAllowanceListFromManagerAsString( Event eEvent )
	{
		String		sReturn = "";
		Collection	cAllowanceList;
		//get instance of eventmanager
		EventManager myEventManager;
		try
		{
			myEventManager = EventManager.getInstance();
		}
		catch( IllegalArgumentException iae )
		{
			this.sErrorMessage += ERRORMSG_NOEVENTMANAGER;
			return "";
		}

		//get Collection from Manager and parse it to string
		cAllowanceList = myEventManager.getAllowanceList( eEvent.getID() );
		Iterator iAL = cAllowanceList.iterator();
		while( iAL.hasNext() )
		{
			String	thisNumber = (String) iAL.next();
			if( !sReturn.equals( "" ) )
				sReturn += ALLOWANCELISTSEPARATOR;
			sReturn += thisNumber;
		}

		return sReturn;
	}



	// GETTER AND SETTER
	public String getCreate()
	{
		return ""+this.bCreate;
	}

	public String getEventName()
	{
		return this.eEvent.getName();
	}
	public void setEventName( String sName )
	{
		this.eEvent.setName( sName );
	}

	public String getSelectedEventType()
	{
		return this.sSelectedEventType;
	}
	public void setSelectedEventType( String SelectEventType )
	{
		this.sSelectedEventType = SelectEventType;
	}
	public Collection getEventTypeList()
	{
		return this.cEventTypeList;
	}

	public String getSelectedLecturer()
	{
		return this.sSelectedLecturer;
	}
	public void setSelectedLecturer( String SelectLecturer )
	{
		this.sSelectedLecturer = SelectLecturer;
	}
	public Collection getLecturerList()
	{
		return this.cLecturerList;
	}

	public String getEventLecturer()
	{
		UserManager myUserManager = UserManager.getInstance();
		if( myUserManager == null )
			return "";
		User myUser = myUserManager.getUser( this.eEvent.getLecturer() );
		if( myUser == null )
			return "";
		return myUser.getSurname() + ", " + myUser.getFirstName();
	}

	public String getTimeStampFormat()
	{
		return this.sTimeStampFormat;
	}

	public String getEventStart()
	{
		return this.sStart;
	}
	public void setEventStart( String sStart )
	{
		this.sStart = sStart;
	}
	public String getEventSubscriptionStart()
	{
		return this.sSubscriptionStart;
	}
	public void setEventSubscriptionStart( String sSubscriptionStart )
	{
		this.sSubscriptionStart = sSubscriptionStart;
	}
	public String getEventSubscriptionEnd()
	{
		return this.sSubscriptionEnd;
	}
	public void setEventSubscriptionEnd( String sSubscriptionEnd )
	{
		this.sSubscriptionEnd = sSubscriptionEnd;
	}
	public String getEventUnsubscriptionEnd()
	{
		return this.sUnsubscriptionEnd;
	}
	public void setEventUnsubscriptionEnd( String sUnsubscriptionEnd )
	{
		this.sUnsubscriptionEnd = sUnsubscriptionEnd;
	}

	//getter for time-collection (includes rhythm, location, etc)
	//getter and setter for add-time
	public String getHasEventTimes()
	{
		return ""+this.bHasEventTimes;
	}
	public Collection getEventTimeEntryList()
	{
		return this.cEventTimeEntryList;
	}

	//getter and setter for addTime
	public String getNewTimeSelectedDayName()
	{
		return this.sNewTimeSelectedDayName;
	}
	public void setNewTimeSelectedDayName( String sNewTimeSelectedDayName )
	{
		this.sNewTimeSelectedDayName = sNewTimeSelectedDayName;
	}
	public Collection getNewTimeDayNameList()
	{
		return this.cNewTimeDayNameList;
	}
	public String getNewTimeClockTime()
	{
		return this.sNewTimeClockTime;
	}
	public void setNewTimeClockTime( String sNewTimeClockTime )
	{
		this.sNewTimeClockTime = sNewTimeClockTime;
	}
	public Collection getNewTimeRhythmList()
	{
		return this.cNewTimeRhythmList;
	}
	public String getNewTimeSelectedRhythm()
	{
		return this.sNewTimeSelectedRhythm;
	}
	public void setNewTimeSelectedRhythm( String sNewTimeSelectedRhythm )
	{
		this.sNewTimeSelectedRhythm = sNewTimeSelectedRhythm;
	}

	public String getNewTimeLocation()
	{
		return this.sNewTimeLocation;
	}
	public void setNewTimeLocation( String sNewTimeLocation )
	{
		this.sNewTimeLocation = sNewTimeLocation;
	}


	public String getEventMaxSubscriptions()
	{
		return this.sMaxSubscriptions;
	}
	public void setEventMaxSubscriptions( String sMaxSubscriptions )
	{
		this.sMaxSubscriptions = sMaxSubscriptions;
	}

	public String getEventInfoText()
	{
		return this.eEvent.getInfoText();
	}
	public void setEventInfoText( String sInfoText )
	{
		this.eEvent.setInfoText( sInfoText );
	}


	//getter and setter for the action
	public Event getEvent()
	{
		return this.eEvent;
	}
	public String getNewTimeDayName()
	{
		String	returnString = "";
		Iterator iDNC = this.cNewTimeDayNameList.iterator();
		while( iDNC.hasNext() )
		{
			DropDownEntry curWeekDay = (DropDownEntry) iDNC.next();
			if( this.sNewTimeSelectedDayName.equals( ""+curWeekDay.getValue() ) )
			{
				returnString = curWeekDay.getLabel();
				break;
			}
		}

		return returnString;
	}
	public int getNewTimeRhythm()
	{
		int returnInt = -1;
		Iterator iRC = this.cNewTimeRhythmList.iterator();
		while( iRC.hasNext() )
		{
			DropDownEntry curRhythm = (DropDownEntry) iRC.next();
			if( this.sNewTimeSelectedRhythm.equals( ""+curRhythm.getValue() ) )
			{
				returnInt = curRhythm.getValue();
				break;
			}
		}

		return returnInt;
	}

	public String getHideTypeSelection()
	{
		return this.sHideTypeSelection;
	}

	public String getEventType()
	{
		EventTypeManager myETManager;
		try
		{
			myETManager = EventTypeManager.getInstance();
		}
		catch( IllegalArgumentException ie )
		{
			return "";
		}


		return myETManager.getEventName( this.eEvent.getType() );
	}

	public String getEventAllowanceList()
	{
		return this.sAllowanceList;
	}

	public void setEventAllowanceList( String sAllowanceList )
	{
		this.sAllowanceList = sAllowanceList;
	}

	public String[] getAllowanceListSplitted()
	{
		return this.sAllowanceList.split(this.ALLOWANCELISTSEPARATOR);
	}



    /**
	* Maps the given request to the correct sub-method in this class
    * @return mapping
    */
    protected Map getKeyMethodMap()
    {
		Map map = new HashMap();

		//name of parameter, name of method to run
		map.put( "editEvent.buttonAddTimeEntry",	"validateAddTime" );
		map.put( "editEvent.buttonCreateEvent",		"validateEditEvent" );
		map.put( "editEvent.buttonEditEvent",		"validateEditEvent" );
		return map;
	}

	public ActionErrors validateAddTime(ActionMapping mapping, HttpServletRequest request)
 	{
		ActionErrors errors = new ActionErrors();


//	== edit by Sebastian Kohl:
//			- EventTimes should be strings... for example: "unknown yet" or "13:00-20:00"
//		Pattern pat		= Pattern.compile("[0-9]{2,2}:[0-9]{2,2}");
//		Matcher matcher	= pat.matcher( this.sNewTimeClockTime );
//		boolean bValid	= matcher.matches();

//		if ( !bValid )
//			errors.add( "editEvent.addTime.ClockTimeInvalid", new ActionMessage( "editEvent.invalidNewTimeClockTime" ) );
		//2do: check for invalid clocktime like 27:69	<-- not nessessary any longer
		if( this.sNewTimeClockTime.equals( "" ) ||
			( this.sNewTimeClockTime.length() < 0 ) )
			errors.add( "editEvent.addTime.ClockTimeInvalid", new ActionMessage( "editEvent.invalidNewTimeClockTime" ) );

		if( this.sNewTimeLocation.equals( "" ) ||
			( this.sNewTimeLocation.length() < 0 ) )
			errors.add( "editEvent.addTime.LocationInvalid", new ActionMessage( "editEvent.invalidNewTimeLocation" ) );

		return errors;
	}

	public ActionErrors validateEditEvent(ActionMapping mapping, HttpServletRequest request)
 	{
		ActionErrors		errors				= new ActionErrors();

		SimpleDateFormat	mySDF				= new SimpleDateFormat( TIMESTAMPFORMAT );
		SimpleDateFormat	mySDFNOCLOCKTIME	= new SimpleDateFormat( TIMESTAMPFORMATNOCLOCKTIME );
		Date				parsedDate			= null;

		//first: check if EventName has some minimum length and contains only A-Za-z0-9 and .-"' or so
		Pattern pat		= Pattern.compile("[a-zA-Z0-9 _/=$!,;~#%<>ÄÜÖäüöß'|\"\\(\\)\\{\\}\\-\\[\\]\\.\\+\\*]{3,100}");
		
		//remove leading and ending spaces...
		this.eEvent.setName( this.eEvent.getName().trim() );
		
		Matcher matcher	= pat.matcher( this.eEvent.getName() );
		if( !matcher.matches() )
			errors.add( "editEvent.EventNameInvalid", new ActionMessage( "editEvent.EventNameInvalid" ) );


		//second parse the correct lecturere-login if the user has admin-rights
		if( this.hasAdminRights() )
		{
			//check if the value submitted is not 0
			if( this.sSelectedLecturer.equals( "0" ) )
				errors.add( "editEvent.LecturereNotChoosen", new ActionMessage( "editEvent.LecturerNotChoosen" ) );
			else
			{
				//get correct login of selected lecturer
				String	lecturerLogin = "";
				Iterator iLL = this.cLecturerList.iterator();
				while( iLL.hasNext() )
				{
					DropDownEntry curDropDownEntry = (DropDownEntry) iLL.next();
					if( this.sSelectedLecturer.equals( ""+ curDropDownEntry.getValue() ) )
					{
						lecturerLogin = (String) curDropDownEntry.getObject();
						if( lecturerLogin.equals( "" ) )
						{
							//this should never ever happen, so add to Log
							Log.addLog( "EditEventForm: bad entry for user-login found! (" + curDropDownEntry.getLabel() + ")" );
							lecturerLogin = "-";	//to save the data-integrity
						}
						break;
					}
				}

				this.eEvent.setLecturer( lecturerLogin );
			}
		}

		//parse the correct type
		//check if the value submitted is not 0
		if( this.sSelectedEventType.equals( "0" ) )
			errors.add( "editEvent.EventTypeNotChoosen", new ActionMessage( "editEvent.EventTypeNotChoosen" ) );
		else
		{
			//get correct typeID of selected eventtype
			int	iEventType = -1;
			Iterator iLL = this.cEventTypeList.iterator();
			while( iLL.hasNext() )
			{
				DropDownEntry curDropDownEntry = (DropDownEntry) iLL.next();
				if( this.sSelectedEventType.equals( ""+ curDropDownEntry.getValue() ) )
				{
					iEventType = ((Integer) curDropDownEntry.getObject()).intValue();
					break;
				}
			}
			this.eEvent.setType( iEventType );
		}


		//parse eventStart
		if( !this.sStart.equals( "" ) )
		{
			try
			{
				//try to parse this timecode with date and time
				parsedDate = mySDF.parse( this.sStart );
			}
			catch( ParseException pe )
			{
				//hm, maybe there is no given clock-time...
				try
				{
					parsedDate = mySDFNOCLOCKTIME.parse( this.sStart );

					//2do: add Clock Time and Parse again
				}
				catch( ParseException pe2 )
				{
					errors.add( "editEvent.EventStartBadFormat", new ActionMessage( "editEvent.EventBadTimeFormat" ) );
				}
			}
			if( parsedDate != null )
			{
				this.eEvent.setStart( parsedDate.getTime() );
			}
		}
		else
		{
			this.eEvent.setStart( new Date().getTime() );
		}
		parsedDate = null;

		//parse eventSubscriptionStart
		if( !this.sSubscriptionStart.equals( "" ) )
		{
			try
			{
				//try to parse this timecode with date and time
				parsedDate = mySDF.parse( this.sSubscriptionStart );
			}
			catch( ParseException pe )
			{
				//hm, maybe there is no given clock-time...
				try
				{
					parsedDate = mySDFNOCLOCKTIME.parse( this.sSubscriptionStart );

					//2do: add Clock Time and Parse again
				}
				catch( ParseException pe2 )
				{
					errors.add( "editEvent.EventSubscriptionStartBadFormat", new ActionMessage( "editEvent.EventBadTimeFormat" ) );
				}
			}
			if( parsedDate != null )
			{
				this.eEvent.setSubscriptionStart( parsedDate.getTime() );
			}
		}
		else
		{
				this.eEvent.setSubscriptionStart( -1 );
		}
		parsedDate = null;

		//parse eventSubscriptionEnd
		if( !this.sSubscriptionEnd.equals( "" ) )
		{
			try
			{
				//try to parse this timecode with date and time
				parsedDate = mySDF.parse( this.sSubscriptionEnd );
			}
			catch( ParseException pe )
			{
				//hm, maybe there is no given clock-time...
				try
				{
					parsedDate = mySDFNOCLOCKTIME.parse( this.sSubscriptionEnd );

					//2do: add Clock Time and Parse again
				}
				catch( ParseException pe2 )
				{
					errors.add( "editEvent.EventSubscriptionEndBadFormat", new ActionMessage( "editEvent.EventBadTimeFormat" ) );
				}
			}
			if( parsedDate != null )
			{
				this.eEvent.setSubscriptionEnd( parsedDate.getTime() );
			}
		}
		else
		{
			this.eEvent.setSubscriptionEnd( -1 );
		}
		parsedDate = null;

		//parse eventUnsubscriptionEnd
		if( !this.sUnsubscriptionEnd.equals( "" ) )
		{
			try
			{
				//try to parse this timecode with date and time
				parsedDate = mySDF.parse( this.sUnsubscriptionEnd );
			}
			catch( ParseException pe )
			{
				//hm, maybe there is no given clock-time...
				try
				{
					parsedDate = mySDFNOCLOCKTIME.parse( this.sUnsubscriptionEnd );

					//2do: add Clock Time and Parse again
				}
				catch( ParseException pe2 )
				{
					errors.add( "editEvent.EventUnsubscriptionEndBadFormat", new ActionMessage( "editEvent.EventBadTimeFormat" ) );
				}
			}
			if( parsedDate != null )
			{
				this.eEvent.setUnsubscriptionEnd( parsedDate.getTime() );
			}
		}
		else
		{
			this.eEvent.setUnsubscriptionEnd( -1 );
		}
		parsedDate = null;

		//parse maxSubscriptions...
		if( this.sMaxSubscriptions.equals( "" ) )
		{
			//set to -1 --> so no registration needed...
			this.eEvent.setMaxSubscriptions( -1 );
		}
		else
		{
			try
			{
				int	newMaxSubscriptions = Integer.parseInt( this.sMaxSubscriptions );

				//no negativ values are allowed ;-)
				if( newMaxSubscriptions < 0 )
					errors.add( "editEvent.EventMaxSubscriptionsBadFormat", new ActionMessage( "editEvent.EventBadMaxSubscriptionsFormat" ) );

				this.eEvent.setMaxSubscriptions( newMaxSubscriptions );
			}
			catch( NumberFormatException nfe )
			{
				errors.add( "editEvent.EventMaxSubscriptionsBadFormat", new ActionMessage( "editEvent.EventBadMaxSubscriptionsFormat" ) );
			}
		}

		//check if allowanceList is empty->return
		if( !this.sAllowanceList.equals( "" ) )
		{
			//parse allowanceList
			String[] result = this.sAllowanceList.split(this.ALLOWANCELISTSEPARATOR);

			Pattern numpat	= Pattern.compile("[a-zA-Z0-9_\\-\\(\\)\\.#\\\\/\\ ]+");
			for (int x=0; x<result.length; x++)
			{
				Matcher nummatcher	= numpat.matcher(result[x]);
				if ( !nummatcher.matches() )
				{
					errors.add( "editEvent.NumberList", new ActionMessage("editEvent.BadEntryList") );
					break;
				}
			}
		}

		return errors;
	}


	/**
	 * This method writes all of the entrys to the session after they are validated...
	 * Only a hack to prevent bugs :)
	 */
	public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
	{
		HttpSession curSession = request.getSession();
		if( curSession == null )
			return new ActionErrors();

		ActionErrors rErrors = super.validate( mapping, request ); 	
			
		curSession.setAttribute( "oldEditEvent", 					this.getEvent() );
		curSession.setAttribute( "oldEditEventSelectedEventType",	this.getSelectedEventType() );
		curSession.setAttribute( "oldEditEventSelectedLecturer",	this.getSelectedLecturer() );
		curSession.setAttribute( "oldEditEventStart",				this.getEventStart() );
		curSession.setAttribute( "oldEditEventSubscriptionStart",	this.getEventSubscriptionStart() );
		curSession.setAttribute( "oldEditEventSubscriptionEnd",		this.getEventSubscriptionEnd() );
		curSession.setAttribute( "oldEditEventUnsubscriptionEnd",	this.getEventUnsubscriptionEnd() );
		curSession.setAttribute( "oldEditEventMaxSubscriptions",	this.getEventMaxSubscriptions() );
		curSession.setAttribute( "oldEditEventHideSelectType",		this.getHideTypeSelection() );
		curSession.setAttribute( "oldEditEventAllowanceList",		this.getEventAllowanceList() );

		return rErrors;
	}	
}