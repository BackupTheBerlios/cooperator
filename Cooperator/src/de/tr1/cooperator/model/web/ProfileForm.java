/*
File:		ProfileForm.java
Created:	05-05-31@10:00
Task:		This is the Form-Bean for the Profile.jsp-site
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
 * This is the Form-Bean for the Profile.jsp-site
 * This stores all the date of the formular and verifys for correct input
 *
 * @author Maik Mueller
 * @version 05-05-31@10:00
 *
 */

public class ProfileForm extends Accessable
{
	private String sFirstName;
	private String sSurname;
	private String sPersonalNumber;
	private String sLogin;
	private String sEmailAddress;
	private String sPassword;
	private String sNewPassword;
	private String sAgainPassword;
	private int iRights;
	private String sPassError;
	private String sSucces;

	private boolean bAdminRights;
	private boolean bUserRights;
	private boolean bLectRights;
	private boolean bAdminNewRights;
	private boolean bUserNewRights;
	private boolean bLectNewRights;
	//attributes if admin wants to edit an userProfile
	private boolean sUserLogin;
	private User hTempUser;
	private boolean bEditFlag;

//get-Methoden

	public User getTempUser()
	{
		return this.hTempUser;
	}

	public boolean getEditFlag()
	{
		return this.bEditFlag;
	}

