/*
File:		SubEventBean.java
Created:	05-06-08@18:30
Task:		This is a Form-Bean for the eventInfo.jsp-site
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

public class SubEventBean
{
	protected	String		sName;
	protected	String		sTypeName;
	protected	String		sLecturer;
	protected	int			iID;
	protected	int			iGroup;
	protected	int			iTypeID;
	protected	boolean		bCanSubscribe;
	protected	boolean		bCanUnsubscribe;
	protected	boolean		bCanChangeHere;
	protected	boolean		bIsFirst;
	protected	boolean		bIsLast;
	protected	boolean		bIsFirstInGroup;
	protected	boolean		bIsLastInGroup;

	public SubEventBean( String		Name,
						 String		TypeName,
						 String		Lecturer,
						 int		ID,
						 int		Group,
						 int		TypeID,
						 boolean	CanSubscribe,
						 boolean	CanUnsubscribe,
						 boolean	CanChangeHere,
						 boolean	IsFirst,
						 boolean	IsLast,
						 boolean	IsFirstInGroup,
						 boolean	IsLastInGroup
						  )
	{
		this.sName			= Name;
		this.sTypeName		= TypeName;
		this.sLecturer		= Lecturer;
		this.iID			= ID;
		this.iGroup			= Group;
		this.iTypeID		= TypeID;
		this.bCanSubscribe	= CanSubscribe;
		this.bCanUnsubscribe= CanUnsubscribe;
		this.bCanChangeHere	= CanChangeHere;
		this.bIsFirst		= IsFirst;
		this.bIsLast		= IsLast;
		this.bIsFirstInGroup= IsFirstInGroup;
		this.bIsLastInGroup	= IsLastInGroup;
		this.bIsFirst		= IsFirst;
		this.bIsLast		= IsLast;
	}

	public String getName( )
	{
		return this.sName;
	}

	public String getTypeName( )
	{
		return this.sTypeName;
	}

	public String getLecturer( )
	{
		return this.sLecturer;
	}

	public int getID( )
	{
		return this.iID;
	}

	public int getGroup( )
	{
		return this.iGroup;
	}

	public int getTypeID( )
	{
		return this.iTypeID;
	}

	public boolean getCanSubscribe( )
	{
		return this.bCanSubscribe;
	}

	public boolean getCanUnsubscribe( )
	{
		return this.bCanUnsubscribe;
	}

	public boolean getCanChangeHere( )
	{
		return this.bCanChangeHere;
	}

	public boolean getIsFirst( )
	{
		return this.bIsFirst;
	}

	public boolean getIsLast( )
	{
		return this.bIsLast;
	}

	public void setIsLast( boolean val )
	{
		this.bIsLast = val;
	}

	public boolean getIsLastInGroup( )
	{
		return this.bIsLastInGroup;
	}

	public boolean getIsFirstInGroup( )
	{
		return this.bIsFirstInGroup;
	}

	public void setIsLastInGroup( boolean val )
	{
		this.bIsLastInGroup = val;
	}
}