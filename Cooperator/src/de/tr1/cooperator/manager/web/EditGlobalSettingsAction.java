/*
File:		EditGlobalSettingsAction.java
Created:	05-06-07@11:00
Task:		Action for editing global settings
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


import de.tr1.cooperator.manager.mainframe.UserManager;
import de.tr1.cooperator.manager.mainframe.EventTypeManager;
import de.tr1.cooperator.manager.system.*;
import de.tr1.cooperator.model.mainframe.User;
import de.tr1.cooperator.model.mainframe.EventType;
import de.tr1.cooperator.exceptions.*;
import de.tr1.cooperator.manager.ManagerInitializer;
import de.tr1.cooperator.manager.web.*;
import de.tr1.cooperator.model.web.*;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.*;
import java.util.regex.*;

import org.apache.struts.actions.DispatchAction;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;




/**
 * This class handles the actions called by the struts-framework
 *
 * @author Sebastian Kohl
 * @version 05-06-07@11:00
 */
public class EditGlobalSettingsAction extends Action
{
	/**
	 * This method is called by the struts-framework if the submit-button is pressed
	 */
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		ActionForward forward = mapping.findForward( "GeneralFailure" );

		EditGlobalsForm myForm = (EditGlobalsForm) form;

		try
		{
			UserManager manager = UserManager.getInstance();
			User userLogin = myForm.getUser();

			// set Attributes if User is Admin or Docent
			if( !myForm.hasAdminRights() )
			{
				myForm.initValues( request );
				return mapping.findForward("UpdateFailed");
			}
			String do_param = myForm.getAction( );
			if ( do_param.length() < 1 )
			{
				myForm.initValues( request );
				return mapping.findForward("UpdateNoUser");
			}
			String host = null;

			if ( myForm.getAction().equals("Neuer User-Host") )
			{
				if ( !GlobalVarsManager.getInstance().addUserMail( myForm.getNewUserMail() ) )
				{
					myForm.initValues( request );
					myForm.setUserMessage( "Konnte den Host "+myForm.getNewUserMail()+" nicht hinzufügen" );
					return mapping.findForward( "UpdateFailed" );
				}
				myForm.initValues( request );
				return mapping.findForward( "UpdateSuccessfull" );
			}

			if ( myForm.getAction().equals("Neuer Dozent-Host") )
			{
				if ( !GlobalVarsManager.getInstance().addLecturerMail( myForm.getNewLecturerMail() ) )
				{
					myForm.initValues( request );
					myForm.setLecturerMessage( "Konnte den Host "+myForm.getNewLecturerMail()+" nicht hinzufügen" );
					return mapping.findForward( "UpdateFailed" );
				}
				myForm.initValues( request );
				return mapping.findForward( "UpdateSuccessfull" );
			}

			if ( myForm.getAction().equals("Speichern") )
			{
				if ( !GlobalVarsManager.getInstance().setSystemMail( myForm.getSystemMail() ) )
				{
					myForm.initValues( request );
					myForm.setSaveMessage( "Konnte die System-eMail-Adresse nicht speichern" );
					return mapping.findForward( "UpdateFailed" );
				}
				if ( !GlobalVarsManager.getInstance().setRegisterSubject( myForm.getRegisterSubject() ) )
				{
					myForm.initValues( request );
					myForm.setSaveMessage( "Konnte den Registrierungs-Betreff nicht speichern" );
					return mapping.findForward( "UpdateFailed" );
				}
				if ( !GlobalVarsManager.getInstance().forceUserMails( myForm.getNewForceUserMails() ) )
				{
					myForm.initValues( request );
					myForm.setSaveMessage( "Konnte die User-Einstellung nicht speichern" );
					return mapping.findForward( "UpdateFailed" );
				}
				if ( !GlobalVarsManager.getInstance().forceLecturerMails( myForm.getNewForceLecturerMails() ) )
				{
					myForm.initValues( request );
					myForm.setSaveMessage( "Konnte die Dozenten-Einstellung nicht speichern" );
					return mapping.findForward( "UpdateFailed" );
				}
				myForm.initValues( request );
				return mapping.findForward( "UpdateSuccessfull" );
			}

			if ( myForm.getAction().equals("Neuer Typ") )
			{
				String name = myForm.getNewType();
				int id = EventTypeManager.getInstance().getFreeID();
				if ( (id==-1) || (!EventTypeManager.getInstance().add( name, id )) )
				{
					myForm.initValues( request );
					myForm.setLecturerMessage( "Konnte den Typ "+name+" nicht hinzufügen" );
					return mapping.findForward( "UpdateFailed" );
				}
				myForm.initValues( request );
				return mapping.findForward( "UpdateSuccessfull" );
			}

			if ( myForm.getAction().equals("Umbenennen") )
			{
				String name = myForm.getEditType();
				int tid  = myForm.getEdit();
				if ( (tid==-1) || (!EventTypeManager.getInstance().update( name, tid )) )
				{
					myForm.initValues( request );
					myForm.setTypeNameMessage( "Konnte den Typ "+EventTypeManager.getInstance().getEventName(tid)+" ("+tid+") nicht umbenennen" );
					return mapping.findForward( "UpdateFailed" );
				}
				myForm.initValues( request );
				return mapping.findForward( "UpdateSuccessfull" );
			}

			String action = myForm.getAction();
			if ( action==null )
				action = request.getParameter( "action" );
			if ( action.equals("deleteUserHost") )
			{
				String mail = request.getParameter( "mail" );
				if ( (mail==null) || (mail.length()<1) )
				{
					myForm.initValues( request );
					myForm.setUserMessage( "Konnte den Host #"+mail+" nicht entfernen" );
					return mapping.findForward( "UpdateFailed" );
				}
				int mindex = -1;
				try
				{
					mindex = Integer.parseInt( mail );
				}
				catch ( Exception e ){}
				// grab the mail by the given index
				Iterator it = myForm.getUserMails().iterator();
				DropDownEntry dde = null;
				while ( it.hasNext() )
				{
					DropDownEntry dde2 = (DropDownEntry)(it.next());
					if ( dde2.getValue()==mindex )
					{
						dde = dde2;
						break;
					}
				}
				if ( (dde==null) || (!GlobalVarsManager.getInstance().removeUserMail( dde.getLabel() )) )
				{
					myForm.initValues( request );
					myForm.setUserMessage( "Konnte den Host #"+mail+" nicht entfernen" );
					return mapping.findForward( "UpdateFailed" );
				}
				myForm.initValues( request );
				return mapping.findForward( "UpdateSuccessfull" );
			}

			if ( action.equals("deleteLectHost") )
			{
				String mail = request.getParameter( "mail" );
				if ( (mail==null) || (mail.length()<1) )
				{
					myForm.initValues( request );
					myForm.setLecturerMessage( "Konnte den Host #"+mail+" nicht entfernen" );
					return mapping.findForward( "UpdateFailed" );
				}
				int mindex = -1;
				try
				{
					mindex = Integer.parseInt( mail );
				}
				catch ( Exception e ){}
				// grab the mail by the given index
				Iterator it = myForm.getLecturerMails().iterator();
				DropDownEntry dde = null;
				while ( it.hasNext() )
				{
					DropDownEntry dde2 = (DropDownEntry)(it.next());
					if ( dde2.getValue()==mindex )
					{
						dde = dde2;
						break;
					}
				}
				if ( (dde==null) || (!GlobalVarsManager.getInstance().removeLecturerMail( dde.getLabel() )) )
				{
					myForm.initValues( request );
					myForm.setLecturerMessage( "Konnte den Host #"+mail+" nicht entfernen" );
					return mapping.findForward( "UpdateFailed" );
				}
				myForm.initValues( request );
				return mapping.findForward( "UpdateSuccessfull" );
			}

			if ( action.equals("deleteType") )
			{
				String type = request.getParameter( "type" );
				if ( (type==null) || (type.length()<1) )
				{
					myForm.initValues( request );
					myForm.setTypeNameMessage( "Konnte den Typ #"+type+" nicht entfernen" );
					return mapping.findForward( "UpdateFailed" );
				}
				int mindex = -1;
				try
				{
					mindex = Integer.parseInt( type );
				}
				catch ( Exception e ){}
				// check if this type is used
				Iterator iLBB	 = myForm.getEventTypes().iterator();
				while ( iLBB.hasNext() )
				{
					LinkListBean lbb = (LinkListBean)(iLBB.next());
					if ( (lbb.getID()==mindex) && (lbb.getAdditional().equals("true")) )
					{
						myForm.initValues( request );
						myForm.setTypeNameMessage( "Konnte den Typ #"+type+" nicht entfernen, wird verwendet!" );
						return mapping.findForward( "UpdateFailed" );
					}
				}
				if ( !((mindex!=-1) && (EventTypeManager.getInstance().delete( mindex ))) )
				{
					myForm.initValues( request );
					myForm.setTypeNameMessage( "Konnte den Typ #"+type+" nicht entfernen" );
					return mapping.findForward( "UpdateFailed" );
				}
				myForm.initValues( request );
				return mapping.findForward( "UpdateSuccessfull" );
			}
		}
		catch(SingletonException se)
		{
			Log.addLog( "Can't change Profile because of a bad or not initialized Singleton: " + se );
			forward = mapping.findForward( "GeneralFailure" );
		}
		catch (Exception e)
		{
			myForm.initValues( request );
			myForm.setSaveMessage( "Konnte die Einstellung nicht speichern" );
			return mapping.findForward( "UpdateFailed" );
		}
		return forward;
	}

}