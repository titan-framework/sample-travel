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

package com.carromeu.titan.sample.travel;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.carromeu.titan.sample.travel.dao.EmbassyDAO;
import com.carromeu.titan.sample.travel.model.Embassy;
import com.carromeu.titan.sample.travel.util.RoboSherlockActivityAbstract;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;

@ContentView (R.layout.embassy_view)
public class EmbassyViewActivity extends RoboSherlockFragmentActivity implements RoboSherlockActivityAbstract.DoNothing
{
	@InjectView (R.id.embassy_view_description) TextView description;
	
	public static String CODE = "CODE";

	private Embassy embassy;
	
	@Override
	public void onCreate (Bundle bundle)
	{
		super.onCreate (bundle);
		
		getSupportActionBar ().setHomeButtonEnabled (true);

		getSupportActionBar ().setDisplayHomeAsUpEnabled (true);

		embassy = EmbassyDAO.singleton ().get (getIntent ().getStringExtra (CODE));

		getSupportActionBar ().setTitle (embassy.getCountry ());
		
		description.setText (Html.fromHtml (embassy.getDescription ()));
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
}
