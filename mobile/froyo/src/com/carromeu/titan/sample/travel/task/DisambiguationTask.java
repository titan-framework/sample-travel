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

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.carromeu.titan.sample.travel.Travel;
import com.carromeu.titan.sample.travel.util.LogHelper;
import com.carromeu.titan.sample.travel.util.Preferences;
import com.carromeu.titan.sample.travel.ws.DisambiguationWebService;

public class DisambiguationTask extends AsyncTask<Void, Void, Boolean>
{
	private Activity activity;
	private Exception exception;
	
	public DisambiguationTask (Activity activity)
	{
		this.activity = activity;
	}
	
	@Override
	protected Boolean doInBackground (Void... params)
	{
		try
		{
			if (!Preferences.singleton ().getString ("disambiguation", "").replaceAll ("[^0-9]", "").equals (""))
				return true;
			
			Travel app = (Travel) activity.getApplication ();
			
			String disambiguation = DisambiguationWebService.disambiguation ();
			
			app.setDisambiguation (disambiguation);
			
			SharedPreferences.Editor editor = Preferences.singleton ().edit ();
			
			editor.putString ("disambiguation", disambiguation);

			editor.commit ();
			
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
		if (!success)
			LogHelper.error (this, "Falha na obtenção do desambiguador!", exception);
	}
}