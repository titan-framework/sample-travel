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

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.json.JSONObject;

import com.carromeu.titan.sample.travel.exception.TechnicalException;
import com.carromeu.titan.sample.travel.util.HttpHelper;
import com.carromeu.titan.sample.travel.util.WebServiceHelper;

public class RegisterWebService
{
	private HttpResponse response;
	
	public RegisterWebService ()
	{}
	
	public JSONObject register (String email, String token, String device, Long timestamp)
	{
		try
		{
			Map<String, String> params = new HashMap<String, String> ();
			
			params.put ("email", email);
			params.put ("token", WebServiceHelper.singleton ().encrypt (token, timestamp));
			params.put ("device", device);
			
			response = WebServiceHelper.singleton ().post ("/register", params, timestamp);
			
			return HttpHelper.parseToJson (response);
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
	
	public long getServerTime ()
	{
		return WebServiceHelper.singleton ().getServerTime (response);
	}
}