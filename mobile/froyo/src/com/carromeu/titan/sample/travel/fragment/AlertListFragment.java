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

package com.carromeu.titan.sample.travel.fragment;

import java.util.List;

import roboguice.inject.InjectView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.carromeu.titan.sample.travel.Travel;
import com.carromeu.titan.sample.travel.R;
import com.carromeu.titan.sample.travel.adapter.AlertListAdapter;
import com.carromeu.titan.sample.travel.dao.AlertDAO;
import com.carromeu.titan.sample.travel.model.Alert;
import com.carromeu.titan.sample.travel.task.AlertCheckTask;
import com.carromeu.titan.sample.travel.task.AlertTask;
import com.carromeu.titan.sample.travel.util.NetWorkVerifyer;

import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;

public class AlertListFragment extends RoboSherlockFragment
{
	@InjectView (android.R.id.list) ListView alerts;

	private Boolean hasConnection = true;

	private Boolean hasSync = false;

	@Override
	public void onCreate (Bundle bundle)
	{
		super.onCreate (bundle);

		hasSync = ((Travel) getActivity ().getApplication ()).alreadySync (getClass ().getName ());

		if (!hasSync)
			syncronize ();
	}

	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate (R.layout.list, null);
	}

	public void onActivityCreated (Bundle bundle)
	{
		super.onActivityCreated (bundle);

		if (!hasConnection || hasSync)
			refresh ();
	}

	public void syncronize ()
	{
		hasConnection = NetWorkVerifyer.hadNetworkConnection (getActivity ());

		if (hasConnection)
			(new AlertTask (this)).execute ();
		else
			Toast.makeText (this.getActivity (), "Não há conexão ativa para baixar dados!", Toast.LENGTH_LONG).show ();
	}

	public void refresh ()
	{
		List<Alert> l = AlertDAO.singleton ().list ();
		
		if (l.size () == 0)
		{
			l.add (new Alert (Long.valueOf (0), "Nenhum alerta!", "CONFIRM", false));
			
			alerts.setAdapter (new AlertListAdapter (getActivity (), l));
			
			alerts.setSelector (android.R.color.transparent);
			alerts.setDividerHeight (0);
			alerts.setDivider (null);
		}
		else
		{
			alerts.setAdapter (new AlertListAdapter (getActivity (), l));
	
			alerts.setOnItemClickListener (new OnItemClickListener ()
			{
				@Override
				public void onItemClick (AdapterView<?> adapter, View view, int position, long id)
				{
					hasConnection = NetWorkVerifyer.hadNetworkConnection (getActivity ());
					
					final Alert item = (Alert) adapter.getItemAtPosition (position);
					
					item.setRead (true);
					
					((TextView) view.findViewById (R.id.alert_text)).setTypeface (null, Typeface.NORMAL);
					
					new AlertDialog.Builder (getActivity ()).setTitle ("Notificação").setMessage (item.getMessage ()).setIcon (item.getIconAsResource ()).setCancelable (false).setNegativeButton ("Apagar", new OnClickListener ()
					{
						@Override
						public void onClick (DialogInterface dialog, int which)
						{
							if (hasConnection)
							{
								(new AlertCheckTask (AlertListFragment.this, item, AlertCheckTask.DELETE)).execute ();
								
								AlertDAO.singleton ().delete (item.getId ());
								
								refresh ();
								
								Toast.makeText (getActivity (), "Notificação apagada!", Toast.LENGTH_LONG).show ();
							}
							else
								Toast.makeText (getActivity (), "Não há conexão ativa para atualizar o status desta notificação no Travel!", Toast.LENGTH_LONG).show ();
						}
					}).setPositiveButton ("Fechar", new OnClickListener ()
					{
						@Override
						public void onClick (DialogInterface dialog, int which)
						{
							if (hasConnection)
								(new AlertCheckTask (AlertListFragment.this, item, AlertCheckTask.READ)).execute ();
							else
								Toast.makeText (getActivity (), "Não há conexão ativa para atualizar o status desta notificação no Travel!", Toast.LENGTH_LONG).show ();
						}
					}).show ();
				}
			});
		}
	}
	
	public void fail (String error)
	{
		final View view = getActivity ().getLayoutInflater ().inflate (R.layout.generic_error, null);

		final TextView message = (TextView) view.findViewById (R.id.error_message);

		message.setText (error);

		AlertDialog.Builder builder = new AlertDialog.Builder (getActivity ());

		builder.setTitle ("Erro!");

		builder.setView (view);

		builder.setCancelable (false);

		builder.setNegativeButton ("Ok", new OnClickListener ()
		{
			@Override
			public void onClick (DialogInterface dialog, int which)
			{
				dialog.dismiss ();
			}
		});

		builder.show ();

		return;
	}
}