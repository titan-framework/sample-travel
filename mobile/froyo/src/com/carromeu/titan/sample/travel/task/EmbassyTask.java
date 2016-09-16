/**
 * Copyright © 2013 Titan Framework. All Rights Reserved.
 *
 * Developed by Laboratory for Precision Livestock, Environment and Software Engineering (PLEASE Lab)
 * of Embrapa Beef Cattle at Campo Grande - MS - Brazil.
 * 
 * @see http://please.cnpgc.embrapa.br
 * 
 * @author Camilo Carromeu <camilo@carromeu.com>
 * @author Jairo Ricardes Rodrigues Filho <jairocgr@gmail.com>
 * 
 * @version 14.06-1-alpha
 */

package com.carromeu.titan.sample.travel.task;

import java.util.List;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.carromeu.titan.sample.travel.Travel;
import com.carromeu.titan.sample.travel.converter.EmbassyConverter;
import com.carromeu.titan.sample.travel.dao.EmbassyDAO;
import com.carromeu.titan.sample.travel.fragment.EmbassyListFragment;
import com.carromeu.titan.sample.travel.model.Embassy;
import com.carromeu.titan.sample.travel.util.Preferences;
import com.carromeu.titan.sample.travel.util.ScreenHelper;
import com.carromeu.titan.sample.travel.ws.EmbassyWebService;

public class EmbassyTask extends AsyncTask<Void, Void, Boolean>
{
	private ProgressDialog progress;
	
	private EmbassyListFragment fragment;
	
	private Exception exception;
	
	public EmbassyTask (EmbassyListFragment fragment)
	{
		this.fragment = fragment;
	}

	@Override
	protected void onPreExecute ()
	{
		ScreenHelper.lock (fragment.getActivity ());
		
		progress = ProgressDialog.show (fragment.getActivity (), "Sincronizando...", "Aguarde! Se esta for a primeira vez que você faz esta sincronização, poderá demorar alguns minutos.", true, false);
	}

	@Override
	protected Boolean doInBackground (Void... params)
	{
		try
		{
			EmbassyDAO.singleton ().load (fragment.getActivity ().getApplication ().getAssets ());
			
			String active = EmbassyWebService.active ();
			
			EmbassyDAO.singleton ().deleteNonActive (active);
			
			SharedPreferences preferences = Preferences.singleton ();
			
			long time = preferences.getLong ("lastSyncForEmbassy", 0);
			
			EmbassyWebService ws = new EmbassyWebService ();
			
			String json = ws.list (time);
			
			List<Embassy> list = EmbassyConverter.fromJsonString (json);
			
			if (list.size () == 0)
				return true;
			
			EmbassyDAO.singleton ().insertOrUpdate (list);
			
			SharedPreferences.Editor editor = preferences.edit ();
			
			editor.putLong ("lastSyncForEmbassy", ws.getServerTime ());

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
		progress.dismiss ();
		
		ScreenHelper.unlock (fragment.getActivity ());
		
		if (!success && exception != null)
			fragment.fail (exception.getMessage ());
		
		((Travel) fragment.getActivity ().getApplication ()).checkSync (fragment.getClass ().getName ());
		
		fragment.refresh ();
	}
}
