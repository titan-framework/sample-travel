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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.carromeu.titan.sample.travel.contract.AlertContract;
import com.carromeu.titan.sample.travel.exception.TechnicalException;

public class Database extends SQLiteOpenHelper
{
	private static final String NAME = "Travel.db";
	
	private String PATH;
	
	private static Database database;
	
	private Context context;
	
	public static final int VERSION = 1;
	
	private Database (Context context)
	{
		super (context, NAME, null, VERSION);
		
		this.context = context;

		PATH = context.getFilesDir ().getParentFile ().getPath () + "/databases/";
	}
	
	public static Database singleton (Context context)
	{
		if (database == null)
			database = new Database (context); 
		
		return database;
	}
	
	public static Database singleton ()
	{
		if (database == null)
			return null; 
		
		return database;
	}

	@Override
	public void onCreate (SQLiteDatabase db)
	{}

	@Override
	public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion)
	{}
	
	@Override
	protected void finalize () throws Throwable
	{
		Database.singleton ().close ();
		
		super.finalize ();
	}
	
	public void create ()
	{
		if (exists ())
			return;

		this.getReadableDatabase ();

		copy ();

		SQLiteDatabase db = this.getWritableDatabase ();

		db.execSQL (AlertContract.ddl ());
	}

	private void copy ()
	{
		InputStream input;

		try
		{
			input = context.getAssets ().open (NAME);

			String file = PATH + NAME;

			OutputStream output = new FileOutputStream (file);

			byte [] buffer = new byte [1024];

			int length;

			while ( (length = input.read (buffer)) > 0)
				output.write (buffer, 0, length);

			output.flush ();

			output.close ();

			input.close ();
		}
		catch (IOException e)
		{
			throw new TechnicalException ("Erro ao inicializar banco de dados!", e);
		}
	}

	private boolean exists ()
	{
		SQLiteDatabase db = null;

		try
		{
			String path = PATH + NAME;

			db = SQLiteDatabase.openDatabase (path, null, SQLiteDatabase.OPEN_READONLY);
		}
		catch (SQLiteException e)
		{}

		if (db == null)
			return false;
		
		db.close ();

		return true;
	}
}