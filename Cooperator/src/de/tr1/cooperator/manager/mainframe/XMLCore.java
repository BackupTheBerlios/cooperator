/*
File:		XMLCore.java
Created:	05-05-24@11:30
Task:		Handles XML-Files
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

package de.tr1.cooperator.manager.mainframe;

import de.tr1.cooperator.exceptions.XMLException;
import de.tr1.cooperator.manager.system.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;



/**
 * This class gives general functionality for XML-Files
 *
 * @author Peter Matjeschk
 * @version 05-05-24@11:30
 */
public class XMLCore
{
	private 			Element		hRoot;
	private 			Document	hDocument;
	private 			String		sBaseFile;


	/**
	 * Will create the needed instance, but throw a SingletonException, if already instanciated
	 */
	public XMLCore( String BaseFile ) throws IllegalArgumentException
	{
		if( BaseFile == null || BaseFile.equals("") )
			throw new IllegalArgumentException( "Bad File name" );

		sBaseFile = BaseFile;
	}

	public Element getHRoot()
	{
		return hRoot;
	}

	/**
	 * Creates a XML-dome from a file and stores it at @see hDocument
	 */
	public void createDOM( ) throws XMLException, IOException
	{
		SAXBuilder b	= new SAXBuilder( );
		try
		{
			hDocument		= b.build( sBaseFile );
		}
		catch( JDOMException je )
		{
			throw new XMLException( "Bad XML-File-Format for File: "+sBaseFile );
		}

		hRoot = hDocument.getRootElement( );
	}

	/**
	 * Creates a XML-DOM with the given root element
	 *
	 * @param RootElementName
	 */
	public void createDomFromScratch(String RootElementName)
	{
		hDocument	= new Document( new Element( RootElementName ) );
		hRoot		= hDocument.getRootElement( );
	}

	/**
	 * Saves the XML-DOM to disk
	 */
	synchronized public boolean saveDom( )
	{
		Format format = Format.getRawFormat( );

		format.setIndent		("  ");
		format.setLineSeparator	("\n");
		format.setTextMode		( Format.TextMode.NORMALIZE );

		XMLOutputter out 	= new XMLOutputter( format );

		try
		{
			FileOutputStream o 		 = new FileOutputStream(new File(sBaseFile));
			BufferedOutputStream bos = new BufferedOutputStream(o);
			out.output	( hDocument, bos );
			bos.close	( );
			o.close		( );
		}
		catch( IOException io )
		{
			return false;
		}

		return true;
	}

	/**
	 * Searches the subtree after an element with given attribute and returns NULL if nothing was found
	 *
	 * @param Start
	 * @param Tag
	 * @param Attrib
	 * @param Wert
	 */
	public static Element find( Element Start, String Tag, String Attrib, String Wert)
	{
		if ( (Attrib == null) || (Wert == null) )
			return null;

		Iterator it;
		if (Tag != null)
			it = Start.getChildren(Tag).iterator( );
		else
			it = Start.getChildren( ).iterator( );

		while ( it.hasNext( ) )
		{
			Element cur = (Element)it.next( );
			if ( Wert.equals( cur.getAttributeValue( Attrib ) ) )
				return cur;
		}
		return null;
	}

	/**
	 * Searches the subtree after an element with given attribute and returns NULL if nothing was found
	 *
	 * @param Tag
	 * @param Attrib
	 * @param Wert
	 */
	public Element find( String Tag, String Attrib, String Wert )
	{
		return find( hRoot, Tag, Attrib, Wert );
	}
	
	public static XMLCore loadFile( String FilePath, boolean CreateIfNotExists, String ScratchTag ) throws IOException
	{
		File f = new File( FilePath );
		if( (f.exists() && (!f.canWrite()) && (f.isFile())) ) return null;

		XMLCore hCore = null;

		if ( !f.exists( ) )
		{
			if (CreateIfNotExists)
			{
				if ( (ScratchTag==null) || (ScratchTag.length()<1 ) )
				{
					Log.addLog( "Could not create XML-File, because Main-Tag was invalid: " + FilePath );
					throw new IOException( );
				}
				hCore = new XMLCore( FilePath );
				hCore.createDomFromScratch( ScratchTag );
				if ( !hCore.saveDom( ) )
				{
					Log.addLog( "Could not create XML-File: " + FilePath );
					throw new IOException( );
				}
			}
		}
		else
		{
			try
			{
				hCore	= new XMLCore( FilePath );
				hCore.createDOM( );
			}
			catch ( IOException e )
			{
				Log.addLog( "Bad XML-File: " + FilePath );
				throw new IOException( );
			}
				return hCore;
			}
		return hCore;
	}
}