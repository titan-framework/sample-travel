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

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jayway.restassured.filter.log.ErrorLoggingFilter;
import com.jayway.restassured.filter.log.RequestLoggingFilter;
import com.jayway.restassured.filter.log.ResponseLoggingFilter;

public class WebServiceTest
{
	private static String API_URI = "http://localhost:8090/api";

	private static String APPLICATION_ID = "mobile";
	private static String APPLICATION_TOKEN = "xcN48zoAJ1a06I4403p0NS81inVWD8zg2B6OytXoik7ydSPu6bRztTQ3k957a2IQ";

	private static String CLIENT_ID = "1";
	private static String CLIENT_PRIVATE_KEY = "2GJ63XYPGHHP3GMQ";
	
	private Integer timestamp;

	private HttpEmbrapaAuthCredential clientCredential;
	private HttpEmbrapaAuthCredential applicationCredential;

	@Before
	public void setUp ()
	{
		timestamp = (int) (new Date ().getTime () / 1000) - 60;
		
		clientCredential = new HttpEmbrapaAuthCredential (timestamp, CLIENT_ID, CLIENT_PRIVATE_KEY);
		
		applicationCredential = new HttpEmbrapaAuthCredential (timestamp, APPLICATION_ID, APPLICATION_TOKEN);
	}

	@Test
	public void tAuthCredentials () throws UnsupportedEncodingException
	{
		given ().filter (new RequestLoggingFilter ()).filter (new ResponseLoggingFilter ()).filter (new ErrorLoggingFilter ())

		.header ("x-embrapa-auth-timestamp", String.valueOf (timestamp)).and ()

		.header ("x-embrapa-auth-client-id", CLIENT_ID).and ().header ("x-embrapa-auth-client-signature", clientCredential.signature ())
		
		.header ("x-embrapa-auth-application-id", APPLICATION_ID).and ().header ("x-embrapa-auth-application-signature", applicationCredential.signature ())

		.expect ().statusCode (200).content (equalTo ("")).when ().get (API_URI + "/auth");
	}

	@Test
	public void tDisambiguation () throws UnsupportedEncodingException
	{
		given ().filter (new RequestLoggingFilter ()).filter (new ResponseLoggingFilter ()).filter (new ErrorLoggingFilter ())

		.header ("x-embrapa-auth-timestamp", String.valueOf (timestamp)).and ()

		.header ("x-embrapa-auth-client-id", CLIENT_ID).and ().header ("x-embrapa-auth-client-signature", clientCredential.signature ())

		.header ("x-embrapa-auth-application-id", APPLICATION_ID).and ().header ("x-embrapa-auth-application-signature", applicationCredential.signature ())

		.expect ().statusCode (200).content (equalTo ("")).when ().get (API_URI + "/disambiguation");
	}

	@Test
	public void tGetListOfAlerts () throws UnsupportedEncodingException
	{
		given ().filter (new RequestLoggingFilter ()).filter (new ResponseLoggingFilter ()).filter (new ErrorLoggingFilter ())

		.header ("x-embrapa-auth-timestamp", String.valueOf (timestamp)).and ()

		.header ("x-embrapa-auth-client-id", CLIENT_ID).and ().header ("x-embrapa-auth-client-signature", clientCredential.signature ())
		
		.header ("x-embrapa-auth-application-id", APPLICATION_ID).and ().header ("x-embrapa-auth-application-signature", applicationCredential.signature ())

		.expect ().statusCode (200).content (equalTo ("")).when ().get (API_URI + "/alerts");
	}

	@Test
	public void tDeleteAlert () throws UnsupportedEncodingException
	{
		given ().filter (new RequestLoggingFilter ()).filter (new ResponseLoggingFilter ()).filter (new ErrorLoggingFilter ())

		.header ("x-embrapa-auth-timestamp", String.valueOf (timestamp)).and ()

		.header ("x-embrapa-auth-client-id", CLIENT_ID).and ().header ("x-embrapa-auth-client-signature", clientCredential.signature ())
		
		.header ("x-embrapa-auth-application-id", APPLICATION_ID).and ().header ("x-embrapa-auth-application-signature", applicationCredential.signature ())

		.expect ().statusCode (200).content (equalTo ("")).when ().delete (API_URI + "/alert/10");
	}

