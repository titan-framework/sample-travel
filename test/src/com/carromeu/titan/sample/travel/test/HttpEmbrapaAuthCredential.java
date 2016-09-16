/**
 * Copyright © 2014 Embrapa. All Rights Reserved.
 *
 * Developed by Laboratory for Precision Livestock, Environment and Software Engineering (PLEASE Lab)
 * of Embrapa Beef Cattle at Campo Grande - MS - Brazil.
 * 
 * @see http://please.cnpgc.embrapa.br
 * 
 * @author Jairo Ricardes Rodrigues Filho <jairocgr@gmail.com>
 * @author Camilo Carromeu <camilo.carromeu@embrapa.br>
 * 
 * @version 14.05-1-alpha
 */

package com.carromeu.titan.sample.travel.test;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class HttpEmbrapaAuthCredential
{
	private Integer timestamp;
	private String publicKey;
	private String privateKey;

	public HttpEmbrapaAuthCredential (Integer timestamp, String publicKey, String privateKey)
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
			throw new RuntimeException ("Erro na computação da assinatura", e);
		}
	}

	public static String md5 (String input)
	{
		MessageDigest md;

		try
		{
			md = MessageDigest.getInstance ("MD5");

			md.update (input.getBytes ());

			byte [] b = md.digest ();

			String output = "";

			for (int i = 0; i < b.length; i++)
				output += Integer.toString ( (b [i] & 0xff) + 0x100, 16).substring (1);

			return output;
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace ();
		}

		throw new RuntimeException ();
	}

	public String encrypt (String input)
	{
		String key = HttpEmbrapaAuthCredential.md5 (privateKey + String.valueOf (timestamp));

		return HttpEmbrapaAuthCredential.encrypt (input, key);
	}

	public static String encrypt (String input, String secret)
	{
		secret = secret.substring (0, 16);

		Charset encode = Charset.forName ("ISO-8859-1");

		SecretKeySpec key = new SecretKeySpec (secret.getBytes (encode), "Blowfish");

		try
		{
			Cipher cipher = Cipher.getInstance ("Blowfish/ECB/PKCS5Padding");

			cipher.init (Cipher.ENCRYPT_MODE, key);

			byte [] encrypted = cipher.doFinal (input.getBytes (encode));

			return Base64.encodeToString (encrypted, Base64.DEFAULT);
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace ();
		}
		catch (NoSuchPaddingException e)
		{
			e.printStackTrace ();
		}
		catch (InvalidKeyException e)
		{
			e.printStackTrace ();
		}
		catch (IllegalBlockSizeException e)
		{
			e.printStackTrace ();
		}
		catch (BadPaddingException e)
		{
			e.printStackTrace ();
		}

		return null;
	}

	public static String decrypt (String input, String secret)
	{
		secret = secret.substring (0, 16);

		byte [] data = Base64.decode (input, Base64.DEFAULT);

		Charset encode = Charset.forName ("ISO-8859-1");

		SecretKeySpec key = new SecretKeySpec (secret.getBytes (encode), "Blowfish");

		try
		{
			Cipher cipher = Cipher.getInstance ("Blowfish/ECB/PKCS5Padding");

			cipher.init (Cipher.DECRYPT_MODE, key);

			byte [] encrypted = cipher.doFinal (data);

			return new String (encrypted, encode);
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace ();
		}
		catch (NoSuchPaddingException e)
		{
			e.printStackTrace ();
		}
		catch (InvalidKeyException e)
		{
			e.printStackTrace ();
		}
		catch (IllegalBlockSizeException e)
		{
			e.printStackTrace ();
		}
		catch (BadPaddingException e)
		{
			e.printStackTrace ();
		}

		return null;
	}
}