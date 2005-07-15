/*
File:		EmailSender.java
Created:	05-06-07@18:30
Task:		Class for sending emails over localhost
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

package de.tr1.cooperator.manager.web;

import java.net.InetAddress;
import sun.net.smtp.SmtpClient;
import java.io.*;

import de.tr1.cooperator.manager.system.Log;

/**
 * This can send eMails over localhost
 *
 * @author Sebastian Kohl
 * @version 05-06-07@18:30
 */
public class EmailSender
{
	public static boolean send( String From, String To, String Subject, String Text )
	{
		PrintStream ps 		 = null;
		SmtpClient 	sendmail = null;
		try
		{
			sendmail = new SmtpClient(InetAddress.getLocalHost().getHostName());
			sendmail.from( From );
			sendmail.to( To );
			ps = sendmail.startMessage( );
		}
		catch (Exception e)
		{
			Log.addLog( "ERROR: An eMail could not be sendet from "+From+" to "+To+" - Error on opening smtp-client" );
			return false;
		}
		try
		{
			ps.println( "From: " + From );
			ps.println( "To: " + To );
			ps.println( "Subject: " + Subject );
			ps.print( "\r\n" );
			ps.println( Text );
			ps.flush( );
			ps.close( );
			sendmail.closeServer();
		}
		catch (Exception e)
		{
			Log.addLog( "ERROR: An eMail could not be sendet from "+From+" to "+To+" - Error on creating eMail" );
			return false;
		}
		return true;
	}
}