/*
File:		CreateSubscriberListActionHelper.java
Created:	05-06-17@23:55
Task:		EventHandlers for PageEvents during creation of a pdf-file
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

import java.text.*;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import java.io.FileOutputStream;
import java.io.IOException;


/**
 * EventHandlers for PageEvents during creation of a pdf-file
 *
 * @author Peter Matjeschk
 * @version 05-06-17@23:55
 */
public class CreateSubscriberListActionHelper implements PdfPageEvent
{
	/** left text in the site-header */
	private	String	sHeaderLeft;
	
	/** right text in the site-header */
	private	String	sHeaderRight;


	/** The headertable. */
	private PdfPTable tHeader;
	/** This is the contentbyte object of the writer */
	private PdfContentByte cb;
	/** we will put the final number of pages in a template */
	private PdfTemplate template;
	/** this is the BaseFont we are going to use for the header / footer */
	private BaseFont bf = null;


	/**
	 * @param sHeaderLeft Text on the left site-header
	 * @param sHeaderRight Text on the right site-header
	 */
	public CreateSubscriberListActionHelper( String sHeaderLeft, String sHeaderRight )
	{
		this.sHeaderLeft = sHeaderLeft;
		this.sHeaderRight = sHeaderRight;
	}

	/**
	 * The first thing to do when the document is opened, is to define the BaseFont,
	 * get the Direct Content object and create the template that will hold the final
	 * number of pages.
	 * @see com.lowagie.text.pdf.PdfPageEventHelper#onOpenDocument(com.lowagie.text.pdf.PdfWriter, com.lowagie.text.Document)
	 */
	public void onOpenDocument(PdfWriter writer, Document document)
	{
		try
		{
			bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			cb = writer.getDirectContent();

			// initialization of the header table
			tHeader = new PdfPTable(2);
			Phrase p = new Phrase();

			tHeader.getDefaultCell().setBorder(Rectangle.NO_BORDER);


			tHeader.addCell( sHeaderLeft );

			tHeader.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			tHeader.addCell( sHeaderRight );

			template = cb.createTemplate(50, 50);
		}
		catch (DocumentException de)
		{
		}
		catch (IOException ioe)
		{
		}
	}

	/**
	 * After the content of the page is written, we put page X / Y
	 * @see com.lowagie.text.pdf.PdfPageEventHelper#onEndPage(com.lowagie.text.pdf.PdfWriter, com.lowagie.text.Document)
	 */
	public void onEndPage(PdfWriter writer, Document document)
	{
		int pageN = writer.getPageNumber();
		String text = ""+pageN;
		String separator = " / ";
		float text_len = bf.getWidthPoint(text, 8);
		float sep_len = bf.getWidthPoint(separator, 8);
		cb.beginText();
		cb.setFontAndSize(bf, 8);
		float absMiddle = (document.right()-document.left())/2+document.left();
		cb.setTextMatrix(absMiddle-text_len-(sep_len/2), 30);
		cb.showText(text);
		cb.setTextMatrix( absMiddle-(sep_len/2), 30 );
		cb.showText(separator);
		cb.endText();
		cb.addTemplate(template, absMiddle+(sep_len/2), 30);


		// write the headertable
		tHeader.setTotalWidth(document.right() - document.left());
		tHeader.writeSelectedRows(0, -1, document.left(), document.getPageSize().height() - 20, cb);
	}

	/**
	 * Just before the document is closed, we add the final number of pages to
	 * the template.
	 * @see com.lowagie.text.pdf.PdfPageEventHelper#onCloseDocument(com.lowagie.text.pdf.PdfWriter, com.lowagie.text.Document)
	 */
	public void onCloseDocument(PdfWriter writer, Document document)
	{
		template.beginText();
		template.setFontAndSize(bf, 8);
		template.showText(String.valueOf(writer.getPageNumber() - 1));
		template.endText();
	}

	//These are just empty methods for the interface..
    public void onStartPage(PdfWriter writer, Document document){}
    public void onParagraph(PdfWriter writer, Document document, float paragraphPosition){}
    public void onParagraphEnd(PdfWriter writer,Document document,float paragraphPosition){}
    public void onChapter(PdfWriter writer,Document document,float paragraphPosition, Paragraph title){}
    public void onChapterEnd(PdfWriter writer,Document document,float paragraphPosition){}
    public void onSection(PdfWriter writer,Document document,float paragraphPosition, int depth, Paragraph title){}
    public void onSectionEnd(PdfWriter writer,Document document,float paragraphPosition){}
    public void onGenericTag(PdfWriter writer, Document document, Rectangle rect, String text){}
}