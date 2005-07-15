/*
File:		Log.java
Created:	05-05-23@20:00
Task:		Will be a singleton and manage a global Log-File
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
package de.tr1.cooperator.manager.system;

import java.util.Date;
import java.text.SimpleDateFormat;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import de.tr1.cooperator.exceptions.*;
/**
 * Will write logs to a global file for debugging and action-logging
 *
 * @author Sebastian Kohl
 * @version 05-05-24@10:00
 *
 */

public class Log
{
	private static Log		hInstance	= null;
	private		   String	sFileName	= null;

	/**
	 * Will create the needed instance, but throw a SingletonException, if already instanciated
	 */
	protected Log( ) throws SingletonException
	{
		if ( Log.hInstance != null )
			throw new SingletonException();
		hInstance = this;
	}

	/**
	 * Will return the valid singleton-instance and set the name of the log-file, if not already done.
	 * @param FilePath
	 * @return Log
	 */
	synchronized public static Log getInstance( String FilePath ) throws IllegalArgumentException
	{
		if ( hInstance == null )
		{
			if ( FilePath == null )
				throw new IllegalArgumentException("FilePath was null and can not be set!");
			hInstance = new Log( );
			hInstance.sFileName = FilePath;
		}
		return hInstance;
	}

	/**
	 * Returns a valid singleton-instance, if it was initalized already. If not, it will throw an IllegalArgumentException!
	 * @ return Log
	 */
	synchronized public static Log getInstance( ) throws IllegalArgumentException
	{
		if ( hInstance == null )
			throw new IllegalArgumentException("Class Log was not already initialized. Call getInstance(FilePath) first!");
		return hInstance;
	}

	/**
	 * This writes a Text-Line for the Log into the file. The line-break will be added automatically.
	 * @param Text
	 */
	 synchronized protected boolean addLogInternal( String Text ) throws Exception
	 {
	 	if ( Text == null )
	 		return false;//throw new RuntimeException("Log-String was null!");

	 	if ( this.sFileName == null )
	 		return false;//throw new RuntimeException("FileName was null, but should not have been!");

	//	try
		{
	 		File file = new File( this.sFileName );
	 		if ( (!file.exists()) || (file.exists() && file.isFile() && file.canWrite()) )
	 		{
	 			FileOutputStream fos = new FileOutputStream( this.sFileName, true);
				SimpleDateFormat myDateFormat = new SimpleDateFormat( "dd.MM.yyyy HH:mm:ss  -  " );
				long lVersionTime = new Date().getTime();
				Text = myDateFormat.format( new java.util.Date( lVersionTime ) ) + Text;
	 			Text += "\r\n";
	 			for ( int i=0, l=Text.length(); i<l; i++)
			 		fos.write( Text.charAt(i) );
			 	fos.flush( );
	 			fos.close( );
	 		}
	 	}
	// 	catch ( Exception e )
	 	{
	// 		return false;
	 	}
	 	return true;
	 }

	 public static boolean addLogExtreme( String Text ) throws Exception
	 {
	 	return Log.getInstance().addLogInternal( Text );
	 }

	 public static boolean addLog( String Text )
	 {
	 	try
	 	{
	 		return Log.getInstance().addLogInternal( Text );
	 	}
	 	catch ( Exception re )
	 	{
	 		return false;
	 	}
	 }
}