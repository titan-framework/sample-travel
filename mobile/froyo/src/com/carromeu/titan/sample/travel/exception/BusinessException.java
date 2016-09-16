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

public class BusinessException extends ApplicationException
{
	private static final long serialVersionUID = -3645963555638956284L;

	public BusinessException ()
	{
		super ();
	}

	public BusinessException (String detailMessage, Throwable throwable)
	{
		super (detailMessage, throwable);
	}

	public BusinessException (String detailMessage)
	{
		super (detailMessage);
	}

	public BusinessException (Throwable throwable)
	{
		super (throwable);
	}
}