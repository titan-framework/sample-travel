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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;

import com.carromeu.titan.sample.travel.exception.TechnicalException;

public class HttpMultipartPostSender
{
	private HttpPost request;
	
	private MultipartEntityBuilder builder;

	public HttpMultipartPostSender ()
	{	
		builder = MultipartEntityBuilder.create ();
	}

	public HttpMultipartPostSender doHttpPostTo (String uri)
	{
		log("building multipart http post");
		
		request = new HttpPost (uri);
		
		return this;
	}

	private HttpPost getRequest ()
	{
		if (request == null)
			throw new IllegalStateException ("Não foi criado o objeto conexão através do método doHttpPost");

		return request;
	}

	public HttpMultipartPostSender param (String name, String value) throws UnsupportedEncodingException
	{
		logParam(name, value);
		
		builder.addPart (name, new StringBody (value, ContentType.DEFAULT_TEXT));
		
		return this;
	}

	public HttpMultipartPostSender param (String name, Integer value) throws UnsupportedEncodingException
	{
		logParam(name, String.valueOf(value));
		
		builder.addPart (name, new StringBody (String.valueOf (value), ContentType.DEFAULT_TEXT));
		
		return this;
	}

	public HttpMultipartPostSender param (String name, Long value) throws UnsupportedEncodingException
	{
		logParam(name, String.valueOf(value));
		
		builder.addPart (name, new StringBody (String.valueOf (value), ContentType.DEFAULT_TEXT));
		
		return this;
	}

	public HttpMultipartPostSender param (String name, Double value) throws UnsupportedEncodingException
	{
		logParam(name, String.valueOf(value));
		
		builder.addPart (name, new StringBody (String.valueOf (value), ContentType.DEFAULT_TEXT));
		
		return this;
	}

	public HttpMultipartPostSender param (String name, File file, String mimeType)
	{
		logParam(name, file);
		
		builder.addBinaryBody (name, file, ContentType.DEFAULT_BINARY, file.getName ());
		
		return this;
	}

	private void logParam(String name, File file) {
		logParam(name, file.getPath());
	}
	
	private void log(String msg) {
		LogHelper.debug(this, msg);
	}

	private void logParam(String name, String value) {
		LogHelper.debug(this, name + ": " + value);
	}

	public HttpResponse perform ()
	{
		try
		{
			HttpEntity entity = builder.build();
			
			getRequest ().setEntity (entity);

			return HttpHelper.doHttpPost (request);
		}
		catch (Exception e)
		{
			throw new TechnicalException ("Falha na comunicação com o servidor!", e);
		}
	}

	public HttpMultipartPostSender header (String name, String value)
	{
		getRequest ().setHeader (name, value);
		
		return this;
	}
}