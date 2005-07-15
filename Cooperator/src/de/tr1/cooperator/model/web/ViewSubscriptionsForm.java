/*
File:		ViewSubscriptionsForm.java
Created:	05-06-11@09:00
Task:		This is the Form-Bean for the viewSubscriptions.jsp-site
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
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import java.util.*;
import java.util.regex.*;
import java.text.SimpleDateFormat;

import de.tr1.cooperator.model.mainframe.*;
import de.tr1.cooperator.manager.web.*;
import de.tr1.cooperator.model.web.*;
import de.tr1.cooperator.exceptions.*;
import de.tr1.cooperator.manager.ManagerInitializer;
import de.tr1.cooperator.manager.mainframe.UserManager;
import de.tr1.cooperator.manager.mainframe.EventTypeManager;
import de.tr1.cooperator.manager.mainframe.EventManager;
import de.tr1.cooperator.manager.mainframe.EventResultManager;
import de.tr1.cooperator.manager.system.GlobalVarsManager;
import de.tr1.cooperator.manager.system.Log;

/**
 * This is the Form-Bean for the viewSubscriptions.jsp-site
 * This stores all the date of the formular and verifys for correct input
 *
 * @author Sebastian Kohl
 * @version 05-06-11@09:00
 *
 */

public class ViewSubscriptionsForm extends Accessable
{
	protected	String		sNewSubscriber;					// for adding a new subscriber
	protected	String		sNewSubscriberPersonalNumber;	// for adding a new result
	protected	double		sNewSubscriberResult;			// for adding a new result
	protected	String		sSubscribeMessage;
	protected	String		sResultMessage;
	protected	String		sAction;
	protected	Event		hEvent;
	protected	Collection	cNaviPathes;
	protected	Collection	cSubscriberList;		// all subscribed users
	protected	Collection	cExamResults;			// all aviable user results
	protected	Collection	cSubscriberResultList;	// union of subscribed users and there results
	protected	int			iEventID = -1;

//======= get-Methods ===============

	public	boolean	getHasSubscriptions( )
	{
		if ( this.hEvent == null )
			return false;
		else
			return (this.hEvent.getSubscriberList().size()>0);
	}

	public	boolean getAllowedToEdit( )
	{
		if ( (this.hEvent == null) || (this.hUser==null) )
			return false;
		else
			return ( this.hasAdminRights()
					|| this.hEvent.getLecturer().equals(this.hUser.getLogin()) );
	}

	public 	boolean getEventIsFull( )
	{
		if ( this.hEvent == null )
			return false;
		else
			return (this.hEvent.getSubscriberList().size()>=this.hEvent.getMaxSubscriptions());
	}

	public	Collection	getSubscribers( )
	{
		if ( this.hEvent == null )
			return new ArrayList();
		else
			return this.cSubscriberList;
	}

	public	Collection	getSubscriberResultList( )
	{
		if ( this.hEvent == null )
			return new ArrayList();
		else
			return this.cSubscriberResultList;
	}

	public Collection getExamResults( )
	{
		if ( this.hEvent == null )
			return new ArrayList();
		else
			return this.cExamResults;

	}
	public	int	getEventID( )
	{
		if ( this.hEvent == null )
			return -1;
		else
			return this.hEvent.getID();
	}

	public	int	getEventSubscribedCount( )
	{
		if ( this.hEvent == null )
			return -1;
		else
			return this.hEvent.getSubscriberList().size();
	}

	public	int	getEventSubscribeMax( )
	{
		if ( this.hEvent == null )
			return -1;
		else
			return this.hEvent.getMaxSubscriptions();
	}

	public	String	getEventName( )
	{
		if ( this.hEvent == null )
			return "";
		else
			return this.hEvent.getName( );
	}

	public	Event	getEvent( )
	{
		return this.hEvent;
	}

