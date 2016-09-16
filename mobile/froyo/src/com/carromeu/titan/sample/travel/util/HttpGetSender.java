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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import com.carromeu.titan.sample.travel.exception.TechnicalException;

public class HttpGetSender
{
	private HttpGet request;

	private List<NameValuePair> params;

	public static HttpGetSender get (String uri)
	{
		return new HttpGetSender ().doGetTo (uri);
	}

	public HttpGetSender ()
	{
		params = new LinkedList<NameValuePair> ();
	}

	public HttpGetSender doGetTo (String uri)
	{
		request = new HttpGet (uri);

		return this;
	}

	private HttpGet getRequest ()
	{
		if (request == null)
			throw new IllegalStateException ("Não foi criado o objeto conexão através do método doHttpGet!");

		return request;
	}

	public HttpGetSender param (String name, String value)
	{
		params.add (new BasicNameValuePair (name, value));

		return this;
	}

	public HttpResponse perform ()
	{
		URI uri = request.getURI ();

		try
		{

			request.setURI (new URI (uri.toString () + "?" + URLEncodedUtils.format (params, "UTF-8")));

		}
		catch (URISyntaxException e)
		{
			throw new TechnicalException ("Problemas na requisição feita com o servidor!", e);
		}

		return HttpHelper.doHttpGet (request);
	}

	public HttpGetSender header (String name, String value)
	{
		getRequest ().setHeader (name, value);

		return this;
	}

	public HttpGetSender param (String name, Boolean value)
	{
		params.add (new BasicNameValuePair (name, value.toString ()));

		return this;
	}
}