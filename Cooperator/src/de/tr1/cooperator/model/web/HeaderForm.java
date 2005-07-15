/*
File:		HeaderForm.java
Created:	05-05-31@18:00
Task:		This is for the menu in the header
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

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import de.tr1.cooperator.manager.mainframe.UserManager;
import de.tr1.cooperator.manager.web.LoginManager;
import de.tr1.cooperator.manager.system.Log;
import de.tr1.cooperator.model.mainframe.User;
import de.tr1.cooperator.exceptions.*;

import java.util.Date;
/**
 * This is the Form-Bean for the Profile.jsp-site
 * This stores all the date of the formular and verifys for correct input
 *
 * @author Peter Matjeschk
 * @version 05-06-06@19:00
 *
 */

public class HeaderForm extends Accessable
{
	private boolean bShowLogin;
	private boolean bShowLogout;
	private boolean bShowProfile;
	private boolean bShowGlobalSettings;
	private boolean bShowEvents;
	private boolean bShowUsers;

	private User	uUser;

	synchronized public void initValues( HttpServletRequest request )
	{
		super.initValues( request );

		uUser = this.getUser();
		if( uUser != null )
		{
			bShowLogin = false;
			bShowLogout = true;
			bShowProfile = true;

			if( (uUser.getRights() & User.ADMIN ) > 0 )
			{
				bShowGlobalSettings = true;
				bShowUsers = true;
			}
			else
			{
				bShowGlobalSettings = false;
				bShowUsers = false;
			}
			
			bShowEvents = true;
		}
		else
		{
			bShowLogin = true;
			bShowLogout = false;
			bShowProfile = false;
			bShowGlobalSettings = false;
			bShowEvents = false;
			bShowUsers = false;
		}
	}

	public String getShowLogin()
	{
		return ""+bShowLogin;
	}
	public String getShowLogout()
	{
		return ""+bShowLogout;
	}
	public String getShowProfile()
	{
		return ""+bShowProfile;
	}
	public String getShowGlobalSettings()
	{
		return ""+bShowGlobalSettings;
	}
	public String getShowEvents()
	{
		return ""+bShowEvents;
	}
	public String getShowUsers()
	{
		return ""+bShowUsers;
	}
}