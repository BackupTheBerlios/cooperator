/*
File:		EventInfoAction.java
Created:	05-06-10@20:00
Task:		Action-Class for the EventInfo-Page
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

package de.tr1.cooperator.manager.web;


import de.tr1.cooperator.manager.mainframe.UserManager;
import de.tr1.cooperator.manager.system.Log;
import de.tr1.cooperator.exceptions.*;
import de.tr1.cooperator.manager.ManagerInitializer;
import de.tr1.cooperator.manager.mainframe.*;
import de.tr1.cooperator.manager.web.*;
import de.tr1.cooperator.model.web.*;
import de.tr1.cooperator.model.mainframe.*;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.*;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;




/**
 * Action-Class for the EventInfo-Page
 *
 * @author Sebastian Kohl
 * @version 05-06-10@20:00
 */
public class EventInfoAction extends Action
{
	/**
	 * This method is called by the struts-framework if the submit-button is pressed
	 * or the action is called directly
	 */
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		ActionForward forward = mapping.findForward( "EventInfoSuccessfull" );
		EventInfoForm myForm  = (EventInfoForm) form;

		try
		{
			// there should be an action in the request
			String action	= request.getParameter( "action" );
			String sEventID = request.getParameter( "subEvent" );
			String sE_ID 	= request.getParameter( "eventID" );
			int	   iEventID	= -1;
			Event  hEvent	= null;
			Collection cAll = EventManager.getInstance().getAllEvents();
			if ( (action==null) || (action.length()<1) )
			{
				myForm.initValues( request );
				myForm.setErrorMessage( "Action falsch angegeben!" );
				return mapping.findForward( "EventInfoFailed" );
			}
			// try to grab and parse "subEvent" out of the request
			if ( sEventID!=null )
			{
				try
				{
					iEventID = Integer.parseInt( sEventID );
					hEvent	 = EventManager.getInstance().getEventByID( iEventID );
					if ( hEvent==null )
					{
						myForm.initValues( request );
						myForm.setErrorMessage( "Angegebenes Event existiert nicht!" );
						return mapping.findForward( "EventInfoFailed" );
					}
				}
				catch ( Exception e ){}
			}
			else
				hEvent = myForm.getEvent();

//============== Subscribe Action ===================

			if ( action.equals("subscribe") )
			{
				// check if possible
				if ( myForm.hasStudentRights()
					&& (!hEvent.isSubscribed( myForm.getUser().getLogin() ))
					&& (hEvent.canSubscribe( myForm.getUser().getLogin() )) )
				{
					// check if not subscibed in group
					Iterator it = cAll.iterator();
					int		gid = hEvent.getGroup();
					while ( it.hasNext() )
					{
						Event ev = (Event)(it.next());
						if ( (ev.getGroup()==gid) && (ev.isSubscribed(myForm.getUser().getLogin())) )
						{
							myForm.initValues( request );
							myForm.setSubscribeMessage( "Einschreibung nicht möglich." );
							return mapping.findForward( "EventInfoFailed" );
						}
					}
					// subscribe
					hEvent.subscribe( myForm.getUser().getLogin(), false );
					if ( !EventManager.getInstance().updateEvent( hEvent ) )
					{
						myForm.initValues( request );
						myForm.setSubscribeMessage( "Einschreibung fehlgeschlagen." );
						return mapping.findForward( "EventInfoFailed" );
					}
					else
						Log.addLog( "User "+myForm.getUser().getFirstName()+" "+myForm.getUser().getSurname()+" ("+myForm.getUser().getLogin()+") subscribed to event \""+hEvent.getName()+"\" (#"+hEvent.getID()+")" );
				}
				else
				{
					myForm.initValues( request );
					myForm.setSubscribeMessage( "Einschreibung nicht möglich." );
					return mapping.findForward( "EventInfoFailed" );
				}
			}

//============== Usubscribe Action ===================

			if ( action.equals("unsubscribe") )
			{
				// check if possible
				if ( myForm.hasStudentRights()
					&& (hEvent.isSubscribed( myForm.getUser().getLogin() ))
					&& (hEvent.canUnsubscribe( myForm.getUser().getLogin() )) )
				{
					// subscribe
					hEvent.unsubscribe( myForm.getUser().getLogin(), false );
					if ( !EventManager.getInstance().updateEvent( hEvent ) )
					{
						myForm.initValues( request );
						myForm.setSubscribeMessage( "Ausschreibung fehlgeschlagen." );
						return mapping.findForward( "EventInfoFailed" );
					}
					Log.addLog( "User "+myForm.getUser().getFirstName()+" "+myForm.getUser().getSurname()+" ("+myForm.getUser().getLogin()+") unsubscribed from event \""+hEvent.getName()+"\" (#"+hEvent.getID()+")" );
				}
				else
				{
					myForm.initValues( request );
					myForm.setSubscribeMessage( "Ausschreibung nicht möglich." );
					return mapping.findForward( "EventInfoFailed" );
				}
			}

//============== Change Subscription Action ===================

			if ( action.equals("swapHere") )
			{
				// check if possible
				if ( myForm.hasStudentRights()
					&& (!hEvent.isSubscribed( myForm.getUser().getLogin() ))
					&& (hEvent.canSubscribe( myForm.getUser().getLogin() )) )
				{
					// check if subscibed in group
					Iterator it   = cAll.iterator();
					int		gid   = hEvent.getGroup();
					boolean isin  = false;
					Event inEvent = null;
					while ( it.hasNext() )
					{
						Event ev = (Event)(it.next());
						if ( (ev.getGroup()==gid) && (ev.isSubscribed(myForm.getUser().getLogin())) )
						{
							isin 	= true;
							inEvent = ev;
							break;
						}
					}
					if ( (!isin) || (!inEvent.canUnsubscribe( myForm.getUser().getLogin() )) )
					{
						myForm.initValues( request );
						myForm.setSubscribeMessage( "Umschreibung nicht möglich." );
						return mapping.findForward( "EventInfoFailed" );
					}
					// subscribe
					hEvent.subscribe( myForm.getUser().getLogin(), true );		// force it, because of the timestamps
					inEvent.unsubscribe( myForm.getUser().getLogin(), true );	// in critical times, a subscription will work, but the unsubsciption may be to late
					if (  (!EventManager.getInstance().updateEvent( hEvent ))
						|| (!EventManager.getInstance().updateEvent( inEvent )) )
					{
						myForm.initValues( request );
						myForm.setSubscribeMessage( "Umschreibung fehlgeschlagen." );
						return mapping.findForward( "EventInfoFailed" );
					}
					else
					{
						Log.addLog( "User "+myForm.getUser().getFirstName()+" "+myForm.getUser().getSurname()+" ("+myForm.getUser().getLogin()+") unsubscribed from event \""+inEvent.getName()+"\" (#"+inEvent.getID()+")" );
						Log.addLog( "User "+myForm.getUser().getFirstName()+" "+myForm.getUser().getSurname()+" ("+myForm.getUser().getLogin()+") subscribed to event \""+hEvent.getName()+"\" (#"+hEvent.getID()+")" );
					}
				}
				else
				{
					myForm.initValues( request );
					myForm.setSubscribeMessage( "Umschreibung nicht möglich." );
					return mapping.findForward( "EventInfoFailed" );
				}
			}

//============ ACTION END ===================

		}
		catch ( Exception e )
		{
			forward = mapping.findForward( "GeneralFailure" );
			return forward;
		}
		myForm.initValues( request );
		return mapping.findForward( "EventInfoSuccessfull" );
	}
}