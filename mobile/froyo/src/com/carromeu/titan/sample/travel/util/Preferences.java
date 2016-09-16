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

import android.content.Context;
import android.content.SharedPreferences;
import com.carromeu.titan.sample.travel.Travel;

public class Preferences
{
	private static SharedPreferences preferences;
	
	private Preferences ()
	{}
	
	public static SharedPreferences singleton (Travel app)
	{
		if (preferences == null)
			preferences = app.getSharedPreferences ("com.carromeu.titan.sample.travel", Context.MODE_PRIVATE);
		
		return preferences;
	}
	
	public static SharedPreferences singleton ()
	{
		return preferences;
	}
	
	public static void clear ()
	{
		SharedPreferences.Editor editor = singleton ().edit ();
		
		editor.clear ();
		
		editor.commit ();
	}

	public static void set(String key, Long value) {
		SharedPreferences.Editor editor = singleton().edit();
		
		editor.putLong(key, value);
		
		editor.commit();
	}

	public static void unset(String key) {
		SharedPreferences.Editor editor = singleton().edit();
		
		editor.remove(key);
		
		editor.commit();
	}
}