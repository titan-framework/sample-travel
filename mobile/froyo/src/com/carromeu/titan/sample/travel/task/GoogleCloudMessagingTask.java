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
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;
import com.carromeu.titan.sample.travel.Travel;
import com.carromeu.titan.sample.travel.util.Preferences;
import com.carromeu.titan.sample.travel.ws.GoogleCloudMessagingWebService;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GoogleCloudMessagingTask extends AsyncTask<Void, Void, Boolean>
{
	private Activity activity;
	
	private Exception exception;
	
	private String device;
	
	public GoogleCloudMessagingTask (Activity activity)
	{
		this.activity = activity;
	}
	
	@Override
	protected Boolean doInBackground (Void... params)
	{
		try
		{
			GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance (activity.getApplicationContext ());
			
			String registrationId = gcm.register (Travel.googleApiProjectNumber);
			
			JSONObject json = GoogleCloudMessagingWebService.register (registrationId);
			
			device = json.getString ("device");
			
			SharedPreferences.Editor prefs = Preferences.singleton ().edit ();

			prefs.putString ("gcm-id", registrationId);
			prefs.putInt ("gcm-version", ((Travel) activity.getApplication ()).getVersion ());
			
			prefs.commit ();
			
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
		if (success)
			Toast.makeText (activity, "Dispositivo \"" + device + "\" registrado com sucesso!", Toast.LENGTH_LONG).show ();
		else
			Toast.makeText (activity, "Erro ao registrar dispositivo: " + exception.getMessage (), Toast.LENGTH_LONG).show ();
	}
}