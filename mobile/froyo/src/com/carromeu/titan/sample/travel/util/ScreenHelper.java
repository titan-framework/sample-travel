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

package com.carromeu.titan.sample.travel.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.WindowManager;

public class ScreenHelper
{
	private ScreenHelper ()
	{}

	public static void lock (Activity activity)
	{
		WindowManager windowManager = (WindowManager) activity.getSystemService (Context.WINDOW_SERVICE);

		Configuration configuration = activity.getResources ().getConfiguration ();

		int rotation = windowManager.getDefaultDisplay ().getRotation ();

		if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE && (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) || configuration.orientation == Configuration.ORIENTATION_PORTRAIT && (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270))
		{
			switch (rotation)
			{
				case Surface.ROTATION_0:
					activity.setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
					break;

				case Surface.ROTATION_90:
					activity.setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
					break;

				case Surface.ROTATION_180:
					activity.setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
					break;

				case Surface.ROTATION_270:
					activity.setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
					break;
			}
		}
		else
		{
			switch (rotation)
			{
				case Surface.ROTATION_0:
					activity.setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
					break;

				case Surface.ROTATION_90:
					activity.setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
					break;

				case Surface.ROTATION_180:
					activity.setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
					break;

				case Surface.ROTATION_270:
					activity.setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
					break;
			}
		}
	}

	public static void unlock (Activity activity)
	{
		activity.setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
	}

	public static Dimension getScreenDimension (Context context)
	{
		WindowManager wm = (WindowManager) context.getSystemService (Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics ();
		wm.getDefaultDisplay ().getMetrics (metrics);

		return new Dimension (metrics.widthPixels, metrics.heightPixels, metrics.scaledDensity);
	}
}