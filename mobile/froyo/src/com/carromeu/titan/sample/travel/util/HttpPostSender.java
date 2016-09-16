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

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

public class HttpPostSender
{
	private HttpPost request;
	
	private List<NameValuePair> params;

	public HttpPostSender ()
	{
		params = new LinkedList<NameValuePair> ();
	}

	public HttpPostSender doPostTo (String uri)
	{
		request = new HttpPost (uri);
		
		return this;
	}

	private HttpPost getRequest ()
	{
		if (request == null)
			throw new IllegalStateException ("Não foi criado o objeto conexão através do método doHttpPost!");

		return request;
	}

	public HttpPostSender param (String name, String value)
	{
		params.add (new BasicNameValuePair (name, value));
		
		return this;
	}

	public HttpResponse perform ()
	{
		try
		{
			getRequest ().setEntity (new UrlEncodedFormEntity (params));

			return HttpHelper.doHttpPost (request);
		}
		catch (UnsupportedEncodingException e)
		{
			throw new RuntimeException ("Falha na comunicação com o servidor!", e);
		}
	}

	public HttpPostSender header (String name, String value)
	{
		getRequest ().setHeader (name, value);
		
		return this;
	}
}