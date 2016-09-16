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

package com.carromeu.titan.sample.travel.task;

import java.io.IOException;
import java.util.Date;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.carromeu.titan.sample.travel.util.CredentialHelper;
import com.carromeu.titan.sample.travel.util.LogHelper;
import com.carromeu.titan.sample.travel.util.ScreenHelper;
import com.carromeu.titan.sample.travel.util.WebServiceHelper;
import com.carromeu.titan.sample.travel.ws.RegisterWebService;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;

public class RegisterTask extends AsyncTask<Void, Void, Boolean>
{
	private CredentialHelper credential;
	
	private Activity activity;
	
	private String mail;
	
	private Exception exception;
	
	private ProgressDialog dialog;
	
	private String id;
	
	private String pk;
	
	public RegisterTask (Activity activity, CredentialHelper credential, String mail)
	{
		this.activity = activity;
		
		this.credential = credential;
		
		this.mail = mail;
	}
	
	@Override
	protected void onPreExecute ()
	{
		ScreenHelper.lock (activity);
		
		dialog = ProgressDialog.show (activity, "Registrando Usuário", "Seu cadastro no Embaixadas Brasileiras está sendo criado! Por favor, aguarde...", true, false);
	}

	@Override
	protected Boolean doInBackground (Void... params)
	{
		try
		{
			String token = fetchToken ();
			
			if (token == null)
				return null;
			
			Log.e (this.getClass ().getName (), "Google Token Fetched: " + token);
			
			RegisterWebService ws = new RegisterWebService ();
			
			Long timestamp = new Date ().getTime () / 1000;
			
			JSONObject jsonClient = ws.register (mail, token, android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL, timestamp);
			
			id = jsonClient.getString ("id");
			
			pk = WebServiceHelper.singleton ().decrypt (jsonClient.getString ("pk"), timestamp);
			
			Log.e (this.getClass ().getName (), "Client Credentials Getted: " + id + " / " + pk);
			
			GoogleAuthUtil.invalidateToken (activity, token);
			
			return true;
		}
		catch (Exception e)
		{
			Log.e (getClass ().getName (), e.getMessage ());
			
			exception = e;
		}
		
		return false;
	}
	
	@Override
	protected void onPostExecute (Boolean success)
	{
		dialog.dismiss ();
		
		ScreenHelper.unlock (activity);
		
		if (success == null)
			return;
		
		if (success)
			credential.getUserByClientCredential (id, pk);
		else
		{
			LogHelper.error (this, "Falha na autenticação", exception);
			
			credential.failClientCredential (exception.getMessage ());
		}
	}
	
	protected String fetchToken () throws IOException, GoogleAuthException
	{
		try
		{
			return GoogleAuthUtil.getToken (activity, mail, CredentialHelper.SCOPE);
		}
		catch (GooglePlayServicesAvailabilityException playEx)
		{
			Log.e (getClass ().getName (), "11");
			
			credential.showGoogleDialog (playEx.getConnectionStatusCode ());
		}
		catch (UserRecoverableAuthException userRecoverableException)
		{
			Log.e (getClass ().getName (), "22");
			
			activity.startActivityForResult (userRecoverableException.getIntent (), CredentialHelper.AUTH_ERROR);
		}
		
		return null;
	}
}