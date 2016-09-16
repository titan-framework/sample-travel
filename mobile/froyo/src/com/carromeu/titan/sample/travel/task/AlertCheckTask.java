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

import android.os.AsyncTask;
import android.widget.Toast;
import com.carromeu.titan.sample.travel.fragment.AlertListFragment;
import com.carromeu.titan.sample.travel.model.Alert;
import com.carromeu.titan.sample.travel.ws.AlertWebService;

public class AlertCheckTask extends AsyncTask<Void, Void, Boolean>
{	
	private AlertListFragment fragment;
	
	private Exception exception;
	
	private String action;
	
	private Alert alert;
	
	public static final String READ = "READ";
	public static final String DELETE = "DELETE";

	public AlertCheckTask (AlertListFragment fragment, Alert alert, String action)
	{
		this.fragment = fragment;
		
		this.alert = alert;
		
		this.action = action;
	}

	@Override
	protected Boolean doInBackground (Void... params)
	{
		try
		{
			if (action == DELETE)
				AlertWebService.deleteAlert (alert.getId ());
			else if (action == READ)
				AlertWebService.readAlert (alert.getId ());
			
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
		if (!success && exception != null)
			Toast.makeText (fragment.getActivity (), exception.getMessage (), Toast.LENGTH_LONG).show ();
	}
}