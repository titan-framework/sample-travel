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

package com.carromeu.titan.sample.travel.ws;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.carromeu.titan.sample.travel.util.HttpHelper;
import com.carromeu.titan.sample.travel.util.WebServiceHelper;

public class GoogleCloudMessagingWebService
{
	private GoogleCloudMessagingWebService ()
	{}

	public static JSONObject register (String registrationId)
	{
		Map<String, String> params = new HashMap<String, String> ();
		
		params.put ("gcm", registrationId);
		
		return HttpHelper.parseToJson (WebServiceHelper.singleton ().post ("/gcm", params));
	}
}