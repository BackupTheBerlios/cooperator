/*
File:		EditEventAction.java
Created:	05-06-07@11:00
Task:		Action for editEvent.jsp
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
package de.tr1.cooperator.manager.web;

import de.tr1.cooperator.manager.mainframe.EventManager;
import de.tr1.cooperator.manager.system.Log;
import de.tr1.cooperator.model.mainframe.Event;
import de.tr1.cooperator.model.mainframe.EventTime;
import de.tr1.cooperator.model.web.EditEventForm;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.actions.LookupDispatchAction;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import java.io.*;

import java.util.*;


/**
 * This class handles the actions called by the struts-framework
 *
 * @author Peter Matjeschk
 * @version 05-05-30@21:00
 */
public class EditEventAction extends LookupDispatchAction
{
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception, IOException
	{
		String parameter = mapping.getParameter();
        if( parameter != null )
        {
			String keyName = request.getParameter( parameter );
			if( "delete".equals( keyName ) )
			{
				return delTime( mapping, form, request, response );
        	}
        	else
        		return super.execute( mapping, form, request, response );
        }
        else
        	return super.execute( mapping, form, request, response );
	}

	public ActionForward delTime(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception, IOException
	{
		ActionForward forward = mapping.findForward( "GeneralFailure" );

		EditEventForm myForm = (EditEventForm) form;

		//write edited event also in the session (because the reset function is called - dont know why)
		HttpSession mySession = request.getSession();
		if( mySession == null )
		{
			//if this happens the user get logged out...
			return forward;
		}

		forward = mapping.findForward( "TimeEntryUpdateSuccesful" );


		String	sTimeID = request.getParameter( "entry" );
		if( sTimeID == null )
			return forward;			//dont make any noise and return without doing anything :)


		int	iTimeID;
		try
		{
			iTimeID = Integer.parseInt( sTimeID );
		}
		catch( NumberFormatException nfe )
		{
			return forward;			//look above :)
		}

		//delete the correct entry

		//get Event from Form-Bean
		Event eOldEditEvent = myForm.getEvent();

		//update Time-Entrys
		Collection cTimeCollection = eOldEditEvent.getTimes();
		EventTime myTime = null;
		Iterator cTC = cTimeCollection.iterator();
		while( cTC.hasNext() && ( iTimeID >= 0 ) )
		{
			myTime = (EventTime) cTC.next();
			iTimeID--;
		}

		if( iTimeID == -1 )
			cTimeCollection.remove( myTime );
		eOldEditEvent.setTimes( cTimeCollection );


		mySession.setAttribute( "oldEditEvent", eOldEditEvent );

		//this is for the other fields - they have to be saved during add-times...
		mySession.setAttribute( "oldEditEventSelectedEventType",	myForm.getSelectedEventType() );
		mySession.setAttribute( "oldEditEventSelectedLecturer",		myForm.getSelectedLecturer() );
		mySession.setAttribute( "oldEditEventStart",				myForm.getEventStart() );
		mySession.setAttribute( "oldEditEventSubscriptionStart",	myForm.getEventSubscriptionStart() );
		mySession.setAttribute( "oldEditEventSubscriptionEnd",		myForm.getEventSubscriptionEnd() );
		mySession.setAttribute( "oldEditEventUnsubscriptionEnd",	myForm.getEventUnsubscriptionEnd() );
		mySession.setAttribute( "oldEditEventMaxSubscriptions",		myForm.getEventMaxSubscriptions() );
		mySession.setAttribute( "oldEditEventHideSelectType",		myForm.getHideTypeSelection() );
		mySession.setAttribute( "oldEditEventAllowanceList",		myForm.getEventAllowanceList() );


		//write event also in the current editited form and update the collection for output
		myForm.getEvent().setTimes( cTimeCollection );
		myForm.updateTimeCollection();

		//set the input-values to 0
		myForm.setNewTimeSelectedDayName( "0" );
		myForm.setNewTimeClockTime( "" );
		myForm.setNewTimeSelectedRhythm( "1" );
		myForm.setNewTimeLocation( "" );


		return forward;
	}

	public ActionForward addTime(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception, IOException
	{
		ActionForward forward = mapping.findForward( "GeneralFailure" );

		Collection cTimeCollection;
		EditEventForm myForm = (EditEventForm) form;

		//add the entry
		EventTime myEventTime = new EventTime( myForm.getNewTimeDayName(),
												myForm.getNewTimeClockTime(),
												myForm.getNewTimeRhythm(),
												myForm.getNewTimeLocation() );


		//get Event from Form-Bean
		Event eOldEditEvent = myForm.getEvent();

		//update Time-Entrys
		cTimeCollection = eOldEditEvent.getTimes();
		cTimeCollection.add( myEventTime );
		eOldEditEvent.setTimes( cTimeCollection );


		//write edited event also in the session (because the reset function is called - dont know why)
		HttpSession mySession = request.getSession();
		if( mySession == null )
		{
			//if this happens the user get logged out...
			return forward;
		}
		mySession.setAttribute( "oldEditEvent", eOldEditEvent );

		//this is for the other fields - they have to be saved during add-times...
		mySession.setAttribute( "oldEditEventSelectedEventType",	myForm.getSelectedEventType() );
		mySession.setAttribute( "oldEditEventSelectedLecturer",		myForm.getSelectedLecturer() );
		mySession.setAttribute( "oldEditEventStart",				myForm.getEventStart() );
		mySession.setAttribute( "oldEditEventSubscriptionStart",	myForm.getEventSubscriptionStart() );
		mySession.setAttribute( "oldEditEventSubscriptionEnd",		myForm.getEventSubscriptionEnd() );
		mySession.setAttribute( "oldEditEventUnsubscriptionEnd",	myForm.getEventUnsubscriptionEnd() );
		mySession.setAttribute( "oldEditEventMaxSubscriptions",		myForm.getEventMaxSubscriptions() );
		mySession.setAttribute( "oldEditEventHideSelectType",		myForm.getHideTypeSelection() );
		mySession.setAttribute( "oldEditEventAllowanceList",		myForm.getEventAllowanceList() );


		//write event also in the current editited form and update the collection for output
		myForm.getEvent().setTimes( cTimeCollection );
		myForm.updateTimeCollection();

		//set the input-values to 0
		myForm.setNewTimeSelectedDayName( "0" );
		myForm.setNewTimeClockTime( "" );
		myForm.setNewTimeSelectedRhythm( "1" );
		myForm.setNewTimeLocation( "" );

		forward = mapping.findForward( "TimeEntryUpdateSuccesful" );
		return forward;
	}

	public ActionForward editEvent(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception, IOException
	{
		ActionForward forward = mapping.findForward( "GeneralFailure" );

		//get instance of this form
		EditEventForm myForm = (EditEventForm) form;
		if( myForm == null )
			return forward;

		//get instance of eventmanager
		EventManager myEventManager = null;
		try
		{
			myEventManager = EventManager.getInstance();
		}
		catch( IllegalArgumentException iae )
		{
			return forward;
		}

		//get the event...
		Event myEvent = myForm.getEvent();

		//check if the rights are valid....
		if( !(myForm.hasAdminRights() ||
			(myForm.hasLecturerRights() && myEvent.getLecturer().equals( myForm.getUserLogin() ) ) ) )
		{
			return forward;
		}

		//check if this event has to be added or updated
		if(	myEvent.getID() == -1 )
		{
			//add new Event
			//get freeEventID
			int newID = myEventManager.getFreeID();
			if( newID == -1 )
				forward = mapping.findForward( "AddEventUnsuccesful" );
			else
			{
				//pass it to the manager
				myEvent.setID( newID );
				if( myEventManager.addEvent( myEvent ) )
				{
					// add the parameter to the session for forwarding
					request.getSession(true).setAttribute( "wrapEventID", ""+myEvent.getID() );

					//get the allowance list
					//parse allowanceList
					String[] result = myForm.getAllowanceListSplitted();

					Collection cAllowanceList = new ArrayList();
					for (int x=0; x<result.length; x++)
					{
						if( !result[x].equals( "" ) )
							cAllowanceList.add( result[x] );
					}
					
					//set this AllowanceList to the manager
					if( !myEventManager.setAllowanceList( cAllowanceList, myEvent.getID() ) )
					{
						//2do: forward to something else...
						Log.addLog( "Event \""+myEvent.getName()+"\" (#"+myEvent.getID()+") by \""+myForm.getUser().getFirstName()+" "+myForm.getUser().getSurname()+"\" ("+myForm.getUser().getLogin()+") : AllowanceList creation failed" );
					}

					forward = mapping.findForward( "AddEventSuccesful" );
					Log.addLog( "Event \""+myEvent.getName()+"\" (#"+myEvent.getID()+") by \""+myForm.getUser().getFirstName()+" "+myForm.getUser().getSurname()+"\" ("+myForm.getUser().getLogin()+")" );
				}
				else
					forward = mapping.findForward( "AddEventUnsuccesful" );
			}
		}
		else
		{
			if( myEventManager.updateEvent( myEvent ) )
			{
				//get the allowance list
				//parse allowanceList
				String[] result = myForm.getAllowanceListSplitted();

				Collection cAllowanceList = new ArrayList();
				for (int x=0; x<result.length; x++)
					cAllowanceList.add( result[x] );

				//set this AllowanceList to the manager
				if( !myEventManager.setAllowanceList( cAllowanceList, myEvent.getID() ) )
				{
					//2do: forward to something else...
					Log.addLog( "Event \""+myEvent.getName()+"\" (#"+myEvent.getID()+") by \""+myForm.getUser().getFirstName()+" "+myForm.getUser().getSurname()+"\" ("+myForm.getUser().getLogin()+") : AllowanceList update failed" );
				}

				// add the parameter to the response for forwarding
				request.getSession(true).setAttribute( "wrapEventID", ""+myEvent.getID() );
				forward = mapping.findForward( "UpdateEventSuccesful" );
			}
			else
				forward = mapping.findForward( "UpdateEventUnsuccesful" );
		}

		return forward;
	}

    /**
	* Maps the given request to the correct sub-method in this class
    * @return mapping
    */
    protected Map getKeyMethodMap()
    {
		Map map = new HashMap();

		//name of parameter, name of method to run
		map.put( "editEvent.buttonAddTimeEntry",	"addTime" );
		map.put( "editEvent.buttonCreateEvent",		"editEvent" );
		map.put( "editEvent.buttonEditEvent",		"editEvent" );
		return map;
	}
}