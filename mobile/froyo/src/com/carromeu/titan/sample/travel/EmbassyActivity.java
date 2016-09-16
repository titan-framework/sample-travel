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

import java.util.ArrayList;

import roboguice.inject.ContentView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.carromeu.titan.sample.travel.fragment.EmbassyListFragment;
import com.carromeu.titan.sample.travel.fragment.MenuListFragment;
import com.carromeu.titan.sample.travel.util.RoboSherlockActivityAbstract;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnCloseListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;

@ContentView (R.layout.main)
public class EmbassyActivity extends RoboSherlockFragmentActivity implements RoboSherlockActivityAbstract.DoNothing
{
	private SlidingMenu menu = null;
	
	private Menu absMenu;
	
	public static final int SEARCH = 1;
	private static final int REFRESH = 2;
	public static final int VOICE = 3;
	
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
		
		getSupportFragmentManager ().beginTransaction ().replace (R.id.base, new EmbassyListFragment ()).commit ();
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
				
				((EmbassyListFragment) getSupportFragmentManager ().findFragmentById (R.id.base)).syncronize ();
				
				return true;
			
			case SEARCH:
				
				EditText search = (EditText) item.getActionView ();

				search.addTextChangedListener (new TextWatcher()
				{	
					@Override
					public void onTextChanged (CharSequence s, int start, int before, int count)
					{
						((EmbassyListFragment) getSupportFragmentManager ().findFragmentById (R.id.base)).search (String.valueOf (s));
					}
					
					@Override
					public void beforeTextChanged (CharSequence s, int start, int count, int after)
					{}
					
					@Override
					public void afterTextChanged (Editable s)
					{}
				});

				search.requestFocus ();
				
				InputMethodManager input = (InputMethodManager) getSystemService (Context.INPUT_METHOD_SERVICE);
				
				input.toggleSoftInput (InputMethodManager.SHOW_FORCED, 0);

				return true;
			
			case VOICE:
				
				Intent intent = new Intent (RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

				intent.putExtra (RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

				intent.putExtra (RecognizerIntent.EXTRA_PROMPT, "Fale em VOZ ALTA o termo a ser buscado...");

				startActivityForResult (intent, VOICE);

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
		absMenu = menu;
		
		absMenu.add (0, SEARCH, 0, "Buscar").setIcon (R.drawable.ic_action_search).setActionView (R.layout.search_bar).setShowAsAction (MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
		absMenu.add (0, VOICE, 0, "Busca por Voz").setIcon (R.drawable.ic_action_mic).setShowAsAction (MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		absMenu.add (0, REFRESH, 0, "Sincronizar").setIcon (R.drawable.ic_action_refresh).setShowAsAction (MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		return super.onCreateOptionsMenu (absMenu);
	}

	public SlidingMenu getMenu ()
	{
		return menu;
	}
	
	public Menu getSherlockMenu ()
	{
		return absMenu;
	}
	
	public void about (View view)
	{
		startActivity (new Intent (this, AboutActivity.class));
	}
	
	@Override
	protected void onActivityResult (int code, int result, Intent data)
	{
		if (result == Activity.RESULT_OK)
			switch (code)
			{
				case VOICE:

					ArrayList<String> matches = data.getStringArrayListExtra (RecognizerIntent.EXTRA_RESULTS);
					
					if (matches != null && matches.size () > 0)
					{
						((EmbassyListFragment) getSupportFragmentManager ().findFragmentById (R.id.base)).search (matches.get (0));
						
						MenuItem item = absMenu.findItem (SEARCH);
						
						EditText search = (EditText) item.getActionView ();
						
						search.setText (matches.get (0));

						if (!item.isActionViewExpanded ())
							item.expandActionView ();
						
						search.addTextChangedListener (new TextWatcher ()
						{
							@Override
							public void onTextChanged (CharSequence s, int start, int before, int count)
							{
								((EmbassyListFragment) getSupportFragmentManager ().findFragmentById (R.id.base)).search (String.valueOf (s));
							}

							@Override
							public void beforeTextChanged (CharSequence s, int start, int count, int after)
							{}

							@Override
							public void afterTextChanged (Editable s)
							{}
						});

						search.requestFocus ();

						((InputMethodManager) getSystemService (Context.INPUT_METHOD_SERVICE)).toggleSoftInput (InputMethodManager.SHOW_FORCED, 0);
					}

					break;
			}
	}
}