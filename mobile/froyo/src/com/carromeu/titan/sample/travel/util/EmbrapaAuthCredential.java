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

import java.math.BigInteger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.carromeu.titan.sample.travel.exception.TechnicalException;

public class EmbrapaAuthCredential
{
	private Long timestamp;
	private String publicKey;
	private String privateKey;

	public EmbrapaAuthCredential (Long timestamp, String publicKey, String privateKey)
	{
		super ();
		
		this.timestamp = timestamp;
		this.publicKey = publicKey;
		this.privateKey = privateKey;
	}

	public String signature ()
	{
		return hmacSha1 (String.valueOf (timestamp) + publicKey, privateKey);
	}

	private String hmacSha1 (String data, String key)
	{
		try
		{
			SecretKeySpec signingKey = new SecretKeySpec (key.getBytes (), "HmacSHA1");
			
			Mac mac = Mac.getInstance ("HmacSHA1");
			
			mac.init (signingKey);
			
			byte [] rawHmac = mac.doFinal (data.getBytes ());
			
			return String.format ("%1$040x", new BigInteger (1, rawHmac));
		}
		catch (Exception e)
		{
			throw new TechnicalException ("Erro na computação da assinatura!", e);
		}
	}

	public String publicKey ()
	{
		return publicKey;
	}

	public void updateTimestamp (Long newTimestamp)
	{
		timestamp = newTimestamp;
	}
}