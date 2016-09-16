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

package com.carromeu.titan.sample.travel.util;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.carromeu.titan.sample.travel.exception.TechnicalException;

public class HttpHelper
{
	private HttpHelper ()
	{}

	public static HttpResponse doHttpPost (HttpPost request)
	{
		try
		{
			HttpClient http = new DefaultHttpClient ();

			LogHelper.debug (HttpHelper.class, request);

			HttpResponse response = http.execute (request);

			LogHelper.debug (HttpHelper.class, response);

			return response;
		}
		catch (ClientProtocolException e)
		{
			throw new TechnicalException ("Problemas na conexão com o servidor!", e);
		}
		catch (IOException e)
		{
			throw new TechnicalException ("Sem rede de dados! Impossível se comunicar com o servidor", e);
		}
	}
	
	public static HttpResponse doHttpPut (HttpPut request)
	{
		try
		{
			HttpClient http = new DefaultHttpClient ();

			LogHelper.debug (HttpHelper.class, request);

			HttpResponse response = http.execute (request);

			LogHelper.debug (HttpHelper.class, response);

			return response;
		}
		catch (ClientProtocolException e)
		{
			throw new TechnicalException ("Problemas na conexão com o servidor!", e);
		}
		catch (IOException e)
		{
			throw new TechnicalException ("Sem rede de dados! Impossível se comunicar com o servidor", e);
		}
	}

	public static HttpResponse doHttpGet (HttpGet request)
	{
		try
		{
			HttpClient http = new DefaultHttpClient ();

			LogHelper.debug (HttpHelper.class, request);

			HttpResponse response = http.execute (request);

			LogHelper.debug (HttpHelper.class, response);

			return response;
		}
		catch (ClientProtocolException e)
		{
			throw new TechnicalException ("Problemas na conexão com o servidor!", e);
		}
		catch (IOException e)
		{
			throw new TechnicalException ("Sem rede de dados! Impossível se comunicar com o servidor", e);
		}
	}
	
	public static HttpResponse doHttpDelete (HttpDelete request)
	{
		try
		{
			HttpClient http = new DefaultHttpClient ();
			
			HttpResponse response = http.execute (request);

			LogHelper.debug (HttpHelper.class, response);

			return response;
		}
		catch (ClientProtocolException e)
		{
			throw new TechnicalException ("Problemas na conexão com o servidor!", e);
		}
		catch (IOException e)
		{
			throw new TechnicalException ("Sem rede de dados! Impossível se comunicar com o servidor", e);
		}
	}

	public static String getResponseContentString (HttpResponse response)
	{
		try
		{
			String conversionResult = EntityUtils.toString (response.getEntity ());

			LogHelper.debug (HttpHelper.class, "--- Begin of the HTTP response ---");
			LogHelper.debug (HttpHelper.class, conversionResult);
			LogHelper.debug (HttpHelper.class, "--- End of the HTTP response ---");

			return conversionResult;
		}
		catch (ParseException e)
		{
			throw new TechnicalException ("Problemas na comunicação com o servidor!", e);
		}
		catch (IOException e)
		{
			throw new TechnicalException ("Problemas na comunicação com o servidor!", e);
		}
	}

	public static JSONObject parseToJson (HttpResponse httpResponse)
	{
		try
		{
			return new JSONObject (getResponseContentString (httpResponse));
		}
		catch (Exception e)
		{
			throw new TechnicalException ("Problemas na comunicação com o servidor!", e);
		}
	}
	
	public static JSONArray parseToJsonArray (HttpResponse httpResponse)
	{
		try
		{
			return new JSONArray (getResponseContentString (httpResponse));
		}
		catch (Exception e)
		{
			throw new TechnicalException ("Problemas na comunicação com o servidor!", e);
		}
	}
	
	public static Boolean isAnHttpError (int statusCode)
	{
		return statusCode >= 400;
	}

	public static boolean isJsonResponse (HttpResponse response)
	{
		return contentType (response).contains ("application/json");
	}

	public static String contentType (HttpResponse response)
	{
		Header [] contentTypeHeader = response.getHeaders ("Content-Type");

		return contentTypeHeader [0].getValue ();
	}
}