/*
File:		RegisterAction.java
Created:	05-06-07@12:00
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


import de.tr1.cooperator.manager.system.GlobalVarsManager;
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

import java.util.*;



/**
 * This class handles the actions called by the struts-framework
 *
 * @author Maik Mueller
 * @version 05-06-07@12:00
 */
public class RegisterAction extends Action
{
	private final String sEmailError 		= "Ihr Passwort konnte nicht gesendet werden bitte wenden sie sich an den ADMIN" ;
	private final String sAdminEmailError	= "Beim Passwort versenden ist ein Fehler aufgetreten";
	private final String sUserError			= "Bitte anderen Usernamen wählen";
	//private final String sFrom			= "Cooperator" ;
	//private final String sSubject			= "Cooperator Anmeldung" ;
	private final String sText				= "Ihr Passwort: " ;


	/**
	 * Methode zur Passwort Generierung
	 * @param length länge des Passwortes
	 */
	private String generatePW(int length)
	{
		Random rand 		= new Random(System.currentTimeMillis());
		StringBuffer buffer = new StringBuffer(length);

		for(int i=0;i<length;i++)
		{
			int temp = rand.nextInt(2);
			if(temp==0)
			{
				buffer.append((char)(48+rand.nextInt(10)));
			}
			if(temp==1)
			{
				buffer.append((char)(65+rand.nextInt(26)));
			}
			if(temp==2)
			{
				buffer.append((char)(97+rand.nextInt(26)));
			}
		}
		return buffer.toString();
	}
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
		ActionForward forward = mapping.findForward( "RegisterUnsuccesful" );

		RegisterForm myRegisterForm = (RegisterForm) form;

		try
		{


			UserManager manager		= UserManager.getInstance();
			GlobalVarsManager vars	= GlobalVarsManager.getInstance();

			String sPass 	= this.generatePW(7);
			String eMail	= myRegisterForm.getEmailAddress();
			if ( myRegisterForm.getUseDropDown() )
			{	// force hosts, so grab it
				Iterator it = myRegisterForm.getUserHosts().iterator();
				while ( it.hasNext() )
				{
					DropDownEntry dde = (DropDownEntry)(it.next());
					if ( dde.getValue()==myRegisterForm.getUsedHost() )
					{
						eMail += "@"+dde.getLabel();
						break;
					}
				}
			}
			User userLogin	= new User(
                myRegisterForm.getLogin(),User.cryptLikePW(sPass),myRegisterForm.getRights(),
				myRegisterForm.getPersonalNumber(),myRegisterForm.getFirstName(),
				myRegisterForm.getSurname(), eMail );



			if(!manager.addUser(userLogin))
			{
				myRegisterForm.initValues( request );
				myRegisterForm.setUserError(this.sUserError);
				return mapping.findForward("RegisterUnsuccesful");

			}

			if(!EmailSender.send( vars.getSystemMail(), userLogin.getEmailAddress(),
								  vars.getRegisterSubject(),this.sText+sPass))
			{
				myRegisterForm.initValues( request );
				if(myRegisterForm.hasAdminRights())
				{
					myRegisterForm.setEmailError(this.sAdminEmailError);
				}
				else
				{
					myRegisterForm.setEmailError(this.sEmailError);
				}
				return mapping.findForward("RegisterUnsuccesful");
			}
			if(myRegisterForm.hasAdminRights())
			{
				myRegisterForm.initValues( request );
				forward = mapping.findForward("NewUserSuccesful");
			}
			else
			{
				myRegisterForm.initValues( request );
				forward = mapping.findForward("RegisterSuccesful");
			}
		}

		catch(SingletonException se)
		{
			Log.addLog( "Can't do Register because of a bad or not initialized Singleton: " + se );
			forward = mapping.findForward( "GeneralFailure" );
		}
		catch(IllegalArgumentException iae)
		{
			Log.addLog( "Can't do ForgotPass because of a bad or not initialized Singleton: " + iae );
			forward = mapping.findForward( "GeneralFailure" );
		}
		return forward;
	}

}