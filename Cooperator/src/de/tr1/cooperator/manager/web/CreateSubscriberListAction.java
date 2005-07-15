/*
File:		CreateSubscriberListAction.java
Created:	05-06-17@23:55
Task:		Creates PDF with the given parameters...
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

import de.tr1.cooperator.manager.mainframe.EventManager;
import de.tr1.cooperator.manager.mainframe.EventResultManager;
import de.tr1.cooperator.manager.mainframe.UserManager;
import de.tr1.cooperator.manager.system.Log;
import de.tr1.cooperator.model.mainframe.Event;
import de.tr1.cooperator.model.mainframe.ExamResult;
import de.tr1.cooperator.model.mainframe.User;
import de.tr1.cooperator.model.web.CreateSubscriberListForm;
import de.tr1.cooperator.model.web.UserResult;
import de.tr1.cooperator.model.web.UserResultSortByName;
import de.tr1.cooperator.model.web.UserResultSortByPersonalNumber;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;


import java.text.*;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import java.io.FileOutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.Iterator;
import java.util.Collections;


/**
 * This class handles the actions called by the struts-framework
 *
 * @author Peter Matjeschk
 * @version 05-06-20@12:00
 */
public class CreateSubscriberListAction extends Action
{
	private	final	int		TABLECELL_FONTSIZE = 10;
	private	final	int		TABLEHEADER_FONTSIZE = 12;

	private	final	String	TABLEHEADER_NUMBER			= "Nr.  ";
	private	final	String	TABLEHEADER_PERSONALNUMBER	= "Mtr.-Nr.";
	private	final	String	TABLEHEADER_NAME			= "Name                        ";
	private	final	String	TABLEHEADER_EMAIL			= "eMail                                ";
	private	final	String	TABLEHEADER_RESULT			= "Ergebnis";
	private	final	String	TABLEHEADER_INFO			= "Bemerkungen    ";
	private	final	String	TABLEHEADER_SIGN			= "Unterschrift";

