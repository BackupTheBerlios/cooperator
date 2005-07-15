/*
File:		ProfileAction.java
Created:	05-05-31@10:00
Task:		Login-Action for Struts-Framework
Author:		Maik Mueller

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




/**
 * This class handles the actions called by the struts-framework
 *
 * @author Maik Mueller
 * @version 05-05-31@10:00
 */
public class ProfileAction extends Action
{
	private final String sPassError = "Das Eingegeben Passwort stimmt nicht" ;
	private final String sSucces	= "Profil speichern erfolgreich " ;

	/**
	 * This method is called by the struts-framework if the submit-button is pressed
	 */
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		ActionForward forward = mapping.findForward( "ProfileUnsuccesful" );

		ProfileForm myProfileForm = (ProfileForm) form;

		try
		{
				UserManager manager = UserManager.getInstance();
				User userLogin;

				if(myProfileForm.getEditFlag())
				{
					 userLogin	=	myProfileForm.getTempUser();
				}
				else
				{
					userLogin	=	myProfileForm.getUser();
				}
				userLogin.setFirstName(myProfileForm.getFirstName());
				userLogin.setSurname(myProfileForm.getSurname());
				userLogin.setEmailAddress(myProfileForm.getEmailAddress());

				// set Attributes if User is Admin or Docent
				if(myProfileForm.hasLecturerRights() || myProfileForm.hasAdminRights() )
				{
					userLogin.setPersonalNumber( myProfileForm.getPersonalNumber() );
				}
				// set Attributes if User is Admin
				if( myProfileForm.hasAdminRights( ) )
				{
					int rights = 0;
					if ( myProfileForm.getNewAdminRights( ) )
						rights += User.ADMIN;
				//set this if username is admin
					if (!myProfileForm.getNewAdminRights( ) && myProfileForm.getLogin().equals("admin"))
						rights += User.ADMIN;
					if ( myProfileForm.getNewLectRights( ) )
						rights += User.LECTURER;
					if ( myProfileForm.getNewStudentRights( ) )
						rights += User.STUDENT;
					userLogin.setRights( rights );
					userLogin.setLogin( myProfileForm.getLogin( ) );
				}
				if(myProfileForm.getPassword()!= null && myProfileForm.getPassword().length() > 1)
				{
					/*Wenn in der Profile.jsp das alte Passwort richtig ist, und das Neue sowie das Again Passwort ?bereinstimmen
					* wird das neue Passwort ?bernommen
					*
					*
					**/
					if(userLogin.checkPassword(myProfileForm.getPassword())&& (myProfileForm.getNewPassword().equals(myProfileForm.getAgainPassword())))
					{
						userLogin.setPassword(User.cryptLikePW(myProfileForm.getNewPassword()));
					}
					else
					{

						myProfileForm.initValues( request );
						myProfileForm.setPassError(this.sPassError);
						return mapping.findForward("ProfileUnsuccesful");
					}
				}

			if(!manager.updateUser(userLogin))
			{
				myProfileForm.initValues( request );
				return mapping.findForward("ProfileUnsuccesful");
			}
			myProfileForm.initValues( request );
			myProfileForm.setSucces(this.sSucces);
			forward = mapping.findForward("ProfileSuccesful");
		}
		catch(SingletonException se){
			Log.addLog( "Can't change Profile because of a bad or not initialized Singleton: " + se );
			forward = mapping.findForward( "GeneralFailure" );
		}
		return forward;
	}

}