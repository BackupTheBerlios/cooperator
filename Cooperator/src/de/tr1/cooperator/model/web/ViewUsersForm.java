/*
File:       ViewUsersForm.java
Created:    05-06-11@15:00
Task:       FormBean for viewUsers.jsp
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
package de.tr1.cooperator.model.web;

import de.tr1.cooperator.manager.system.Log;
import de.tr1.cooperator.model.mainframe.User;
import de.tr1.cooperator.model.web.UserSession;
import de.tr1.cooperator.manager.mainframe.UserManager;
import de.tr1.cooperator.manager.web.LoginManager;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import java.util.*;

/**
 * FormBean for viewUsers.jsp
 *
 * @author Michael Thiele
 * @version 05-06-11@15:30
 */
public class ViewUsersForm extends Accessable
{
    private final	String	DROPDOWNALL				= "alle User";
    private final	String	DROPDOWNALLFROM			= "alle ";
    private final	String	DROPDOWNADMINS			= "Administratoren";
    private final	String	DROPDOWNLECTURERS		= "Dozenten";
    private final	String	DROPDOWNSTUDENTS		= "Studenten";

    private Collection	cSelectUserList;            // this is needed for the Drop-Down-Menu
    private String		sSelectedUserValue;         // this is needed for the Drop-Down-Menu

    private Collection	cUserList;                  // this stores all registered users
    private Collection	cCurUserList;               // this stores all the currently displayed users
    private Collection	cLoggedUserList;            // this stores all registered users, which are currently logged in
    private boolean		bHasUsers;                  // if no users are present...
    private boolean		bHasLoggedUsers;			// if no users are logged in...

    private boolean		bCurCountFirst;				// is list on first position?
	private boolean		bCurCountLast;				// is list on last position?
	private int			iUserCount;					// how many user are registert
	private int			iCurCount;					// which part of the userlist is shown topicaly. show users from index (iCurCount * SHOWUSERCOUNT) to ( (iCurCount * SHOWUSERCOUNT) + SHOWUSERCOUNT)
	private int			iDesiredCount;				// desired number of users to show at the same time
    /**
     * Creates an ArrayList including the possible dropdown values
     *
     * @return ArrayList
     */
    public ArrayList createDropDownArrayList()
    {
        ArrayList           alReturn;
        int                 iEntryCounter = 0;

        alReturn = new ArrayList();

        alReturn.add( new DropDownEntry( iEntryCounter++, DROPDOWNALL, new Integer( -1 ) ) );
        alReturn.add( new DropDownEntry( iEntryCounter++, DROPDOWNADMINS, new Integer( -1 ) ) );
        alReturn.add( new DropDownEntry( iEntryCounter++, DROPDOWNLECTURERS, new Integer( -1 ) ) );
        alReturn.add( new DropDownEntry( iEntryCounter++, DROPDOWNSTUDENTS, new Integer( -1 ) ) );

        return alReturn;
    }


    /**
     * This method intialises all the values
     *
     * @param request this is need if some values from the session are needed
     */
    synchronized public void initValues( HttpServletRequest request )
    {
        super.initValues( request );

        //init with standart....
        initValues( request, "0", 1, 20 );
    }

