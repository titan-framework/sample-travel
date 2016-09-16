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

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.carromeu.titan.sample.travel.util.CredentialHelper;
import com.carromeu.titan.sample.travel.util.LogHelper;
import com.carromeu.titan.sample.travel.util.ScreenHelper;
import com.carromeu.titan.sample.travel.ws.AuthWebService;

public class AuthTask extends AsyncTask<Void, Void, Boolean>
{
	private ProgressDialog progressDialog;
	private Activity activity;
	private CredentialHelper credential;
	private Exception exception;
	
	private String clientId;
	private String clientPk;

	public AuthTask (Activity activity, CredentialHelper credential, String id, String pk)
	{
		this.activity = activity;
		this.credential = credential;
		
		clientId = id;
		clientPk = pk;
	}

	@Override
	protected void onPreExecute ()
	{
		ScreenHelper.lock (activity);
		
		progressDialog = ProgressDialog.show (activity, "Autenticando", "Verificando credenciais do dispositivo...", true, false);
	}

	@Override
	protected Boolean doInBackground (Void... params)
	{
		try
		{
			JSONObject json = AuthWebService.auth (clientId, clientPk);
			
			credential.setUserId (json.getLong ("id"));
			credential.setUserName (json.getString ("name"));
			credential.setUserMail (json.getString ("mail"));
			credential.setUserType (json.getString ("type"));
			
			return true;
		}
		catch (Exception e)
		{
			exception = e;
		}
		
		return false;
	}

	@Override
	protected void onPostExecute (Boolean success)
	{
		progressDialog.dismiss ();
		
		ScreenHelper.unlock (activity);

		if (success)
			credential.successClientCredential ();
		else
		{
			LogHelper.error (this, "Falha na autenticação", exception);
			
			credential.failClientCredential (exception.getMessage ());
		}
	}
}