	public String getPassError()
	{
		return this.sPassError;
	}
	public String getSucces()
	{
		return this.sSucces;
	}
	public boolean getAdminRights()
	{
		return this.bAdminRights;
	}
	public boolean getStudentRights()
	{
		return this.bUserRights;
	}
	public boolean getLectRights()
	{
		return this.bLectRights;
	}
	public boolean getNewAdminRights()
	{
		return this.bAdminNewRights;
	}
	public boolean getNewStudentRights()
	{
		return this.bUserNewRights;
	}
	public boolean getNewLectRights()
	{
		return this.bLectNewRights;
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
	public String getPassword()
	{
		return sPassword;
	}
	public String getNewPassword()
	{
		return sNewPassword;
	}
	public String getAgainPassword()
	{
		return sAgainPassword;
	}
	public int getRights(){
		return this.iRights;
	}
	public String getRightsAsString()
	{
		return this.hUser.getRightsAsString();
	}
	public String getIsAdmin()
	{
		return ""+this.hasAdminRights();
	}
	public String getIsLecturer()
	{
		return ""+this.hasLecturerRights();
	}
	public String getIsNoStudent()
	{
		return ""+(this.hasAdminRights() || this.hasLecturerRights());
	}
	public String getIsStudent()
	{
		return ""+this.hasStudentRights();
	}
	public String getIsOnlyStudent()
	{
		return ""+!(this.hasAdminRights() || this.hasLecturerRights());
	}
//set-Methoden
	public void setEditFlag(boolean set)
	{
		this.bEditFlag = set;
	}
	public void setAdminRights(boolean set)
	{
		this.bAdminRights = set;
		this.bAdminNewRights = set;
	}
	public void setLectRights(boolean set)
	{
		this.bLectRights = set;
		this.bLectNewRights = set;
	}
	public void setStudentRights(boolean set)
	{
		this.bUserRights = set;
		this.bUserNewRights = set;
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
	public void setPassword(String sPassword)
	{
		this.sPassword = sPassword.trim();
	}
	public void setNewPassword(String sNewPassword)
	{
		this.sNewPassword = sNewPassword.trim();
	}
	public void setAgainPassword(String sAgainPassword)
	{
		this.sAgainPassword = sAgainPassword.trim();
	}
	public void setRights(int iRights)
	{
		this.iRights = iRights;
	}
	public void setPassError(String error)
	{
		this.sPassError = error;
	}
	public void setSucces(String sSucces)
	{
		this.sSucces = sSucces;
	}
//Ende getter/setter

	synchronized public void initValues( HttpServletRequest request )
	{
		super.initValues(request);

		String sUserLogin =  request.getParameter( "login" );
		if( sUserLogin == null )
		{
			sFirstName 		= this.getUserFirstName();
			sSurname 		= this.getUserSurname();
			sPersonalNumber	= this.getUserPersonalNumber();
			sLogin			= this.getUserLogin();
			sEmailAddress	= this.getUserEmailAddress();
			iRights			= 0;

			sPassword		= null;
			sNewPassword	= null;
			sAgainPassword	= null;
			bAdminRights	= this.hasAdminRights();
			bLectRights		= this.hasLecturerRights();
			bUserRights		= this.hasStudentRights();

			sPassError	= "";
			sSucces		= "";

			bAdminNewRights	= false;
			bLectNewRights	= false;
			bUserNewRights	= false;

			bEditFlag		= false;
		}
		else
		{
			if(this.hasAdminRights())
			{
				UserManager myUserManager;

				try
				{
					myUserManager = UserManager.getInstance();
				}
				catch(SingletonException se)
				{
					Log.addLog( "Can't change Profile because of a bad or not initialized Singleton: " + se );
					this.sErrorMessage += "UserManager nicht bereit";
					return;
				}

				hTempUser = myUserManager.getUser( sUserLogin );
				if( hTempUser == null )
				{
					this.sErrorMessage += "User existiert nicht";
					return;
				}

				sFirstName 	= this.hTempUser.getFirstName();
				sSurname	= this.hTempUser.getSurname();
				sPersonalNumber	= this.hTempUser.getPersonalNumber();
				sLogin		= this.hTempUser.getLogin();
				sEmailAddress	= this.hTempUser.getEmailAddress();
				iRights		= 0;

				sPassword	= null;
				sNewPassword	= null;
				sAgainPassword	= null;
				bAdminRights	= (this.hTempUser.getRights( ) & User.ADMIN) != 0;
				bLectRights	= (this.hTempUser.getRights( ) & User.LECTURER) != 0;
				bUserRights	= (this.hTempUser.getRights( ) & User.STUDENT) != 0;

				sPassError	= "";
				sSucces		= "";

				bAdminNewRights	= false;
				bLectNewRights	= false;
				bUserNewRights	= false;

				bEditFlag	= true;
			}
			else
			{
				this.sErrorMessage += "User has no Admin Rights";
				return;
			}
		}
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
 	{
		ActionErrors errors = new ActionErrors();
		// Check email with regular expressions
		// to be validated.
	/*	Pattern pat		= Pattern.compile("[a-zA-Z0-9äüöß_\\-\\.]+@[\\.a-zA-Z0-9äüöß\\-_]+\\.[a-zA-Z]{2,5}");
		Matcher matcher	= pat.matcher(this.sEmailAddress);
		boolean flag	= matcher.matches();
	//**/
		//wenn ein passwort eingegeben wurde sollte das neue- und das againPasswort nicht leer sein

		if ( (this.sPassword != null  && this.sPassword.length() >1 ) && ((this.sAgainPassword.length()<1)|| (this.sAgainPassword == null)


									  || (this.sNewPassword.length()<1)|| (this.sNewPassword == null) ))
			errors.add( "profile.newPassword", new ActionMessage( "profile.pwempty" ) );

		if(( this.getFirstName()== null) || ( this.getFirstName().length()<1))
			errors.add("profile.FirstName", new ActionMessage("profile.fnempty"));

		if(( this.getEmailAddress()== null) || ( this.getEmailAddress().length()<1))
			errors.add("profile.EmailAddress", new ActionMessage("profile.emempty"));
		Pattern pat		= Pattern.compile("[a-zA-Z0-9öäüß_\\-\\.]+@[\\.a-zA-Z0-9öäüß\\-_]+\\.[a-zA-Z]{2,5}");
		Matcher matcher	= pat.matcher(this.getEmailAddress());
		boolean flag	= matcher.matches();
		if  (!flag)
			errors.add("profile.EmailInvalid", new ActionMessage("profile.emailnotvalid"));


		if(( this.getSurname()== null) || ( this.getSurname().length()<1))
			errors.add("profile.Surname", new ActionMessage("profile.snempty"));

		//only checked if user is admin or docent
		if(this.hUser.getRights() >= 2)
		{
			if(( this.getPersonalNumber()== null) || ( this.getPersonalNumber().length()<1))
			{
				errors.add("profile.PersonalNumber", new ActionMessage("profile.pnempty"));
			}
		}
		else
		{
			Pattern numpat		= Pattern.compile("[a-zA-Z0-9_\\-\\(\\)\\.#\\\\/\\ ]+");
			Matcher nummatcher	= numpat.matcher(this.getPersonalNumber());
			boolean numflag		= nummatcher.matches();
			if ( !numflag )
				errors.add("register.PersonalNumber", new ActionMessage("register.pnfalse"));
		}

		//only checked if user is admin
		if(this.hUser.getRights() == 4)
		{
			if(( this.getLogin()== null) || ( this.getLogin().length()<1))
			{
				errors.add("profile.Login", new ActionMessage("profile.lnempty"));
			}


		}
		return errors;
	}
}