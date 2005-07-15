/*
File:       ViewUserAction.java
Created:    05-06-11@23:00
Task:       Action for viewUsers.jsp
Author:     Michael Thiele

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
import de.tr1.cooperator.model.web.ViewUsersForm;
import de.tr1.cooperator.model.web.UserSession;
import de.tr1.cooperator.manager.mainframe.UserManager;
import de.tr1.cooperator.manager.mainframe.EventManager;
import de.tr1.cooperator.manager.web.LoginManager;
import de.tr1.cooperator.model.mainframe.*;

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
import java.util.*;


/**
 * This class handles the actions called by the struts-framework
 *
 * @author Michael Thiele
 * @version 05-06-12@09:00
 */
public class ViewUsersAction extends LookupDispatchAction
{
	public ActionForward execute(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception, IOException
	{
		String parameter = mapping.getParameter();
        if( parameter != null )
        {
			String keyName = request.getParameter( parameter );
			if( "deleteUser".equals( keyName ) )
			{
				return deleteUser( mapping, form, request, response );
        	}
			else if( "showUser".equals( keyName ) )
			{
				return showUser( mapping, form, request, response );
        	}
			else if( "killUser".equals( keyName ) )
			{
				return killUser( mapping, form, request, response );
        	}
        	else if( "back".equals( keyName ) )
			{
				return back( mapping, form, request, response );
        	}
        	else if( "next".equals( keyName ) )
			{
				return next( mapping, form, request, response );
        	}
        	else if( "overtake".equals( keyName ) )
			{
				return overtake( mapping, form, request, response );
        	}
        	else
        		return super.execute( mapping, form, request, response );
        }
        else
        	return super.execute( mapping, form, request, response );
	}

	/**
     * updates the site wiht new selected dropdown-value
     */
    public ActionForward changeUserType(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception, IOException
    {
        ActionForward forward = mapping.findForward( "GeneralFailure" );

        //this is need for update the values...
        ViewUsersForm vuForm = (ViewUsersForm) form;

        vuForm.initValues( request, vuForm.getSelectedUserValue(), vuForm.getCurCount(), vuForm.getDesiredCount() );    //init with correct collection

        forward = mapping.findForward( "RefreshSite" );

        return forward;
    }

    /**
     * link to create a new user
     */
    public ActionForward createUser(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception, IOException
    {
        ActionForward   forward = mapping.findForward( "GeneralFailure" );
        HttpSession curSession = request.getSession();
        if( curSession == null )
            return forward;

        forward = mapping.findForward( "CreateUser" );
        return forward;
    }

    /**
     * link to show selected user profile
     */
    public ActionForward showUser(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception, IOException
    {
        ActionForward   forward = mapping.findForward( "GeneralFailure" );
        HttpSession curSession = request.getSession();
        if( curSession == null )
            return forward;

        forward = mapping.findForward( "ShowUser" );
        return forward;
    }

    /**
     * link for deleting a user
     */
    public ActionForward deleteUser(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception, IOException
    {
        ActionForward   forward = mapping.findForward( "GeneralFailure" );
        HttpSession curSession = request.getSession();
        if( curSession == null )
            return forward;

        forward = mapping.findForward( "DeleteUser" );
        return forward;
    }

    /**
     * updates the site after killing a logged in user
     */
    public ActionForward killUser(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception, IOException
    {
        ActionForward forward = mapping.findForward( "GeneralFailure" );

		String sessid			= request.getParameter( "sessid" );
		ViewUsersForm	vuForm	= (ViewUsersForm)(form);
		Collection		logs	= vuForm.getLoggedUserList();
		Iterator 		sess	= logs.iterator();

		if( !vuForm.getMySessionId().equals(sessid) )
		{
			while ( sess.hasNext() )
			{
				UserSession USession= (UserSession)(sess.next());
				if ( USession.getSessionId().equals(sessid) )
				{
					LoginManager TempLoginManager = LoginManager.getInstance();
		    	    TempLoginManager.kill( USession );
			        break;
			    }
			}
		}
        //this is need for update the values...
        vuForm.initValues( request, vuForm.getSelectedUserValue(), vuForm.getCurCount(), vuForm.getDesiredCount() );    //init with correct collection

        forward = mapping.findForward( "RefreshSite" );

        return forward;
    }

    /**
     * updates the site after killing all logged in users
     */
    public ActionForward killAllUsers(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception, IOException
    {
        ActionForward forward = mapping.findForward( "GeneralFailure" );

		ViewUsersForm	myForm	= (ViewUsersForm)(form);
		UserSession USession	= new UserSession(	request.getSession(true),
													myForm.getUser().getLogin(),
													new Date().getTime() );

        // Kill all Users
        LoginManager.getInstance().killAll();
        // re-init me or only the session will be enough
		USession.setSession( request.getSession(true) );
		// re-enter me
        LoginManager.getInstance().add( USession );
		USession.getSession().setAttribute( "login", myForm.getUser().getLogin() );

        //this is need for update the values...
        ViewUsersForm vuForm = (ViewUsersForm) form;
        vuForm.initValues( request, vuForm.getSelectedUserValue(), vuForm.getCurCount(), vuForm.getDesiredCount() );    //init with correct collection

        forward = mapping.findForward( "RefreshSite" );

        return forward;
    }

    /**
     * list the a specified number of users, the users in front of the topical shown users, if there are some
     */
    public ActionForward back(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception, IOException
    {
        ActionForward   forward = mapping.findForward( "GeneralFailure" );
        HttpSession curSession = request.getSession();
        if( curSession == null )
            return forward;

		// get the combined request-parameter
		String requestString 	= request.getParameter( "selectedUserValueCurCountDesiredCount" );
		StringTokenizer st 		= new StringTokenizer( requestString, " " );
		String value 		= st.nextToken();
		int curCount 		= Integer.parseInt( st.nextToken() );
		int desiredCount 	= Integer.parseInt( st.nextToken() );

		/*
		int i 		= Integer.parseInt( s );
		String value 		= "" + ((int)( i/100 ));
		int curCount 		= (int)((i%100)/10);
		int desiredCount 	= i%10;*/

		//this is need for update the values...
        ViewUsersForm vuForm = (ViewUsersForm) form;

        vuForm.initValues( request, value, ( curCount - desiredCount ),desiredCount );    //init with correct collection

        forward = mapping.findForward( "RefreshSite" );
        return forward;
    }

    /**
     * list the a specified number of users, the users after the topical shown users, if there are some
     */
    public ActionForward next(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception, IOException
    {
        ActionForward   forward = mapping.findForward( "GeneralFailure" );
        HttpSession curSession = request.getSession();
        if( curSession == null )
            return forward;

		// get the combined request-parameter
		String requestString 	= request.getParameter( "selectedUserValueCurCountDesiredCount" );
		StringTokenizer st 		= new StringTokenizer( requestString, " " );
		String value 		= st.nextToken();
		int curCount 		= Integer.parseInt( st.nextToken() );
		int desiredCount 	= Integer.parseInt( st.nextToken() );
		/*
		int i 		= Integer.parseInt( s );
		String value 		= "" + ((int)( i/100 ));
		int curCount 		= (int)((i%100)/10);
		int desiredCount 	= i%10;*/

        //this is need for update the values...
        ViewUsersForm vuForm = (ViewUsersForm) form;

        vuForm.initValues( request, value, ( curCount + desiredCount ), desiredCount );    //init with correct collection

        forward = mapping.findForward( "RefreshSite" );
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
        map.put( "viewUsers.buttonChangeType",     "changeUserType" );
		map.put( "viewUsers.buttonCreateUser",     "createUser" );
		map.put( "viewUsers.buttonKillAllExeptMe", "killAllUsers" );
		return map;
	}

    /**
     * Gives the user another identity
     */
    public ActionForward overtake(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception, IOException
    {
        ActionForward forward	= mapping.findForward( "GeneralFailure" );

		ViewUsersForm	myForm	= (ViewUsersForm)(form);
		String userlogin		= request.getParameter( "login" );
		if ( (userlogin==null) || (userlogin.length()<1) )
		{
			//this is need for update the values...
			myForm.initValues( request, myForm.getSelectedUserValue(), myForm.getCurCount(), myForm.getDesiredCount() );    //init with correct collection
			// exit
			return forward = mapping.findForward( "RefreshSite" );
		}

		// create the session that has to be killed
		UserSession USession	= new UserSession(	request.getSession(true),
													myForm.getUser().getLogin(),
													new Date().getTime() );
        // Kill this User
        LoginManager.getInstance().kill( USession );
        // recreate the session with the new login
		USession.setSession( request.getSession(true) );
		USession.setUserLogin( userlogin );
		// re-enter me
        LoginManager.getInstance().add( USession );
		USession.getSession().setAttribute( "login", userlogin );

		forward = mapping.findForward( "GoToViewEvents" );
        return forward;
    }

}