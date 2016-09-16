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
import com.carromeu.titan.sample.travel.util.DrawableHelper;

public class CacheUrlImageTask extends AsyncTask<Void, Void, Boolean>
{
	private String url;

	public CacheUrlImageTask (String url)
	{
		this.url = url;
	}

	@Override
	protected Boolean doInBackground (Void... params)
	{
		if (url.equals (""))
			return false;

		try
		{
			DrawableHelper.singleton ().getFromUrl (url);

			return true;
		}
		catch (Exception e)
		{}

		return false;
	}
}