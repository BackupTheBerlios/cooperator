/*
File:		GlobalVarsManager.java
Created:	05-06-05@10:30
Task:		Will be a singleton and manage all global values needed for the plattform
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
package de.tr1.cooperator.manager.system;

import java.util.*;
import java.text.SimpleDateFormat;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import org.jdom.Element;

import de.tr1.cooperator.exceptions.*;
import de.tr1.cooperator.manager.system.Log;
import de.tr1.cooperator.manager.mainframe.XMLCore;
/**
 * Will be a singleton and manage all global values needed for the plattform *
 * @author Sebastian Kohl
 * @version 05-06-05@11:30
 *
 */

public class GlobalVarsManager
{
	public  static	final String		XML_VARS			=	"vars";
	public  static	final String		XML_GLOBAL			=	"global";
	public  static	final String		XML_GLOBAL_FORCE_EMAILS_ON_USER		=	"forceEMailsOnUsers";
	public  static	final String		XML_GLOBAL_FORCE_EMAILS_ON_LECTURER	=	"forceEMailsOnLecturers";
	public  static	final String		XML_GLOBAL_SYSTEM_MAIL				=	"systemMailAddress";
	public  static	final String		XML_GLOBAL_REGISTER_SUBJECT			=	"registerSubject";
	public  static	final String		XML_USER_MAIL		=	"usermail";
	public  static	final String		XML_LECTURER_MAIL	=	"lectmail";
	public  static	final String		XML_MAIL			=	"mail";
	public  static	final String		XML_BOOLEAN_TRUE	=	"true";
	public  static	final String		XML_BOOLEAN_FALSE	=	"false";

	private static	GlobalVarsManager	hInstance	= null;
	private			XMLCore				hCore		= null;

	/**
	 * Will create the needed instance, but throw a SingletonException, if already instanciated
	 */
	protected GlobalVarsManager( ) throws SingletonException
	{
		if ( this.hInstance != null )
			throw new SingletonException( );
		hInstance 	= this;
	}

	/**
	 * Will return the valid singleton-instance and set the name of the type-file, if not already done.
	 * @param FilePath
	 * @return GlobalVarsManager
	 */
	synchronized public static GlobalVarsManager getInstance( String FilePath ) throws IllegalArgumentException,
																					  XMLException,
																					  IOException
	{
		if ( hInstance == null )
		{
			hInstance = new GlobalVarsManager( );
			if ( (FilePath == null) || (FilePath.length()<1) )
				throw new IllegalArgumentException("FilePath was null and can not be set!");

			File f = new File( FilePath );
			if( (f.exists() && (!f.canWrite()) && (f.isFile())) )
				throw new IOException();

			if ( !f.exists( ) )
			{
				hInstance.hCore = new XMLCore( FilePath );
				hInstance.hCore.createDomFromScratch( hInstance.XML_VARS );
				Element newElem = new Element( hInstance.XML_GLOBAL );
				newElem.setAttribute( hInstance.XML_GLOBAL_FORCE_EMAILS_ON_USER,	hInstance.XML_BOOLEAN_TRUE );
				newElem.setAttribute( hInstance.XML_GLOBAL_FORCE_EMAILS_ON_LECTURER,hInstance.XML_BOOLEAN_TRUE );
				hInstance.hCore.getHRoot().addContent( newElem );
				if ( !hInstance.hCore.saveDom( ) )
				{
					Log.addLog( "Could not create XML-File: " + FilePath );
					throw new IOException( );
				}
			}
			else
			{
				try
				{
					hInstance.hCore	= new XMLCore( FilePath );
					hInstance.hCore.createDOM( );
				}
				catch ( IOException e )
				{
					Log.addLog( "Bad XML-File: " + FilePath );
					throw new IOException( );
				}
			}
		}
		return hInstance;
	}

	/**
	 * Returns a valid singleton-instance, if it was initalized already. If not, it will throw an IllegalArgumentException!
	 * @return GlobalVarsManager
	 */
	synchronized public static GlobalVarsManager getInstance( ) throws IllegalArgumentException
	{
		if ( hInstance == null )
			throw new IllegalArgumentException("Class GlobalVarsManager was not already initialized. Call getInstance(FilePath) first!");
		return hInstance;
	}

	/**
	 * Will add an email-host for lecturers to the database
	 * @param String the host for the email. This means all after the "@"
	 * @return boolean
	 */
	synchronized public boolean addLecturerMail( String Host )
	{
		if ( (Host==null) || (Host.length()<1) )
			return false;
		return this.addMail( Host, this.XML_LECTURER_MAIL );
	}

