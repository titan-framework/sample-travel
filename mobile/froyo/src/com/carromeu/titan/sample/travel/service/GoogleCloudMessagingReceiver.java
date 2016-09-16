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

package com.carromeu.titan.sample.travel.service;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class GoogleCloudMessagingReceiver extends WakefulBroadcastReceiver
{
	@Override
	public void onReceive (Context context, Intent intent)
	{
		// Explicitly specify that GcmIntentService will handle the intent.
		ComponentName comp = new ComponentName (context.getPackageName (), GoogleCloudMessagingService.class.getName ());
		
		// Start the service, keeping the device awake while it is launching.
		startWakefulService (context, (intent.setComponent (comp)));
		
		setResultCode (Activity.RESULT_OK);
	}
}
