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

package com.carromeu.titan.sample.travel.contract;

public class EmbassyContract 
{
	public static String TABLE = "embassy";
	
	public static String CODE = "code";
	public static String COUNTRY = "country";
	public static String DESCRIPTION = "description";
	public static String LAST_CHANGE = "last_change";
	
	public static String ddl ()
	{
		String ddl = "CREATE TABLE " + TABLE + " (" +
				CODE + " TEXT PRIMARY KEY, " +
				COUNTRY + " TEXT, " +
				DESCRIPTION + " TEXT, " +
				LAST_CHANGE + " INTEGER" +
			");";
		
		return ddl;
	}
	
	public static String [] columns ()
	{
		return new String [] { CODE, COUNTRY, DESCRIPTION, LAST_CHANGE };
	}
}
