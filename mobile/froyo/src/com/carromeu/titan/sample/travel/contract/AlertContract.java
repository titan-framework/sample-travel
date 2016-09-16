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

package com.carromeu.titan.sample.travel.contract;

public class AlertContract
{
	public static String TABLE = "alert";
	
	public static String ID = "id";
	public static String MESSAGE = "message";
	public static String ICON = "icon";
	public static String READ = "read";
	
	public static String ddl ()
	{
		String ddl = "CREATE TABLE " + TABLE + " (" +
				ID + " INTEGER PRIMARY KEY, " +
				MESSAGE + " TEXT, " +
				ICON + " TEXT, " +
				READ + " INTEGER" +
			");";
		
		return ddl;
	}
	
	public static String [] columns ()
	{
		return new String [] { ID, MESSAGE, ICON, READ };
	}
}