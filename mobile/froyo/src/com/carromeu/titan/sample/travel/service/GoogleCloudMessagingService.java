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

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.carromeu.titan.sample.travel.AlertActivity;
import com.carromeu.titan.sample.travel.R.drawable;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GoogleCloudMessagingService extends IntentService
{
	public static final String NOTIFICATION_TITLE = "Embaixadas Brasileiras";
	
	private NotificationManager manager;
	
	private static final String TAG = "Titan-Service";
	
	public GoogleCloudMessagingService ()
	{
		super ("GoogleCloudMessagingService");
	}

	@Override
	protected void onHandleIntent (Intent intent)
	{
		Bundle extras = intent.getExtras ();
		
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance (this);
		
		String messageType = gcm.getMessageType (intent);

		if (!extras.isEmpty ())
		{
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals (messageType))
				sendNotification (0, "Error: " + extras.toString ());
			else
				if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals (messageType))
					sendNotification (0, "Deleted: " + extras.toString ());
				else
					if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals (messageType))
					{
						Log.e (TAG, extras.toString ());
						
						sendNotification (extras.getInt ("id"), extras.getString ("message"));
					}
		}
		
		GoogleCloudMessagingReceiver.completeWakefulIntent (intent);
	}
	
	private void sendNotification (Integer id, String message)
	{
		manager = (NotificationManager) this.getSystemService (Context.NOTIFICATION_SERVICE);

		PendingIntent intent = PendingIntent.getActivity (this, 0, new Intent (this, AlertActivity.class), 0);

		NotificationCompat.Builder builder = new NotificationCompat.Builder (this).setSmallIcon (drawable.notification).setContentTitle (NOTIFICATION_TITLE).setStyle (new NotificationCompat.BigTextStyle ().bigText (message)).setContentText (message);

		builder.setContentIntent (intent);
		
		manager.notify (id, builder.build ());
	}
}