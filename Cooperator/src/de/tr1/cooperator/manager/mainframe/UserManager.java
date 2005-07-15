/*
File:		UserManager.java
Created:	05-05-30@21:00
Task:		Manager that Handles all Users...
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

package de.tr1.cooperator.manager.mainframe;

import de.tr1.cooperator.model.mainframe.User;
import de.tr1.cooperator.exceptions.*;
import de.tr1.cooperator.manager.system.Log;
import de.tr1.cooperator.manager.mainframe.XMLCore;

import java.io.File;
import java.io.IOException;

import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Iterator;

import org.jdom.Element;


/**
 * This class handles everything for an XML-based user-database
 *
 * @author Peter Matjeschk
 * @version 05-05-30@21:00
 */
public class UserManager
{
	//constants for the XML-File
	public static final String XMLROOT						= "USERS";
	public static final String XMLTAG 						= "USER";
	public static final String XMLATTRIBUTE_LOGIN 			= "login";
	public static final String XMLATTRIBUTE_PASSWORD		= "pw" ;
	public static final String XMLATTRIBUTE_RIGHTS			= "ri";
	public static final String XMLATTRIBUTE_PERSONALNUMBER	= "pn";
	public static final String XMLATTRIBUTE_FIRSTNAME		= "fn";
	public static final String XMLATTRIBUTE_SURNAME			= "sn";
	public static final String XMLATTRIBUTE_EMAIL			= "em";

	private static	UserManager	hInstance = null;

	private			XMLCore		userXML = null;


	/**
	 * Will create the needed instance, but throws Exceptions, if Errors occur
	 */
	private UserManager( String BaseFile ) throws SingletonException, IllegalArgumentException, IOException
	{
		if( hInstance != null )
			throw new SingletonException();
		if( BaseFile == null || BaseFile.equals("") )
			throw new IllegalArgumentException( "Bad File name" );


		File f = new File( BaseFile );
		if( (f.exists() && (!f.canWrite()) && (f.isFile())) )
			throw new IOException();

		if ( !f.exists( ) )
		{
			this.userXML = new XMLCore( BaseFile );
			this.userXML.createDomFromScratch( this.XMLROOT );

			User new_admin = new User( "admin",							//Login
										User.cryptLikePW( "nimda" ),	//PW
										User.ADMIN,						//Role
										"007",							//Personal number
										"Administrator",				//first name
										"ADMINISTRATOR",				//surname
										"" );							//email
			this.addUser( new_admin );
			if( !this.userXML.saveDom( ) )
				throw new IOException();
		}
		else
		{
			try
			{
				this.userXML = new XMLCore( BaseFile );
			}
			catch( XMLException xe )
			{
				Log.addLog( "Bad XML-File: " + BaseFile );
				throw new IOException();
			}
			this.userXML.createDOM( );
		}

		hInstance = this;
	}

	/**
	 * Will return the valid singleton-instance and set the name of the xml-file
	 * @param sBaseFile
	 * @return UserManager
	 */
	synchronized public static UserManager getInstance( String sBaseFile ) throws SingletonException, IllegalArgumentException, IOException
	{
		if( hInstance == null )
			hInstance = new UserManager( sBaseFile );
		return hInstance;
	}

	/**
	 * Returns a valid singleton-instance, if it was initalized already. If not, it will throw an SingletonException!
	 * @ return UserManager
	 */
	synchronized public static UserManager getInstance() throws SingletonException
	{
		if( hInstance == null )
			throw new SingletonException();

		return hInstance;
	}

	/**
	 * Returns the user if it was found in the database
	 * @param UserLogin
	 * @return User
	 */
	public User getUser( String UserLogin )
	{
		if( UserLogin == null)
			return null;
		else
		{
			//Search for the User in the in the dom...
			Element currentUser = this.userXML.find( this.XMLTAG, this.XMLATTRIBUTE_LOGIN, UserLogin );
			if ( currentUser == null)
				return null;

			//Parse the data
			String 	sPassword		= currentUser.getAttributeValue( this.XMLATTRIBUTE_PASSWORD );
			String 	sRights			= currentUser.getAttributeValue( this.XMLATTRIBUTE_RIGHTS );
			int		iRights;
			String	sPersonalNumber	= currentUser.getAttributeValue( this.XMLATTRIBUTE_PERSONALNUMBER );
			String	sFirstName		= currentUser.getAttributeValue( this.XMLATTRIBUTE_FIRSTNAME );
			String	sSurname		= currentUser.getAttributeValue( this.XMLATTRIBUTE_SURNAME );
			String	sEmail			= currentUser.getAttributeValue( this.XMLATTRIBUTE_EMAIL );

			//parse the role
			try
			{
				iRights = Integer.parseInt( sRights );
			}
			catch( NumberFormatException nfe )
			{
				Log.addLog( "Bad entry for rights in userdatabase. Damaged user-entry "
							+ UserLogin );
				return null;
			}
			User returnUser = new User( UserLogin,
										sPassword,
										iRights,
										sPersonalNumber,
										sFirstName,
										sSurname,
										sEmail );

			return returnUser;
		}
	}

