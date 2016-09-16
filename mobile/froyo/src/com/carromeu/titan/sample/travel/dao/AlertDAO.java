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

package com.carromeu.titan.sample.travel.dao;

import java.util.LinkedList;
import java.util.List;

import com.carromeu.titan.sample.travel.contract.AlertContract;
import com.carromeu.titan.sample.travel.converter.AlertConverter;
import com.carromeu.titan.sample.travel.exception.TechnicalException;
import com.carromeu.titan.sample.travel.model.Alert;
import com.carromeu.titan.sample.travel.util.Database;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class AlertDAO
{
	private static AlertDAO dao; 
	
	private SQLiteDatabase db;
	
	private AlertDAO ()
	{
		db = Database.singleton ().getWritableDatabase ();
	}
	
	public static AlertDAO singleton ()
	{
		if (dao == null)
			dao = new AlertDAO ();
		
		return dao;
	}
	
	public List<Alert> list ()
	{
		Cursor cursor = db.query (AlertContract.TABLE, AlertContract.columns (), null, null, null, null, AlertContract.ID + " DESC");

		List<Alert> alerts = new LinkedList<Alert> ();
		
		while (cursor.moveToNext ())
			alerts.add (AlertConverter.from (cursor));

		cursor.close ();

		return alerts;
	}
	
	public void truncate ()
	{
		db.delete (AlertContract.TABLE, null, null);
	}
	
	public void insert (List<Alert> alerts)
	{
		for (Alert alert : alerts)
		{
			ContentValues v = AlertConverter.toContentValue (alert);
			
			try
			{
				db.insertOrThrow (AlertContract.TABLE, null, v);
			}
			catch (SQLException e)
			{
				throw new TechnicalException ("Impossível atualizar os alertas!", e);
			}
		}
	}
	
	public void delete (Long id)
	{
		db.delete (AlertContract.TABLE, AlertContract.ID + " = ?", new String [] { String.valueOf (id) });
	}
}