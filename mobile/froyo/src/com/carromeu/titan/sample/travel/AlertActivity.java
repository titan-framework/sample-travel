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
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.carromeu.titan.sample.travel.fragment.AlertListFragment;
import com.carromeu.titan.sample.travel.fragment.MenuListFragment;
import com.carromeu.titan.sample.travel.task.GoogleCloudMessagingTask;
import com.carromeu.titan.sample.travel.util.CredentialHelper;
import com.carromeu.titan.sample.travel.util.NetWorkVerifyer;
import com.carromeu.titan.sample.travel.util.RoboSherlockActivityAbstract;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnCloseListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;

@ContentView (R.layout.main)
public class AlertActivity extends RoboSherlockFragmentActivity implements RoboSherlockActivityAbstract.DoNothing
{
	private SlidingMenu menu = null;
	
	private static final int REFRESH = 1;
	
	@Override
	public void onCreate (Bundle bundle)
	{
		super.onCreate (bundle);
		
		menu = new SlidingMenu (this);
		menu.setMode (SlidingMenu.LEFT);
		menu.setTouchModeAbove (SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidth (20);
		menu.setShadowDrawable (R.drawable.shadow);
		menu.setBehindOffset (150);
		menu.setFadeDegree (0.50f);
		menu.setHorizontalFadingEdgeEnabled (true);
		menu.setFadeEnabled (true);
		menu.setFadingEdgeLength (30);
		menu.attachToActivity (this, SlidingMenu.SLIDING_WINDOW);
		menu.setMenu (R.layout.menu);

		getSupportFragmentManager ().beginTransaction ().replace (R.id.menu_base, new MenuListFragment ()).commit ();

		menu.setOnOpenListener (new OnOpenListener ()
		{
			@Override
			public void onOpen ()
			{
				((Vibrator) getSystemService (Context.VIBRATOR_SERVICE)).vibrate (15);

				ImageView logo = (ImageView) findViewById (android.R.id.home);

				if (logo != null)
					logo.setImageResource (R.drawable.indicator_opened);
			}
		});

		menu.setOnCloseListener (new OnCloseListener ()
		{
			@Override
			public void onClose ()
			{
				((Vibrator) getSystemService (Context.VIBRATOR_SERVICE)).vibrate (15);

				ImageView logo = (ImageView) findViewById (android.R.id.home);

				if (logo != null)
					logo.setImageResource (R.drawable.indicator_closed);
			}
		});

		getSupportActionBar ().setHomeButtonEnabled (true);
		
		getSupportFragmentManager ().beginTransaction ().replace (R.id.base, new AlertListFragment ()).commit ();
		
		if (NetWorkVerifyer.hadNetworkConnection (this) && !CredentialHelper.isRegisteredAtGoogleCloudMessaging ((Travel) getApplication ()) && CredentialHelper.checkPlayServices (this))
			new GoogleCloudMessagingTask (this).execute ();
	}

	@Override
	public boolean onOptionsItemSelected (MenuItem item)
	{
		switch (item.getItemId ())
		{
			case android.R.id.home:

				menu.toggle ();

				return true;

			case REFRESH:

				if (menu.isMenuShowing ())
					menu.toggle ();
				
				((AlertListFragment) getSupportFragmentManager ().findFragmentById (R.id.base)).syncronize ();
				
				return true;
		}

		return super.onOptionsItemSelected (item);
	}

	@Override
	public void onBackPressed ()
	{
		if (menu.isMenuShowing ())
			menu.toggle ();
		else
			((MenuListFragment) getSupportFragmentManager ().findFragmentById (R.id.menu_base)).exitDialog ();
	}
	
	@Override
	public boolean onKeyUp (int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_MENU)
			menu.toggle ();
		
		return super.onKeyUp (keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu (Menu menu)
	{
		menu.add (0, REFRESH, 0, "Sincronizar").setIcon (R.drawable.ic_action_refresh).setShowAsAction (MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		return super.onCreateOptionsMenu (menu);
	}

	public SlidingMenu getMenu ()
	{
		return menu;
	}
	
	public void about (View view)
	{
		startActivity (new Intent (this, AboutActivity.class));
	}
}