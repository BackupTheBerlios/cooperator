/*
File:		EventInfoForm.java
Created:	05-06-08@14:30
Task:		This is the Form-Bean for the eventInfo.jsp-site
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
import de.tr1.cooperator.manager.mainframe.EventResultManager;
import de.tr1.cooperator.manager.mainframe.EventManager;
import de.tr1.cooperator.manager.system.GlobalVarsManager;
import de.tr1.cooperator.manager.system.Log;

/**
 * This is the Form-Bean for the eventInfo.jsp-site
 * This stores all the date of the formular and verifys for correct input
 *
 * @author Sebastian Kohl
 * @version 05-05-31@10:00
 *
 */

public class EventInfoForm extends Accessable
{

	protected	boolean		bHasMultipleTimes;
	protected	boolean		bIsInGroup;
	protected	boolean		bHasResult;
	protected	double		dResult;
	protected	int			iEventID;
	protected	Event		hEvent;
	protected	User		hEventLecturer;
	protected	Collection	cEventTimes;
	protected	Collection	cSubEvents;
	protected	Collection	cNaviPathes;
	protected	String		sSubscribeMessage;

//======= get-Methods ===============

	public	Event	getEvent( )
	{
		return this.hEvent;
	}

	public	boolean getHasResult( )
	{
		return this.bHasResult;
	}

	public	boolean getAtLeastLecturer( )
	{
		if ( this.hUser==null )
			return false;
		else
			return (this.hasAdminRights() || this.hasLecturerRights() );
	}

	public	boolean	getEventIsSubscribable( )
	{
		if ( this.hEvent == null )
			return false;
		else
			return (this.hEvent.getMaxSubscriptions()!=-1);
	}

	public	boolean	getEventHasMultipleTimes( )
	{
		return this.bHasMultipleTimes;
	}

	public	boolean getEventIsSubscribed( )
	{
		if ( (this.hEvent == null) || (this.hUser==null) )
			return false;
		else
			return this.hEvent.isSubscribed(this.hUser.getLogin());
	}

	public	boolean getEventCanSubscribe( )
	{
		if ( (this.hEvent == null) || (this.hUser==null) )
			return false;
		else
			return ( this.hasStudentRights()
					&& (!this.hEvent.isSubscribed(this.hUser.getLogin()))
					&& this.hEvent.canSubscribe(this.hUser.getLogin())
					&& (!this.bIsInGroup) );
	}

	public	boolean getEventCanUnsubscribe( )
	{
		if ( (this.hEvent == null) || (this.hUser==null) )
			return false;
		else
			return (   this.hEvent.isSubscribed(this.hUser.getLogin())
				 	&& this.hEvent.canUnsubscribe(this.hUser.getLogin()));
	}

	public	boolean getEventCanSwapHere( )
	{
		if ( (this.hEvent == null) || (this.hUser==null) )
			return false;
		else
			return ( this.hasStudentRights()
					&& this.hEvent.canSubscribe(this.hUser.getLogin())
					&& (!this.hEvent.isSubscribed(this.hUser.getLogin()))
					&& this.bIsInGroup );
	}

	public	boolean getEventIsInGroup( )
	{
		if ( (this.hEvent == null) || (this.hUser==null) )
			return false;
		else
			return this.bIsInGroup;
	}

	public	boolean getHasEditRights( )
	{
		return ( this.hasAdminRights() || this.hasLecturerRights() );
	}

	public	boolean getEventHasTimes( )
	{
		if ( this.hEvent == null )
			return false;
		else
			return (this.hEvent.getTimes().size() > 0);
	}

	public	boolean getAllowedToEdit( )
	{
		if ( (this.hEvent == null) || (this.hUser==null) )
			return false;
		else
			return ( this.hasAdminRights()
					|| this.hEvent.getLecturer().equals(this.hUser.getLogin()) );
	}

	public	int	getEventID( )
	{
		if ( this.hEvent == null )
			return -1;
		else
			return this.hEvent.getID();
	}

	public	double getResult( )
	{
		return this.dResult;
	}

