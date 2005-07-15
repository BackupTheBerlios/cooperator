/*
File:		LinkListBean.java
Created:	05-06-09@13:00
Task:		This is a Container for links with id and additional text
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
package de.tr1.cooperator.model.web;


/**
 * This is needed for the navigation-links on the Event-Info-Page
 *
 * @author Link-List-Bean
 * @version 05-06-09@13:00
 */
public class LinkListBean
{
	private String	name;
	private int		id;
	private String	additional;			//this stores further informations....
	private String	additional2;		//this stores further informations....

	/**
	 * Constructor for one Entry
	 * @param id This is the unique identifier for one options-entry
	 * @param name This is the Text for this entry
	 * @param additional This is additional Text for this entry
	 */
	public LinkListBean( int id, String name, String additional )
	{
		this.id	= id;
		this.name	= name;
		this.additional	= additional;
		this.additional2	= "";
	}
	/**
	 * Constructor for one Entry
	 * @param id This is the unique identifier for one options-entry
	 * @param name This is the Text for this entry
	 * @param additional This is additional Text for this entry
	 * @param additional2 This is secound additional Text for this entry
	 */
	public LinkListBean( int id, String name, String additional, String additional2 )
	{
		this.id	= id;
		this.name	= name;
		this.additional	= additional;
		this.additional2	= additional2;
	}

	/**
	 * @return the id
	 */
	public int getID()
	{
		return this.id;
	}
	public void setID( int val )
	{
		this.id = val;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return this.name;
	}
	public void setName( String val )
	{
		this.name = val;
	}

	/**
	 * @return additional text
	 */
	public String getAdditional()
	{
		return this.additional;
	}
	public void setAdditional( String val )
	{
		this.additional = val;
	}

	/**
	 * @return additional text
	 */
	public String getAdditional2()
	{
		return this.additional2;
	}
	public void setAdditional2( String val )
	{
		this.additional2 = val;
	}
}