/*
File:		ManagerInitializer.java
Created:	05-05-23@20:00
Task:		Will be complete static class and give the given path to all managers in the package, who need it.
Author:	Sebastian Kohl

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

package de.tr1.cooperator.manager;

import java.util.Date;
import java.text.SimpleDateFormat;

import java.io.File;

import de.tr1.cooperator.exceptions.*;
import de.tr1.cooperator.manager.system.*;
import de.tr1.cooperator.manager.mainframe.*;

/**
 * Will be a complete static class and give the given path to all managers in the package, who need it.
 *
 * @author Sebastian Kohl
 * @version 05-05-24@10:00
 *
 */

public class ManagerInitializer
{
	/**
	 * Completely private, because it should not be able to get an instance of this class
	 */
	private ManagerInitializer( )
	{
	}

	/**
	 * Will pass the given FilePath to all known Managers of the package and sub-packages
	 * @param FilePath
	 * @return Log
	 */
	synchronized public static String initAll( String FilePath ) throws Exception
	{
		if ( !File.separator.equals( ""+FilePath.charAt( FilePath.length()-1 ) ) )
			FilePath += File.separator;
		Log.getInstance					( FilePath + "Log"  + File.separator + "Log.txt"  );
		GlobalVarsManager.getInstance	( FilePath + "GlobalVariables.xml" );
		UserManager.getInstance			( FilePath + "User" + File.separator + "User.xml" );
		EventManager.getInstance		( FilePath + "Events" + File.separator );
		EventTypeManager.getInstance	( FilePath + "Events" + File.separator + "EventTypes.xml" );
		EventResultManager.getInstance	( FilePath + "Events" + File.separator );
		if (1==1) return FilePath;
		return FilePath;
	}
}