    /**
     * This method intialises all the values
     *
     * @param treeRoot 2do: this will be the treeRoot for the init...
     * @param request this is need if some values from the session are needed
     */
    synchronized public void initValues( HttpServletRequest request, String sWhatUserType, int newCurCount, int newDesiredCount )
    {
        super.initValues( request );

        if( this.hasAdminRights( ) )
        {
        	//create standard dropdown list wiht selected value
        	this.cSelectUserList = createDropDownArrayList();
        	sSelectedUserValue = sWhatUserType;


        	// create selected userlist

        	Collection cCreateUserList;
        	try
        	{
            	UserManager TempUserManager = UserManager.getInstance();
            	cCreateUserList = TempUserManager.getAllUsers();

            	//check if there are some elements in this collection
            	if( cCreateUserList.size() == 0 )
            		cCreateUserList = null;
        	}
        	catch( Exception e )        //2do: this should only be a SingletonException (or IllegalArgumentException)
        	{
            	Log.addLog( "UserManager not initialized or with bad XML-File" );
            	cCreateUserList = null;
        	}

			if( newCurCount < 1 )
				  iCurCount = 1;
			else
				iCurCount = newCurCount;

        	iDesiredCount	= newDesiredCount;
        	bCurCountFirst 	= false;
        	bCurCountLast 	= false;
        	iUserCount		= 0;

        	if( cCreateUserList != null )
        	{
            	bHasUsers = true;

            	Collection cNewUserList = new ArrayList();
            	User currentUser;
            	int iCUR;

            	Iterator itNewSelectedUserList = cCreateUserList.iterator();
            	while( itNewSelectedUserList.hasNext())
            	{
                	currentUser = (User) itNewSelectedUserList.next();
                	iCUR = currentUser.getRights();

                	// compare wiht selected usertype and choose which users get in displayed list
                   	if( sSelectedUserValue.equals( "1" ) )
                   	{
                       	if( iCUR == 4 || iCUR == 6 || iCUR == 5 || iCUR == 7)
                           	cNewUserList.add(currentUser);
                   	}
                   	else if( sSelectedUserValue.equals( "2" ) )
                   	{
                       	if( iCUR == 2 || iCUR == 3 || iCUR == 6 || iCUR == 7)
                           	cNewUserList.add(currentUser);
                   	}
                   	else if( sSelectedUserValue.equals( "3" ) )
                   	{
                       	if( iCUR == 1 || iCUR == 3 || iCUR == 5 || iCUR == 7)
                           	cNewUserList.add(currentUser);
                   	}
                   	else cNewUserList.add(currentUser);
	       		}

				cUserList = new ArrayList();
				Collections.sort( (List) cNewUserList );
            	cUserList = cNewUserList;

            	// now show only a specified number of users

            	iUserCount = cUserList.size();
            	if( iCurCount > 1)
            		bCurCountFirst = true;
            	if( iCurCount + iDesiredCount -1 < iUserCount )
            		bCurCountLast = true;

            	User curUser;
            	cCurUserList	= new ArrayList();
            	int itCount		= 1;
            	int countFrom	= iCurCount;
            	int countTo		= iCurCount + iDesiredCount;
            	Iterator it = cUserList.iterator();
            	while( it.hasNext() )
            	{
            		curUser = (User) it.next();
            		if( (itCount >= countFrom) && (itCount < countTo) )
            		{
            			cCurUserList.add(curUser);
            		}
            		itCount++;
            	}
        	}
        	else
        	{
            	bHasUsers = false;
            	cUserList = null;
        	}

        	// create logged-in userlist
        	Collection cCreateLoggedUserList;
        	try
        	{
            	LoginManager TempLoginManager = LoginManager.getInstance();
            	cCreateLoggedUserList = TempLoginManager.getAllAsCollection();

            	//check if there are some elements in this collection
            	if( cCreateLoggedUserList.size() == 0 )
            		cCreateLoggedUserList = null;
        	}
        	catch( Exception e )        //2do: this should only be a SingletonException (or IllegalArgumentException)
        	{
        		Log.addLog( "LoginManager not initialized or with bad hUserList" );
        		cCreateLoggedUserList = null;
        	}

        	if( cCreateLoggedUserList != null )
        	{
            	bHasLoggedUsers = true;
            	cLoggedUserList = cCreateLoggedUserList;
        	}
        	else
        	{
            	bHasLoggedUsers = false;
            	cLoggedUserList = null;
        	}
        }
        else
        	sErrorMessage += " Sie besitzen keine Administratorrechte.";
    }

    /**
     * This method validates the input
     *
     * @param mapping this is the forward (see also your struts-config.xml)
     * @param request
     * @return Collection (?) of Errors which occured during validation...
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
    {
		ActionErrors errors = new ActionErrors();
        return errors;
    }



    //GETTER AND SETTER

    /**
     * This method is called by the struts-tag-library
     *
     * @return Collection for the DropDownMenue
     */
    public Collection getSelectUserList()
    {
        return this.cSelectUserList;
    }

    /**
     * This method is called by the struts-tag-library
     *
     * @return unique ID for the DropDownMenu (for preselect)
     */
    public String getSelectedUserValue()
    {
        return this.sSelectedUserValue;
    }
    /**
     * This method is called by the struts-framework
     *
     * @param SelectUserType this is the unique ID of the DropDownMenu
     */
    public void setSelectedUserValue( String SelectUserType )
    {
        this.sSelectedUserValue = SelectUserType;
    }

    /**
     * This method is called by the struts-tag-library
     *
     * @return Collection of UserEntrys
     */
    public Collection getUserList()
    {
        return this.cUserList;
    }

    /**
     * This method is called by the struts-tag-library
     *
     * @return Collection of UserEntrys
     */
    public Collection getLoggedUserList()
    {
        return this.cLoggedUserList;
    }

    /**
     * This method is called by the struts-tag-library
     *
     * @return boolean true if some users are registered
     */
    public String getHasUsers()
    {
        return ""+this.bHasUsers;
    }

    /**
     * This method is called by the struts-tag-library
     *
     * @return boolean true if some users are currently logged in
     */
    public String getHasLoggedUsers()
    {
        return ""+this.bHasLoggedUsers;
    }

    public Collection getCurUserList()
    {
        return this.cCurUserList;
    }

    public int getUserCount()
    {
        return this.iUserCount;
    }

    public int getCurCount()
    {
        return this.iCurCount;
    }

    public int getCurCountAddDesired()
    {
    	if( this.iCurCount + this.iDesiredCount -1 <= iUserCount )
    		return (this.iCurCount + this.iDesiredCount -1 );
    	return iUserCount;
    }

    public boolean getCurCountFirst()
    {
        return this.bCurCountFirst;
    }

    public boolean getCurCountLast()
    {
        return this.bCurCountLast;
    }

    public void setDesiredCount(int count)
    {
        this.iDesiredCount = count;
    }

    public int getDesiredCount()
    {
        return this.iDesiredCount;
    }

    public String getSelectedUserValueCurCountDesiredCount()
    {
    	String s = ""+ this.sSelectedUserValue +" "+ this.iCurCount +" "+ this.iDesiredCount;
        return s;
    }
}