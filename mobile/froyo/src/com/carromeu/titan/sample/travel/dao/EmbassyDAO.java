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

package com.carromeu.titan.sample.travel.dao;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.carromeu.titan.sample.travel.Travel;
import com.carromeu.titan.sample.travel.contract.EmbassyContract;
import com.carromeu.titan.sample.travel.converter.EmbassyConverter;
import com.carromeu.titan.sample.travel.exception.TechnicalException;
import com.carromeu.titan.sample.travel.model.Embassy;
import com.carromeu.titan.sample.travel.util.Database;
import com.carromeu.titan.sample.travel.util.Preferences;

public class EmbassyDAO 
{
	private static EmbassyDAO dao;
	
	private SQLiteDatabase db;
	
	private SharedPreferences preferences;
	
	private EmbassyDAO ()
	{
		db = Database.singleton ().getWritableDatabase ();
		
		preferences = Preferences.singleton ();
	}
	
	public static EmbassyDAO singleton ()
	{
		if (dao == null)
			dao = new EmbassyDAO ();
		
		return dao;
	}
	
	public String next ()
	{
		long id = preferences.getLong ("lastIdForEmbassy", 0) + 1;

		SharedPreferences.Editor editor = preferences.edit ();

		editor.putLong ("lastIdForEmbassy", id);

		editor.commit ();

		return Travel.singleton ().getDisambiguation () + "." + String.valueOf (id);
	}
	
	public int load (AssetManager assets)
	{
		int lines = 0;
		
		try
		{
			if (preferences.getLong ("lastSyncForEmbassy", 0) > 0)
				return 0;
			
			if (this.empty ())
			{
				InputStream is = assets.open (EmbassyContract.TABLE + ".sql");
				
				BufferedReader br = new BufferedReader (new InputStreamReader (is));
				
				String line;
				
				while ((line = br.readLine ()) != null)
				{
					Log.i (getClass ().getName (), line);
					
					db.execSQL (line);
					
					lines++;
				}
				
				br.close ();
				
				is.close ();
			}
			
			Cursor cursor = db.query (EmbassyContract.TABLE, new String [] { EmbassyContract.LAST_CHANGE }, null, null, null, null, EmbassyContract.LAST_CHANGE + " DESC", "1");

			cursor.moveToNext ();
			
			SharedPreferences.Editor editor = preferences.edit ();
			
			editor.putLong ("lastSyncForEmbassy", cursor.getLong (cursor.getColumnIndexOrThrow (EmbassyContract.LAST_CHANGE)));

			editor.commit ();
			
			cursor.close ();
		}
		catch (Exception e)
		{
			throw new TechnicalException ("Impossível carregar os dados iniciais!", e);
		}
		
		return lines;
	}
	
	public List<Embassy> list ()
	{
		Cursor cursor = db.query (EmbassyContract.TABLE, EmbassyContract.columns (), null, null, null, null, EmbassyContract.COUNTRY);
		
		List<Embassy> list = new LinkedList<Embassy> ();
		
		while (cursor.moveToNext ())
			list.add (EmbassyConverter.from (cursor));
		
		cursor.close ();
		
		return list;
	}
	
	public List<Embassy> search (String term)
	{
		Cursor cursor = db.query (EmbassyContract.TABLE, EmbassyContract.columns (), EmbassyContract.COUNTRY + " LIKE ? OR " + EmbassyContract.DESCRIPTION + " LIKE ?", new String [] { "%" + term + "%", "%" + term + "%" }, null, null, EmbassyContract.COUNTRY);
		
		List<Embassy> list = new LinkedList<Embassy> ();
		
		while (cursor.moveToNext ())
			list.add (EmbassyConverter.from (cursor));
		
		cursor.close ();
		
		return list;
	}
	
	public List<Embassy> changed (long time)
	{
		Cursor cursor = db.query (EmbassyContract.TABLE, EmbassyContract.columns (), EmbassyContract.LAST_CHANGE + " > ?", new String [] { String.valueOf (time) }, null, null, null);
		
		List<Embassy> list = new LinkedList<Embassy> ();
		
		while (cursor.moveToNext ())
			list.add (EmbassyConverter.from (cursor));
		
		cursor.close ();
		
		return list;
	}
	
	public Embassy get (String code)
	{
		Cursor cursor = db.query (EmbassyContract.TABLE, EmbassyContract.columns (), EmbassyContract.CODE + " = ?", new String [] { code }, null, null, null, "1");

		cursor.moveToNext ();

		Embassy p = EmbassyConverter.from (cursor);
		
		cursor.close ();
		
		return p;
	}
	
	public void truncate ()
	{
		db.delete (EmbassyContract.TABLE, null, null);
	}
	
	public void insert (List<Embassy> list)
	{
		for (Embassy item : list)
			insert (item);
	}
	
	public void insert (Embassy item)
	{
		ContentValues v = EmbassyConverter.toContentValue (item);
		
		try
		{
			db.insertOrThrow (EmbassyContract.TABLE, null, v);
		}
		catch (SQLException e)
		{
			throw new TechnicalException ("Impossível gravar os dados!", e);
		}
	}
	
	public void update (List<Embassy> list)
	{
		for (Embassy item : list)
			update (item);
	}
	
	public void update (Embassy item)
	{
		ContentValues v = EmbassyConverter.toContentValue (item);
		
		try
		{
			db.update (EmbassyContract.TABLE, v, EmbassyContract.CODE + " = ?", new String [] { String.valueOf (item.getCode ()) });
		}
		catch (SQLException e)
		{
			throw new TechnicalException ("Impossível atualizar os dados!", e);
		}
	}
	
	public void insertOrUpdate (List<Embassy> list)
	{
		for (Embassy item : list)
			insertOrUpdate (item);
	}
	
	public void insertOrUpdate (Embassy item)
	{
		ContentValues v = EmbassyConverter.toContentValue (item);
		
		try
		{
			db.insertOrThrow (EmbassyContract.TABLE, null, v);
		}
		catch (SQLException e)
		{
			db.update (EmbassyContract.TABLE, v, EmbassyContract.CODE + " = ?", new String [] { String.valueOf (item.getCode ()) });
		}
	}
	
	public void delete (String code)
	{
		db.delete (EmbassyContract.TABLE, EmbassyContract.CODE + " = ?", new String [] { code });
	}
	
	public void delete (Embassy item)
	{
		db.delete (EmbassyContract.TABLE, EmbassyContract.CODE + " = ?", new String [] { String.valueOf (item.getCode ()) });
	}
	
	public void deleteNonActive (String active)
	{
		db.delete (EmbassyContract.TABLE, EmbassyContract.CODE + " NOT IN (" + active.replaceAll ("[^0-9,\\.\"]", "") + ")", null);
	}
	
	public boolean empty ()
	{
		Cursor cursor = db.query (EmbassyContract.TABLE, new String [] { EmbassyContract.CODE }, null, null, null, null, null);
		
		return cursor.getCount () == 0;
	}
}
