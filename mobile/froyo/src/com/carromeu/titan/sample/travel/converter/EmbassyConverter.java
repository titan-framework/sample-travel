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

package com.carromeu.titan.sample.travel.converter;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;

import com.carromeu.titan.sample.travel.exception.TechnicalException;
import com.carromeu.titan.sample.travel.contract.EmbassyContract;
import com.carromeu.titan.sample.travel.model.Embassy;

public class EmbassyConverter 
{
	private EmbassyConverter ()
	{}
	
	public static List<Embassy> fromJsonString (String json)
	{
		try
		{
			JSONArray array = new JSONArray (json);
			
			List<Embassy> list = new LinkedList<Embassy> ();
			
			for (int i = 0; i < array.length (); i++)
				list.add (from (array.getJSONObject (i)));

			return list;
		}
		catch (JSONException e)
		{
			throw new TechnicalException ("Falha ao tentar abrir a lista de itens (JSON)!", e);
		}
	}

	public static Embassy from (JSONObject json)
	{
		try
		{
			Embassy item = new Embassy ();
			
			item.setCode (json.getString (EmbassyContract.CODE));
			item.setCountry (json.getString (EmbassyContract.COUNTRY));
			item.setDescription (json.getString (EmbassyContract.DESCRIPTION));
			item.setLastChange (new Date (json.getLong (EmbassyContract.LAST_CHANGE) * 1000));
			
			return item;
		}
		catch (JSONException e)
		{
			e.printStackTrace ();
			
			throw new TechnicalException ("Falha ao tentar converter JSON!", e);
		}
	}

	public static Embassy from (Cursor cursor)
	{
		Embassy item = new Embassy ();

		item.setCode (cursor.getString (cursor.getColumnIndexOrThrow (EmbassyContract.CODE)));
		item.setCountry (cursor.getString (cursor.getColumnIndexOrThrow (EmbassyContract.COUNTRY)));
		item.setDescription (cursor.getString (cursor.getColumnIndexOrThrow (EmbassyContract.DESCRIPTION)));
		item.setLastChange (new Date (cursor.getLong (cursor.getColumnIndexOrThrow (EmbassyContract.LAST_CHANGE)) * 1000));
		
		return item;
	}
	
	public static ContentValues toContentValue (Embassy item)
	{
		ContentValues value = new ContentValues ();

		value.put (EmbassyContract.CODE, item.getCode ());
		value.put (EmbassyContract.COUNTRY, item.getCountry ());
		value.put (EmbassyContract.DESCRIPTION, item.getDescription ());
		
		if (item.getLastChange () != null)
			value.put (EmbassyContract.LAST_CHANGE, item.getLastChange ().getTime () / 1000);
		
		
		return value;
	}
}
