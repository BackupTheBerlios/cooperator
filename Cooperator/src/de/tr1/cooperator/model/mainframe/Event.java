/*
File:		Event.java
Created:	05-06-01@13:00
Task:		This class handles everything for an event such as subscription/unsubscription and so on
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
package de.tr1.cooperator.model.mainframe;

import java.util.*;

import de.tr1.cooperator.manager.mainframe.EventManager;

/**
 * This class handles everything for an event
 *
 * @author Peter Matjeschk, Sebastian Kohl
 * @version 05-06-04@09:00
 */
public class Event implements Comparable
{
	private	String		sName;						//Name of this Event
	private	int			iType;						//Type of this event - take a look at eventtype
	private	int			iID;						//id of this event
	private	int			iParent;					//parent for this event or -1
	private int			iGroup;						// all members of one group do have this same id

	private	String		sLecturer;					//login of the Lecturer (check the UserManager for correct Name!)
	private	String		sInfoText;					//Description of this event

	private	long		lStart;						//UTS for start of event
	private	Collection	cTimes;						//Collection of EventTime

	private	long		lSubscriptionStart;			//UTS for start of subscription for this event
	private	long		lSubscriptionEnd;			//UTS for end of subscription for this event

	private	long		lUnsubscriptionEnd;			//UTS for latest unsubscription-time...

	private	Collection	cSubscriberList;			//Collection of User.getLogin() - Strings...
	private	int			iMaxSubscriptions;			//max. amount of users for this event



	/**
	 * This is the Constructor for Event
	 *
	 * @param sName Name of this Event
	 * @param iType Type of this Event
	 * @param iID ID of this Event
	 * @param iParent Parent of this Event
	 * @param sLecturer Login of the Lecturer of this Event
	 * @param sInfoText Description of this Event
	 * @param lStart Begin of this Event in Unix-Time-Stamp-Format
	 * @param cTimes Collection of all Times of this event
	 * @param lSubscriptionStart Start of the Subscription in Unix-Time-Stamp-Format
	 * @param lSubscriptionEnd End of the Subscription in Unix-Time-Stamp-Format
	 * @param lUnsubscriptionEnd End of the Unsubscription in Unix-Time-Stamp-Format
	 * @param cSubscriberList Collection of Logins from Users who have subscribed to this event
	 * @param iMaxSubscriptions Maximum number of users who can subscribe to this event
	 */
	public Event( String sName, int iType, int iID, int iParent, int iGroup,
					String sLecturer, String sInfoText,
					long lStart, Collection cTimes,
					long lSubscriptionStart, long lSubscriptionEnd,
					long lUnsubscriptionEnd,
					Collection cSubscriberList, int iMaxSubscriptions )
	{
		this.sName				= sName;
		if ( this.sName == null )
			this.sName="";

		this.iType				= iType;
		this.iID				= iID;
		this.iParent			= iParent;
		this.iGroup				= iGroup;

		this.sLecturer			= sLecturer;
		if ( this.sLecturer == null )
			this.sName="";

		this.sInfoText			= sInfoText;
		if ( this.sInfoText == null )
			this.sInfoText="";

		this.lStart				= lStart;
		this.cTimes				= cTimes;
		if ( this.cTimes == null )
			this.cTimes = Collections.synchronizedList(new ArrayList());

		this.lSubscriptionStart	= lSubscriptionStart;
		this.lSubscriptionEnd	= lSubscriptionEnd;

		this.lUnsubscriptionEnd	= lUnsubscriptionEnd;

		this.cSubscriberList	= cSubscriberList;
		if ( this.cSubscriberList == null )
			this.cSubscriberList = Collections.synchronizedList(new ArrayList());
		this.iMaxSubscriptions	= iMaxSubscriptions;
	}

	/**
	 * This method will check, if a subscription is possible due to the rules beeing applied
	 * @parem UserLogin The Login of the User
	 * @return boolean can join without problems
	 */
	public boolean canSubscribe( String UserLogin )
	{
		// check for validity
		if ( (UserLogin == null) || (UserLogin.length()<1) )
			return false;
		// check for requirement-list
		if (!EventManager.getInstance().isInRequirementList( UserLogin, this.iID ))
			return false;
		// is the list of subscribers allready full?
		if ( this.iMaxSubscriptions <= this.cSubscriberList.size() )
			return false;
		// are the dates ok?
		long date = new Date().getTime();
		if ( (date<this.lSubscriptionStart) || (date>lSubscriptionEnd) )
			return false;
		// all ok
		return true;
	}

