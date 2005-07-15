/*
File:		EventEntry.java
Created:	05-06-01@13:00
Task:		This is the Container which is stored in the EventCollections used in the JSP-Sites..
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



import de.tr1.cooperator.model.mainframe.Event;
import de.tr1.cooperator.model.mainframe.User;
import de.tr1.cooperator.manager.mainframe.UserManager;
import de.tr1.cooperator.manager.system.Log;
import de.tr1.cooperator.exceptions.SingletonException;
import java.util.*;

/**
 * This is need for all the JSP-Sites which use collections of Events...
 * @author Peter Matjeschk
 * @version 05-06-04@08:30
 */
public class EventEntry extends Event
{
	protected	final	String	HTMLNOBREAKSPACE = "&nbsp;";
	/** This stores the login of the current user */
	protected String	sUserLogin;
	/** This stores the deep of this entry (the SubClass) */
	protected	int		iDeep;
	/** This is for the topic */
	protected	String	sTopic;


	/**
	 * This is the Constructor for EventEntry
	 * It's much the same like Event, however it takes another Parameter: sUserLogin
	 * This is needed for functions like isSubscribed or something
	 *
	 * @param sUserLogin Login of the current User
	 * @param iDeep deep of this Event (SubClass)
	 * @param sName Name of this Event
	 * @param iType Type of this Event
	 * @param iID ID of this Event
	 * @param iParent Parent of this Event
	 * @param sLecturer Login of the Lecturer of this Event
	 * @param sLocation Location of this Event
	 * @param sInfoText Description of this Event
	 * @param lStart Begin of this Event in Unix-Time-Stamp-Format
	 * @param cTimes Collection of all Times of this event
	 * @param lSubscriptionStart Start of the Subscription in Unix-Time-Stamp-Format
	 * @param lSubscriptionEnd End of the Subscription in Unix-Time-Stamp-Format
	 * @param lUnsubscriptionEnd End of the Unsubscription in Unix-Time-Stamp-Format
	 * @param cSubscriberList Collection of Logins from Users who have subscribed to this event
	 * @param iMaxSubscriptions Maximum number of users who can subscribe to this event
	 */
	public EventEntry( String sUserLogin, int iDeep,
					String sName, int iType, int iID, int iGroup, int iParent,
					String sLecturer, String sInfoText,
					long lStart, Collection cTimes,
					long lSubscriptionStart, long lSubscriptionEnd,
					long lUnsubscriptionEnd,
					Collection cSubscriberList, int iMaxSubscriptions )
	{
		super( sName, iType, iID, iParent, iGroup,
				sLecturer, sInfoText,
				lStart, cTimes,
				lSubscriptionStart, lSubscriptionEnd,
				lUnsubscriptionEnd,
				cSubscriberList, iMaxSubscriptions );

		this.sUserLogin	= sUserLogin;
		this.iDeep		= iDeep;
		this.sTopic		= "";
	}
	
	/**
	 * This is the Constructor for EventEntry which is not clickable, it just stores an Empty event (for security-reasons) an a Topic...
	 *
	 * @param sUserLogin Login of the current User
	 * @param sTopic the text to show...
	 * @param iDeep deep of this Event (SubClass)
	 */
	public EventEntry( String sUserLogin, String sTopic, int iDeep )
	{
		//create an "empty" event, so if the jsp ist bad it wont get an error...
		super( sTopic, -1, -1, -1, -1,
				"-", "",
				-1, new ArrayList(),
				-1, -1,
				-1,
				new ArrayList(), -1 );
				
		this.sUserLogin = sUserLogin;
		this.sTopic = sTopic;
		this.iDeep = iDeep;
	}

	/**
	 * This is the Constructor for EventEntry
	 * It's much the same like Event, however it takes another Parameter: sUserLogin
	 * This is needed for functions like isSubscribed or something
	 *
	 * @param sUserLogin Login of the current User
	 * @param iDeep deep of this Event (SubClass)
	 * @param event the Event for this entry
	 */
	public EventEntry( String sUserLogin, int iDeep, Event event )
	{
		super( event.getName(), event.getType(), event.getID(), event.getGroup(), event.getParent(),
				event.getLecturer(), event.getInfoText(),
				event.getStart(), event.getTimes(),
				event.getSubscriptionStart(), event.getSubscriptionEnd(),
				event.getUnsubscriptionEnd(),
				event.getSubscriberList(), event.getMaxSubscriptions() );

		this.sUserLogin	= sUserLogin;
		this.iDeep		= iDeep;
		this.sTopic		= "";
	}

	/**
	 * This method get's an instance of the @see UserManager and get the correct
	 * surname of the lecturer
	 * @return The surname of the Lecturer
	 */
	public String getLecturerName()
	{
		UserManager	umUserManager;
		User		uUser;

		try
		{
			umUserManager = UserManager.getInstance();
		}
		catch( SingletonException se )
		{
			//THIS SHOULD NEVER EVER HAPPEN, ONLY IF SOMETHING IS CODED VERY VERY BADLY...
			Log.addLog( "UserManager: SingletonException: someone did something very shitty" );
			return "";
		}

		uUser = umUserManager.getUser( this.getLecturer() );
		if( uUser == null )
			return "-";
		return uUser.getSurname();
	}

	/**
	 * This method returns a string for shift of the EvenEntry
	 *
	 * @return shift-String for EventEntry
	 */
	public String getShiftIn()
	{
		String	sReturn = "";
		for( int temp = 0; temp < iDeep; temp++ )
			sReturn += "." + HTMLNOBREAKSPACE + HTMLNOBREAKSPACE + HTMLNOBREAKSPACE;

		return sReturn;
	}

	public String getIdAsString()
	{
		return ""+this.getID();
	}

	public String getSubscriptionStatusAsString()
	{
		return ""+this.isSubscribed( this.sUserLogin );
	}
	
	public String getTopic()
	{
		return this.sTopic;
	}

}