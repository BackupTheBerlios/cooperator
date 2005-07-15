/*
File:		Log.java
Created:	05-05-24@10:00
Task:		Will be a container for session-login-datas
Author:		Sebastian Kohl

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

/**
 * Will be a container for session-login-datas
 *
 * @author Sebastian Kohl
 * @version 05-05-24@10:00
 *
 */
public class UserSession
{
	private HttpSession	hSession;
	private String		sUserLogin;
	private long		lLoginTime;

	/**
	 * Constructor accepting all private Datas
	 * @param Session
	 * @param UserLogin
	 * @param LoginTime
	 */
	public UserSession( HttpSession Session, String UserLogin, long LoginTime )
	{
		this.hSession	= Session;
		this.sUserLogin = UserLogin;
		this.lLoginTime = LoginTime;
	}

	/**
	 * Sets the session
	 * @param Session
	 */
	public void setSession( HttpSession Session )
	{
		this.hSession	= Session;
	}

	/**
	 * Sets the UserLogin
	 * @param UserLogin
	 */
	public void setUserLogin( String UserLogin )
	{
		this.sUserLogin = UserLogin;
	}

	/**
	 * Sets the login-time
	 * @param LoginTime
	 */
	public void setLoginTime( long LoginTime )
	{
		this.lLoginTime = LoginTime;
	}

	/**
	 * Returns the session
	 * @return Session
	 */
	public HttpSession getSession( )
	{
		return this.hSession;
	}

	/**
	 * Returns the sessionId as String
	 * @return String
	 */
	public String getSessionId( )
	{
		String sSessionId = "" + this.hSession.getId( );
		return sSessionId;
	}

	/**
	 * Returns the UserLogin
	 * @return UserLogin
	 */
	public String getUserLogin( )
	{
		return this.sUserLogin;
	}

	/**
	 * Returns the login-time
	 * @return LoginTime
	 */
	public long getLoginTime( )
	{
		return this.lLoginTime;
	}
}