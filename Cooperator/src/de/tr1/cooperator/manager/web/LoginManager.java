/*
File:		LoginManager.java
Created:	05-05-24@11:00
Task:		Will be a singleton and manage login-datas
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

package de.tr1.cooperator.manager.web;

import java.util.Date;
import java.util.*;
import java.text.SimpleDateFormat;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import de.tr1.cooperator.exceptions.*;
import de.tr1.cooperator.manager.system.Log;
import de.tr1.cooperator.model.web.*;

import javax.servlet.http.HttpSession;

/**
 * Will be a singleton and handle all login-datas globally
 *
 * @author Sebastian Kohl
 * @version 05-05-24@12:00
 *
 */

public class LoginManager
{
	private static	LoginManager	hInstance	= null;
	private			LinkedList		hUserList	= null;
	private			long			lLifeTime	= 20*60*1000;	// 20 Minutes
	private	final	boolean			DEBUGMODE	= false;

	/**
	 * Will create the needed instance, but throw a SingletonException, if already instanciated
	 */
	protected LoginManager( ) throws SingletonException
	{
		if ( hInstance != null )
			throw new SingletonException( );
		this.hUserList	= new LinkedList( );
		hInstance		= this;
	}

	/**
	 * Will return the valid singleton-instance of this class and init it, if not done already
	 * @return LoginManager
	 */
	synchronized public static LoginManager getInstance( ) throws SingletonException
	{
		if ( hInstance == null )
			hInstance = new LoginManager( );
		return hInstance;
	}

	/**
	 * Will add a session to the manager, if not already existent or expired
	 * @param USession
	 * @return boolean
	 */
	synchronized public boolean add( UserSession USession )
	{
		try
		{
			// update all sessions in the list
			this.updateAll( );

			if ( USession == null )
				return false;

			// check if new session is valid or already expired
			long lTime = new Date().getTime();
			if ( USession.getLoginTime()+this.lLifeTime < lTime )
				return false;

			// check, if this session-id is not already in use
			String usessid = USession.getSession().getId().toString();
			Iterator i = this.hUserList.iterator();
			while ( i.hasNext() )
			{
				UserSession usess = (UserSession)i.next();
				if ( usess.getSession().getId().toString().equals(usessid) )
					return false;
			}
			USession.getSession().setMaxInactiveInterval( (int)this.lLifeTime );
		}
		catch (IllegalStateException ie)
		{
			return false;
		}
		// ok, this session seams to be valid, so enter it
		this.hUserList.add( USession );
		Log.addLog("User \""+USession.getUserLogin()+"\" logged in with sess-id "+USession.getSession().getId().toString() );
		return true;
	}

	/**
	 * Will check, if a session exists in the manager
	 * @param USession
	 * @return boolean
	 */
	public boolean isValid( UserSession USession )
	{
		if ( this.DEBUGMODE )
			return true;
		else
			return this.isValid( USession, true, false, false, true );
	}

	/**
	 * Will check for a session and remove and kill it, if wanted.<br>A session kann not be killed without removing!<br>Also an update of all sessions will be made if requested.
	 */
	synchronized protected boolean isValid( UserSession USession, boolean UpdateAll, boolean RemoveIt, boolean KillIt, boolean UpdateTime )
	{
		// first update all and kick expired sessions
		this.updateAll( );

		if ( USession == null)
			return false;

		// check, if this session is in the list
		String usessid = USession.getSession().getId().toString();
		String ulogin  = USession.getUserLogin();
		Iterator i = this.hUserList.iterator();
		while ( i.hasNext() )
		{
			UserSession usess = (UserSession)i.next();
			try
			{
				if ( usess.getSession().getId().toString().equals(usessid) )
				{
					// ok, the session.id is here, but is there also the name the same?
					if ( usess.getUserLogin().equals( ulogin ) )
					{
						if ( RemoveIt )
						{
							if ( this.hUserList.remove( usess ) == false )
								return false;
							if ( KillIt )
							{
								Log.addLog("Session killed: "+ulogin);
								usess.getSession().invalidate();
							}
						}
						if ( UpdateTime )
						{
							long lTime = new Date( ).getTime( );
							usess.setLoginTime( lTime );
						}
						return true;
					}
					else
						return false;
				}
			}
			catch (IllegalStateException ie)
			{
				// if some session was invalid, then remove it
				this.hUserList.remove( usess );
			}
		}
		// not found, so give false back to the caller
		return false;
	}

	/**
	 * Will kill a session, if exists
	 * @param USession
	 * @return boolean
	 */
	synchronized public boolean kill( UserSession USession )
	{
		boolean result = this.isValid( USession, false, true, true, false );
		this.updateAll( );
		return result;
	}

	/**
	 * Will kill and remove all sessions
	 */
	synchronized public void killAll( )
	{
		Iterator i = this.hUserList.iterator( );
		while ( i.hasNext( ) )
		{
			UserSession usess = (UserSession)i.next( );
			Log.addLog("Session killed: "+usess.getUserLogin() );
			try
			{
				usess.getSession( ).invalidate( );
			}
			catch ( IllegalArgumentException ie )
			{	// ignore an already expired session
			}
		}
		this.hUserList.clear( );
	}

	/**
	 * This will return all saved Sessions in an array
	 */
	public UserSession[] getAll( )
	{
		return (UserSession[])this.hUserList.toArray( );
	}


	/**
	 * This will return all saved Sessions in an linked list
	 */
	public Collection getAllAsCollection( )
	{
		return this.hUserList;
	}

	/**
	 * Here all sessions are traced and killed, if expired due to an old date
	 */
	synchronized public void updateAll( )
	{
		Iterator i = this.hUserList.iterator( );
		long lTime = new Date( ).getTime( );
		while ( i.hasNext( ) )
		{
			UserSession usess = (UserSession)i.next( );

			try
			{
				// try to read the id. on exception, the session expired and should be removed
				String id = usess.getSession().getId().toString();
				// check if the session is already expired
				if ( usess.getLoginTime( )+this.lLifeTime < lTime )
				{
					this.hUserList.remove( usess );
					Log.addLog("Session killed: "+usess.getUserLogin() );
					usess.getSession( ).invalidate( );
				}
			}
			catch ( IllegalArgumentException ie )
			{	// if the real session expired, ignore it
				this.hUserList.remove( usess );
			}
		}
	}

	/**
	 * Sets the specific LifeTime of the sessions
	 * @param LifeTime
	 */
	public void setLifeTime( long LifeTime )
	{
		this.lLifeTime = LifeTime;
	}

	/**
	 * Returns the LifeTime of the sessions
	 * @return LifeTime
	 */
	public long getLifeTime( )
	{
		return this.lLifeTime;
	}

}