	public	int	getEventType( )
	{
		if ( this.hEvent == null )
			return 0;
		else
			return this.hEvent.getType();
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

	public	User	getEventLecturer( )
	{
		return this.hEventLecturer;
	}

	public	String	getEventName( )
	{
		if ( this.hEvent == null )
			return "";
		else
			return this.hEvent.getName( );
	}

	public	String	getEventInfo( )
	{
		if ( this.hEvent == null )
			return "";
		else
		{
			String info = this.hEvent.getInfoText( );
			info		= info.replaceAll("<","&lt;");
			info		= info.replaceAll(">","&gt;");
			info		= info.replaceAll("\r\n","<br />\r\n");
			info		= info.replaceAll( "ä", "&auml;" );
			info		= info.replaceAll( "ö", "&ouml;" );
			info		= info.replaceAll( "ü", "&uuml;" );
			info		= info.replaceAll( "Ä", "&Auml;" );
			info		= info.replaceAll( "Ö", "&Ouml;" );
			info		= info.replaceAll( "Ü", "&Uuml;" );
			info		= info.replaceAll( "ß", "&szlig;" );
			info		= info.replaceAll( "\\[b\\]", "<b>" );
			info		= info.replaceAll( "\\[/b\\]", "</b>" );
			info		= info.replaceAll( "\\[i\\]", "<i>" );
			info		= info.replaceAll( "\\[/i\\]", "</i>" );
			info		= info.replaceAll( "\\[hr\\]", "<hr />" );
			// now replace all urls that can be found
			// verry dirty method, because matcher.replace and event string.replaceAll seams not
			// to work on the same regular expressions that where found by mather.find(void).
			// Don't know why... :-/
			String	wwwpatt	= "http://[a-zA-Z0-9äöüßÖÄÜ_\\.\\-/]+\\.[a-zA-Z]{2,5}";
			Pattern pattern = Pattern.compile("("+wwwpatt+")", Pattern.DOTALL | Pattern.MULTILINE);
			Matcher matcher = pattern.matcher(info);
			int pos = 0, add = 0;
			String inf = info;
			while( matcher.find(pos) )
			{
				String url	  = matcher.group(1);
				int start	  = matcher.start();
				String newstr = "<a href=\""+url+"\">"+url+"</a>";
				pos			  = start + url.length();
				inf = inf.substring(0,start+add)+newstr+inf.substring(start+add+url.length());
				add			 += newstr.length()-url.length();
			}
			return inf;
		}
	}

	public	String	getEventLecturerCompleteName( )
	{
		return this.hEventLecturer.getFirstName( )+" "+this.hEventLecturer.getSurname( );
	}

	public	String	getEventLecturerMail( )
	{
		if ( this.hEventLecturer == null )
			return "";
		else
			return this.hEventLecturer.getEmailAddress( );
	}

	public	String	getEventStart( )
	{
		if ( this.hEvent == null )
			return "";
		else
		{
			SimpleDateFormat myDateFormat = new SimpleDateFormat( "dd.MM.yyyy" );
			return myDateFormat.format( new java.util.Date( this.hEvent.getStart() ) );
		}
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

	public	String	getSubscribeMessage( )
	{
		return this.sSubscribeMessage;
	}

	public	Collection getEventTimes( )
	{
		return this.cEventTimes;
	}

	public	Collection getSubEvents( )
	{
		return this.cSubEvents;
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

	public	void setEventID( int id )
	{
		this.iEventID = id;
	}

//======== non-set-get-Methods ===============

	synchronized public void initValues( HttpServletRequest request )
	{
		super.initValues(request);

		// grab the event-ID out of the request
		this.iEventID		= -1;
		this.hEvent			= null;
		this.hEventLecturer	= null;
		this.cEventTimes	= new ArrayList( );
		this.cNaviPathes	= new ArrayList( );
		this.bHasMultipleTimes	= false;
		this.sSubscribeMessage	= "";
		if ( this.hUser == null )
			return;
		String EventID		= (String)request.getSession(true).getAttribute("wrapEventID");
		try
		{
			this.iEventID = Integer.parseInt( EventID );
			request.getSession(true).removeAttribute("wrapEventID");
		}
		catch ( Exception e )
		{
			try
			{
				EventID		  = request.getParameter( "eventID" );
				this.iEventID = Integer.parseInt( EventID );
			}
			catch ( Exception e2 )
			{
				this.sErrorMessage += "Falsche oder ungültige Event-ID angegeben!";
				return;
			}
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
			this.hEventLecturer = UserManager.getInstance().getUser( this.hEvent.getLecturer( ) );
			if ( this.hEventLecturer == null )
			{
				this.hEventLecturer = new User( "", "", User.LECTURER, "---",
								    			"niemand", "", "" );
			}
			// grab the Result
			ExamResult EResult = EventResultManager.getInstance().getResult( this.hUser.getPersonalNumber(), this.hEvent.getID() );
			if ( EResult==null )
				this.bHasResult = false;
			else
			{
				this.bHasResult	= true;
				this.dResult	= EResult.getResult();
			}

			// iterate all event-Times
			Iterator it = this.hEvent.getTimes( ).iterator( );
			int times = 0;
			while ( it.hasNext() )
			{
				EventTime et = (EventTime)(it.next());
				if ( (et.getRhythm() != EventTime.ONCE) && (et.getRhythm() != EventTime.ALTERNATIVE) )
					this.bHasMultipleTimes = true;
				this.cEventTimes.add( et );
				times++;
			}
			this.cSubEvents	= new ArrayList();
			// trace the sub-events and create the form-beans for the web-page
			// first get a Collection of all events with a parent-id equal to this event-ID
			Collection all	= EventManager.getInstance().getAllEvents();
			// now trace all and grab the children
			ArrayList children = new ArrayList();
			it = all.iterator();
			while ( it.hasNext() )
			{
				Event ev = (Event)(it.next());
				if ( ev.getParent() == this.hEvent.getID() )
					children.add( ev );
			}
			// all children found, so now create Map of Collections, where the keys are the type-ids
			// all these keys are also stored in a collection, so that they can afterwards be iterated
			// OK, there needs to be created a list of lists of list of events:
			// --------------------------------------------------- DropDownEntry( type-id, TYPE-MAP )
			//  | type=0           | type=1           | type=2
			//  |                  |                  |
			//  |                  |---- - -          |----------------------------- - - COLLECTION
			//  |                  |  group=2         |   group=1   |            |
			//  |                  |                  |
			//  |                  |---- - -          |
			//  |                  |  group=4         |----------------------------- - - COLLECTION
			//  |                  |                  |   group=5   |            |
			//  |                  |                  |             | id=13      |
			//                                                      |            | id=9
			//  |                  |                  |           EVENT        EVENT
			//
			//  |                  |                  |
			//                                  DropDownEntry( group-id, GROUP-COLLECTION )
			ArrayList TypeIDs	= new ArrayList();
			Iterator childs = children.iterator();
			while ( childs.hasNext() )
			{
				Event ev = (Event)(childs.next());
				// look, if this type already exists and create it if not
				DropDownEntry dde = this.find( TypeIDs, ev.getType() );
				if ( dde==null )
				{
					// add the type to the list
					dde = new DropDownEntry( ev.getType(), null, new ArrayList() );
					TypeIDs.add( dde );
				}
				// ok, the type is here, so check for a group in it
				DropDownEntry gdde = this.find( (Collection)dde.getObject(), ev.getGroup() );
				if ( gdde==null )
				{
					// add the group to the list
					gdde = new DropDownEntry( ev.getGroup(), null, new ArrayList() );
					((Collection)dde.getObject()).add( gdde );
				}
				// add the event to the collection
				((Collection)gdde.getObject()).add( ev );
			}
			// now all can be grabbed and be patched to one single Collection
			Iterator type = TypeIDs.iterator();
			while ( type.hasNext() )		// iterate all types
			{
				DropDownEntry tdde	= (DropDownEntry)(type.next());
				ArrayList aGroups	= (ArrayList)tdde.getObject( );
				boolean firstInType	= true;
				Iterator group		= aGroups.iterator();
				SubEventBean val	= null;
				while ( group.hasNext() )	// iterate all groups with this type
				{
					boolean firstInGroup= true;
					DropDownEntry gdde	= (DropDownEntry)(group.next());
					ArrayList aEvents	= (ArrayList)gdde.getObject( );
					Iterator event		= aEvents.iterator();
					int subscribedWhere	= -1;
					// check if subscripted to any of this group
					while ( event.hasNext() )
					{
						Event ev = (Event)(event.next());
						if ( ev.isSubscribed( this.hUser.getLogin() ) )
						{
							subscribedWhere = ev.getID();
							break;
						}
					}
					// parse the whole group
					event		= aEvents.iterator();
					while ( event.hasNext() )// iterate all Events in this group
					{
						Event ev = (Event)event.next();
						// is it the first of this type?
						String TypeName = "";
						if ( firstInType )
							TypeName = EventTypeManager.getInstance().getEventName( ev.getType() );
						boolean canSub	= ev.canSubscribe( this.hUser.getLogin() ) && this.hasStudentRights();
						String lect		= "-";
						User lecturer	= UserManager.getInstance().getUser( ev.getLecturer() );
						if ( lecturer != null )
							lect = lecturer.getSurname();
						val = new SubEventBean( ev.getName(),
										 TypeName,
										 lect,
										 ev.getID(),
										 ev.getGroup(),
										 ev.getType(),
										 canSub && (subscribedWhere==-1),											// has allowance and is not in group
										 ev.canUnsubscribe(this.hUser.getLogin()) && (subscribedWhere==ev.getID()),	// has allowance and is in
										 canSub && (subscribedWhere!=ev.getID()) && (subscribedWhere!=-1),			// has allowance, is not in yet, but is in group
										 firstInType, false, firstInGroup, false );
						this.cSubEvents.add( val );
						firstInType = false;
						firstInGroup = false;
					}
					if ( val!=null )
						val.setIsLastInGroup( true );
					firstInType = false;
				}
				if ( val!=null )
					val.setIsLast( true );
			}
			// create the navigation-collection
			String addPath = "";
			this.traceNavigation( all, this.cNaviPathes, this.hEvent );
			Iterator pathes = this.cNaviPathes.iterator();
			while ( pathes.hasNext() )
			{
				LinkListBean llb = (LinkListBean)pathes.next();
				llb.setAdditional( addPath );
				addPath += "&nbsp;&nbsp;";
			}

			// set the value for "isingroup"
			it   = all.iterator();
			int		gid   = this.hEvent.getGroup();
			this.bIsInGroup  = false;
			while ( it.hasNext() )
			{
				Event ev = (Event)(it.next());
				if ( (ev.getGroup()==gid) && (ev.isSubscribed(this.hUser.getLogin())) )
				{
					this.bIsInGroup	= true;
					break;
				}
			}

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