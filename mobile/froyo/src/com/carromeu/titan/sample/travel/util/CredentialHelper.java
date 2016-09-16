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

package com.carromeu.titan.sample.travel.util;

import java.util.Arrays;
import java.util.List;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.carromeu.titan.sample.travel.EmbassyActivity;
import com.carromeu.titan.sample.travel.Travel;
import com.carromeu.titan.sample.travel.task.AuthTask;
import com.carromeu.titan.sample.travel.task.DisambiguationTask;
import com.carromeu.titan.sample.travel.task.GoogleCloudMessagingTask;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class CredentialHelper
{
	public static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/plus.login";

	public static final int AUTH_ERROR = 1001;
	public static final int PLAY_SERVICES_ERROR = 1002;
	
	private Activity activity;
	
	private AccountManager manager;

	private String tempClientId;
	private String tempClientPrivateKey;

	private Long userId;
	private String userName;
	private String userMail;
	private String userType;
	
	public CredentialHelper (Activity a)
	{
		activity = a;
	}

	public void getUserByClientCredential (String id, String pk)
	{
		tempClientId = id;
		tempClientPrivateKey = pk;

		new AuthTask (activity, this, id, pk).execute ();
	}

	public void successClientCredential ()
	{
		new AlertDialog.Builder (activity).setTitle ("Atenção!").setMessage ("Este dispositivo será habilitado para acessar dados restritos de " + userName + " (" + userMail + ")! Você confirma ser esta pessoa?").setCancelable (false).setPositiveButton ("Sim", new OnClickListener ()
		{
			@Override
			public void onClick (DialogInterface dialog, int which)
			{
				setCredential (tempClientId, tempClientPrivateKey);
			}
		}).setNegativeButton ("Não", null).show ();

		return;
	}

	public void failClientCredential (String error)
	{
		new AlertDialog.Builder (activity).setTitle ("Erro!").setMessage (error).setCancelable (false).setNegativeButton ("Ok", null).show ();

		return;
	}

	private void setCredential (String givenClientId, String givenPrivateKey)
	{
		if (givenClientId == null || givenClientId.trim ().length () == 0)
		{
			new AlertDialog.Builder (activity).setTitle ("Erro!").setMessage ("Cliente ID não pode ser vazio!").setIcon (android.R.drawable.ic_dialog_alert).setCancelable (false).setNegativeButton ("Ok", null).show ();

			return;
		}

		if (!givenClientId.matches ("^[0-9]+$"))
		{
			new AlertDialog.Builder (activity).setTitle ("Erro!").setMessage ("Client ID inválido!").setIcon (android.R.drawable.ic_dialog_alert).setCancelable (false).setNegativeButton ("Ok", null).show ();

			return;
		}

		if (givenPrivateKey == null || givenPrivateKey.trim ().length () == 0)
		{
			new AlertDialog.Builder (activity).setTitle ("Erro!").setMessage ("Chave privada não pode ser vazia!").setIcon (android.R.drawable.ic_dialog_alert).setCancelable (false).setNegativeButton ("Ok", null).show ();

			return;
		}

		if (!givenPrivateKey.matches ("^[A-Z0-9]+$"))
		{
			new AlertDialog.Builder (activity).setTitle ("Erro!").setMessage ("Chave privada inválida!").setIcon (android.R.drawable.ic_dialog_alert).setCancelable (false).setNegativeButton ("Ok", null).show ();

			return;
		}
		
		Preferences.clear ();
		
		SharedPreferences.Editor preferenceEditor = Preferences.singleton ().edit ();

		preferenceEditor.putString ("client-id", givenClientId);
		preferenceEditor.putString ("private-key", givenPrivateKey);

		preferenceEditor.putLong ("user-id", userId);
		preferenceEditor.putString ("user-name", userName);
		preferenceEditor.putString ("user-mail", userMail);
		preferenceEditor.putString ("user-type", userType);

		preferenceEditor.commit ();
		
		((Travel) activity.getApplication ()).clearSync ();

		Toast.makeText (activity, "Código de acesso obtido com sucesso!", Toast.LENGTH_LONG).show ();
		
		if (checkPlayServices (activity))
			new GoogleCloudMessagingTask (activity).execute ();
		
		if (((Travel) activity.getApplication ()).getDisambiguation ().equals (""))
			new DisambiguationTask (activity).execute ();
		
		activity.startActivity (new Intent (activity, EmbassyActivity.class));
	}

	public static boolean hasValidCredentials ()
	{
		String id = Preferences.singleton ().getString ("client-id", "").trim ();

		String pk = Preferences.singleton ().getString ("private-key", "").trim ();
		
		return !(id.equals ("") || pk.equals (""));
	}
	
	public static boolean isRegisteredAtGoogleCloudMessaging (Travel app)
	{
		String id = Preferences.singleton ().getString ("gcm-id", "").trim ();
		int version = Preferences.singleton ().getInt ("gcm-version", 0);
		
		return !id.equals ("") || version != app.getVersion ();
	}
	
	public static boolean checkPlayServices (Activity activity)
	{
		int code = GooglePlayServicesUtil.isGooglePlayServicesAvailable (activity);
		
		if (code != ConnectionResult.SUCCESS)
		{
			if (GooglePlayServicesUtil.isUserRecoverableError (code))
				GooglePlayServicesUtil.getErrorDialog (code, activity, 9000).show ();
			else
				Toast.makeText (activity, "Este dispositivo não é plenamente suportado pelo Embaixadas Brasileiras! Desta forma, você não receberá alertas do sistema quando o aplicativo não estiver aberto.", Toast.LENGTH_LONG).show ();
			
			return false;
		}
		
		return true;
	}
	
	public void showGoogleDialog (final int code)
	{
		activity.runOnUiThread (new Runnable ()
		{
			@Override
			public void run ()
			{
				Dialog d = GooglePlayServicesUtil.getErrorDialog (code, activity, PLAY_SERVICES_ERROR);

				d.show ();
			}
		});
	}

	public List<String> getAccountNames ()
	{
		manager = AccountManager.get (activity);

		Account [] accounts = manager.getAccountsByType (GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);

		String [] names = new String [accounts.length];

		for (int i = 0; i < names.length; i++)
			names [i] = accounts [i].name;

		return Arrays.asList (names);
	}

	public void setUserId (Long userId)
	{
		this.userId = userId;
	}

	public void setUserName (String userName)
	{
		this.userName = userName;
	}

	public void setUserMail (String userMail)
	{
		this.userMail = userMail;
	}

	public void setUserType (String userType)
	{
		this.userType = userType;
	}
}