	/**
	 * Will add an email-host for users to the database
	 * @param String the host for the email. This means all after the "@"
	 * @return boolean
	 */
	synchronized public boolean addUserMail( String Host )
	{
		if ( (Host==null) || (Host.length()<1) )
			return false;
		return this.addMail( Host, this.XML_USER_MAIL );
	}

	synchronized protected boolean addMail( String Host, String MailType )
	{
		if ( (Host==null) || (Host.length()<1) )
			return false;
		if ( (MailType==null) || (MailType.length()<1) )
			return false;
		Element globals = this.hCore.getHRoot().getChild( this.XML_GLOBAL );
		// is the tag existent?
		if ( globals == null )
		{
			Log.addLog( "ERROR: Database for global variables contains no "+this.XML_GLOBAL+"-tag!");
			return false;
		}
		Element found = this.hCore.find( globals, MailType, this.XML_MAIL, Host );
		// if found, it already exists
		if ( found != null )
			return false;

		// create a new entry
		Element newType = new Element( MailType );
		newType.setAttribute(	this.XML_MAIL, 	Host );

		// pass it to the Core
		globals.addContent( newType );
		// and save the file
		return this.hCore.saveDom( );
	}

	/**
	 * Will remove an email-host for lecturers from the database
	 * @param String the host for the email. This means all after the "@"
	 * @return boolean
	 */
	synchronized public boolean removeLecturerMail( String Host )
	{
		if ( (Host==null) || (Host.length()<1) )
			return false;
		return this.removeMail( Host, this.XML_LECTURER_MAIL );
	}

	/**
	 * Will remove an email-host for users from the database
	 * @param String the host for the email. This means all after the "@"
	 * @return boolean
	 */
	synchronized public boolean removeUserMail( String Host )
	{
		if ( (Host==null) || (Host.length()<1) )
			return false;
		return this.removeMail( Host, this.XML_USER_MAIL );
	}

	synchronized protected boolean removeMail( String Host, String MailType )
	{
		if ( (Host==null) || (Host.length()<1) )
			return false;
		if ( (MailType==null) || (MailType.length()<1) )
			return false;
		Element globals = this.hCore.getHRoot().getChild( this.XML_GLOBAL );
		// is the tag existent?
		if ( globals == null )
		{
			Log.addLog( "ERROR: Database for global variables contains no "+this.XML_GLOBAL+"-tag!");
			return false;
		}
		Element found = this.hCore.find( globals, MailType, this.XML_MAIL, Host );
		// if not found, it does not exists
		if ( found == null )
			return false;

		// pass it to the Core
		globals.removeContent( found );
		// and save the file
		return this.hCore.saveDom( );
	}

	/**
	 * Will return all LecturerHosts in the database
	 * @return Collection Collection of Strings with Hosts (all after the "@")
	 */
	public Collection getLecturerMails( )
	{
		return this.getMails( this.XML_LECTURER_MAIL );
	}

	/**
	 * Will return all UserHosts in the database
	 * @return Collection Collection of Strings with Hosts (all after the "@")
	 */
	public Collection getUserMails( )
	{
		return this.getMails( this.XML_USER_MAIL );
	}

	protected Collection getMails( String MailType )
	{
		Collection res = Collections.synchronizedList( new ArrayList() );
		if ( (MailType==null) || (MailType.length()<1) )
			return res;
		Element globals = this.hCore.getHRoot().getChild( this.XML_GLOBAL );
		// is the tag existent?
		if ( globals == null )
		{
			Log.addLog( "ERROR: Database for global varibales contains no "+this.XML_GLOBAL+"-tag!");
			return res;
		}
		// step all
		Iterator it = globals.getChildren( MailType ).iterator();
		while ( it.hasNext() )
		{
			String host = ((Element)it.next()).getAttributeValue( this.XML_MAIL );
			if ( (host==null) || (host.length()<1) )
			{
				Log.addLog( "ERROR: Database for global variables contains a damaged "+MailType+"-entry!");
				return res;
			}
			res.add( host );
		}
		return res;
	}

	/**
	 * Will return if mail-hosts for users should be forced
	 * @return boolean force or not?
	 */
	public boolean forceUserMails( )
	{
		return this.getBoolean( this.XML_GLOBAL_FORCE_EMAILS_ON_USER );
	}

