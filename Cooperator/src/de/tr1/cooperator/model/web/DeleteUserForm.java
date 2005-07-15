/*
File:		DeleteUserForm.java
Created:	05-06-16@22:00
Task:		This is the Form-Bean for the deleteUser.jsp
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
import de.tr1.cooperator.model.web.*;
import de.tr1.cooperator.manager.mainframe.*;
import de.tr1.cooperator.manager.system.*;
import de.tr1.cooperator.manager.web.*;
import de.tr1.cooperator.exceptions.*;
/*
import de.tr1.cooperator.manager.ManagerInitializer;
import de.tr1.cooperator.manager.mainframe.UserManager;
import de.tr1.cooperator.manager.mainframe.EventTypeManager;
import de.tr1.cooperator.manager.mainframe.EventManager;
*/

/**
 * This is the Form-Bean for the deleteUser.jsp
 * This stores all the date of the formular and verifys for correct input
 *
 * @author Michael Thiele
 * @version 05-06-16@22:00
 *
 */

public class DeleteUserForm extends Accessable
{
	private	String		sDeleteUserLogin;		// the Login of the user, which will be deleted
	private	String		sMessage;				// any messages

	private boolean		bDeleteIsAdmin;			// is the useran administrator?

	private boolean		bIsSubscribed;			// is the user, if student, subscribed in any event?
	private Collection	cSubscribedEvents;		// list of events the student is subscribed in

	private boolean		bIsLecturing;			// is the user, if lecturer, lecturing any event?
	private Collection	cLecturedEvents;		// list of events the lecturer manages

	private boolean		bDeleted;


	synchronized public void initValues( HttpServletRequest request )
	{
		super.initValues(request);
		initValuesDeleted( request, false );
	}

	synchronized public void initValuesDeleted( HttpServletRequest request, boolean isDeleted )
	{
		super.initValues(request);

		if( this.hUser == null )
			return;

		if( this.hasAdminRights() )
		{
			bDeleted			= isDeleted;
			sDeleteUserLogin	= "";
			sMessage			= "";
			bDeleteIsAdmin		= false;
			bIsSubscribed		= false;
			cSubscribedEvents 	= new ArrayList();
			bIsLecturing		= false;
			cLecturedEvents 	= new ArrayList();

			// grab the userLogin out of the request
			sDeleteUserLogin += request.getParameter( "login" );

			if( sDeleteUserLogin.equals( "" ) )
			{
				this.sMessage += "Falscher oder ungültiger Login angegeben!";
				return;
			}
			// check if admin
			User deleteUser = UserManager.getInstance().getUser( sDeleteUserLogin );
			int iRights = 1;
			if( deleteUser != null )
				iRights = deleteUser.getRights();
			if( iRights == 4 || iRights == 5 || iRights == 6 || iRights == 7 )
				bDeleteIsAdmin = true;

			// now get the eventlists
			// the eventlist for the student, contains all events he´s subscribed in
			cSubscribedEvents	= EventManager.getInstance().getSubscribedEvents( sDeleteUserLogin );

			if( cSubscribedEvents.size() > 0 )
				bIsSubscribed = true;

			// the eventlist for the lecturer, contains all events he is lecturer of
			cLecturedEvents		= EventManager.getInstance().getLecturedEvents( sDeleteUserLogin );
			if( cLecturedEvents.size() > 0 )
				bIsLecturing = true;
		}
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
 	{
		ActionErrors errors = new ActionErrors();
		return errors;
	}

//===== Getter & Setter =====

// Getter
	public String getMessage( )
	{
		return this.sMessage;
	}

	public String getDeleteUserLogin( )
	{
		return this.sDeleteUserLogin;
	}

	public Collection getSubscribedEvents()
	{
		return this.cSubscribedEvents;
	}

	public Collection getLecturedEvents()
	{
		return this.cLecturedEvents;
	}

	public boolean getIsSubscribed()
	{
		return this.bIsSubscribed;
	}

	public boolean getIsLecturing()
	{
		return this.bIsLecturing;
	}

	public boolean getDeleteIsAdmin()
	{
		return this.bDeleteIsAdmin;
	}

	public boolean getDeleted()
	{
		return this.bDeleted;
	}

// Setter
	public void setDeleteUserLogin( String login )
	{
		this.sDeleteUserLogin = login;
	}

	public void setMessage( String message)
	{
		this.sMessage = message;
	}

	public void setDeleted( boolean deleted)
	{
		this.bDeleted = deleted;
	}
// Adder
	public void addMessage( String message)
	{
		this.sMessage += message;
	}
}