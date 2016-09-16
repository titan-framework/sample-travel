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

package com.carromeu.titan.sample.travel.model;

import com.carromeu.titan.sample.travel.R;

public class Alert
{
	private Long id;
	private String message;
	private String icon;
	private Boolean read;
	
	public Alert ()
	{
		super ();
	}
	
	public Alert (Long id, String message, String icon, Boolean read)
	{
		this.id = id;
		this.message = message;
		this.icon = icon;
		this.read = read;
	}
	
	public Long getId ()
	{
		return id;
	}

	public void setId (Long id)
	{
		this.id = id;
	}

	public String getMessage ()
	{
		return message;
	}

	public void setMessage (String message)
	{
		this.message = message;
	}

	public String getIcon ()
	{
		return icon;
	}

	public void setIcon (String icon)
	{
		this.icon = icon;
	}

	public Boolean getRead ()
	{
		return read;
	}
	
	public void setRead (Boolean read)
	{
		this.read = read;
	}
	
	public Boolean isRead ()
	{
		return read;
	}
	
	public int getIconAsResource ()
	{
		switch (Icon.valueOf (icon))
		{
			case SECURITY:
				return R.drawable.alert_security;
				
			case CONFIRM:
				return R.drawable.alert_confirm;
				
			case WARNING:
				return R.drawable.alert_warning;
			
			case INFO:
				return R.drawable.alert_info;
		}
		
		return R.drawable.alert_info;
	}
	
	public enum Icon { SECURITY, INFO, CONFIRM, WARNING }
}