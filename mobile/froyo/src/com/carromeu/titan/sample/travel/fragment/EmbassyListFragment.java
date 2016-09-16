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

import roboguice.inject.InjectView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.carromeu.titan.sample.travel.EmbassyViewActivity;
import com.carromeu.titan.sample.travel.R;
import com.carromeu.titan.sample.travel.Travel;
import com.carromeu.titan.sample.travel.adapter.EmbassyListAdapter;
import com.carromeu.titan.sample.travel.dao.EmbassyDAO;
import com.carromeu.titan.sample.travel.model.Embassy;
import com.carromeu.titan.sample.travel.task.EmbassyTask;
import com.carromeu.titan.sample.travel.util.NetWorkVerifyer;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;

public class EmbassyListFragment extends RoboSherlockFragment
{
	@InjectView (android.R.id.list) ListView list;

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
			(new EmbassyTask (this)).execute ();
		else
			Toast.makeText (this.getActivity (), "Não há conexão ativa para baixar dados!", Toast.LENGTH_LONG).show ();
	}

	public void refresh ()
	{
		list.setAdapter (new EmbassyListAdapter (getActivity (), EmbassyDAO.singleton ().list ()));
		
		list.setOnItemClickListener (new OnItemClickListener ()
		{
			@Override
			public void onItemClick (AdapterView<?> adapter, View view, int position, long id)
			{
				Intent i = new Intent (EmbassyListFragment.this.getActivity (), EmbassyViewActivity.class);
				
				i.putExtra (EmbassyViewActivity.CODE, ((Embassy) adapter.getItemAtPosition (position)).getCode ());
				
				startActivity (i);
			}
		});
	}
	
	public void search (String term)
	{
		list.setAdapter (new EmbassyListAdapter (getActivity (), EmbassyDAO.singleton ().search (term)));
		
		list.setOnItemClickListener (new OnItemClickListener ()
		{
			@Override
			public void onItemClick (AdapterView<?> adapter, View view, int position, long id)
			{
				Intent i = new Intent (EmbassyListFragment.this.getActivity (), EmbassyViewActivity.class);
				
				i.putExtra (EmbassyViewActivity.CODE, ((Embassy) adapter.getItemAtPosition (position)).getCode ());
				
				startActivity (i);
			}
		});
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