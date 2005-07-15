/*
File:		LoginForm.java
Created:	05-05-30@21:00
Task:		This is the Form-Bean for the login.jsp-site
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

import de.tr1.cooperator.manager.web.LoginManager;
import de.tr1.cooperator.model.web.UserSession;

import de.tr1.cooperator.manager.system.Log;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import java.util.Date;

/**
 * This is the Form-Bean for the login.jsp-site
 * This stores all the date of the formular and verifys for correct input
 *
 * @author Peter Matjeschk
 * @version 05-05-30@21:00
 */
public class LoginForm extends ActionForm
{
	String		loginName;
	String		loginPW;
	boolean		bAlreadyLoggedIn;

	//Getter and Setter
	public String getLoginName()
	{
		return this.loginName;
	}
	public void setLoginName( String newLoginName )
	{
		this.loginName = newLoginName;
	}
	public String getLoginPW()
	{
		return this.loginPW;
	}
	public void setLoginPW( String newLoginPW )
	{
		this.loginPW = newLoginPW;
	}

	public String getAlreadyLoggedIn()
	{
		return ""+this.bAlreadyLoggedIn;
	}



	/**
	 * This method intialises all the values
	 */
	synchronized public void initValues( HttpServletRequest request )
	{
		loginName = null;
		loginPW = null;
		bAlreadyLoggedIn = false;
		

		//check if already logged in...
		HttpSession	session	= request.getSession();
		
		//if no session was there, we are logged off...
		if( session == null )
			return;

		String		statusLogin = (String) session.getAttribute( "login" );

		//check if there are entrys in this session
		if ( statusLogin != null )
		{
			UserSession hUserSession = new UserSession(
												session,
												statusLogin,
												new Date().getTime() );

			hUserSession.setUserLogin( statusLogin );

			//check if this session is valid anymore
			if ( LoginManager.getInstance( ).isValid( hUserSession ) )
			{
				//we are already logged in...
				bAlreadyLoggedIn = true;
			}
			else
			{
				//session has attributes, but login is unvalid
				//invalidate this session
				session.invalidate();
			}
		}
	}

	/**
	 * This method resets all the values
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		this.initValues( request );
	}
	
	/**
	 * This method validates the input
	 */
 	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
 	{
		ActionErrors errors = new ActionErrors();

		if ( (this.loginPW==null) || (this.loginPW.length()<1) )
			errors.add( "login.password", new ActionMessage( "login.pwempty" ) );

		if ( (this.loginName==null) || (this.loginName.length()<1) )
		{
			errors.add( "login.username", new ActionMessage( "login.unempty" ) );

			//reset passwort so that the user has to reenter it and it cant be read through the internet
			this.loginPW = "";
		}

		return errors;
	}
}