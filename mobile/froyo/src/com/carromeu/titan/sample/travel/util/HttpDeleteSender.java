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

import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.message.BasicNameValuePair;

public class HttpDeleteSender
{
	private HttpDelete request;
	
	private List<NameValuePair> params;

	public HttpDeleteSender ()
	{
		params = new LinkedList<NameValuePair> ();
	}

	public HttpDeleteSender doDeleteTo (String uri)
	{
		request = new HttpDelete (uri);
		
		return this;
	}

	private HttpDelete getRequest ()
	{
		if (request == null)
			throw new IllegalStateException ("Não foi criado o objeto conexão através do método doHttpDelete!");

		return request;
	}

	public HttpDeleteSender param (String name, String value)
	{
		params.add (new BasicNameValuePair (name, value));
		
		return this;
	}

	public HttpResponse perform ()
	{
		return HttpHelper.doHttpDelete (request);
	}

	public HttpDeleteSender header (String name, String value)
	{
		getRequest ().setHeader (name, value);
		
		return this;
	}
}