/**
 * Copyright © 2013 Titan Framework. All Rights Reserved.
 *
 * Developed by Laboratory for Precision Livestock, Environment and Software Engineering (PLEASE Lab)
 * of Embrapa Beef Cattle at Campo Grande - MS - Brazil.
 * 
 * @see http://please.cnpgc.embrapa.br
 * 
 * @author Camilo Carromeu <camilo@carromeu.com>
 * @author Jairo Ricardes Rodrigues Filho <jairocgr@gmail.com>
 * 
 * @version 14.06-1-alpha
 */

package com.carromeu.titan.sample.travel.ws;

import org.apache.http.HttpResponse;

import com.carromeu.titan.sample.travel.exception.TechnicalException;
import com.carromeu.titan.sample.travel.util.HttpHelper;
import com.carromeu.titan.sample.travel.util.WebServiceHelper;

public class EmbassyWebService
{
	private HttpResponse response;
	
	public EmbassyWebService ()
	{}
	
	public String list (long time)
	{
		try
		{
			response = WebServiceHelper.singleton ().get ("/embassy/list/" + time);
			
			return HttpHelper.getResponseContentString (response);
		}
		catch (TechnicalException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new TechnicalException ("Problemas técnicos na autenticação!", e);
		}
	}
	
	public static String active ()
	{
		try
		{
			return HttpHelper.getResponseContentString (WebServiceHelper.singleton ().get ("/embassy/active"));
		}
		catch (TechnicalException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new TechnicalException ("Problemas técnicos na autenticação!", e);
		}
	}
	
	public long getServerTime ()
	{
		return WebServiceHelper.singleton ().getServerTime (response);
	}
}
