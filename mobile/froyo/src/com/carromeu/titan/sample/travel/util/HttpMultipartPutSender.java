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
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;

import com.carromeu.titan.sample.travel.exception.TechnicalException;

public class HttpMultipartPutSender
{
	private HttpPut request;
	
	private MultipartEntityBuilder builder;

	public HttpMultipartPutSender ()
	{	
		builder = MultipartEntityBuilder.create ();
	}

	public HttpMultipartPutSender doHttpPutTo (String uri)
	{
		request = new HttpPut (uri);
		
		return this;
	}

	private HttpPut getRequest ()
	{
		if (request == null)
			throw new IllegalStateException ("Não foi criado o objeto conexão através do método doHttpPut");

		return request;
	}

	public HttpMultipartPutSender param (String name, String value) throws UnsupportedEncodingException
	{
		if (value == null)
			value = "";
		
		builder.addPart (name, new StringBody (value, ContentType.DEFAULT_TEXT));
		
		return this;
	}

	public HttpMultipartPutSender param (String name, Integer value) throws UnsupportedEncodingException
	{
		if (value == null)
			value = 0;
		
		builder.addPart (name, new StringBody (String.valueOf (value), ContentType.DEFAULT_TEXT));
		
		return this;
	}

	public HttpMultipartPutSender param (String name, Long value) throws UnsupportedEncodingException
	{
		if (value == null)
			value = Long.valueOf (0);
		
		builder.addPart (name, new StringBody (String.valueOf (value), ContentType.DEFAULT_TEXT));
		
		return this;
	}

	public HttpMultipartPutSender param (String name, Double value) throws UnsupportedEncodingException
	{
		if (value == null)
			value = Double.valueOf (0);
		
		builder.addPart (name, new StringBody (String.valueOf (value), ContentType.DEFAULT_TEXT));
		
		return this;
	}

	public HttpMultipartPutSender param (String name, File file, String mimeType)
	{
		builder.addBinaryBody (name, file, ContentType.DEFAULT_BINARY, file.getName ());
		
		return this;
	}

	public HttpResponse perform ()
	{
		try
		{
			getRequest ().setEntity (builder.build ());

			return HttpHelper.doHttpPut (request);
		}
		catch (Exception e)
		{
			throw new TechnicalException ("Falha na comunicação com o servidor!", e);
		}
	}

	public HttpMultipartPutSender header (String name, String value)
	{
		getRequest ().setHeader (name, value);
		
		return this;
	}
}