	public	String	getEventSubscriptionStart( )
	{
		if ( this.hEvent == null )
			return "";
		else
		{
			long Time = new Date().getTime();
			SimpleDateFormat myDateFormat = new SimpleDateFormat( "dd.MM.yyyy, HH:mm" );
			return myDateFormat.format( new java.util.Date( this.hEvent.getSubscriptionStart() ) );
		}
	}
	public	String	getEventSubscriptionEnd( )
	{
		if ( this.hEvent == null )
			return "";
		else
		{
			long Time = new Date().getTime();
			SimpleDateFormat myDateFormat = new SimpleDateFormat( "dd.MM.yyyy, HH:mm" );
			return myDateFormat.format( new java.util.Date( this.hEvent.getSubscriptionEnd() ) );
		}
	}
	public	String	getEventUnsubscriptionEnd( )
	{
		if ( this.hEvent == null )
			return "";
		else
		{
			long Time = new Date().getTime();
			SimpleDateFormat myDateFormat = new SimpleDateFormat( "dd.MM.yyyy, HH:mm" );
			return myDateFormat.format( new java.util.Date( this.hEvent.getUnsubscriptionEnd() ) );
		}
	}

	public	String	getAction( )
	{
		return this.sAction;
	}

	public	String	getNewSubscriber( )
	{
		return this.sNewSubscriber;
	}

	public	double	getNewSubscriberResult( )
	{
		return this.sNewSubscriberResult;
	}

	public	String	getNewSubscriberPersonalNumber( )
	{
		return this.sNewSubscriberPersonalNumber;
	}

	public	String	getSubscribeMessage( )
	{
		return this.sSubscribeMessage;
	}

	public	String	getResultMessage( )
	{
		return this.sResultMessage;
	}

	public	Collection getNaviPathes( )
	{
		return this.cNaviPathes;
	}

//======= set-Methods ===============


	public	void setSubscribeMessage( String val )
	{
		this.sSubscribeMessage = val;
	}

	public	void setResultMessage( String val )
	{
		this.sResultMessage = val;
	}

	public	void setEventID( int id )
	{
		this.iEventID = id;
	}

	public	void setNewSubscriber( String val )
	{
		this.sNewSubscriber = val;
	}

	public	void setNewSubscriberPersonalNumber( String val )
	{
		this.sNewSubscriberPersonalNumber = val;
	}

	public	void setNewSubscriberResult( double val )
	{
		this.sNewSubscriberResult = val;
	}

	public	void setAction( String val )
	{
		this.sAction = val;
	}

//======== non-set-get-Methods ===============

