/**
 * Copyright © 2014 Titan Framework. All Rights Reserved.
 *
 * Developed by Laboratory for Precision Livestock, Environment and Software Engineering (PLEASE Lab)
 * of Embrapa Beef Cattle at Campo Grande - MS - Brazil.
 * 
 * @see http://please.cnpgc.embrapa.br
 * 
 * @author Camilo Carromeu <camilo.carromeu@embrapa.br>
 * @author Jairo Ricardes Rodrigues Filho <jairocgr@gmail.com>
 * 
 * @version 14.06-1-alpha
 */

package com.carromeu.titan.sample.travel.exception;

public class ApplicationException extends RuntimeException
{
	private static final long serialVersionUID = 4485985233420536642L;

	public ApplicationException ()
	{
		super ();
	}

	public ApplicationException (String detailMessage, Throwable throwable)
	{
		super (detailMessage, throwable);
	}

	public ApplicationException (String detailMessage)
	{
		super (detailMessage);
	}

	public ApplicationException (Throwable throwable)
	{
		super (throwable);
	}
}