/*
Datei: User.java
Autor: Michael Thiele
Version: 05-05-26@18:40
Funktion: Klasse für einen einzelnen User

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


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Die Userklasse:
 *
 *
 *
 * @author Michael Thiele
 * @version 05-05-26@13:00
 */

public class User implements Comparable
{
    private int     iRights;
    private String  sPassword;
    private String  sFirstName;
    private String  sLogin;

    private String  sSurname;
    private String  sPersonalNumber;
    private String  sEmailAddress;

    public static final int STUDENT      = 1;
    public static final int LECTURER     = 2;
    public static final int ADMIN        = 4;

    /**
     * Konstruktor fuer die Klasse User.
     *
     * @param sLogin
     * @param sPassword
     * @param iRights
     * @param sPersonalNumber
     * @param sFirstName
     * @param sSurname
     * @param sEmailAddress
     */
    public User( String sLogin, String sPassword, int iRights, String sPersonalNumber,
    			String sFirstName, String sSurname, String sEmailAddress )
    {
        this.sLogin				= sLogin;
        this.sPassword			= sPassword;
        this.iRights			= iRights;
        this.sPersonalNumber	= sPersonalNumber;
        this.sFirstName			= sFirstName;
        this.sSurname			= sSurname;
        this.sEmailAddress		= sEmailAddress;
    }

    /* (c) Thorsten Berger */
    public boolean checkPassword(String klartextPwd)     // checkt, ob das Passwort korrekt ist.
    {
        return cryptLikePW(klartextPwd).equals(this.sPassword);
    }

    /* (c) Thorsten Berger */
    public static String cryptLikePW(String arg)     // verschluesselt das Passwort mittels MD5-Algorithmus
    {
            try
            {
                MessageDigest md = MessageDigest.getInstance( "MD5" );
                byte digest[ ] = md.digest( arg.getBytes( ) );
                StringBuffer buffer = new StringBuffer( );
                for( int i = 0; i < digest.length; i++ )
                {
                    buffer.append( digest[i] );
                }
                return new String( buffer );
            } catch ( NoSuchAlgorithmException e )
            {
                e.printStackTrace( );
            }
            return null;
    }


// Getter

    /**
     * @return int
     */
    public int getRights( )
    {
        return this.iRights;
    }

    /**
     * @return String
     */
    public String getRightsAsString( )
    {
        String  sRights = "";
        if ( (this.iRights & User.STUDENT)     != 0 )  sRights += " Student";
        if ( (this.iRights & User.LECTURER)    != 0 )  sRights += " Dozent";
        if ( (this.iRights & User.ADMIN)       != 0 )  sRights += " Administrator";

        return sRights;
    }

    /**
     * @return String
     */
    public String getPassword( )
    {
        return this.sPassword;
    }

    /**
     * @return String
     */
    public String getFirstName( )
    {
        return this.sFirstName;
    }

    /**
     * @return String
     */
    public String getLogin( )
    {
        return this.sLogin;
    }

    /**
     * @return String
     */
    public String getSurname( )
    {
        return this.sSurname;
    }

    /**
     * @return String
     */
    public String getPersonalNumber( )
    {
        return this.sPersonalNumber;
    }

    /**
     * @return String
     */
    public String getEmailAddress( )
    {
        return this.sEmailAddress;
    }


// Setter

    /**
     * @param newRights
     */
    public void setRights( int newRights )
    {
        this.iRights = newRights;
    }

    /**
     * @param newPassword
     */
    public void setPassword( String newPassword )
    {
        this.sPassword   = newPassword;
    }

    /**
     * @param newName
     */
    public void setFirstName( String newFirstName )
    {
        this.sFirstName       = newFirstName;
    }

    /**
     * @param newLogin
     */
    public void setLogin( String newLogin )
    {
        this.sLogin      = newLogin;
    }

    /**
     * @param newSurname
     */
    public void setSurname( String newSurname )
    {
        this.sSurname      = newSurname;
    }

    /**
     * @param newPersonalNumber
     */
    public void setPersonalNumber( String newPersonalNumber )
    {
        this.sPersonalNumber      = newPersonalNumber;
    }

    /**
     * @param newEmailAddress
     */
    public void setEmailAddress( String newEmailAddress )
    {
        this.sEmailAddress       = newEmailAddress;
    }
    
	/**
	 * This is implementation of the Interface comparable and need for sorting and so on
	 *
	 * @param compareUser compare this user to another user
	 * @return -x: less, 0: equal +x: greater
	 */
	public int compareTo( Object compareUser )
	{
		int iCompareValue;
		
		//first compare Surname
		iCompareValue = this.sSurname.compareTo( ((User) compareUser ).getSurname() );
		
		//if the surname is the same, compare firstname
		if( iCompareValue == 0 )
			iCompareValue = this.sFirstName.compareTo( ((User) compareUser ).getFirstName() );
			
		return iCompareValue;
	}

}