	synchronized public void initValues( HttpServletRequest request )
	{
		super.initValues(request);

		// grab the event-ID out of the request
		String EventID						= request.getParameter( "eventID" );
		this.hEvent							= null;
		this.cNaviPathes					= new ArrayList( );
		this.sSubscribeMessage				= "";
		this.sResultMessage					= "";
		this.sNewSubscriber 				= "";
		this.sNewSubscriberPersonalNumber 	= "";
		this.sNewSubscriberResult 			= 0;
		this.sAction						= "";

		if ( this.hUser == null )
			return;
		try
		{
			if ( this.iEventID == -1 )
				this.iEventID = Integer.parseInt( EventID );
		}
		catch ( Exception e )
		{
			this.sErrorMessage += "Falsche oder ungültige Event-ID angegeben!";
			return;
		}
		// grab the event
		try
		{
			this.hEvent		= EventManager.getInstance().getEventByID( this.iEventID );
			if ( this.hEvent == null )
			{
				this.sErrorMessage += "Ungültige Event-ID angegeben, Event existiert nicht!";
				return;
			}
			if ( !this.getAllowedToEdit() )
			{
				this.sErrorMessage += "Sie haben keine Rechte zum Editieren der Einschreibung dieser Veranstaltung.";
				return;
			}

//	== edit by Peter Matjschk:
//			-getUser uses much to many iteratorions, so use method from usermanager

/*			// create the User-List
			Collection names = this.hEvent.getSubscriberList();
			Iterator it = names.iterator();
			this.cSubscriberList = new ArrayList();
			while ( it.hasNext() )
			{
				String login = (String)(it.next());
				User user	 = UserManager.getInstance().getUser(login);
				if ( user==null )
				{
					Log.addLog( "ERROR: User with login "+login+" subscribed in event \""+this.hEvent.getName()+"\" (#"+this.hEvent.getID()+"), but does not exist in User-Database!" );
					this.sErrorMessage += "Unbekannter Benutzer in der Einschreibungsliste gefunden!";
				}
				else
					this.cSubscriberList.add( user );
			}*/
			this.cSubscriberList = UserManager.getInstance().getUsersByCollection( this.hEvent.getSubscriberList() );
			this.cExamResults = EventResultManager.getInstance().getResults( this.hEvent.getID() );

			Iterator cSubscriberListIT = cSubscriberList.iterator();

			cSubscriberResultList = new ArrayList();

			while( cSubscriberListIT.hasNext() )
			{
				User CurUser = (User) cSubscriberListIT.next();
				String UserPersonalNumber = CurUser.getPersonalNumber();

				Iterator cExamResultsIT = cExamResults.iterator();
				ExamResult	curExamResult 				= null;
				String		ResultUserPersonalNumber 	= null;
				while( cExamResultsIT.hasNext() )
				{
					curExamResult = (ExamResult) cExamResultsIT.next();
					ResultUserPersonalNumber = curExamResult.getUserPersonalNumber();
					if( UserPersonalNumber.equals( ResultUserPersonalNumber ) )
						break;
				}

				if( UserPersonalNumber.equals( ResultUserPersonalNumber ) )
				{
					UserResult newUserResult = new UserResult( CurUser, ""+curExamResult.getResult() );
					cSubscriberResultList.add(newUserResult);
				}
				else
				{
					UserResult newUserResult = new UserResult( CurUser, "-" );
					cSubscriberResultList.add(newUserResult);
				}

			}
			Collections.sort( (List) cSubscriberResultList );

			// create the navigation-collection
			String addPath = "";
			Collection all = EventManager.getInstance().getAllEvents();
			this.traceNavigation( all, this.cNaviPathes, this.hEvent );
			Iterator pathes = this.cNaviPathes.iterator();
			while ( pathes.hasNext() )
			{
				LinkListBean llb = (LinkListBean)pathes.next();
				llb.setAdditional( addPath );
				addPath += "&nbsp;&nbsp;";
			}
			this.cNaviPathes.add( new LinkListBean( this.hEvent.getID(), "", addPath, "::system::viewSubscribersForEvent::" ) );
		}
		catch ( Exception e )
		{
			this.sErrorMessage += "Ein Fehler ist aufgetreten: \r\n" + e;
			return;
		}
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
 	{
		ActionErrors errors = new ActionErrors();
		if ( this.sAction.equals("hinzufügen") )
		{
			if ( (this.sNewSubscriber==null) || (this.sNewSubscriber.length()<1) )
				errors.add("viewsubs.InvalidLogin", new ActionMessage("viewsubs.invalidlogin"));
		}
		return errors;
	}

	/**
	 * reqires Collections of DropDownEntries
	 */
	private DropDownEntry find ( Collection list, int key )
	{
		Iterator it = list.iterator();
		while ( it.hasNext() )
		{
			DropDownEntry dde = (DropDownEntry)(it.next());
			if ( dde.getValue() == key )
				return dde;
		}
		return null;
	}

	private void traceNavigation( Collection AllEvents, Collection pathes, Event ActualEvent )
	{
		// if it has no parents, add it and return
		if ( ActualEvent.getParent()==-1 )
		{
			pathes.add( new LinkListBean( ActualEvent.getID(), ActualEvent.getName(), "" ) );
			return;
		}
		// else search it's parent
		Iterator it = AllEvents.iterator();
		Event parent= null;
		while ( it.hasNext() )
		{
			Event temp = (Event)it.next();
			if ( ActualEvent.getParent()==temp.getID() )
			{
				parent = temp;
				break;
			}
		}
		if ( parent == null )
		{
			this.sErrorMessage += "Path could not be created... "+ActualEvent.getName()+" has no parent!";
			return;
		}
		this.traceNavigation( AllEvents, pathes, parent );
		pathes.add( new LinkListBean( ActualEvent.getID(), ActualEvent.getName(), "" ) );
	}
}