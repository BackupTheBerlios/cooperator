/*
File:		LoginAction.java
Created:	05-05-30@21:00
Task:		Login-Action for Struts-Framework
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


import de.tr1.cooperator.manager.mainframe.UserManager;
import de.tr1.cooperator.manager.system.Log;
import de.tr1.cooperator.model.mainframe.User;
import de.tr1.cooperator.exceptions.*;
import de.tr1.cooperator.manager.ManagerInitializer;
import de.tr1.cooperator.manager.web.*;
import de.tr1.cooperator.model.web.*;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;

import java.util.Date;
import java.io.File;

import java.lang.Thread;

/**
 * This class handles the actions called by the struts-framework
 *
 * @author Peter Matjeschk
 * @version 05-05-30@21:00
 */
public class LoginAction extends Action
{
	/**
	 * This method is called by the struts-framework if the login-button is pressed
	 */
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{

		String path = this.getServlet().getServletContext().getRealPath( "WEB-INF" + File.separator + "Datas" ) + File.separator;
		ManagerInitializer.initAll( path );

		ActionForward forward = mapping.findForward( "LoginUnsuccesful" );

		LoginForm myLoginForm = (LoginForm) form;
		try
		{

			User loginUser = UserManager.getInstance().getUser( myLoginForm.getLoginName() );
			
			if( loginUser == null )
			{
				ActionErrors myErrors = new ActionErrors();
				myErrors.add( "login.username", new ActionMessage( "login.unnotexists" ) );
				this.addErrors( request,  myErrors );

				myLoginForm.setLoginPW( "" );
				return mapping.getInputForward();
			}

			if( loginUser.checkPassword( myLoginForm.getLoginPW() ) )
			{
				//2do: logout old user

				//create an userSession
				HttpSession mySession = request.getSession( true );
				mySession.setAttribute( "login", myLoginForm.getLoginName() );
				UserSession loginSession = new UserSession(
													mySession,
													myLoginForm.getLoginName(),
													new Date().getTime() );

				//add this session
				LoginManager.getInstance().add( loginSession );

				forward = mapping.findForward( "LoginSuccesful" );
			}
			else
			{
				//wait 2secs...
				Thread.sleep(2000);
				
				//add this error
				ActionErrors myErrors = new ActionErrors();
				myErrors.add( "login.password", new ActionMessage( "login.pwincorrect" ) );
				this.addErrors( request,  myErrors );
				
				//reset password-field
				myLoginForm.setLoginPW( "" );
				
				//return to the inputs...
				return mapping.getInputForward();
			}
		}
		catch( SingletonException se )
		{
			Log.addLog( "Can't login because of a bad or not initialized Singleton: " + se );

			forward = mapping.findForward( "GeneralFailure" );
		}
		catch( XMLException ie )
		{
			Log.addLog( "Can't login because of a bad XML file: " + ie );

			forward = mapping.findForward( "GeneralFailure" );
		}
		catch( Exception e )
		{
			Log.addLog( "Can't login because of a general Exception: " + e );

			forward = mapping.findForward( "GeneralFailure" );
		}


		return forward;
	}
}