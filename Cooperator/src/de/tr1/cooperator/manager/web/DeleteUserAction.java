/*
File:		DeleteUserAction.java
Created:	05-06-17@09:00
Task:		Action-Class for the DeleteUser-Page
Author:		Michael Thiele

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
import de.tr1.cooperator.model.web.DeleteUserForm;
import org.apache.struts.actions.LookupDispatchAction;


import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.net.*;
import java.util.*;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;




/**
 * Action-Class for the DeleteUser-Page
 *
 * @author Michael Thiele
 * @version 05-06-17@09:00
 */
public class DeleteUserAction extends Action
{
	private final String DELETEDSUCCESSFULLY 	= "Der User wurde erfolgreich gelöscht.";
	private final String DELETIONFAILD			= "Der User konnte aufgrund eines Fehlers nicht gelöscht werden.";
	private final String DELETIONFAILDLECTURER	= " Nicht allen Veranstaltungen konnte ein neuer Leiter zugewiesen werden.";
	private final String DELETIONFAILDSTUDENT	= " Er konnte nicht aus allen Veranstaltungen ausgetragen werden.";
	private final String YOUARENOADMIN			= " Sie dürfen keinen User löschen. Sie besitzen keine Administratorrechte.";
    /**
     * This method is called by the struts-framework if the submit-button is pressed
	 * or the action is called directly.
     * It unsubscribes the user from all events, renames the lecturer of the events
     * he managed and finaly deletes the user
     */
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
        ActionForward forward = mapping.findForward( "GeneralFailure" );
		HttpSession curSession = request.getSession();
        if( curSession == null )
            return forward;

		DeleteUserForm duForm			= (DeleteUserForm) form;
        String	login					= duForm.getDeleteUserLogin();
        boolean bUserDeleted 			= false;
        boolean bSubscriptionsDeleted 	= false;
        boolean bEventsLecturerRenamed	= false;

        if( duForm.hasAdminRights() )
        {
        	// if user is student, then delete his subscriptions
	        if( duForm.getIsSubscribed() )
    	    	bSubscriptionsDeleted = EventManager.getInstance().unsubscribeEverywhere( login );
        	else
	        	bSubscriptionsDeleted = true;

			//if user is lecturer, then change the Event.Lecturer attribute into admin in every event he managed
			if( duForm.getIsLecturing() )
				bEventsLecturerRenamed = EventManager.getInstance().renameLecturer( login, "-" );
			else
				bEventsLecturerRenamed = true;

			// when everything is done, then delete the user
			if( bSubscriptionsDeleted && bEventsLecturerRenamed )
				bUserDeleted = UserManager.getInstance().deleteUser( login );

	        //this is need for update the values...
    	    duForm.initValuesDeleted( request, false );    				//new init
			duForm.setDeleted( true );
			duForm.setDeleteUserLogin( login );
			if( bUserDeleted )
				duForm.setMessage( DELETEDSUCCESSFULLY );
			else
				duForm.setMessage( DELETIONFAILD );
			if( !bEventsLecturerRenamed )
				duForm.addMessage( DELETIONFAILDLECTURER );
			if( !bSubscriptionsDeleted )
				duForm.addMessage( DELETIONFAILDSTUDENT );
		}
		else
		{
			duForm.initValuesDeleted( request, false );    				//new init
			duForm.addMessage( YOUARENOADMIN );
		}
        forward = mapping.findForward( "DeletedOrNot" );

        return forward;
    }
}