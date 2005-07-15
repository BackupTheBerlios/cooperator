/*
File:		UserResultSortByName.java
Created:	05-06-20@14:00
Task:		This implements the compareTo method to sort UserResult lists by thier name
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

import de.tr1.cooperator.model.web.UserResult;
import de.tr1.cooperator.model.mainframe.User;


/**
 * This implements the compareTo method to sort UserResult lists by thier name
 *
 * @author Peter Matjeschk
 * @version 05-06-20@14:30
 */
public class UserResultSortByName extends UserResult implements Comparable
{

	public UserResultSortByName( User uUser, String sResult)
	{
		super( uUser, sResult );
	}

	/**
	 * This is implementation of the Interface comparable and need for sorting and so on
	 *
	 * @param compareUserResult compare this userResult to another userResult
	 * @return -x: less, 0: equal +x: greater
	 */
	public int compareTo( Object compareUserResult )
	{
		int iCompareValue;

		//first compare Surname
		iCompareValue = this.getSurname().compareTo( ((UserResult) compareUserResult ).getSurname() );

		//if the surname is the same, compare firstname
		if( iCompareValue == 0 )
			iCompareValue = this.getFirstName().compareTo( ((UserResult) compareUserResult ).getFirstName() );

		return iCompareValue;
	}
}