/*
File:		EditGlobalsForm.java
Created:	05-06-05@13:00
Task:		This is the Form-Bean for the editGlobalSettings.jsp-site
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

import de.tr1.cooperator.model.mainframe.User;
import de.tr1.cooperator.model.mainframe.Event;
import de.tr1.cooperator.model.mainframe.EventType;
import de.tr1.cooperator.exceptions.*;
import de.tr1.cooperator.manager.ManagerInitializer;
import de.tr1.cooperator.manager.mainframe.UserManager;
import de.tr1.cooperator.manager.mainframe.EventTypeManager;
import de.tr1.cooperator.manager.mainframe.EventManager;
import de.tr1.cooperator.manager.system.GlobalVarsManager;
import de.tr1.cooperator.manager.system.Log;
import de.tr1.cooperator.manager.web.*;
import de.tr1.cooperator.model.web.*;
/**
 * This is the Form-Bean for the Profile.jsp-site
 * This stores all the date of the formular and verifys for correct input
 *
 * @author Sebastian Kohl
 * @version 05-06-08@14:30
 *
 */

public class EditGlobalsForm extends Accessable
{
	protected	boolean		bForceUserMails;
	protected	boolean		bForceLecturerMails;
	protected	boolean		bNewForceUserMails;
	protected	boolean		bNewForceLecturerMails;
	protected	int			iEdit;
	protected	Collection	cUserMails;
	protected	Collection	cLecturerMails;
	protected	Collection	cEventTypes;
	protected	String		sNewUserMail;
	protected	String		sNewLecturerMail;
	protected	String		sNewType;
	protected	String		sAction;
	protected	String		sSaveMessage;
	protected	String		sUserMessage;
	protected	String		sLecturerMessage;
	protected	String		sTypeMessage;
	protected	String		sTypeNameMessage;
	protected	String		sSystemMail;
	protected	String		sRegisterSubject;
	protected	String		sEditType;

//======= get-Methods ===============

	public	String getSystemMail( )
	{
		return this.sSystemMail;
	}

	public	String getRegisterSubject( )
	{
		return this.sRegisterSubject;
	}

	public	String getNewType( )
	{
		return this.sNewType;
	}

	public	String getTypeMessage( )
	{
		return this.sTypeMessage;
	}

	public	String getTypeNameMessage( )
	{
		return this.sTypeNameMessage;
	}

	public	String getSaveMessage( )
	{
		return this.sSaveMessage;
	}

	public	String getUserMessage( )
	{
		return this.sUserMessage;
	}

	public	String getLecturerMessage( )
	{
		return this.sLecturerMessage;
	}

	public	String getAction( )
	{
		return this.sAction;
	}

	public	boolean getForceUserMails( )
	{
		return this.bForceUserMails;
	}

	public	boolean getForceLecturerMails( )
	{
		return this.bForceLecturerMails;
	}

	public	boolean getNewForceUserMails( )
	{
		return this.bNewForceUserMails;
	}

	public	boolean getNewForceLecturerMails( )
	{
		return this.bNewForceLecturerMails;
	}

	public	Collection getEventTypes( )
	{
		return this.cEventTypes;
	}

	public	int getEventTypeCount( )
	{
		return this.cEventTypes.size();
	}

	public	int getEdit( )
	{
		return this.iEdit;
	}

	public	Collection getUserMails( )
	{
		return this.cUserMails;
	}

	public	Collection getLecturerMails( )
	{
		return this.cLecturerMails;
	}

	public	boolean getUserHasMails( )
	{
		return ((this.cUserMails.size()) > 0 );
	}

	public	boolean getLecturerHasMails( )
	{
		return ((this.cLecturerMails.size()) > 0 );
	}

	public	String getNewUserMail( )
	{
		return this.sNewUserMail;
	}

	public	String getNewLecturerMail( )
	{
		return this.sNewLecturerMail;
	}

	public	String getEditType( )
	{
		return this.sEditType;
	}

	public boolean getHasAdminRights( )
	{
		return this.hasAdminRights();
	}

//======= set-Methods ===============

	public	void setSystemMail( String mail )
	{
		this.sSystemMail = mail;
	}

	public	void setRegisterSubject( String subj )
	{
		this.sRegisterSubject = subj;
	}

	public	void setNewType( String type )
	{
		this.sNewType = type;
	}

	public	void setSaveMessage( String mess )
	{
		this.sSaveMessage = mess;
	}

	public	void setLecturerMessage( String mess )
	{
		this.sLecturerMessage = mess;
	}

	public	void setUserMessage( String mess )
	{
		this.sUserMessage = mess;
	}

	public	void setTypeNameMessage( String name )
	{
		this.sTypeNameMessage = name;
	}

	public	void setAction( String act )
	{
		this.sAction = act;
	}

	public	void setEditType( String name )
	{
		this.sEditType = name;
	}

	public	void setEdit( int val )
	{
		this.iEdit = val;
	}

	public	void setForceUserMails( boolean force )
	{
		this.bForceUserMails	= force;
		this.bNewForceUserMails	= force;
	}

	public	void setForceLecturerMails( boolean force )
	{
		this.bForceLecturerMails	= force;
		this.bNewForceLecturerMails	= force;
	}

	public	void setNewUserMail( String mail )
	{
		this.sNewUserMail = mail;
	}

