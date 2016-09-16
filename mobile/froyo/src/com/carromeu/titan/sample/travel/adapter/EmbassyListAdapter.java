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

package com.carromeu.titan.sample.travel.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.carromeu.titan.sample.travel.R;
import com.carromeu.titan.sample.travel.model.Embassy;

public class EmbassyListAdapter extends ArrayAdapter<Embassy>
{
	public EmbassyListAdapter (Context context, List<Embassy> list)
	{
		super (context, 0, list);
	}

	public View getView (int position, View view, ViewGroup parent)
	{
		if (view == null)
			view = LayoutInflater.from (getContext ()).inflate (R.layout.embassy_row, null);
		
		TextView title = (TextView) view.findViewById (R.id.embassy_row_title);
		title.setText (getItem (position).getCountry ());

		return view;
	}
}
