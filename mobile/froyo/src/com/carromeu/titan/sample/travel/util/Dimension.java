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

public class Dimension
{
	private Integer width;
	private Integer height;
	private Float density;

	public Integer getWidth ()
	{
		return width;
	}

	public Integer getHeight ()
	{
		return height;
	}

	public Integer getWidthSp ()
	{
		return (int) (width / density);
	}

	public Integer getHeightSp ()
	{
		return (int) (height / density);
	}

	public Float getDensity ()
	{
		return density;
	}

	public Dimension (Integer width, Integer height, Float density)
	{
		super ();
		
		this.width = width;
		this.height = height;
		this.density = density;
	}
}