	public	void setNewLecturerMail( String mail )
	{
		this.sNewLecturerMail = mail;
	}

//======== non-set-get-Methods ===============

	synchronized public void initValues( HttpServletRequest request )
	{
		super.initValues(request);

		this.sSystemMail		 = GlobalVarsManager.getInstance().getSystemMail();
		this.sRegisterSubject	 = GlobalVarsManager.getInstance().getRegisterSubject();
		this.iEdit				 = -1;
		String sEdit			 = request.getParameter( "editType" );
		try { this.iEdit = Integer.parseInt( sEdit ); }
			catch ( Exception e ) {};
		this.sAction			 = "";
		this.sTypeMessage		 = "";
		this.sTypeNameMessage	 = "";
		this.sSaveMessage		 = "";
		this.sUserMessage		 = "";
		this.sLecturerMessage	 = "";
		this.sEditType			 = "";
		this.sNewUserMail		 = null;
		this.sNewLecturerMail	 = null;
		this.bForceUserMails 	 = GlobalVarsManager.getInstance().forceUserMails();
		this.bForceLecturerMails = GlobalVarsManager.getInstance().forceLecturerMails();
		this.bNewForceLecturerMails	= false;
		this.bNewForceUserMails		= false;
		Collection	aTypes		 = EventTypeManager.getInstance().getAllEventTypes();
		Collection	aEvents		 = EventManager.getInstance().getAllEvents();
		this.cEventTypes		 = new ArrayList();
		// Types need to be traced in order to find out, if they are used at the moment
		// if so -> don't allow them to be deleted!
		// so first create a Map with all used keys
		Map	TypeMap				 = new HashMap();
		Iterator iEvent			 = aEvents.iterator();
		while ( iEvent.hasNext() )
		{
			Event	ev = (Event)(iEvent.next());
			if ( !TypeMap.containsKey( ""+ev.getType() ) )
				TypeMap.put( ""+ev.getType(), new Boolean(true) );
		}
		// now get all Types and add them to the real List
		Iterator iType			 = aTypes.iterator();
		while ( iType.hasNext() )
		{
			EventType et	= (EventType)(iType.next());
			String add		= "false";
			if ( TypeMap.containsKey( ""+et.getID() ) )
				add = "true";
			String edit		= "false";
			if ( this.iEdit == et.getID() )
			{
				edit = "true";
				this.sEditType = et.getName();
			}
			LinkListBean llb = new LinkListBean( et.getID(), et.getName(), add, edit );
			this.cEventTypes.add( llb );
		}
		// init the rest
		this.cUserMails			 = new ArrayList();
		this.cLecturerMails		 = new ArrayList();
		Iterator it				 = GlobalVarsManager.getInstance().getUserMails().iterator();
		int i = 0;
		while ( it.hasNext() )
		{
			String host = (String)it.next();
			this.cUserMails.add( new DropDownEntry( i++, host, null ) );
		}
		it	= GlobalVarsManager.getInstance().getLecturerMails().iterator();
		i 	= 0;
		while ( it.hasNext() )
		{
			String host = (String)it.next();
			this.cLecturerMails.add( new DropDownEntry( i++, host, null ) );
		}
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
 	{
		ActionErrors errors = new ActionErrors();

		String host = null;
		if ( this.sAction.equals("Neuer User-Host") )
			host = this.sNewUserMail;
		if ( this.sAction.equals("Neuer Dozent-Host") )
			host = this.sNewLecturerMail;
		if ( host != null )
		{
			Pattern pat		= Pattern.compile("[\\.a-zA-Z0-9öäüß\\-_]+\\.[a-zA-Z]{2,5}");
			Matcher matcher	= pat.matcher(host);
			boolean flag	= matcher.matches();
			if ( !flag )
			{
				if ( this.sAction.equals("Neuer User-Host") )
					errors.add("global.UserHostInvalid", new ActionMessage("globals.hostinvalid"));
				else
					errors.add("global.LectHostInvalid", new ActionMessage("globals.hostinvalid"));
			}
		}
		if ( this.sAction.equals("Neuer Typ")
			 && ((this.sNewType==null) || (this.sNewType.length()<1)) )
			errors.add("global.TypeInvalid", new ActionMessage("globals.typeinvalid"));
		if ( this.sAction.equals("Umbenennen")
			 && ((this.sEditType==null) || (this.sEditType.length()<1)) )
			errors.add("global.TypeNameInvalid", new ActionMessage("globals.typeinvalid"));
		if ( this.sAction.equals("Speichern") )
		{
			Pattern pat		= Pattern.compile("[a-zA-Z0-9öäüß_\\-\\.]+@[\\.a-zA-Z0-9öäüß\\-_]+\\.[a-zA-Z]{2,5}");
			Matcher matcher	= pat.matcher( this.sSystemMail );
			boolean flag	= matcher.matches( );
			if ( (this.sSystemMail==null) || (this.sSystemMail.length()<1) || (!flag) )
				errors.add("global.SystemInvalid", new ActionMessage("globals.systeminvalid"));
			if ( (this.sRegisterSubject==null) || (this.sRegisterSubject.length()<1) )
				errors.add("global.SubjectInvalid", new ActionMessage("globals.subjectinvalid"));
		}
		return errors;
	}
}