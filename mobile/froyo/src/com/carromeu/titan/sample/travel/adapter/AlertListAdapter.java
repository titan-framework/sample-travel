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

package com.carromeu.titan.sample.travel.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.carromeu.titan.sample.travel.R;
import com.carromeu.titan.sample.travel.model.Alert;

public class AlertListAdapter extends ArrayAdapter<Alert>
{
	public AlertListAdapter (Context context, List<Alert> list)
	{
		super (context, 0, list);
	}

	public View getView (int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
			convertView = LayoutInflater.from (getContext ()).inflate (R.layout.alert_row, null);

		ImageView icon = (ImageView) convertView.findViewById (R.id.alert_icon);
		icon.setImageResource (getItem (position).getIconAsResource ());

		TextView title = (TextView) convertView.findViewById (R.id.alert_text);
		title.setText (getItem (position).getMessage ());

		if (!getItem (position).isRead ())
			title.setTypeface (null, Typeface.BOLD);
		else
			title.setTypeface (null, Typeface.NORMAL);

		return convertView;
	}
}