	/**
	 * Returns the user if it was found in the database
	 * @param PersonalNumber
	 * @return User
	 */
	public User getUserByPersonalNumber( String PersonalNumber )
	{
		if( PersonalNumber == null)
			return null;
		else
		{
			//Search for the User in the in the dom...
			Element currentUser = this.userXML.find( this.XMLTAG, this.XMLATTRIBUTE_PERSONALNUMBER, PersonalNumber );
			if ( currentUser == null)
				return null;

			//Parse the data
			String	sLogin			= currentUser.getAttributeValue( this.XMLATTRIBUTE_LOGIN );
			String 	sPassword		= currentUser.getAttributeValue( this.XMLATTRIBUTE_PASSWORD );
			String 	sRights			= currentUser.getAttributeValue( this.XMLATTRIBUTE_RIGHTS );
			int		iRights;
			String	sFirstName		= currentUser.getAttributeValue( this.XMLATTRIBUTE_FIRSTNAME );
			String	sSurname		= currentUser.getAttributeValue( this.XMLATTRIBUTE_SURNAME );
			String	sEmail			= currentUser.getAttributeValue( this.XMLATTRIBUTE_EMAIL );

			//parse the role
			try
			{
				iRights = Integer.parseInt( sRights );
			}
			catch( NumberFormatException nfe )
			{
				Log.addLog( "Bad entry for rights in userdatabase. Damaged user-entry "
							+ sLogin );
				return null;
			}
			User returnUser = new User( sLogin,
										sPassword,
										iRights,
										PersonalNumber,
										sFirstName,
										sSurname,
										sEmail );

			return returnUser;
		}
	}

	/**
	 * Will add the user to the database
	 * @param TheUser
	 * @return boolean
	 */
	synchronized public boolean addUser( User TheUser )
	{
		//check if user with this login is already there....
		User oldUser = this.getUser( TheUser.getLogin() );

		//is there already an user with this login?
		if( oldUser != null )
			return false;			//baaaaaaaaaaaaaaaaaaaaad.... :)

		//create Element for dom....
		Element newUserElement = new Element( this.XMLTAG );

		newUserElement.setAttribute( this.XMLATTRIBUTE_LOGIN,			TheUser.getLogin() );
		newUserElement.setAttribute( this.XMLATTRIBUTE_PASSWORD,		TheUser.getPassword() );
		newUserElement.setAttribute( this.XMLATTRIBUTE_RIGHTS,			""+TheUser.getRights() );
		newUserElement.setAttribute( this.XMLATTRIBUTE_PERSONALNUMBER,	TheUser.getPersonalNumber() );
		newUserElement.setAttribute( this.XMLATTRIBUTE_FIRSTNAME,		TheUser.getFirstName() );
		newUserElement.setAttribute( this.XMLATTRIBUTE_SURNAME,			TheUser.getSurname() );
		newUserElement.setAttribute( this.XMLATTRIBUTE_EMAIL,			TheUser.getEmailAddress() );


		this.userXML.getHRoot().addContent( newUserElement );

		//check if saved to disk
		if( !this.userXML.saveDom() )
		{
			this.userXML.getHRoot().removeContent( newUserElement );
			return false;
		}
		return true;
	}

	/**
	 * Will update the profile of an existing User
	 * @param TheUser
	 * @return boolean
	 */
	synchronized public boolean updateUser( User TheUser )
	{
		//check if user with this login exists....
		User oldUser = this.getUser( TheUser.getLogin() );

		//if the user didnt exist then return...
		if( oldUser == null )
			return false;

		//delete user
		if  ( !this.deleteUser( oldUser.getLogin(), true ) )
			return false;

		//add user with new settings
		if ( !this.addUser( TheUser ) )
			return false;
		return true;
	}

	/**
	 * Will remove an exisiting user
	 * @param UserLogin
	 * @return boolean
	 */
	synchronized public boolean deleteUser( String UserLogin )
	{
		return this.deleteUser( UserLogin, false );
	}

