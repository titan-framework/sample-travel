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

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import com.carromeu.titan.sample.travel.util.DrawableHelper;

public class LoadUrlImageTask extends AsyncTask<Void, Void, Boolean>
{
	private ImageView image;
	private String url;
	private Boolean showLoader;

	private Bitmap bmp;

	private ProgressDialog progress;

	public LoadUrlImageTask (ImageView image, String url)
	{
		this.image = image;
		this.url = url;
		this.showLoader = false;
	}

	public LoadUrlImageTask (ImageView image, String url, Boolean showLoader)
	{
		this.image = image;
		this.url = url;
		this.showLoader = showLoader;
	}

	@Override
	protected void onPreExecute ()
	{
		super.onPreExecute ();

		if (showLoader)
			progress = ProgressDialog.show (image.getContext (), "Carregando", "Carregando imagem...");
	}

	@Override
	protected Boolean doInBackground (Void... params)
	{
		if (url.equals (""))
			return false;

		try
		{
			bmp = DrawableHelper.singleton ().getFromUrl (url);

			return true;
		}
		catch (Exception e)
		{}

		return false;
	}

	@Override
	protected void onPostExecute (Boolean success)
	{
		if (success && bmp != null)
		{
			image.setImageBitmap (bmp);
			image.setVisibility (View.VISIBLE);
		}

		if (showLoader)
			progress.dismiss ();
	}
}