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

import roboguice.inject.ContentView;
import android.os.Bundle;
import com.carromeu.titan.sample.travel.util.RoboSherlockActivityAbstract;

import com.actionbarsherlock.view.MenuItem;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;

@ContentView (R.layout.about)
public class AboutActivity extends RoboSherlockFragmentActivity implements RoboSherlockActivityAbstract.DoNothing
{
	@Override
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate (savedInstanceState);

		getSupportActionBar ().setHomeButtonEnabled (true);

		getSupportActionBar ().setDisplayHomeAsUpEnabled (true);
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
	}
}