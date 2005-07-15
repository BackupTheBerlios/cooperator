/*
File:		Accessable.java
Created:	05-05-24@12:30
Task:		This is a parent-container for nearly all ActionForms and will have a set of functions, that always stay the same.
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

import de.tr1.cooperator.model.mainframe.*;
import de.tr1.cooperator.model.web.*;
import de.tr1.cooperator.model.web.*;
import de.tr1.cooperator.manager.web.*;
import de.tr1.cooperator.manager.mainframe.*;
import de.tr1.cooperator.manager.*;

import java.io.File;

import java.util.*;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * This is a parent-container for nearly all ActionForms and will have a set of functions, that always stay the same.
 *
 * @author Sebastian Kohl
 * @version 05-05-24@13:00
 *
 */
public class Accessable extends ActionForm
{
	protected	User		hUser			= null;
	protected	String		sErrorMessage	= "";
	protected	String		sDataPath		= "";
	protected	UserSession	hUserSession	= null;

	synchronized protected final boolean initAccessValues( HttpServletRequest Request )
	{
		try
		{
			this.hUser		   = null;
			this.sErrorMessage = "";
			this.sDataPath 	   = "";
			String path = this.servlet.getServletContext().getRealPath( "WEB-INF"+File.separator+
												            "Datas" )+File.separator;
			this.sDataPath = ManagerInitializer.initAll( path );
			// session-Daten einlesen

			HttpSession session	= Request.getSession(true);
			if ( session.getAttribute( "login" ) == null )
			{
				this.sErrorMessage += "No valid Session-Login, please go back to the login-page";
				return false;
			}
			String statusLogin = session.getAttribute( "login" ).toString( );
			this.hUserSession = new UserSession(
												session,
												statusLogin,
												new Date().getTime() );
			this.hUserSession.setUserLogin	( statusLogin );

			if ( !LoginManager.getInstance( ).isValid( this.hUserSession ) )
			{
				this.sErrorMessage += "Sorry, you're not logged in correctly or your login was killed";
				return false;
			}
			hUser 		= UserManager.getInstance( ).getUser( statusLogin );
		}
		catch (Exception e)
		{
			this.sErrorMessage += e;
			return false;
		}
		return true;
	}

	synchronized public void initValues( HttpServletRequest Request )
	{
		// call this every time, bevor doing anything else!
		if ( !this.initAccessValues( Request ) )
			return;
		// do the rest of the initialisation
	}

	public User getUser( )
	{
		return this.hUser;
	}

	public String getErrorMessage( )
	{
		return this.sErrorMessage;
	}
	public void setErrorMessage( String message )
	{
	}

	public String getUserFirstName( )
	{
		if ( this.hUser == null )
			return "";
		else
			return this.hUser.getFirstName( );
	}

	public String getUserSurname( )
	{
		if ( this.hUser == null )
			return "";
		else
			return this.hUser.getSurname( );
	}

	public String getUserRightsAsString( )
	{
		if ( this.hUser == null )
			return "";
		else
			return this.hUser.getRightsAsString( );
	}

	public int getUserRights( )
	{
		if ( this.hUser == null )
			return 0;
		else
			return this.hUser.getRights( );
	}

	public String getUserLogin( )
	{
		if ( this.hUser == null )
			return "";
		else
			return this.hUser.getLogin( );
	}

	public String getUserPersonalNumber()
	{
		if ( this.hUser == null )
			return "";
		else
			return this.hUser.getPersonalNumber( );
	}

	public String getUserEmailAddress()
	{
		if ( this.hUser == null )
			return "";
		else
			return this.hUser.getEmailAddress( );
	}

	public boolean hasAdminRights( )
	{
		if ( this.hUser == null )
			return false;
		else
			return (this.hUser.getRights( ) & User.ADMIN) != 0;
	}

	public boolean hasLecturerRights( )
	{
		if ( this.hUser == null )
			return false;
		else
			return (this.hUser.getRights( ) & User.LECTURER) != 0;
	}

	public boolean hasStudentRights( )
	{
		if ( this.hUser == null )
			return false;
		else
			return (this.hUser.getRights( ) & User.STUDENT) != 0;
	}

	public String getDataPath( )
	{
		return this.sDataPath;
	}

	public String getIsAdmin()
	{
		return ""+this.hasAdminRights();
	}

	public String getIsLecturer()
	{
		return ""+this.hasLecturerRights();
	}

	public String getIsStudent()
	{
		return ""+this.hasStudentRights();
	}

	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		this.initValues( request );
	}

	public String getMySessionId()
	{
		return ""+this.hUserSession.getSessionId( );
	}
}