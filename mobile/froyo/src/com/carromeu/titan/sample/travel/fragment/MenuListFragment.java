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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.carromeu.titan.sample.travel.AlertActivity;
import com.carromeu.titan.sample.travel.EmbassyActivity;
import com.carromeu.titan.sample.travel.R;
import com.carromeu.titan.sample.travel.Travel;
import com.carromeu.titan.sample.travel.task.LoadUrlImageTask;
import com.carromeu.titan.sample.travel.util.Preferences;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockListFragment;

public class MenuListFragment extends RoboSherlockListFragment
{
	@InjectView (R.id.menu_user_photo) ImageView userPhoto;
	@InjectView (R.id.menu_user_name) TextView userName;
	@InjectView (R.id.menu_user_mail) TextView userMail;

	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate (R.layout.menu_list, null);
	}

	public void onActivityCreated (Bundle bundle)
	{
		super.onActivityCreated (bundle);

		SharedPreferences preferences = Preferences.singleton ();

		Long auxUserId = preferences.getLong ("user-id", 0);
		String auxUserName = preferences.getString ("user-name", "");
		String auxUserMail = preferences.getString ("user-mail", "");

		if (auxUserId > 0)
			(new LoadUrlImageTask (userPhoto, Travel.URL + "picture/" + auxUserId + "_0x100_0.jpg")).execute ();

		userName.setText (auxUserName);
		userMail.setText (auxUserMail);

		MenuAdapter adapter = new MenuAdapter (getActivity ());

		adapter.add (new MenuItem ("Embaixadas", R.drawable.menu_home));
		adapter.add (new MenuItem ("Notificações", R.drawable.menu_alerts));
		adapter.add (new MenuItem ("Sair", R.drawable.menu_exit));

		setListAdapter (adapter);
	}

	private class MenuItem
	{
		public String text;
		public int icon;

		public MenuItem (String text, int icon)
		{
			this.text = text;
			this.icon = icon;
		}
	}

	public class MenuAdapter extends ArrayAdapter<MenuItem>
	{
		public MenuAdapter (Context context)
		{
			super (context, 0);
		}

		public View getView (int position, View convertView, ViewGroup parent)
		{
			if (convertView == null)
				convertView = LayoutInflater.from (getContext ()).inflate (R.layout.menu_row, null);

			ImageView icon = (ImageView) convertView.findViewById (R.id.menu_icon);
			icon.setImageResource (getItem (position).icon);

			TextView title = (TextView) convertView.findViewById (R.id.menu_text);
			title.setText (getItem (position).text);

			return convertView;
		}
	}

	@Override
	public void onListItemClick (ListView list, View view, int position, long id)
	{
		switch (position)
		{
			case 0:
				
				if (!getActivity ().getClass ().equals (EmbassyActivity.class))
					getActivity ().startActivity (new Intent (getActivity (), EmbassyActivity.class));
				else
					((EmbassyActivity) getActivity ()).getMenu ().toggle ();
				
				break;
				
			case 1:
				
				if (!getActivity ().getClass ().equals (AlertActivity.class))
					getActivity ().startActivity (new Intent (getActivity (), AlertActivity.class));
				else
					((AlertActivity) getActivity ()).getMenu ().toggle ();

				break;
			
			case 2:

				exit ();

				break;
		}
	}
	
	public void exitDialog ()
	{
		new AlertDialog.Builder (getActivity ()).setTitle ("Deseja sair?").setMessage ("Clique em SAIR para encerrar o aplicativo.").setIcon (android.R.drawable.ic_dialog_alert).setPositiveButton ("Sair", new DialogInterface.OnClickListener ()
		{
			public void onClick (DialogInterface dialog, int whichButton)
			{
				exit ();
			}
		}).setNegativeButton ("Cancelar", null).show ();
	}
	
	public void exit ()
	{
		Intent intent = new Intent (Intent.ACTION_MAIN);

		intent.addCategory (Intent.CATEGORY_HOME);

		intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK);

		startActivity (intent);
	}
}