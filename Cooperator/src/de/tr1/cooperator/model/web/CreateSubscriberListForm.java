/*
File:		CreateSubscriberListForm.java
Created:	05-06-17@23:50
Task:		This is the Form-Bean for the createSubscriberList.jsp
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

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import java.util.Collection;
import java.util.ArrayList;

/**
 * This is the Form-Bean for the createSubscriberList.jsp
 * This stores all the date of the formular and verifys for correct input
 *
 * @author Peter Matjeschk
 * @version 05-06-17@23:55
 */
public class CreateSubscriberListForm extends Accessable
{
	public	final	String				SORTBYPN	= "1";
	public	final	String				SORTBYNAME	= "2";
	
	private	final	String				NOTENOUGHRIGHTS = "Kein Admin oder Dozent! \r\n";

	private	String	sHeaderLeft;			//text which is in the header on the left
	private	String	sHeaderRight;			//text which is in the header on the right
	private	String	sInfoText;				//text which is shown direclty befor the table starts

	private	boolean	bShowNumber;			//show running number or not
	private	boolean	bShowPersonalNumber;	//show personal number or not
	private	boolean	bShowName;				//show name and Surname or not
	private boolean	bShowEmail;				//show email or not
	private	boolean	bShowResult;			//show result or not
	private	boolean	bAddInfoField;			//add field for notes or not
	private	boolean	bAddSignField;			//add field for sign or not

	private	boolean	bShowNumberNew;			//show running number or not
	private	boolean	bShowPersonalNumberNew;	//show personal number or not
	private	boolean	bShowNameNew;			//show name and Surname or not
	private boolean	bShowEmailNew;			//show email or not
	private	boolean	bShowResultNew;			//show result or not
	private	boolean	bAddInfoFieldNew;		//add field for notes or not
	private	boolean	bAddSignFieldNew;		//add field for sign or not
	
	private	String	sSortBy;				//sort-by


	private	String	sEventID;
	

	synchronized public void initValues( HttpServletRequest request )
	{
		super.initValues( request );

		this.sEventID = request.getParameter( "eventID" );


		this.sInfoText = "";
		this.sHeaderLeft = "";
		this.sHeaderRight = "";

		this.bShowNumber			= true;
		this.bShowPersonalNumber	= true;
		this.bShowName				= true;
		this.bShowEmail				= false;
		this.bShowResult			= false;
		this.bAddInfoField			= true;
		this.bAddSignField			= true;

		this.bShowNumberNew			= false;
		this.bShowPersonalNumberNew	= false;
		this.bShowNameNew			= false;
		this.bShowEmailNew			= false;
		this.bShowResultNew			= false;
		this.bAddInfoFieldNew		= false;
		this.bAddSignFieldNew		= false;
		
		this.sSortBy				= "1";

		//not logged in (anymore)
		if( this.hUser == null )
		
			return;
		if( !(this.hasAdminRights() || this.hasLecturerRights()) )
			this.sErrorMessage += NOTENOUGHRIGHTS ;
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
 	{
		ActionErrors errors = new ActionErrors();

		//set status of checkboxes to the values
		this.bShowNumber			= this.bShowNumberNew;
		this.bShowPersonalNumber	= this.bShowPersonalNumberNew;
		this.bShowName				= this.bShowNameNew;
		this.bShowEmail				= this.bShowEmailNew;
		this.bShowResult			= this.bShowResultNew;
		this.bAddInfoField			= this.bAddInfoFieldNew;
		this.bAddSignField			= this.bAddSignFieldNew;

		//check for bad combinations
		if( !(this.bShowName || this.bShowPersonalNumber) )
			errors.add( "createsubscriberlist.noidentityidentifier", new ActionMessage( "createsubscriberlist.noidentity" ) );
			
		//check if sort by a non shown-field...
		if( (!this.bShowName && this.sSortBy.equals( SORTBYNAME ) ) )
			errors.add( "createsubscriberList.badsortby", new ActionMessage( "createsubscriberlist.badsortby" ) );
		if( (!this.bShowPersonalNumber && this.sSortBy.equals( SORTBYPN ) ) )
			errors.add( "createsubscriberList.badsortby", new ActionMessage( "createsubscriberlist.badsortby" ) );

		return errors;
	}


//Getter and Setter for the Elements...
	public String getHeaderLeft()
	{
		return this.sHeaderLeft;
	}
	public void setHeaderLeft( String sHeaderLeft )
	{
		this.sHeaderLeft = sHeaderLeft;
	}

	public String getHeaderRight()
	{
		return this.sHeaderRight;
	}
	public void setHeaderRight( String sHeaderRight )
	{
		this.sHeaderRight = sHeaderRight;
	}

	public String getInfoText()
	{
		return this.sInfoText;
	}
	public void setInfoText( String sInfoText )
	{
		this.sInfoText = sInfoText;
	}

	public boolean getShowNumber()
	{
		return this.bShowNumber;
	}
	public void setShowNumber( boolean bShowNumber )
	{
		this.bShowNumberNew = bShowNumber;
	}

	public boolean getShowName()
	{
		return this.bShowName;
	}
	public void setShowName( boolean bShowName )
	{
		this.bShowNameNew = bShowName;
	}

	public boolean getShowPersonalNumber()
	{
		return this.bShowPersonalNumber;
	}
	public void setShowPersonalNumber( boolean bShowPersonalNumber )
	{
		this.bShowPersonalNumberNew = bShowPersonalNumber;
	}

	public boolean getShowEmail()
	{
		return this.bShowEmail;
	}
	public void setShowEmail( boolean bShowEmail )
	{
		this.bShowEmailNew = bShowEmail;
	}

	public boolean getShowResult()
	{
		return this.bShowResult;
	}
	public void setShowResult( boolean bShowResult )
	{
		this.bShowResultNew = bShowResult;
	}

	public boolean getAddInfoField()
	{
		return this.bAddInfoField;
	}
	public void setAddInfoField( boolean bAddInfoField )
	{
		this.bAddInfoFieldNew = bAddInfoField;
	}

	public boolean getAddSignField()
	{
		return this.bAddSignField;
	}
	public void setAddSignField( boolean bAddSignField )
	{
		this.bAddSignFieldNew = bAddSignField;
	}

	public int	getEventID()
	{
		try
		{
			int ret = Integer.parseInt( sEventID );
			return ret;
		}
		catch( NumberFormatException nfe )
		{
			return -1;
		}
	}

	public String	getEventIDString()
	{
		return this.sEventID;
	}

	public void	setEventIDString( String sEventID)
	{
		this.sEventID = sEventID;
	}
	
	public String	getSortBy()
	{
		return this.sSortBy;
	}
	
	public void	setSortBy( String sSortBy )
	{
		this.sSortBy = sSortBy;
	}


}