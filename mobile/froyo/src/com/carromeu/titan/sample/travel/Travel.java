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

package com.carromeu.titan.sample.travel;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;

import com.carromeu.titan.sample.travel.util.Database;
import com.carromeu.titan.sample.travel.util.DrawableHelper;
import com.carromeu.titan.sample.travel.util.Preferences;

public class Travel extends Application
{
	public static final String googleApiProjectNumber = "277344265896";

	public static final String NAME = "travel";

	public static final String URL = "http://titan.cnpgc.embrapa.br/sample/travel/";

	private static Travel app;

	private String disambiguation;

	private Map<String, Boolean> wsSyncControl;
	
	static public Travel singleton ()
	{
		return app;
	}

	@Override
	public void onCreate ()
	{
		super.onCreate ();

		app = this;

		wsSyncControl = new HashMap<String, Boolean> ();

		SharedPreferences preferences = Preferences.singleton (this);

		setDisambiguation (preferences.getString ("disambiguation", ""));

		Database.singleton (this).create ();

		DrawableHelper.singleton (this);

		// For debug only
		// DrawableHelper.singleton ().clear ();

		disableSSL ();
	}

	public Boolean alreadySync (String entity)
	{
		return wsSyncControl.containsKey (entity);
	}

	public void checkSync (String entity)
	{
		wsSyncControl.put (entity, true);
	}

	public void clearSync ()
	{
		wsSyncControl = new HashMap<String, Boolean> ();
	}

	public int getVersion ()
	{
		try
		{
			return getPackageManager ().getPackageInfo (getPackageName (), 0).versionCode;
		}
		catch (NameNotFoundException e)
		{
			throw new RuntimeException ("Could not get package name: " + e);
		}
	}

	public String getDisambiguation ()
	{
		return disambiguation;
	}

	public void setDisambiguation (String disambiguation)
	{
		this.disambiguation = disambiguation.replaceAll ("[^0-9]", "");
	}

	public static void disableSSL ()
	{
		try
		{
			SSLContext ctx = SSLContext.getInstance ("TLS");

			ctx.init (null, new TrustManager [] { new X509TrustManager ()
			{

				@Override
				public X509Certificate [] getAcceptedIssuers ()
				{
					return new X509Certificate [] {};
				}

				@Override
				public void checkServerTrusted (X509Certificate [] chain, String authType) throws CertificateException
				{}

				@Override
				public void checkClientTrusted (X509Certificate [] chain, String authType) throws CertificateException
				{}
			} }, null);

			HttpsURLConnection.setDefaultSSLSocketFactory (ctx.getSocketFactory ());
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace ();
		}
		catch (KeyManagementException e)
		{
			e.printStackTrace ();
		}

		HttpsURLConnection.setDefaultHostnameVerifier (new HostnameVerifier ()
		{
			@Override
			public boolean verify (String hostname, SSLSession session)
			{
				return true;
			}
		});
	}
}