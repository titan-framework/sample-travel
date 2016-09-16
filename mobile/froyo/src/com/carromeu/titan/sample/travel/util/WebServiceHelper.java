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

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import com.carromeu.titan.sample.travel.Travel;
import com.carromeu.titan.sample.travel.exception.BusinessException;
import com.carromeu.titan.sample.travel.exception.TechnicalException;

public class WebServiceHelper
{
	private static WebServiceHelper ws;

	private static final String api = Travel.URL + "api";

	private static final String APPLICATION_ID = "mobile";
	private static final String APPLICATION_KEY = "xcN48zoAJ1a06I4403p0NS81inVWD8zg2B6OytXoik7ydSPu6bRztTQ3k957a2IQ";
	
	private EmbrapaAuthCredential clientCredential;
	private EmbrapaAuthCredential applicationCredential;

	private WebServiceHelper ()
	{}

	public static WebServiceHelper singleton ()
	{
		if (ws == null)
			ws = new WebServiceHelper ();

		return ws;
	}

	public HttpResponse get (String uri)
	{
		try
		{
			Long timestamp = new Date ().getTime () / 1000;

			refreshCredentialsWithNew (timestamp);

			LogHelper.debug (WebServiceHelper.class, "Verificando credenciais com o servidor");

			HttpResponse httpResponse = new HttpGetSender ().doGetTo (api + uri)

			.header ("x-embrapa-auth-timestamp", String.valueOf (timestamp))

			.header ("x-embrapa-auth-client-id", clientCredential.publicKey ()).header ("x-embrapa-auth-client-signature", clientCredential.signature ())

			.header ("x-embrapa-auth-application-id", applicationCredential.publicKey ()).header ("x-embrapa-auth-application-signature", applicationCredential.signature ())

			.perform ();

			handleHttpErrors (httpResponse);

			return httpResponse;
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

	public HttpResponse get (String uri, String clientId, String clientPrivateKey)
	{
		try
		{
			Long timestamp = new Date ().getTime () / 1000;

			EmbrapaAuthCredential auxClientCredential = new EmbrapaAuthCredential (timestamp, clientId, clientPrivateKey);

			applicationCredential = new EmbrapaAuthCredential (timestamp, APPLICATION_ID, APPLICATION_KEY);

			LogHelper.debug (WebServiceHelper.class, "Verificando credenciais com o servidor");

			HttpResponse httpResponse = new HttpGetSender ().doGetTo (api + uri)

			.header ("x-embrapa-auth-timestamp", String.valueOf (timestamp))

			.header ("x-embrapa-auth-client-id", auxClientCredential.publicKey ()).header ("x-embrapa-auth-client-signature", auxClientCredential.signature ())

			.header ("x-embrapa-auth-application-id", applicationCredential.publicKey ()).header ("x-embrapa-auth-application-signature", applicationCredential.signature ())

			.perform ();

			handleHttpErrors (httpResponse);

			return httpResponse;
		}
		catch (TechnicalException e)
		{
			throw e;
		}
		catch (BusinessException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			e.printStackTrace ();

			throw new TechnicalException ("Problemas técnicos na autenticação: " + e.getMessage (), e);
		}
	}

	public HttpResponse post (String uri)
	{
		try
		{
			Long timestamp = new Date ().getTime () / 1000;

			refreshCredentialsWithNew (timestamp);

			LogHelper.debug (WebServiceHelper.class, "Verificando credenciais com o servidor");

			HttpResponse httpResponse = new HttpPostSender ().doPostTo (api + uri)

			.header ("x-embrapa-auth-timestamp", String.valueOf (timestamp))

			.header ("x-embrapa-auth-client-id", clientCredential.publicKey ()).header ("x-embrapa-auth-client-signature", clientCredential.signature ())

			.header ("x-embrapa-auth-application-id", applicationCredential.publicKey ()).header ("x-embrapa-auth-application-signature", applicationCredential.signature ())

			.perform ();

			handleHttpErrors (httpResponse);

			return httpResponse;
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

	public HttpResponse post (String uri, Map<String, String> params)
	{
		try
		{
			Long timestamp = new Date ().getTime () / 1000;

			refreshCredentialsWithNew (timestamp);

			LogHelper.debug (WebServiceHelper.class, "Verificando credenciais com o servidor");

			HttpPostSender request = new HttpPostSender ().doPostTo (api + uri)

			.header ("x-embrapa-auth-timestamp", String.valueOf (timestamp))

			.header ("x-embrapa-auth-client-id", clientCredential.publicKey ()).header ("x-embrapa-auth-client-signature", clientCredential.signature ())

			.header ("x-embrapa-auth-application-id", applicationCredential.publicKey ()).header ("x-embrapa-auth-application-signature", applicationCredential.signature ());

			for (Map.Entry<String, String> entry : params.entrySet ())
				request.param (entry.getKey (), entry.getValue ());

			HttpResponse httpResponse = request.perform ();

			handleHttpErrors (httpResponse);

			return httpResponse;
		}
		catch (TechnicalException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new TechnicalException ("Problemas técnicos na requisição", e);
		}
	}

	public HttpResponse post (String uri, Map<String, String> params, Map<String, File> files)
	{
		try
		{
			Long timestamp = new Date ().getTime () / 1000;

			refreshCredentialsWithNew (timestamp);

			LogHelper.debug (WebServiceHelper.class, "Verificando credenciais com o servidor");

			HttpMultipartPostSender request = new HttpMultipartPostSender ().doHttpPostTo (api + uri)

			.header ("x-embrapa-auth-timestamp", String.valueOf (timestamp))

			.header ("x-embrapa-auth-client-id", clientCredential.publicKey ()).header ("x-embrapa-auth-client-signature", clientCredential.signature ())

			.header ("x-embrapa-auth-application-id", applicationCredential.publicKey ()).header ("x-embrapa-auth-application-signature", applicationCredential.signature ());

			for (Map.Entry<String, String> entry : params.entrySet ()) {
				request.param (entry.getKey (), entry.getValue ());
				logParam(entry);
			}

			for (Map.Entry<String, File> entry : files.entrySet ())	{
				request.param (entry.getKey (), entry.getValue (), DrawableHelper.getMimeType (entry.getValue ().getPath ()));
				logParam(entry.getKey(), entry.getValue());
			}
				
			HttpResponse httpResponse = request.perform ();

			handleHttpErrors (httpResponse);

			return httpResponse;
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

	private void logParam(String name, File file) {
		logParam(name, file.getPath());
	}

	private void logParam(Entry<String, String> entry) {
		logParam(entry.getKey(), entry.getValue());
	}
	
	private void logParam(String name, String value) {
		LogHelper.debug (WebServiceHelper.class, name + ": " + value);
	}

	public HttpResponse post (String uri, Map<String, String> params, Long timestamp)
	{
		try
		{
			applicationCredential = new EmbrapaAuthCredential (timestamp, APPLICATION_ID, APPLICATION_KEY);

			HttpPostSender request = new HttpPostSender ().doPostTo (api + uri)

			.header ("x-embrapa-auth-timestamp", String.valueOf (timestamp))

			.header ("x-embrapa-auth-application-id", applicationCredential.publicKey ()).header ("x-embrapa-auth-application-signature", applicationCredential.signature ());

			for (Map.Entry<String, String> entry : params.entrySet ())
				request.param (entry.getKey (), entry.getValue ());

			HttpResponse httpResponse = request.perform ();

			handleHttpErrors (httpResponse);

			return httpResponse;
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

	public HttpResponse put (String uri, Map<String, String> params, Map<String, File> files)
	{
		try
		{
			Long timestamp = new Date ().getTime () / 1000;

			refreshCredentialsWithNew (timestamp);

			LogHelper.debug (WebServiceHelper.class, "Verificando credenciais com o servidor");

			HttpMultipartPutSender request = new HttpMultipartPutSender ().doHttpPutTo (api + uri)

			.header ("x-embrapa-auth-timestamp", String.valueOf (timestamp))

			.header ("x-embrapa-auth-client-id", clientCredential.publicKey ()).header ("x-embrapa-auth-client-signature", clientCredential.signature ())

			.header ("x-embrapa-auth-application-id", applicationCredential.publicKey ()).header ("x-embrapa-auth-application-signature", applicationCredential.signature ());

			for (Map.Entry<String, String> entry : params.entrySet ())
				request.param (entry.getKey (), entry.getValue ());

			for (Map.Entry<String, File> entry : files.entrySet ())
				request.param (entry.getKey (), entry.getValue (), DrawableHelper.getMimeType (entry.getValue ().getPath ()));

			HttpResponse httpResponse = request.perform ();
			
			handleHttpErrors (httpResponse);

			return httpResponse;
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

	public HttpResponse put (String uri, Map<String, String> params)
	{
		try
		{
			Long timestamp = new Date ().getTime () / 1000;

			refreshCredentialsWithNew (timestamp);

			LogHelper.debug (WebServiceHelper.class, "Verificando credenciais com o servidor");

			HttpPutSender request = new HttpPutSender ().doPutTo (api + uri)

			.header ("x-embrapa-auth-timestamp", String.valueOf (timestamp))

			.header ("x-embrapa-auth-client-id", clientCredential.publicKey ()).header ("x-embrapa-auth-client-signature", clientCredential.signature ())

			.header ("x-embrapa-auth-application-id", applicationCredential.publicKey ()).header ("x-embrapa-auth-application-signature", applicationCredential.signature ());

			for (Map.Entry<String, String> entry : params.entrySet ())
				request.param (entry.getKey (), entry.getValue ());

			HttpResponse httpResponse = request.perform ();

			handleHttpErrors (httpResponse);

			return httpResponse;
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

	public HttpResponse delete (String uri)
	{
		try
		{
			Long timestamp = new Date ().getTime () / 1000;

			refreshCredentialsWithNew (timestamp);

			LogHelper.debug (WebServiceHelper.class, "Verificando credenciais com o servidor");

			HttpResponse httpResponse = new HttpDeleteSender ().doDeleteTo (api + uri)

			.header ("x-embrapa-auth-timestamp", String.valueOf (timestamp))

			.header ("x-embrapa-auth-client-id", clientCredential.publicKey ()).header ("x-embrapa-auth-client-signature", clientCredential.signature ())

			.header ("x-embrapa-auth-application-id", applicationCredential.publicKey ()).header ("x-embrapa-auth-application-signature", applicationCredential.signature ())

			.perform ();

			handleHttpErrors (httpResponse);

			return httpResponse;
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

	public void refreshCredentialsWithNew (Long timestamp)
	{
		clientCredential = new EmbrapaAuthCredential (timestamp, Preferences.singleton ().getString ("client-id", ""), Preferences.singleton ().getString ("private-key", ""));

		applicationCredential = new EmbrapaAuthCredential (timestamp, APPLICATION_ID, APPLICATION_KEY);

		clientCredential.updateTimestamp (timestamp);
	}

	private void handleHttpErrors (HttpResponse response)
	{
		switch (response.getStatusLine ().getStatusCode ())
		{
			case HttpStatus.SC_UNAUTHORIZED:
				if (HttpHelper.isJsonResponse (response))
				{
					try
					{

						String stringResponse = HttpHelper.getResponseContentString (response);

						JSONObject error = new JSONObject (stringResponse);

						throw new BusinessException (error.getString ("MESSAGE"));

					}
					catch (JSONException e)
					{
						throw new TechnicalException ("Problemas na comunicação com o servidor!", e);
					}
				}
				else
				{
					throw new BusinessException ("Acesso negado! Login/Senha inválidos!");
				}

			case HttpStatus.SC_BAD_REQUEST:
				if (HttpHelper.isJsonResponse (response))
				{
					try
					{

						String stringResponse = HttpHelper.getResponseContentString (response);

						JSONObject error = new JSONObject (stringResponse);

						throw new BusinessException (error.getString ("MESSAGE"));

					}
					catch (JSONException e)
					{
						throw new TechnicalException ("Problemas na comunicação com o servidor!");
					}
				}
				else
				{
					throw new TechnicalException ("Problemas na comunicação com o servidor!");
				}

			default:
				if (HttpHelper.isAnHttpError (response.getStatusLine ().getStatusCode ()))
				{
					if (HttpHelper.isJsonResponse (response))
					{
						try
						{

							String stringResponse = HttpHelper.getResponseContentString (response);

							JSONObject error = new JSONObject (stringResponse);

							throw new BusinessException (error.getString ("MESSAGE"));

						}
						catch (JSONException e)
						{
							throw new TechnicalException ("Problemas na comunicação com o servidor!");
						}
					}
					else
					{
						throw new TechnicalException ("Problemas na comunicação com o servidor!");
					}
				}

				break;
		}
	}

	public long getServerTime (HttpResponse response)
	{
		try
		{
			String server = response.getHeaders ("Date") [0].getValue ();

			return new SimpleDateFormat ("EEE, d MMM yyyy HH:mm:ss Z", Locale.US).parse (server).getTime () / 1000;
		}
		catch (ParseException e)
		{
			throw new TechnicalException ("Não é possível obter a hora do servidor!", e);
		}
	}

	public String encrypt (String input, Long timestamp)
	{
		String key = CryptHelper.md5 (APPLICATION_KEY + String.valueOf (timestamp));

		return CryptHelper.encrypt (input, key);
	}

	public String decrypt (String input, Long timestamp)
	{
		String key = CryptHelper.md5 (APPLICATION_KEY + String.valueOf (timestamp));

		return CryptHelper.decrypt (input, key);
	}
}