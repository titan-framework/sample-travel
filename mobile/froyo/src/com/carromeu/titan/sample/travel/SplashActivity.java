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

package com.carromeu.titan.sample.travel;

import java.util.Timer;
import java.util.TimerTask;

import roboguice.inject.ContentView;
import android.content.Intent;
import android.os.Bundle;

import com.carromeu.titan.sample.travel.util.CredentialHelper;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockActivity;

@ContentView (R.layout.splash)
public class SplashActivity extends RoboSherlockActivity
{
	@Override
	protected void onCreate (Bundle bundle)
	{
		super.onCreate (bundle);

		new Timer ().schedule (new TimerTask ()
		{
			@Override
			public void run ()
			{
				if (!CredentialHelper.hasValidCredentials () || ((Travel) getApplication ()).getDisambiguation ().equals (""))
					startActivity (new Intent (SplashActivity.this, RegisterActivity.class));
				else
					startActivity (new Intent (SplashActivity.this, EmbassyActivity.class));
			}
		}, 2000);
	}

	@Override
	protected void onRestart ()
	{
		super.onRestart ();

		finish ();
	}	
}