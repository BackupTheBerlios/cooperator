/*
 * $Id: PDFTest.java,v 1.1 2005/07/15 18:30:34 bluecobold Exp $
 * $Name:  $
 *
 * This code is part of the 'iText Tutorial'.
 * You can find the complete tutorial at the following address:
 * http://itextdocs.lowagie.com/tutorial/
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * itext-questions@lists.sourceforge.net
 */
import java.io.FileOutputStream;

import com.lowagie.text.Document;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;
import Test.*;

/**
 * Demonstrates the use of PageEvents.
 */
public class PDFTest
{

    /**
     * Demonstrates the use of PageEvents.
     * @param args no arguments needed
     */
    public static void main(String[] args)
    {
    	MyPageEvents mpe = new MyPageEvents();
    	
    	mpe.CreatePDF();
    }
}
