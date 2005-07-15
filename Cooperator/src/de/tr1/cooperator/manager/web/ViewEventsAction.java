/*
File:		ViewEventsAction.java
Created:	05-05-30@11:00
Task:		Action for viewEvents.jsp
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
package de.tr1.cooperator.manager.web;

import de.tr1.cooperator.manager.system.Log;
import de.tr1.cooperator.model.web.ViewEventsForm;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.actions.LookupDispatchAction;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.ServletOutputStream;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;


/**
 * This class handles the actions called by the struts-framework
 *
 * @author Peter Matjeschk
 * @version 05-05-30@21:00
 */
public class ViewEventsAction extends LookupDispatchAction
{
	public ActionForward changeEventType(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception, IOException
	{
		ActionForward forward = mapping.findForward( "GeneralFailure" );

		//this is need for update the values...
		ViewEventsForm veForm = (ViewEventsForm) form;
		veForm.initValues( request, "-1", veForm.getSelectedEventValue() );	//init with correct collection

		forward = mapping.findForward( "SelectEventType" );

		return forward;
	}

	public ActionForward createEvent(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception, IOException
	{
		ActionForward	forward = mapping.findForward( "GeneralFailure" );
		HttpSession curSession = request.getSession();
		if( curSession == null )
			return forward;
		
		//we can't pass a param so set an attribute into the session
		curSession.setAttribute( "create", "true" );
		
		forward = mapping.findForward( "CreateEvent" );
		return forward;
	}


    /**
	* Maps the given request to the correct sub-method in this class
    * @return mapping
    */
    protected Map getKeyMethodMap()
    {
		Map map = new HashMap();

		//name of parameter, name of method to run
		map.put( "viewEvents.buttonChangeType", "changeEventType" );
		map.put( "viewEvents.buttonCreateEvent", "createEvent" );
		return map;
	}



}