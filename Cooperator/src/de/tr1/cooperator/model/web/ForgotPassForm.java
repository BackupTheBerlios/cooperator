/*
File:		ForgotPassForm.java
Created:	05-06-12@11:00
Task:		This is the Form-Bean for the forgotPass.jsp-site
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


package de.tr1.cooperator.model.web;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import java.util.regex.*;
import java.util.*;

import de.tr1.cooperator.manager.mainframe.UserManager;
import de.tr1.cooperator.manager.system.Log;
import de.tr1.cooperator.model.mainframe.User;
import de.tr1.cooperator.exceptions.*;
import de.tr1.cooperator.manager.ManagerInitializer;
import de.tr1.cooperator.manager.web.*;
import de.tr1.cooperator.model.web.*;
/**
 * This is the Form-Bean for the forgotPass.jsp-site
 * This stores all the date of the formular and verifys for correct input
 *
 * @author Maik Mueller
 * @version 05-06-12@11:00
 *
 */

public class ForgotPassForm extends Accessable
{
	private String sPersonalNumber;
	private String sLogin;
	
	private String sEmailError;
	private String sUserError;
	
//	get-Methoden

	public String getPersonalNumber()
	{
		return this.sPersonalNumber;
	}
	public String getLogin()
	{
		return this.sLogin;
	}
	
	public String getEmailError()
	{
		return this.sEmailError;
	}
	public String getUserError()
	{
		return this.sUserError;
	}
	
//	set-Methoden

	public void setPersonalNumber(String sPersonalNumber)
	{
		this.sPersonalNumber = sPersonalNumber;
	}
	public void setLogin(String sLogin)
	{
		this.sLogin = sLogin;
	}
	
	public void setEmailError(String error)
	{
		this.sEmailError = error;
	}
	public void setUserError(String error)
	{
		this.sUserError = error;
	}
	
//	Ende getter/setter

	synchronized public void initValues( HttpServletRequest request )
	{
		super.initValues(request);
		
		sPersonalNumber	= "";
		sLogin			= "";
		
		sEmailError		= "";
		sUserError		= "";




	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
 	{
		ActionErrors errors = new ActionErrors();
	

			if(( this.getPersonalNumber()== null) || ( this.getPersonalNumber().length()<1))
			{
				errors.add("forgotPass.PersonalNumber", new ActionMessage("forgotPass.pnempty"));
			}




			if(( this.getLogin()== null) || ( this.getLogin().length()<1))
			{
				errors.add("forgotPass.Login", new ActionMessage("forgotPass.lnempty"));
			}



		return errors;
	}

}