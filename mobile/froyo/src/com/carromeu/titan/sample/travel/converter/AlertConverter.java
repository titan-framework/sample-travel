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

package com.carromeu.titan.sample.travel.converter;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import com.carromeu.titan.sample.travel.contract.AlertContract;
import com.carromeu.titan.sample.travel.exception.TechnicalException;
import com.carromeu.titan.sample.travel.model.Alert;

public class AlertConverter
{
	private AlertConverter ()
	{}
	
	public static List<Alert> fromJsonString (String str)
	{
		List<Alert> items = new LinkedList<Alert> ();
		
		try
		{
			JSONArray array = new JSONArray (str);
			
			for (int i = 0; i < array.length (); i++)
				items.add (from (array.getJSONObject (i)));			
		}
		catch (JSONException e)
		{}
		
		return items;
	}

	public static Alert from (JSONObject jsonObject)
	{
		try
		{
			Alert alert = new Alert ();
			alert.setId (jsonObject.getLong (AlertContract.ID));
			alert.setMessage (jsonObject.getString (AlertContract.MESSAGE));
			alert.setIcon (jsonObject.getString (AlertContract.ICON));
			alert.setRead (Boolean.valueOf (jsonObject.getString (AlertContract.READ)));

			return alert;
		}
		catch (JSONException e)
		{
			throw new TechnicalException ("Falha ao tentar converter JSON de alertas!", e);
		}
	}

	public static Alert from (Cursor c)
	{
		Alert alert = new Alert ();

		alert.setId (c.getLong (c.getColumnIndexOrThrow (AlertContract.ID)));
		alert.setMessage (c.getString (c.getColumnIndexOrThrow (AlertContract.MESSAGE)));
		alert.setIcon (c.getString (c.getColumnIndexOrThrow (AlertContract.ICON)));
		alert.setRead (c.getInt (c.getColumnIndexOrThrow (AlertContract.READ)) == 1 ? true : false);
		
		return alert;
	}
	
	public static ContentValues toContentValue (Alert alert)
	{
		ContentValues value = new ContentValues ();
		
		value.put (AlertContract.ID, alert.getId ());
		value.put (AlertContract.MESSAGE, alert.getMessage ());
		value.put (AlertContract.ICON, alert.getIcon ());
		value.put (AlertContract.READ, alert.getRead () ? 1 : 0);
		
		return value;
	}
}