	/**
	 * Will return if mail-hosts for lecturers should be forced
	 * @return Mail-Address
	 */
	public String getSystemMail( )
	{
		return this.getString( this.XML_GLOBAL_SYSTEM_MAIL );
	}

	/**
	 * Will return if subject of sendet registration-mails
	 * @return String Subject
	 */
	public String getRegisterSubject( )
	{
		return this.getString( this.XML_GLOBAL_REGISTER_SUBJECT );
	}

	/**
	 * Will return if mail-hosts for lecturers should be forced
	 * @return boolean force or not?
	 */
	public boolean forceLecturerMails( )
	{
		return this.getBoolean( this.XML_GLOBAL_FORCE_EMAILS_ON_LECTURER );
	}

	protected boolean getBoolean( String Name )
	{
		if ( (Name==null) || (Name.length()<1) )
			return false;
		Element globals = this.hCore.getHRoot().getChild( this.XML_GLOBAL );
		// is the tag existent?
		if ( globals == null )
		{
			Log.addLog( "ERROR: Database for global variables contains no "+this.XML_GLOBAL+"-tag!");
			return false;
		}
		String val = globals.getAttributeValue( Name );
		if ( (val==null) || (val.length()<1) )
		{
			Log.addLog( "ERROR: Database for global variables contains no entry for "+Name+"!");
			return false;
		}
		return val.equals( this.XML_BOOLEAN_TRUE );
	}

	protected String getString( String Name )
	{
		if ( (Name==null) || (Name.length()<1) )
			return "";
		Element globals = this.hCore.getHRoot().getChild( this.XML_GLOBAL );
		// is the tag existent?
		if ( globals == null )
		{
			Log.addLog( "ERROR: Database for global variables contains no "+this.XML_GLOBAL+"-tag!");
			return "";
		}
		String val = globals.getAttributeValue( Name );
		if ( (val==null) || (val.length()<1) )
		{
			Log.addLog( "ERROR: Database for global variables contains no entry for "+Name+"!");
			return "";
		}
		return val;
	}

	/**
	 * Will set if mail-hosts for users should be forced
	 * @param boolean force or not?
	 * @return boolean successfully set
	 */
	public boolean forceUserMails( boolean Force )
	{
		return this.setBoolean( this.XML_GLOBAL_FORCE_EMAILS_ON_USER, Force );
	}

	/**
	 * Will set if mail-hosts for lecturers should be forced
	 * @param boolean force or not?
	 * @return boolean successfully set
	 */
	public boolean forceLecturerMails( boolean Force )
	{
		return this.setBoolean( this.XML_GLOBAL_FORCE_EMAILS_ON_LECTURER, Force );
	}

	/**
	 * Will set the email-address of the system
	 * @param String eMail-Address
	 * @return boolean successfully set
	 */
	public boolean setSystemMail( String Mail )
	{
		return this.setString( this.XML_GLOBAL_SYSTEM_MAIL, Mail );
	}

	/**
	 * Will set the subject of emails sendet on registration
	 * @param String subject
	 * @return boolean successfully set
	 */
	public boolean setRegisterSubject( String Subject )
	{
		return this.setString( this.XML_GLOBAL_REGISTER_SUBJECT, Subject );
	}

	protected boolean setBoolean( String Name, boolean Value )
	{
		if ( (Name==null) || (Name.length()<1) )
			return false;
		Element globals = this.hCore.getHRoot().getChild( this.XML_GLOBAL );
		// is the tag existent?
		if ( globals == null )
		{
			Log.addLog( "ERROR: Database for global variables contains no "+this.XML_GLOBAL+"-tag!");
			return false;
		}
		String val = Value?this.XML_BOOLEAN_TRUE:this.XML_BOOLEAN_FALSE;
		globals.setAttribute( Name, val );
		return this.hCore.saveDom( );
	}

	protected boolean setString( String Name, String Value )
	{
		if ( (Name==null) || (Name.length()<1) || (Value==null) || (Value.length()<1) )
			return false;
		Element globals = this.hCore.getHRoot().getChild( this.XML_GLOBAL );
		// is the tag existent?
		if ( globals == null )
		{
			Log.addLog( "ERROR: Database for global variables contains no "+this.XML_GLOBAL+"-tag!");
			return false;
		}
		String val = Value;
		globals.setAttribute( Name, val );
		return this.hCore.saveDom( );
	}
}