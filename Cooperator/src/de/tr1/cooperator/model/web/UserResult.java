/*
Datei: UserResult.java
Autor: Michael Thiele & Ibrahim Osman
Version: 05-06-17@16:00
Funktion: Klasse für einen einzelnen User mit dessen Veranstaltungs- bzw. Prüfungsergebnis

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

import de.tr1.cooperator.model.mainframe.User;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * UserResult
 *
 * @author Michael Thiele & Ibrahim Osman
 * @version 05-06-17@16:00
 */

public class UserResult extends User implements Comparable
{
	private String ExamResult; // result for the user, "-" if there's no result

	public UserResult( User user, String sResult)
	{
		super( user.getLogin(), user.getPassword(), user.getRights(), user.getPersonalNumber(),
				user.getFirstName(), user.getSurname(), user.getEmailAddress() );
		ExamResult = sResult;
	}



	public String getResult()
	{
		return this.ExamResult;
	}

	public void setResult( String Result)
	{
		this.ExamResult = Result;
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

		//compare PersonalNumber
		iCompareValue = this.getPersonalNumber().compareTo( ((UserResult) compareUserResult ).getPersonalNumber() );

		return iCompareValue;
	}

}