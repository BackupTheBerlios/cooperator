/*
File:		DropDownEntry.java
Created:	05-05-30@13:00
Task:		This is the Container which is stored in the Collections used for Drop-Down-Entrys in the JSP-Sites..
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


/**
 * This is need for the DropDown-Menues used in the JSP-Sites
 *
 * @author Peter Matjeschk
 * @version 05-05-30@13:00
 */
public class DropDownEntry
{
	private String	label;
	private int		value;
	private Object	object;			//this stores further informations....

	/**
	 * Constructor for one Entry
	 * @param value This is the unique identifier for one options-entry
	 * @param label This is the Text for this entry
	 */
	public DropDownEntry( int value, String label, Object object )
	{
		this.value	= value;
		this.label	= label;
		this.object	= object;
	}

	/**
	 * @return the label (text)
	 */
	public String getLabel()
	{
		return this.label;
	}
	public void setLabel( String val )
	{
		this.label = val;
	}

	/**
	 * @return the value (unique ID)
	 */
	public int getValue()
	{
		return this.value;
	}
	public void setValue( int val )
	{
		this.value = val;
	}

	/**
	 * @return object which can contain further information
	 */
	public Object getObject()
	{
		return this.object;
	}
	public void setObject( Object val )
	{
		this.object = val;
	}
}