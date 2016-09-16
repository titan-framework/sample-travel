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

import java.util.List;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import com.carromeu.titan.sample.travel.Travel;
import com.carromeu.titan.sample.travel.converter.AlertConverter;
import com.carromeu.titan.sample.travel.dao.AlertDAO;
import com.carromeu.titan.sample.travel.fragment.AlertListFragment;
import com.carromeu.titan.sample.travel.model.Alert;
import com.carromeu.titan.sample.travel.util.ScreenHelper;
import com.carromeu.titan.sample.travel.ws.AlertWebService;

public class AlertTask extends AsyncTask<Void, Void, Boolean>
{
	private ProgressDialog progress;
	
	private AlertListFragment fragment;
	
	private Exception exception;

	public AlertTask (AlertListFragment fragment)
	{
		this.fragment = fragment;
	}

	@Override
	protected void onPreExecute ()
	{
		ScreenHelper.lock (fragment.getActivity ());
		
		progress = ProgressDialog.show (fragment.getActivity (), "Sincronizando Notificações", "Aguarde! Verificando suas mensagens de alerta...", true, false);
	}

	@Override
	protected Boolean doInBackground (Void... params)
	{
		try
		{
			String json = AlertWebService.alerts ();
			
			List<Alert> alerts = AlertConverter.fromJsonString (json);
			
			AlertDAO.singleton ().truncate ();

			if (alerts.size () == 0)
				return true;
			
			AlertDAO.singleton ().insert (alerts);
			
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
		progress.dismiss ();
		
		ScreenHelper.unlock (fragment.getActivity ());
		
		if (!success && exception != null)
			fragment.fail (exception.getMessage ());
		
		((Travel) fragment.getActivity ().getApplication ()).checkSync (fragment.getClass ().getName ());
		
		fragment.refresh ();
	}
}