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

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;

import android.util.Log;

public class LogHelper
{
	private LogHelper ()
	{}

	public static void error (@SuppressWarnings ("rawtypes") Class c, String message, Throwable throwable)
	{
		Log.e (c.getName (), message, throwable);
	}

	public static void error (Object o, String message, Throwable throwable)
	{
		error (o.getClass (), message, throwable);
	}

	public static void error (Object o, Throwable throwable)
	{
		error (o.getClass (), throwable.getMessage (), throwable);
	}

	public static void error (@SuppressWarnings ("rawtypes") Class c, String message)
	{
		Log.e (c.getName (), message);
	}

	public static void error (Object o, String message)
	{
		error (o.getClass (), message);
	}

	public static void debug (Object o, HttpGet request)
	{
		debug (o.getClass (), request);
	}

	public static void debug (Object o, HttpPut request)
	{
		debug (o.getClass (), request);
	}

	public static void debug (@SuppressWarnings ("rawtypes") Class c, String message)
	{
		Log.d (c.getName (), message);
	}

	public static void debug (Object o, String message)
	{
		debug (o.getClass (), message);
	}

	public static void debug (@SuppressWarnings ("rawtypes") Class c, HttpGet request)
	{
		String tag = c.getName ();

		Log.d (tag, "HTTP GET");

		Log.d (tag, request.getURI ().toString ());

		for (Header header : request.getAllHeaders ())
			Log.d (tag, header.getName () + ": " + header.getValue ());
	}

	public static void debug (@SuppressWarnings ("rawtypes") Class c, HttpPost request)
	{
		String tag = c.getName ();

		Log.d (tag, "HTTP POST");

		Log.d (tag, request.getURI ().toString ());

		for (Header header : request.getAllHeaders ())
			Log.d (tag, header.getName () + ": " + header.getValue ());
	}

	public static void debug (@SuppressWarnings ("rawtypes") Class c, HttpPut request)
	{
		String tag = c.getName ();

		Log.d (tag, "HTTP PUT");

		Log.d (tag, request.getURI ().toString ());

		for (Header header : request.getAllHeaders ())
			Log.d (tag, header.getName () + ": " + header.getValue ());
	}

	public static void debug (@SuppressWarnings ("rawtypes") Class c, HttpDelete request)
	{
		String tag = c.getName ();

		Log.d (tag, "HTTP DELETE");

		Log.d (tag, request.getURI ().toString ());

		for (Header header : request.getAllHeaders ())
			Log.d (tag, header.getName () + ": " + header.getValue ());
	}

	public static void debug (@SuppressWarnings ("rawtypes") Class c, HttpResponse response)
	{
		String tag = c.getName ();

		Log.d (tag, "HTTP RESPONSE");

		Log.d (tag, "HTTP/" + response.getStatusLine ().getProtocolVersion () + " " + response.getStatusLine ().getStatusCode () + " " + response.getStatusLine ().getReasonPhrase ());

		for (Header header : response.getAllHeaders ())
			Log.d (tag, header.getName () + ": " + header.getValue ());
	}

	public static void debug (Object o, HttpResponse response)
	{
		debug (o.getClass (), response);
	}

	public static void debug (Object caller, HttpPost request)
	{
		debug (caller.getClass (), request);
	}

	public static void debug (Object caller, Object msg)
	{
		debug (caller.getClass (), msg.toString ());
	}

	public static void debug (Object o, Throwable throwable)
	{
		Log.d (o.getClass ().getName (), throwable.getMessage (), throwable);
	}

	public static void debug (String tag, Object msg)
	{
		Log.d (tag, msg.toString ());
	}

	public static void debug (String tag, String msg)
	{
		Log.d (tag, msg);
	}

	public static void d (Object... msgs)
	{
		StackTraceElement [] stack = Thread.currentThread ().getStackTrace ();
		StackTraceElement topElement = stack [0];

		String caller = topElement.getClassName () + "#" + topElement.getMethodName ();

		debug (caller, StringHelper.join (msgs));
	}
}