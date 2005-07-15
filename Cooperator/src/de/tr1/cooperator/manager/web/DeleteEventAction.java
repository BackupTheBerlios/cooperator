/*
File:		DeleteEventAction.java
Created:	05-06-12@13:30
Task:		Action-Class for the DeleteEvent-Page
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
import de.tr1.cooperator.manager.system.*;
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
 * Action-Class for the DeleteEvent-Page
 *
 * @author Sebastian Kohl
 * @version 05-06-12@13:30
 */
public class DeleteEventAction extends Action
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
		DeleteEventForm myForm  = (DeleteEventForm) form;

		try
		{
			// get the Event-List
			Collection events = myForm.getAllEvents();
			Iterator it		  = events.iterator();
			while ( it.hasNext() )
			{
				Event ev = (Event)(it.next());
				if ( (!EventManager.getInstance().deleteEvent( ev ))
					||(!EventResultManager.getInstance().deleteAll( ev.getID() )) )
				{
					myForm.initValuesWithParam( request, false );
					myForm.setMessage( "Fehler beim Löschen von Daten! Der Administrator wurde bereits informiert!" );
					EmailSender.send( GlobalVarsManager.getInstance().getSystemMail(),
									  GlobalVarsManager.getInstance().getSystemMail(),
									  "Critical-Coop-Error",
									  "Ein kritischer Fehler im Cooperator ist aufgetreten beim Löschen von "
									 +"Veranstaltungen! Bitte untersuchen Sie sofort die Datenbank auf Schäden!\r\n"
									 +"Fehler bei Event "+ev.getName()+" (#"+ev.getID()+")." );
					Log.addLog( "ERROR: While deleting complete events with it's datas occured a critical error and event "+ev.getName()+" (#"+ev.getID()+") was not removed! Please inspect the Databases!");
					return mapping.findForward( "DeleteEventFailed" );
				}
				Log.addLog( "Event "+ev.getName()+" (#"+ev.getID()+") was removed by "+myForm.getUser().getFirstName()+" "+myForm.getUser().getSurname()+" ("+myForm.getUser().getLogin()+")" );
			}
			myForm.initValuesWithParam( request, false );
			myForm.setDeleted( true );
			return mapping.findForward( "DeleteEventSuccessfull" );
		}
		catch ( Exception e )
		{
			forward = mapping.findForward( "GeneralFailure" );
			return forward;
		}
	}
}