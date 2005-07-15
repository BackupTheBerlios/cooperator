/*
File:		ForgotPassAction.java
Created:	05-06-12@11:00
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
import de.tr1.cooperator.manager.system.GlobalVarsManager;
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


import java.util.Random;


/**
 * This class handles the actions called by the struts-framework
 *
 * @author Maik Mueller
 * @version 05-06-12@11:00
 */
public class ForgotPassAction extends Action
{
	private final int length = 7 ;
	private final String sEmailError 	= "Ihr Passwort konnte nicht gesendet werden bitte wenden sie sich an den ADMIN" ;
	private final String sUserError		= "Username existiert nicht";
	private final String sNumberError	= "Personal Number/ Matrikel Nummer ist falsch";
	//private final String sFrom			= "Cooperator" ;
	private final String sSubject		= "Ihr neues Password am Cooperator" ;
	private final String sText			= "Ihr Passwort: " ;
	private final String sSucces		= "Ihr Passwort wurde gesendet";


	/**
	 * Methode zur Passwort Generierung
	 * @param length l?nge des Passwortes
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
		ActionForward forward = mapping.findForward( "ForgotPassUnsuccesful" );

		ForgotPassForm myForgotForm = (ForgotPassForm) form;

		try{

			String sTempPass;
			String sOldPass;
			UserManager manager		= UserManager.getInstance();
			GlobalVarsManager vars	= GlobalVarsManager.getInstance();

			User userLogin = manager.getUser(myForgotForm.getLogin());
			if(userLogin	==	null)
			{
				myForgotForm.initValues( request );
				myForgotForm.setUserError(this.sUserError);
				return mapping.findForward("ForgotPassUnsuccesful");
			}
			if(!myForgotForm.getPersonalNumber().equals(userLogin.getPersonalNumber()))
			{
				myForgotForm.initValues( request );
				myForgotForm.setUserError(this.sNumberError);
				return mapping.findForward("ForgotPassUnsuccesful");
			}

			sOldPass	= userLogin.getPassword();
			sTempPass	= generatePW(length);

			userLogin.setPassword(User.cryptLikePW(sTempPass));


			if(!manager.updateUser(userLogin) )
			{
				myForgotForm.initValues( request );
				myForgotForm.setEmailError(this.sEmailError);
				return mapping.findForward("ForgotPassUnsuccesful");
			}
			if(!EmailSender.send(vars.getSystemMail(),userLogin.getEmailAddress(),this.sSubject,this.sText+sTempPass))
			{
			myForgotForm.initValues( request );
			userLogin.setPassword(sOldPass);
			manager.updateUser(userLogin);
			myForgotForm.setEmailError(this.sEmailError);
			return mapping.findForward("ForgotPassUnsuccesful");
			}

			myForgotForm.setUserError(this.sSucces);
			forward = mapping.findForward("ForgotPassSuccesful");

		}

		catch(SingletonException se)
		{
			Log.addLog( "Can't do ForgotPass because of a bad or not initialized Singleton: " + se );
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