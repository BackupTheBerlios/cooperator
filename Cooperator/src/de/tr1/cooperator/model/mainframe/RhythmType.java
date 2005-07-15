/*
File:		RhythmType.java
Created:	05-06-07@16:00
Task:		This is the Container which stores an RhythmType
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
 * This is need for the DropDown-Menues used in the JSP-Sites
 *
 * @author Peter Matjeschk
 * @version 05-05-30@13:00
 */
public class RhythmType implements Comparable
{
	private String	sName;
	private int		iID;

	/**
	 * Constructor for one Rhythm-Type
	 * @param iID id of this RhythmType
	 * @param sName name of this RhythmType
	 */
	public RhythmType( int iID, String sName )
	{
		this.iID	= iID;
		this.sName	= sName;
	}

	/**
	 * @return the Name
	 */
	public String getName()
	{
		return this.sName;
	}

	/**
	 * @return the id
	 */
	public int getID()
	{
		return this.iID;
	}
	
	/**
	 * This is implementation of the Interface comparable and need for sorting and so on
	 *
	 * @param compareRhythmType compare this RhythmType to another
	 * @return -x: less, 0: equal +x: greater
	 */
	public int compareTo( Object compareRhythmType )
	{
		return this.sName.compareTo( ((RhythmType)compareRhythmType).getName() );
	}


}