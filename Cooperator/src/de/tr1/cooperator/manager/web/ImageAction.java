/*
File:		ImageAction.java
Created:	05-06-07@11:00
Task:		Action that outputs an image
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

import de.tr1.cooperator.manager.system.Log;
import de.tr1.cooperator.model.web.Accessable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.ServletOutputStream;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;


/**
 * This class outputs image which can be stored somewhere - however till now it only opens files and outputs them
 *
 * @author Peter Matjeschk
 * @version 05-06-07@11:00
 */
public class ImageAction extends Action
{
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception, IOException
	{
		//2do: check what categorie and so on too
		String subscriptionState = request.getParameter( "subscriptionState" );

		//beware of bad forms...
		if( form == null )
			return null;

		Accessable acForm = (Accessable) form;

		String filename;
		filename = acForm.getDataPath()+".."+File.separator+".."+File.separator+"web"+File.separator+"pics"+File.separator + subscriptionState + ".gif";

		InputStream in;
		OutputStream out;
		try
		{
			in = new BufferedInputStream( new FileInputStream( filename ) );
			out = response.getOutputStream();
		}
		catch( IOException ioe )
		{
			//this can happen, if the image or the outputstream can't be openend
			//2do --> maybe save something like an empty bitmap here in code an try opening the
			//			outputstream somewhere else
			return null;
		}


		String s = URLConnection.guessContentTypeFromStream(in);
		response.setContentType( s );

		byte pic[]= new byte[in.available()];
		in.read( pic );

		out.write( pic );

		return null;
	}

}