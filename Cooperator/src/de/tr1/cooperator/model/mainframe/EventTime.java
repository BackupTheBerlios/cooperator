/*
File:		EventTime.java
Created:	05-06-01@13:00
Task:		This class stores the time for events
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

import de.tr1.cooperator.model.mainframe.RhythmType;
import java.util.Collection;
import java.util.ArrayList;

/**
 * This class stores one termin for an event
 *
 * @author Peter Matjeschk
 * @version 05-06-04@08:30
 */
public class EventTime implements Comparable
{
	//constants for rhythm
	public static final int WEEKLY		= 1;
	public static final int WEEK_A		= 2;
	public static final int WEEK_B		= 3;
	public static final int ONCE		= 4;
	public static final int ALTERNATIVE	= 5;

	private	static	final	String		RHYTHM_WEEKLY		= "jede Woche";
	private	static	final	String		RHYTHM_WEEK_A		= "jede gerade Woche";
	private	static	final	String		RHYTHM_WEEK_B		= "jede ungerade Woche";
	private	static	final	String		RHYTHM_ONCE			= "einmalig";
	private	static	final	String		RHYTHM_ALTERNATIVE	= "siehe Beschreibung";


	protected	String	sDayName;
	protected	String	sClockTime;
	protected	int		iRhythm;
	protected	String	sLocation;					//Location of this event

	/**
	 * Constructor
	 *
	 * @param eventDayName day of the week for this termin
	 * @param eventClockTime time for this termin
	 * @param eventRhythm rhythm for this termin
	 * @param eventLocation location for this termin
	 */
	public EventTime( String eventDayName, String eventClockTime, int eventRhythm, String eventLocation )
	{
		this.sDayName	= eventDayName;
		this.sClockTime	= eventClockTime;
		this.iRhythm	= eventRhythm;
		this.sLocation	= eventLocation;
	}

	//GETTER AND SETTER
	public String getDayName( )
	{
		return this.sDayName;
	}
	public void setDayName( String Name )
	{
		this.sDayName = Name;
	}

	public String getClockTime( )
	{
		return this.sClockTime;
	}
	public void setClockTime( String Time )
	{
		this.sClockTime = Time;
	}

	public int getRhythm( )
	{
		return this.iRhythm;
	}
	public void setRhythm( int Rhythm )
	{
		this.iRhythm = Rhythm;
	}

	public String getRhythmAsString()
	{
		switch( this.iRhythm )
		{

			case WEEKLY:	return RHYTHM_WEEKLY;
			case WEEK_A:	return RHYTHM_WEEK_A;
			case WEEK_B:	return RHYTHM_WEEK_B;
			case ONCE:		return RHYTHM_ONCE;

			default:		return RHYTHM_ALTERNATIVE;
		}
	}

	public static Collection getAllRhythmTypes()
	{
		Collection alReturn = new ArrayList();

		alReturn.add( new RhythmType( WEEKLY,		RHYTHM_WEEKLY ) );
		alReturn.add( new RhythmType( WEEK_A,		RHYTHM_WEEK_A ) );
		alReturn.add( new RhythmType( WEEK_B,		RHYTHM_WEEK_B ) );
		alReturn.add( new RhythmType( ONCE,			RHYTHM_ONCE ) );
		alReturn.add( new RhythmType( ALTERNATIVE,	RHYTHM_ALTERNATIVE ) );

		return alReturn;
	}



	public void setLocation( String sLocation )
	{
		this.sLocation = sLocation;
	}
	public String getLocation()
	{
		return this.sLocation;
	}

	/**
	 * This is implementation of the Interface comparable and need for sorting and so on
	 *
	 * @param compareEvent compare this event to another event
	 * @return -x: less, 0: equal +x: greater
	 */
	public int compareTo( Object compareEvent )
	{
		//2do: this is dirty, have to implement this...
		return 0;
	}

}