	synchronized protected boolean deleteUser( String UserLogin, boolean alsoAdmin )
	{
		// check valid name... admin can not be deleted!!!
		if ( (UserLogin==null) || (UserLogin.length()<1) )
			return false;
		if ( (!alsoAdmin) && UserLogin.equals("admin") )
			return false;

		//get user to remove
		Element currentUserElement = this.userXML.find( this.XMLTAG, this.XMLATTRIBUTE_LOGIN, UserLogin );

		//user was not found
		if( currentUserElement == null )
			return false;

		//save user if write fails...
		User oldUser = this.getUser( UserLogin );

		//user was found and removed
		this.userXML.getHRoot().removeContent( currentUserElement );
		if( !this.userXML.saveDom() )
		{
			addUser( oldUser );
			return false;
		}

		return true;
	}
	/**
	 * Will remove an exisiting user
	 * @param UserLogin
	 * @return boolean
	 */
	synchronized public boolean deleteUser( User delUser )
	{
		//get user to remove
		Element currentUserElement = this.userXML.find( this.XMLTAG, this.XMLATTRIBUTE_LOGIN, delUser.getLogin() );

		//user was not found
		if( currentUserElement == null )
			return false;

		//user was found and removed
		this.userXML.getHRoot().removeContent( currentUserElement );
		if( !this.userXML.saveDom() )
		{
			addUser( delUser );
			return false;
		}

		return true;
	}


	/**
	 * Will create a collection with all of the current know Users
	 * @return Map the created Map of all pairs
	 */
	public Collection getAllUsers( )
	{
		Iterator it = this.userXML.getHRoot().getChildren().iterator();

		Collection res		= Collections.synchronizedList(new ArrayList( ));

		while ( it.hasNext() )
		{
			Element elem = (Element)it.next();

			//Parse the data
			String	sLogin			= elem.getAttributeValue( this.XMLATTRIBUTE_LOGIN );
			String 	sPassword		= elem.getAttributeValue( this.XMLATTRIBUTE_PASSWORD );
			String 	sRights			= elem.getAttributeValue( this.XMLATTRIBUTE_RIGHTS );
			int		iRights;
			String	sPersonalNumber	= elem.getAttributeValue( this.XMLATTRIBUTE_PERSONALNUMBER );
			String	sFirstName		= elem.getAttributeValue( this.XMLATTRIBUTE_FIRSTNAME );
			String	sSurname		= elem.getAttributeValue( this.XMLATTRIBUTE_SURNAME );
			String	sEmail			= elem.getAttributeValue( this.XMLATTRIBUTE_EMAIL );

			//parse the role
			try
			{
				iRights = Integer.parseInt( sRights );
			}
			catch( NumberFormatException nfe )
			{
				Log.addLog( "Bad entry for rights in userdatabase. Damaged user-entry "
							+ sLogin );
				return null;
			}

			res.add( new User( sLogin,
								sPassword,
								iRights,
								sPersonalNumber,
								sFirstName,
								sSurname,
								sEmail ) );
		}
		return res;
	}


	/**
	 * Returns Collection of users
	 * @param cUserLogins Collection<String> which contains user-logins
	 * @return Collection<User>
	 */
	public Collection getUsersByCollection( Collection cUserLogins )
	{
		Collection		alReturn;		//this is the return-Collection

		alReturn = new ArrayList();

		if( (cUserLogins == null) ||
			(cUserLogins.size() == 0 ) )		//Collection is empty
			return alReturn;


		Iterator it;
		it = this.userXML.getHRoot().getChildren(this.XMLTAG).iterator( );


		while ( it.hasNext( ) )
		{

			Element	currentUser = (Element) it.next();

			String	sLogin	= currentUser.getAttributeValue( this.XMLATTRIBUTE_LOGIN );

			if( cUserLogins.contains( sLogin ) )
			{
				//Parse the data
				String	sPassword		= currentUser.getAttributeValue( this.XMLATTRIBUTE_PASSWORD );
				String 	sRights			= currentUser.getAttributeValue( this.XMLATTRIBUTE_RIGHTS );
				int		iRights;
				String	sPersonalNumber	= currentUser.getAttributeValue( this.XMLATTRIBUTE_PERSONALNUMBER );
				String	sFirstName		= currentUser.getAttributeValue( this.XMLATTRIBUTE_FIRSTNAME );
				String	sSurname		= currentUser.getAttributeValue( this.XMLATTRIBUTE_SURNAME );
				String	sEmail			= currentUser.getAttributeValue( this.XMLATTRIBUTE_EMAIL );

				//parse the role
				try
				{
					iRights = Integer.parseInt( sRights );
				}
				catch( NumberFormatException nfe )
				{
					Log.addLog( "Bad entry for rights in userdatabase. Damaged user-entry "
								+ sLogin );
					return null;
				}
				User returnUser = new User( sLogin,
											sPassword,
											iRights,
											sPersonalNumber,
											sFirstName,
											sSurname,
											sEmail );

				alReturn.add( returnUser );
			}
		}

		return alReturn;
	}

}