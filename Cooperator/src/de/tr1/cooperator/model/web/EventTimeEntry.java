/*
File:		EventTimeEntry.java
Created:	05-06-07@14:30
Task:		This is the Container which stores the EventTimeCollections used in the JSP-Sites..
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

import de.tr1.cooperator.model.mainframe.EventTime;

/**
 * This is need for all the JSP-Sites which use collections of EventsTimes...
 * @author Peter Matjeschk
 * @version 05-06-07@14:30
 */
public class EventTimeEntry extends EventTime
{
	private	int		iTimeEntryID;

	public EventTimeEntry( int iTimeEntryID, EventTime etEventTime )
	{
		super( etEventTime.getDayName(), etEventTime.getClockTime(),
				etEventTime.getRhythm(), etEventTime.getLocation() );

		this.iTimeEntryID = iTimeEntryID;
	}

	public int getID()
	{
		return this.iTimeEntryID;
	}
	public void setTimeEntryID( int iTimeEntryID )
	{
		this.iTimeEntryID = iTimeEntryID;
	}

}