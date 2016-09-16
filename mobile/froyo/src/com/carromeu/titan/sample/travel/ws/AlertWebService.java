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

package com.carromeu.titan.sample.travel.ws;

import com.carromeu.titan.sample.travel.exception.TechnicalException;
import com.carromeu.titan.sample.travel.util.HttpHelper;
import com.carromeu.titan.sample.travel.util.WebServiceHelper;

public class AlertWebService
{
	private AlertWebService ()
	{}
	
	public static String alerts ()
	{
		try
		{
			return HttpHelper.getResponseContentString (WebServiceHelper.singleton ().get ("/alerts"));
		}
		catch (TechnicalException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new TechnicalException ("Problemas técnicos na autenticação: " + e.getMessage (), e);
		}
	}
	
	public static void readAlert (Long id)
	{
		try
		{
			WebServiceHelper.singleton ().post ("/alert/" + String.valueOf (id));
		}
		catch (TechnicalException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new TechnicalException ("Problemas técnicos na autenticação: " + e.getMessage (), e);
		}
	}
	
	public static void deleteAlert (Long id)
	{
		try
		{
			WebServiceHelper.singleton ().delete ("/alert/" + String.valueOf (id));
		}
		catch (TechnicalException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new TechnicalException ("Problemas técnicos na autenticação: " + e.getMessage (), e);
		}
	}
}