/*
File:		RegisterForm.java
Created:	05-06-07@12:00
Task:		This is the Form-Bean for the Register.jsp-site
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
import de.tr1.cooperator.manager.system.GlobalVarsManager;
import de.tr1.cooperator.manager.system.Log;
import de.tr1.cooperator.model.mainframe.User;
import de.tr1.cooperator.exceptions.*;
import de.tr1.cooperator.manager.ManagerInitializer;
import de.tr1.cooperator.manager.web.*;
import de.tr1.cooperator.model.web.*;
/**
 * This is the Form-Bean for the Register.jsp-site
 * This stores all the date of the formular and verifys for correct input
 *
 * @author Maik Mueller
 * @version 05-06-07@12:00
 *
 */

public class RegisterForm extends Accessable
{
	private String	sFirstName;
	private String	sSurname;
	private String	sPersonalNumber;
	private String	sLogin;
	private String	sEmailAddress;


	private int 	iRights;
	private String 	sEmailError;
	private String	sUserError;
	protected	boolean		bUseDropDown;
	protected	Collection	cUserHosts;
	protected	int			iUsedHost;


//get-Methoden
	public	boolean	getUseDropDown( )
	{
		return this.bUseDropDown;
	}

	public	int	getUsedHost( )
	{
		return this.iUsedHost;
	}

	public	Collection	getUserHosts( )
	{
		return this.cUserHosts;
	}

	public String getFirstName()
	{
		return this.sFirstName;
	}
	public String getSurname()
	{
		return this.sSurname;
	}
	public String getPersonalNumber()
	{
		return this.sPersonalNumber;
	}
	public String getLogin()
	{
		return this.sLogin;
	}
	public String getEmailAddress()
	{
		return this.sEmailAddress;
	}

	public int getRights()
	{
		return this.iRights;
	}
	public String getEmailError()
	{
		return this.sEmailError;
	}
	public String getUserError()
	{
		return this.sUserError;
	}

//set-Methoden
	public	void setUsedHost( int hostNr )
	{
		this.iUsedHost = hostNr;
	}

	public void setFirstName(String sFirstName)
	{
		this.sFirstName = sFirstName.trim();
	}
	public void setSurname(String sSurname)
	{
		this.sSurname = sSurname.trim();
	}
	public void setPersonalNumber(String sPersonalNumber)
	{

		this.sPersonalNumber = sPersonalNumber.trim();

	}
	public void setLogin(String sLogin)
	{

		this.sLogin = sLogin.trim();

	}
	public void setEmailAddress(String sEmailAddress)
	{
		this.sEmailAddress = sEmailAddress.trim();
	}

	public void setRights(int iRights)
	{
		this.iRights = iRights;
	}

	public void setEmailError(String error)
	{
		this.sEmailError = error;
	}
	public void setUserError(String error)
	{
		this.sUserError = error;
	}
//Ende getter/setter
	/**
	 * This Method sets the start Values for the Form Bean
	 * @param request the Servlet Request
	 */
	synchronized public void initValues( HttpServletRequest request )
	{
		super.initValues(request);
		sFirstName 		= "";
		sSurname 		= "";
		sPersonalNumber	= "";
		sLogin			= "";
		sEmailAddress	= "";
		iRights			= 1;
		sEmailError		= "";
		sUserError		= "";
		this.iUsedHost	= 0;
		this.bUseDropDown	= GlobalVarsManager.getInstance().forceUserMails();
		this.cUserHosts		= new ArrayList();
		Collection hosts	= GlobalVarsManager.getInstance().getUserMails();
		Iterator it = hosts.iterator();
		int i = 0;
		while ( it.hasNext() )
		{
			String host = (String)(it.next());
			DropDownEntry dde = new DropDownEntry( i++, host, null );
			this.cUserHosts.add( dde );
		}
	}



	/**
	 * This Methode checks if the Input Values of the Form Bean are valid
	 * @return Input Value Errors
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
 	{
		ActionErrors errors = new ActionErrors();
		// Check email with regular expressions
		// to be validated.
	/*	Pattern pat		= Pattern.compile("[a-zA-Z0-9οδόφί_\\-\\.]+@[\\.a-zA-Z0-9δόφί\\-_]+\\.[a-zA-Z]{2,5}");
		Matcher matcher	= pat.matcher(this.sEmailAddress);
		boolean flag	= matcher.matches();
	//**/



		if(( this.getFirstName()== null) || ( this.getFirstName().length()<1))
			errors.add("register.FirstName", new ActionMessage("register.fnempty"));

		if(( this.getEmailAddress()== null) || ( this.getEmailAddress().length()<1))
			errors.add("register.EmailAddress", new ActionMessage("register.emempty"));


		if ( !this.bUseDropDown )
		{
			Pattern pat		= Pattern.compile("[a-zA-Z0-9δόφί_\\-\\.]+@[\\.a-zA-Z0-9δόφί\\-_]+\\.[a-zA-Z]{2,5}");
			Matcher matcher	= pat.matcher(this.getEmailAddress());
			boolean flag	= matcher.matches();
			if  (!flag)
			{
				errors.add("register.EmailInvalid", new ActionMessage("register.emailnotvalid"));
			}
		}
		else
		{
			Pattern pat		= Pattern.compile("[a-zA-Z0-9δόφί_\\-\\.]+");
			Matcher matcher	= pat.matcher(this.getEmailAddress());
			boolean flag	= matcher.matches();
			if  (!flag)
			{
				errors.add("register.EmailInvalid", new ActionMessage("register.emailnotvalid"));
			}
		}


		if(( this.getSurname()== null) || ( this.getSurname().length()<1))
		{
			errors.add("register.Surname", new ActionMessage("register.snempty"));
		}

		if(( this.getPersonalNumber()== null) || ( this.getPersonalNumber().length()<1))
		{
			errors.add("register.PersonalNumber", new ActionMessage("register.pnempty"));
		}
		else
		{
			Pattern pat		= Pattern.compile("[a-zA-Z0-9_\\-\\(\\)\\.#\\\\/\\ ]+");
			Matcher matcher	= pat.matcher(this.getPersonalNumber());
			boolean flag	= matcher.matches();
			if ( !flag )
				errors.add("register.PersonalNumber", new ActionMessage("register.pnfalse"));
		}

		if(( this.getLogin()== null) || ( this.getLogin().length()<1))
		{
			errors.add("register.Login", new ActionMessage("register.lnempty"));
		}
		else
		{
			Pattern pat		= Pattern.compile("[a-z_]{3,20}");
			Matcher matcher	= pat.matcher(this.getLogin());
			boolean flag	= matcher.matches();
			if ( !flag )
				errors.add("register.Login", new ActionMessage("register.lnfalse"));
		}



		return errors;
	}
}