	/**
	 * This method is called by the struts-framework if the submit-button is pressed.
	 * It creates a PDF-File and puts in into the output-stream of the response.
	 */
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{

		CreateSubscriberListForm	myForm = (CreateSubscriberListForm) form;
		boolean						bSortByName;

		if( myForm.getSortBy().equals( myForm.SORTBYPN ) )
			bSortByName = false;
		else
			bSortByName = true;


		//create Collection for the results...
		Event		eEvent			= EventManager.getInstance().getEventByID( myForm.getEventID() );
		Collection	cSubscriberList	= UserManager.getInstance().getUsersByCollection( eEvent.getSubscriberList() );
		Collection	cExamResults	= EventResultManager.getInstance().getResults( eEvent.getID() );

		Iterator	cSubscriberListIT = cSubscriberList.iterator();
		Collection	cSubscriberResultList = new ArrayList();

		while( cSubscriberListIT.hasNext() )
		{
			User CurUser = (User) cSubscriberListIT.next();
			String UserPNR = CurUser.getPersonalNumber();

			Iterator cExamResultsIT = cExamResults.iterator();
			ExamResult	curExamResult = null;
			String		ResultUserPNR = null;
			while( cExamResultsIT.hasNext() )
			{
				curExamResult = (ExamResult) cExamResultsIT.next();

				ResultUserPNR = curExamResult.getUserPersonalNumber();
				if( UserPNR.equals( ResultUserPNR ) )
					break;
			}

			if( UserPNR.equals( ResultUserPNR ) )
			{
				if( bSortByName )
				{
					UserResultSortByName URS = new UserResultSortByName( CurUser, ""+curExamResult.getResult() );
					cSubscriberResultList.add(URS);
				}
				else
				{
					UserResultSortByPersonalNumber URS = new UserResultSortByPersonalNumber( CurUser, ""+curExamResult.getResult() );
					cSubscriberResultList.add(URS);
				}
			}
			else
			{
				if( bSortByName )
				{
					UserResultSortByName URS = new UserResultSortByName( CurUser, "-" );
					cSubscriberResultList.add(URS);
				}
				else
				{
					UserResultSortByPersonalNumber URS = new UserResultSortByPersonalNumber( CurUser, "-" );
					cSubscriberResultList.add(URS);
				}
			}

		}

		//sort List
		Collections.sort( (List) cSubscriberResultList );

		BaseFont	bf;
		//36pt = 0.5inch
        Document document = new Document(PageSize.A4, 36, 36, 72, 72);

		try
        {
   			bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
   		}
   		catch( Exception e )
   		{
   			Log.addLog( "CreateSubscriberListAction: Error creating BaseFont: " + e );
   			//2do: add ErrorMessage and return to inputFormular!
   			return mapping.findForward( "GeneralFailure" );
   		}


		//calculate the number of cols and their width
		int cols = 0;
		ArrayList	alWidth = new ArrayList();
		float boldItalicFactor = 1.2f;
		if( myForm.getShowNumber() )
			alWidth.add( new Float( boldItalicFactor*bf.getWidthPoint( TABLEHEADER_NUMBER, TABLEHEADER_FONTSIZE ) ) );

		if( myForm.getShowPersonalNumber() )
			alWidth.add( new Float( boldItalicFactor*bf.getWidthPoint( TABLEHEADER_PERSONALNUMBER, TABLEHEADER_FONTSIZE ) ) );

		if( myForm.getShowName() )
			alWidth.add( new Float( boldItalicFactor*bf.getWidthPoint( TABLEHEADER_NAME, TABLEHEADER_FONTSIZE ) ) );

		if( myForm.getShowEmail() )
			alWidth.add( new Float( boldItalicFactor*bf.getWidthPoint( TABLEHEADER_EMAIL, TABLEHEADER_FONTSIZE ) ) );

		if( myForm.getShowResult() )
			alWidth.add( new Float( boldItalicFactor*bf.getWidthPoint( TABLEHEADER_RESULT, TABLEHEADER_FONTSIZE ) ) );

		if( myForm.getAddInfoField() )
			alWidth.add( new Float( boldItalicFactor*bf.getWidthPoint( TABLEHEADER_INFO, TABLEHEADER_FONTSIZE ) ) );

		if( myForm.getAddSignField() )
			alWidth.add( new Float( boldItalicFactor*bf.getWidthPoint( TABLEHEADER_SIGN, TABLEHEADER_FONTSIZE ) ) );

        cols = alWidth.size();

        float totalWidth = 0;
        //calculate the whole length
        Iterator alIterator = alWidth.iterator();
        for( ; alIterator.hasNext(); totalWidth += ((Float) alIterator.next()).floatValue() );

        //calculate relativ width for the table
        float[] width = new float[cols];
        alIterator = alWidth.iterator();
        int i = 0;
        while( alIterator.hasNext() )
        {
        	float pixLength = ((Float) alIterator.next()).floatValue();
        	//alWidthRelativ.add( new Float( pixLength/totalWidth ) );
        	width[i] = pixLength / totalWidth;
        	i++;
        }

        //needed for the shrink (enlarge?)
        float shrinkFactor;

		try
        {
        	//1st: set correct outputstream
            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream() );

            //1.5st: set content-stuff
       		response.setContentType( "application/pdf" );

            //2nd: set EventManager for PageEvents to Helper
            writer.setPageEvent( new CreateSubscriberListActionHelper( myForm.getHeaderLeft(), myForm.getHeaderRight() ) );


            //3rd: open document for editing the content
            document.open();


			//4th: add content
			Phrase pInfoText = new Phrase( myForm.getInfoText(), new Font( bf, 12, Font.BOLD ) );
			document.add( pInfoText );


            //PdfPTable( cols )
			PdfPTable table = new PdfPTable( width );

			float documentWidth = document.right()-document.left();
			if( documentWidth < totalWidth )
			{
	            table.setTotalWidth( documentWidth );
	            shrinkFactor = documentWidth / totalWidth;
	        }
	        else
	        {
	        	table.setTotalWidth( totalWidth );
	        	shrinkFactor = 1;
	        }
	        table.setLockedWidth( true );

            Font headerFont = new Font( bf, TABLEHEADER_FONTSIZE*shrinkFactor, Font.BOLDITALIC );
            Font cellFont = new Font( bf, TABLECELL_FONTSIZE*shrinkFactor, Font.NORMAL );


            if( myForm.getShowNumber() )			table.addCell( new Phrase( TABLEHEADER_NUMBER, headerFont ) );
            if( myForm.getShowPersonalNumber() )	table.addCell( new Phrase( TABLEHEADER_PERSONALNUMBER, headerFont ) );
            if( myForm.getShowName() )				table.addCell( new Phrase( TABLEHEADER_NAME, headerFont ) );
            if( myForm.getShowEmail() )				table.addCell( new Phrase( TABLEHEADER_EMAIL, headerFont ) );
            if( myForm.getShowResult() )			table.addCell( new Phrase( TABLEHEADER_RESULT, headerFont ) );
            if( myForm.getAddInfoField() )			table.addCell( new Phrase( TABLEHEADER_INFO, headerFont ) );
            if( myForm.getAddSignField() )			table.addCell( new Phrase( TABLEHEADER_SIGN, headerFont ) );

            //fill table
			Iterator iSRL = cSubscriberResultList.iterator();
			int	counter = 1;
			while( iSRL.hasNext() )
			{
				UserResult curResult = (UserResult) iSRL.next();

				table.getDefaultCell().setHorizontalAlignment( Element.ALIGN_RIGHT );
	            if( myForm.getShowNumber() )			table.addCell( new Phrase( ""+counter++, cellFont ) );

				table.getDefaultCell().setHorizontalAlignment( Element.ALIGN_CENTER );
    	        if( myForm.getShowPersonalNumber() )	table.addCell( new Phrase( curResult.getPersonalNumber(), cellFont ) );

    	        table.getDefaultCell().setHorizontalAlignment( Element.ALIGN_LEFT );
        	    if( myForm.getShowName() )				table.addCell( new Phrase( curResult.getSurname() + ", " + curResult.getFirstName(), cellFont ) );
            	if( myForm.getShowEmail() )				table.addCell( new Phrase( curResult.getEmailAddress(), cellFont ) );
	            if( myForm.getShowResult() )			table.addCell( new Phrase( curResult.getResult(), cellFont ) );
    	        if( myForm.getAddInfoField() )			table.addCell( new Phrase( "", cellFont ) );
        	    if( myForm.getAddSignField() )			table.addCell( new Phrase( "", cellFont ) );
			}

			//set how many rows are header...
            table.setHeaderRows(1);

            document.add(table);

            //5th: close document, write the output to the stream...
            document.close();
        }
        catch (Exception de)
        {
        	Log.addLog( "CreateSubscriberListAction: Error creating PDF: " + de );
        }

		//we dont need to return a forward, because we write directly to the outputstream!
		return null;
	}
}