	/**
	 * This method will check, if an unsubscription is possible due to the rules beeing applied
	 * @parem UserLogin The Login of the User
	 * @return boolean can join without problems
	 */
	public boolean canUnsubscribe( String UserLogin )
	{
		// check for validity
		if ( (UserLogin == null) || (UserLogin.length()<1) )
			return false;
		// are the dates ok?
		long date = new Date().getTime();
		if ( date > this.lUnsubscriptionEnd )
			return false;
		// all ok
		return true;
	}

	/**
	 * This method add's an user to the cSubscriberList
	 *
	 * @param userLogin login of the user who has to be added
	 * @param forceSubscription Lecturers and Admins can force (so no other conditions are checked)
	 *
	 * @return success
	 */
	public boolean subscribe( String userLogin, boolean forceSubscription )
	{
		// check for validity
		if ( (userLogin == null) || (userLogin.length()<1) )
			return false;
		//get a list from EventManager which contains logins who are allowed to subscribe
		//but only if forceSubscription = false
		if (this.iMaxSubscriptions==-1)
			return false;
		if ( (!forceSubscription) && (!this.canSubscribe( userLogin )) )
			return false;

		return this.cSubscriberList.add( userLogin );
	}

	/**
	 * This method delete's an user from the cSubscriberList
	 *
	 * @param userLogin login of the user who has to be deletet
	 * @param forceSubscription Lecturers and Admins can force (so no other conditions are checked)
	 *
	 * @return success
	 */
	public boolean unsubscribe( String userLogin, boolean forceSubscription )
	{
		//check if lUnsubscriptionEnd > currentTime
		//but only if forceSubscription = false
		if ( (!forceSubscription) && (!this.canUnsubscribe( userLogin )) )
			return false;

		if ( !this.cSubscriberList.contains( userLogin ) )
			return false;

		return this.cSubscriberList.remove( userLogin );
	}

	/**
	 * This method check's if an user is in the cSubscriberList
	 *
	 * @param userLogin login of the user who has to be checked
	 *
	 * @return user is in list or not
	 */
	public boolean isSubscribed( String userLogin )
	{
		return this.cSubscriberList.contains( userLogin );
	}


	//GETTER AND SETTER
	public void setName( String sName )
	{
		this.sName = sName;
	}
	public String getName()
	{
		return this.sName;
	}


	public void setType( int iType )
	{
		this.iType = iType;
	}
	public int getType()
	{
		return this.iType;
	}


	public void setID( int iID )
	{
		this.iID = iID;
	}
	public int getID()
	{
		return this.iID;
	}


	public void setParent( int iParent )
	{
		this.iParent = iParent;
	}
	public int getParent()
	{
		return this.iParent;
	}


	public void setGroup( int iGroup )
	{
		this.iGroup = iGroup;
	}
	public int getGroup()
	{
		return this.iGroup;
	}


	public void setLecturer( String sLecturer )
	{
		this.sLecturer = sLecturer;
	}
	public String getLecturer()
	{
		return this.sLecturer;
	}


	public void setInfoText( String sInfoText )
	{
		this.sInfoText = sInfoText;
	}
	public String getInfoText()
	{
		return this.sInfoText;
	}


	public void setStart( long lStart )
	{
		this.lStart = lStart;
	}
	public long getStart()
	{
		return this.lStart;
	}

	public void setTimes( Collection cTimes )
	{
		this.cTimes = cTimes;
	}
	public Collection getTimes()
	{
		return this.cTimes;
	}

	public void setSubscriptionStart( long lSubscriptionStart )
	{
		this.lSubscriptionStart = lSubscriptionStart;
	}
	public long getSubscriptionStart()
	{
		return this.lSubscriptionStart;
	}


	public void setSubscriptionEnd( long lSubscriptionEnd )
	{
		this.lSubscriptionEnd = lSubscriptionEnd;
	}
	public long getSubscriptionEnd()
	{
		return this.lSubscriptionEnd;
	}


	public void setUnsubscriptionEnd( long lUnsubscriptionEnd )
	{
		this.lUnsubscriptionEnd = lUnsubscriptionEnd;
	}
	public long getUnsubscriptionEnd()
	{
		return this.lUnsubscriptionEnd;
	}


	public void setSubscriberList( Collection cSubscriberList )
	{
		this.cSubscriberList = cSubscriberList;
	}
	public Collection getSubscriberList()
	{
		return this.cSubscriberList;
	}


	public void setMaxSubscriptions( int iMaxSubscriptions )
	{
		this.iMaxSubscriptions = iMaxSubscriptions;
	}
	public int getMaxSubscriptions()
	{
		return this.iMaxSubscriptions;
	}



	/**
	 * This is implementation of the Interface comparable and need for sorting and so on
	 *
	 * @param compareEvent compare this event to another event
	 * @return -x: less, 0: equal +x: greater
	 */
	public int compareTo( Object compareEvent )
	{
		return this.sName.compareTo( ((Event) compareEvent).getName() );
	}

}