	@Test
	public void tMarkReadAlert () throws UnsupportedEncodingException
	{
		given ().filter (new RequestLoggingFilter ()).filter (new ResponseLoggingFilter ()).filter (new ErrorLoggingFilter ())

		.header ("x-embrapa-auth-timestamp", String.valueOf (timestamp)).and ()

		.header ("x-embrapa-auth-client-id", CLIENT_ID).and ().header ("x-embrapa-auth-client-signature", clientCredential.signature ())
		
		.header ("x-embrapa-auth-application-id", APPLICATION_ID).and ().header ("x-embrapa-auth-application-signature", applicationCredential.signature ())

		.expect ().statusCode (200).content (equalTo ("")).when ().put (API_URI + "/alert/1");
	}

	@Test
	public void tRegisterUserByGoogleAccount () throws UnsupportedEncodingException
	{
		given ().filter (new RequestLoggingFilter ()).filter (new ResponseLoggingFilter ()).filter (new ErrorLoggingFilter ())

		.header ("x-embrapa-auth-timestamp", String.valueOf (timestamp)).and ()

		.header ("x-embrapa-auth-application-id", APPLICATION_ID).and ().header ("x-embrapa-auth-application-signature", applicationCredential.signature ())

		.param ("device", "Samsung Galaxy Note 3")

		.param ("email", "camilo@carromeu.com")

		.param ("token", applicationCredential.encrypt ("1/nTEtiiRmyVz1rD_wICnNGdhu7ds70vROGt27Nzo3Srw"))

		.expect ().statusCode (200).content (equalTo ("")).when ().post (API_URI + "/register");
	}

	@Test
	public void tRegisterDeviceToGoogleCloudMessage ()
	{
		given ().filter (new RequestLoggingFilter ()).filter (new ResponseLoggingFilter ()).filter (new ErrorLoggingFilter ())

		.header ("x-embrapa-auth-timestamp", String.valueOf (timestamp)).and ()

		.header ("x-embrapa-auth-application-id", APPLICATION_ID).and ().header ("x-embrapa-auth-application-signature", applicationCredential.signature ())
		
		.header ("x-embrapa-auth-client-id", CLIENT_ID).and ().header ("x-embrapa-auth-client-signature", clientCredential.signature ())
		
		.param ("gcm", "abcdeFGHIJ123456-987aq")

		.expect ().statusCode (200)

		.when ().post (API_URI + "/gcm");
	}

	@Test
	public void tGetEmbassy () throws UnsupportedEncodingException
	{
		given ().filter (new RequestLoggingFilter ()).filter (new ResponseLoggingFilter ()).filter (new ErrorLoggingFilter ())

		.header ("x-embrapa-auth-timestamp", String.valueOf (timestamp)).and ()

		.header ("x-embrapa-auth-application-id", APPLICATION_ID).and ().header ("x-embrapa-auth-application-signature", applicationCredential.signature ())

		.header ("x-embrapa-auth-client-id", CLIENT_ID).and ().header ("x-embrapa-auth-client-signature", clientCredential.signature ())
		
		.expect ().statusCode (200).content (equalTo ("")).when ().get (API_URI + "/embassy/1");
	}

	@Test
	public void tGetListOfEmbassies () throws UnsupportedEncodingException
	{
		given ().filter (new RequestLoggingFilter ()).filter (new ResponseLoggingFilter ()).filter (new ErrorLoggingFilter ())

		.header ("x-embrapa-auth-timestamp", String.valueOf (timestamp)).and ()

		.header ("x-embrapa-auth-application-id", APPLICATION_ID).and ().header ("x-embrapa-auth-application-signature", applicationCredential.signature ())

		.header ("x-embrapa-auth-client-id", CLIENT_ID).and ().header ("x-embrapa-auth-client-signature", clientCredential.signature ())
		
		.expect ().statusCode (200).content (equalTo ("")).when ().get (API_URI + "/embassy/list/1400262573");
	}

	@Test
	public void tGetActiveEmbassies ()
	{
		given ().filter (new RequestLoggingFilter ()).filter (new ResponseLoggingFilter ()).filter (new ErrorLoggingFilter ())

		.header ("x-embrapa-auth-timestamp", String.valueOf (timestamp)).and ()

		.header ("x-embrapa-auth-application-id", APPLICATION_ID).and ().header ("x-embrapa-auth-application-signature", applicationCredential.signature ())

		.header ("x-embrapa-auth-client-id", CLIENT_ID).and ().header ("x-embrapa-auth-client-signature", clientCredential.signature ())
		
		.expect ().statusCode (200).content (equalTo ("")).when ().get (API_URI + "/embassy/active");
	}

	public String sha1 (String input)
	{
		try
		{
			return DigestUtils.sha1Hex (input.getBytes ("UTF-8"));

		}
		catch (Exception e)
		{
			throw new RuntimeException (e);
		}
	}

	@After
	public void tearDown ()
	{}
}