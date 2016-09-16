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

package com.carromeu.titan.sample.travel;

import java.util.List;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.carromeu.titan.sample.travel.task.RegisterTask;
import com.carromeu.titan.sample.travel.util.CredentialHelper;
import com.carromeu.titan.sample.travel.util.RoboSherlockActivityAbstract;

import com.actionbarsherlock.view.MenuItem;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;

@ContentView (R.layout.register)
public class RegisterActivity extends RoboSherlockFragmentActivity implements RoboSherlockActivityAbstract.DoNothing
{
	@InjectView (android.R.id.list) ListView list;
	
	private CredentialHelper credential;
	
	private String email;

	@Override
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate (savedInstanceState);
		
		if (CredentialHelper.hasValidCredentials () || !((Travel) getApplication ()).getDisambiguation ().equals (""))
			startActivity (new Intent (this, EmbassyActivity.class));
		
		credential = new CredentialHelper (this);
	}
	
	@Override
	public boolean onOptionsItemSelected (MenuItem item)
	{
		switch (item.getItemId ())
		{
			case android.R.id.home:

				super.onBackPressed ();

				return true;
		}

		return super.onOptionsItemSelected (item);
	}

	@Override
	public void onResume ()
	{
		super.onResume ();

		if (CredentialHelper.hasValidCredentials ())
			startActivity (new Intent (this, AlertActivity.class));
		
		list.setAdapter (new GoogleAccountListAdapter (this, credential.getAccountNames ()));

		list.setOnItemClickListener (new OnItemClickListener ()
		{
			@Override
			public void onItemClick (AdapterView<?> adapter, View view, int position, long id)
			{
				String mail = (String) adapter.getItemAtPosition (position);
				
				RegisterActivity.this.email = mail;
				
				Log.e (getClass ().getName (), mail);
				
				(new RegisterTask (RegisterActivity.this, credential, mail)).execute ();
			}
		});
	}

	@Override
	public void onActivityResult (int requestCode, int resultCode, Intent intent)
	{
		if (requestCode == CredentialHelper.AUTH_ERROR)
		{
			if (intent == null)
			{
				Log.e (getClass ().getName (), "Unknown error, click the button again");
				
				return;
			}
			
			if (resultCode == RESULT_OK)
			{
				Log.e (getClass ().getName (), "Retrying");
				
				(new RegisterTask (RegisterActivity.this, credential, email)).execute ();
				
				return;
			}
			
			if (resultCode == RESULT_CANCELED)
			{
				Log.e (getClass ().getName (), "User rejected authorization.");
				
				return;
			}
			
			Log.e (getClass ().getName (), "Unknown error, click the button again");

			return;
		}

		super.onActivityResult (requestCode, resultCode, intent);
	}
	
	public void addGoogleAccount (View view)
	{
		AccountManager.get (this).addAccount ("com.google", "", null, new Bundle (), this, null, null);
	}
	
	public class GoogleAccountListAdapter extends ArrayAdapter<String>
	{
		Context context;
		
		public GoogleAccountListAdapter (Context c, List<String> list)
		{
			super (c, 0, list);
			
			context = c;
		}

		public View getView (int position, View view, ViewGroup parent)
		{
			if (view == null)
				view = LayoutInflater.from (getContext ()).inflate (R.layout.register_row, null);
			
			TextView mail = (TextView) view.findViewById (R.id.register_mail);
			mail.setText (getItem (position));
			
			return view;
		}
	}
}