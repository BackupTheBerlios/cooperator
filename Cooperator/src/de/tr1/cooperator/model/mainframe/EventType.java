/*
File:		EventType.java
Created:	05-06-06@14:00
Task:		This class stores one EventTime
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
package de.tr1.cooperator.model.mainframe;

/**
 * This class stores one EventType
 *
 * @author Peter Matjeschk
 * @version 05-06-06@15:30
 */
public class EventType implements Comparable
{
	private	String	sName;
	private int		iID;
	/**
	 * Constructor
	 *
	 * @param sName Name for this EventType
	 * @param iID ID for this EventType
	 * @param eventRhythm rhythm for this termin
	 */
	public EventType( String sName, int iID )
	{
		this.sName	= sName;
		this.iID	= iID;
	}

	//GETTER AND SETTER
	public String getName( )
	{
		return this.sName;
	}
	public void setName( String sName )
	{
		this.sName = sName;
	}

	public int getID( )
	{
		return this.iID;
	}
	public void setID( int iID )
	{
		this.iID = iID;
	}
	
	/**
	 * This is implementation of the Interface comparable and need for sorting and so on
	 *
	 * @param compareEvent compare this event to another event
	 * @return -x: less, 0: equal +x: greater
	 */
	public int compareTo( Object compareEventType )
	{
		return this.sName.compareTo( ((EventType) compareEventType).getName() );
	}

}