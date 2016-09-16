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

package com.carromeu.titan.sample.travel.model;

import java.util.Date;

public class Embassy 
{
	private String code;
	private String country;
	private String description;
	private Date lastChange;
	
	public String getCode ()
	{
		return code;
	}
	
	public void setCode (String code)
	{
		this.code = code;
	}
	
	public String getCountry ()
	{
		return country;
	}
	
	public void setCountry (String country)
	{
		this.country = country;
	}
	
	public String getDescription ()
	{
		return description;
	}
	
	public void setDescription (String description)
	{
		this.description = description;
	}
	
	public Date getLastChange ()
	{
		return lastChange;
	}
	
	public void setLastChange (Date lastChange)
	{
		this.lastChange = lastChange;
	}
}
