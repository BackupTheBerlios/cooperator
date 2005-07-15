/*
File:		LogoutAction.java
Created:	05-06-06@19:30
Task:		Logout-Action for Struts-Framework
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

import java.util.Date;
import java.io.File;


/**
 * This class handles the actions called by the struts-framework
 *
 * @author Peter Matjeschk
 * @version 05-06-06@21:00
 */
public class LogoutAction extends Action
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
		ActionForward forward = mapping.findForward( "LogoutSuccesful" );

		HttpSession mySession = request.getSession( true );
		String logoutUserLogin = (String) mySession.getAttribute( "login" );

		if( logoutUserLogin != null )
			try
			{

				User logoutUser = UserManager.getInstance().getUser( logoutUserLogin);

				//create an userSession
				UserSession logoutSession = new UserSession(
													mySession,
													logoutUserLogin,
													new Date().getTime() );
				LoginManager.getInstance().kill( logoutSession );
			}
			catch( SingletonException se )
			{
				Log.addLog( "Can't logout because of a bad or not initialized Singleton: " + se );

				forward = mapping.findForward( "GeneralFailure" );
			}
			catch( XMLException ie )
			{
				Log.addLog( "Can't logout because of a bad XML file: " + ie );

				forward = mapping.findForward( "GeneralFailure" );
			}
			catch( Exception e )
			{
				Log.addLog( "Can't logout because of a general Exception: " + e );

				forward = mapping.findForward( "GeneralFailure" );
			}


		return forward;
	}
}