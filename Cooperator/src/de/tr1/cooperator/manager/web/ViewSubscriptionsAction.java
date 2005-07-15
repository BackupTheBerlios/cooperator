/*
File:		ViewSubscriptionsAction.java
Created:	05-06-11@10:30
Task:		Action-Class for the ViewSubscriptions-Page
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
 * Action-Class for the ViewSubscriptions-Page
 *
 * @author Sebastian Kohl
 * @version 05-06-11@10:30
 */
public class ViewSubscriptionsAction extends Action
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
		ActionForward forward = mapping.findForward( "ViewSubscriptionsSuccessfull" );
		ViewSubscriptionsForm myForm  = (ViewSubscriptionsForm) form;

		try
		{
			// there should be an action in the request
			Event hEvent = myForm.getEvent();
			Collection cAll = EventManager.getInstance().getAllEvents();
			String action	= request.getParameter( "action" );
			if ( action==null )
			{
				action = myForm.getAction();
			}
			if ( (action==null) || (action.length()<1) )
			{
				myForm.initValues( request );
				myForm.setErrorMessage( "Action falsch angegeben!" );
				return mapping.findForward( "ViewSubscriptionsFailed" );
			}

//============== Subscribe Action ===================

			if ( action.equals("Teilnehmer hinzufügen") )
			{
				// grab the user
				String login = myForm.getNewSubscriber( );
				User   hUser = UserManager.getInstance().getUser( login );
				if ( hUser==null )
				{
					myForm.initValues( request );
					myForm.setSubscribeMessage( "User existiert nicht, falschen Login angegeben (Case-sensitiv)!" );
					return mapping.findForward( "ViewSubscriptionsFailed" );
				}
				// check if possible
				if ( myForm.getAllowedToEdit()
					&& (!hEvent.isSubscribed( login )) )
				{
					// check if not subscribed in group
					Iterator it = cAll.iterator();
					int		gid = hEvent.getGroup();
					while ( it.hasNext() )
					{
						Event ev = (Event)(it.next());
						if ( (ev.getGroup()==gid) && (ev.isSubscribed(login)) )
						{
							myForm.initValues( request );
							myForm.setSubscribeMessage( "Einschreibung nicht möglich, da bereits in der gleichen Gruppe eingeschrieben." );
							return mapping.findForward( "ViewSubscriptionsFailed" );
						}
					}
					// subscribe
					if ( (!hEvent.subscribe( login, true )) || (!EventManager.getInstance().updateEvent( hEvent )) )
					{
						myForm.initValues( request );
						myForm.setSubscribeMessage( "Einschreibung fehlgeschlagen." );
						return mapping.findForward( "ViewSubscriptionsFailed" );
					}
					else
						Log.addLog( "User "+hUser.getFirstName()+" "+hUser.getSurname()+" ("+login+") subscribed to event \""+hEvent.getName()+"\" (#"+hEvent.getID()+") by "+myForm.getUser().getFirstName()+" "+myForm.getUser().getSurname()+" ("+myForm.getUser().getLogin()+")" );
				}
				else
				{
					myForm.initValues( request );
					myForm.setSubscribeMessage( "Einschreibung nicht möglich." );
					return mapping.findForward( "ViewSubscriptionsFailed" );
				}
			}
//			============== Result Action ===================

			if ( action.equals("Ergebnis hinzufügen") )
			{
				// grab the user
				String personalNumber 	= myForm.getNewSubscriberPersonalNumber( );
				double result			= myForm.getNewSubscriberResult( );
				User   hUser			= UserManager.getInstance().getUserByPersonalNumber( personalNumber );
				String login			= "";
				if( hUser!=null )
					login = hUser.getLogin();

				if ( hUser==null || !hEvent.isSubscribed( login ))
				{
					myForm.initValues( request );
					myForm.setResultMessage( "User existiert nicht, falsche Matrikelnummer angegeben (Case-sensitiv)!" );
					return mapping.findForward( "ViewResultFailed" );
				}
				// check if possible
				if ( myForm.getAllowedToEdit()
					&& (hEvent.isSubscribed( login )) )
				{
					/*// check if not subscribed in group
					Iterator it = cAll.iterator();
					int		gid = hEvent.getGroup();
					while ( it.hasNext() )
					{
						Event ev = (Event)(it.next());
						if ( (ev.getGroup()==gid) && (ev.isSubscribed(login)) )
						{
							myForm.initValues( request );
							myForm.setResultMessage( "Einschreibung nicht möglich, da bereits in der gleichen Gruppe eingeschrieben." );
							return mapping.findForward( "ViewResultFailed" );
						}
					}*/
					//enter result
					boolean added = false;
					if( result == 0 )
					{
						myForm.initValues( request );
						myForm.setResultMessage( "Eintragung fehlgeschlagen. Prüfungsergebnis ungültig." );
						return mapping.findForward( "ViewResultFailed" );
					}

					ExamResult examResult = new ExamResult( personalNumber, hEvent.getID(), ( result ) );
					ExamResult testResult = EventResultManager.getInstance().getResult( personalNumber, hEvent.getID() );
					if( testResult == null )
						added = EventResultManager.getInstance().addResult( examResult );
					else
					{
						EventResultManager.getInstance().deleteResult( testResult );
						added = EventResultManager.getInstance().addResult( examResult );
					}

					if ( !added )
					{
						myForm.initValues( request );
						myForm.setResultMessage( "Eintragung fehlgeschlagen." );
						return mapping.findForward( "ViewResultFailed" );
					}
					else
						Log.addLog( "Result of User "+hUser.getFirstName()+" "+hUser.getSurname()+" ("+login+") was added to event \""+hEvent.getName()+"\" (#"+hEvent.getID()+") by "+myForm.getUser().getFirstName()+" "+myForm.getUser().getSurname()+" ("+myForm.getUser().getLogin()+")" );
				}
				else
				{
					myForm.initValues( request );
					myForm.setResultMessage( "Eintragung nicht möglich. User ist nicht in Gruppe eingetragen." );
					return mapping.findForward( "ViewResultFailed" );
				}
			}

//============== Usubscribe Action ===================

			if ( action.equals("unsubscribe") )
			{
				String login = request.getParameter( "login" );
				User hUser	 = UserManager.getInstance().getUser( login );
				if ( hUser==null )
				{
					myForm.initValues( request );
					myForm.setSubscribeMessage( "Ausschreibung nicht möglich, ungülten Login angegeben." );
					return mapping.findForward( "ViewSubscriptionsFailed" );
				}
				// check if possible
				if ( myForm.getAllowedToEdit()
					&& hEvent.isSubscribed( login ) )
				{
					// subscribe
					if ( (!hEvent.unsubscribe( login, true )) || (!EventManager.getInstance().updateEvent( hEvent )) )
					{
						myForm.initValues( request );
						myForm.setSubscribeMessage( "Ausschreibung fehlgeschlagen." );
						return mapping.findForward( "ViewSubscriptionsFailed" );
					}
						Log.addLog( "User "+hUser.getFirstName()+" "+hUser.getSurname()+" ("+login+") unsubscribed from event \""+hEvent.getName()+"\" (#"+hEvent.getID()+") by "+myForm.getUser().getFirstName()+" "+myForm.getUser().getSurname()+" ("+myForm.getUser().getLogin()+")" );
				}
				else
				{
					myForm.initValues( request );
					myForm.setSubscribeMessage( "Ausschreibung nicht möglich." );
					return mapping.findForward( "ViewSubscriptionsFailed" );
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
		return mapping.findForward( "ViewSubscriptionsSuccessfull" );
	}
}