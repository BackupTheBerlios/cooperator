package Test;

import java.text.*;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import java.io.FileOutputStream;
import java.io.IOException;

public class MyPageEvents implements PdfPageEvent
{
	private	String	sHeaderLeft		= "";
	private	String	sHeaderRight	= "";

	public void CreatePDF()
	{
        Document document = new Document(PageSize.A4, 50, 50, 70, 70);

		try
        {
   			bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

        	//1st: set correct outputstream
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("endpage.pdf"));
            writer.setPageEvent( this );
            
            
            //2nd: open document for editing the content
            document.open();


			//3rd: add content			
			Phrase pInfoText = new Phrase( "Einschreibungsliste", new Font( bf, 12, Font.BOLD ) );
            document.add( pInfoText );

            //PdfPTable( cols )
            PdfPTable table = new PdfPTable( 4 );
            table.addCell( new Phrase( "Mtr-Nr.", new Font( bf, 12, Font.BOLDITALIC ) ) );
            table.addCell( new Phrase( "Name", new Font( bf, 12, Font.BOLDITALIC ) ) );
            table.addCell( new Phrase( "Vorname", new Font( bf, 12, Font.BOLDITALIC ) ) );
            table.addCell( new Phrase( "Bla", new Font( bf, 12, Font.BOLDITALIC ) ) );

            //fill table
            float fontSize = 12;
            for (int k = 0; k < 2000 * 4; ++k)
				table.addCell(new Phrase(String.valueOf(k), new Font(bf, fontSize)));

			//set how many rows are header...
            table.setHeaderRows(1);

            document.add(table);
            
            //4th: close document, write the output to the stream...
            document.close();
        }
        catch (Exception de)
        {
            de.printStackTrace();
        }
	}


//BELOW THIS IS ONLY STUFF NEEDED FOR THE IMPLEMENTATION OF THE INTERFACE
	/** The headertable. */
	private PdfPTable tHeader;
	/** This is the contentbyte object of the writer */
	private PdfContentByte cb;
	/** we will put the final number of pages in a template */
	private PdfTemplate template;
	/** this is the BaseFont we are going to use for the header / footer */
	private BaseFont bf = null;

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
	 * After the content of the page is written, we put page X of Y
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