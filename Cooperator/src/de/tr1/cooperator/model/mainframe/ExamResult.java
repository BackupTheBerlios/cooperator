/*
 * Created on May 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.tr1.cooperator.model.mainframe;

/**
 * @author IO
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ExamResult
{
	private double	result;
	private int		event;
	private String	userPersonalNumber;	// PersonalNumber of  the user

	public ExamResult( String UserPersonalNumber, int EventID, double Result )
	{
		this.userPersonalNumber	= UserPersonalNumber;
		this.event				= EventID;
		this.result				= Result;
	}

	public double getResult()
	{
		return result;
	}
	public void setResult(double result)
	{
		this.result = result;
	}

	public int getEvent()
	{
		return event;
	}
	public void setEvent(int event)
	{
		this.event = event;
	}

	public String getUserPersonalNumber()
	{
		return userPersonalNumber;
	}
	public void setUserPersonalNumber(String userPersonalNumber)
	{
		this.userPersonalNumber = userPersonalNumber;
	}
}
