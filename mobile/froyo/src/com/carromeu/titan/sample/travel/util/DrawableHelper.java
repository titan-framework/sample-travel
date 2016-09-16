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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.webkit.MimeTypeMap;
import com.carromeu.titan.sample.travel.Travel;

public class DrawableHelper
{
	private static DrawableHelper drawable;

	private final Map<String, Bitmap> map;

	private File cache;

	private DrawableHelper (Context context)
	{
		map = new HashMap<String, Bitmap> ();

		if (android.os.Environment.getExternalStorageState ().equals (android.os.Environment.MEDIA_MOUNTED))
			cache = new File (android.os.Environment.getExternalStorageDirectory (), "Travel/image");
		else
			cache = context.getCacheDir ();

		if (!cache.exists ())
			cache.mkdirs ();
	}

	public static DrawableHelper singleton (Context context)
	{
		if (drawable != null)
			return drawable;

		drawable = new DrawableHelper (context);

		return drawable;
	}

	public static DrawableHelper singleton ()
	{
		return drawable;
	}

	public File createImage () throws IOException
	{
		String time = new SimpleDateFormat ("yyyyMMddHHmmss", Locale.US).format (new Date ());

		String name = Travel.NAME + "-" + time;

		return File.createTempFile (name, ".jpg", cache);
	}

	@SuppressLint ("NewApi")
	public Bitmap getFromUrl (String url)
	{
		if (map.containsKey (url))
			return map.get (url);

		Bitmap bmp = null;

		File file = new File (cache, String.valueOf (url.hashCode ()));

		try
		{
			bmp = BitmapFactory.decodeStream (new FileInputStream (file));
		}
		catch (Exception e)
		{}

		try
		{
			if (bmp == null)
			{
				InputStream in = new URL (url).openConnection ().getInputStream ();

				OutputStream out = new FileOutputStream (file);

				copy (in, out);

				bmp = BitmapFactory.decodeStream (in);
			}

			if (bmp != null && android.os.Build.VERSION.SDK_INT >= 12 && bmp.getByteCount () <= 1024 * 1024)
				map.put (url, bmp);

			return bmp;
		}
		catch (Exception e)
		{}

		return null;
	}

	public void clear ()
	{
		File [] files = cache.listFiles ();

		if (files != null)
			for (File f : files)
				f.delete ();
	}

	public static void copy (InputStream is, OutputStream os)
	{
		final int size = 1024;

		try
		{
			byte [] bytes = new byte [size];

			while (true)
			{
				int count = is.read (bytes, 0, size);

				if (count == -1)
					break;

				os.write (bytes, 0, count);
			}
		}
		catch (Exception ex)
		{}
	}

	public static Bitmap resizeImage (String path, int width, int height)
	{
		BitmapFactory.Options options = new BitmapFactory.Options ();

		options.inJustDecodeBounds = true;

		BitmapFactory.decodeFile (path, options);

		int scale = Math.max (options.outWidth / width, options.outHeight / height);

		options = new BitmapFactory.Options ();

		options.inSampleSize = scale;

		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile (path, options);
	}

	public static Bitmap resizeImage (File file, int width, int height)
	{
		return resizeImage (file.getPath (), width, height);
	}

	public static String getMimeType (String path)
	{
		String extension = MimeTypeMap.getFileExtensionFromUrl (path);

		if (extension != null)
			return MimeTypeMap.getSingleton ().getMimeTypeFromExtension (extension);

		return "application/octet-stream";
	}

	public static void fix (String path, int resolution)
	{
		try
		{
			BitmapFactory.Options options = new BitmapFactory.Options ();

			options.inJustDecodeBounds = true;
			
			BitmapFactory.decodeFile (path, options);
			
			int scale = Math.max (options.outWidth / resolution, options.outHeight / resolution);

			options = new BitmapFactory.Options ();
			
			if (scale > 1)
				options.inSampleSize = scale;

			options.inJustDecodeBounds = false;

			Bitmap image = BitmapFactory.decodeFile (path, options);
			
			ExifInterface exif = new ExifInterface (path);
			
			if (exif.getAttribute (ExifInterface.TAG_ORIENTATION).equalsIgnoreCase ("6"))
				image = rotate (image, 90);
			else
				if (exif.getAttribute (ExifInterface.TAG_ORIENTATION).equalsIgnoreCase ("8"))
					image = rotate (image, 270);
				else
					if (exif.getAttribute (ExifInterface.TAG_ORIENTATION).equalsIgnoreCase ("3"))
						image = rotate (image, 180);

			FileOutputStream fos = new FileOutputStream (path);

			BufferedOutputStream bos = new BufferedOutputStream (fos);
			
			image.compress (CompressFormat.JPEG, 80, bos);

			bos.flush ();
			
			bos.close ();
		}
		catch (Exception e)
		{
			e.printStackTrace ();
		}
	}

	public static Bitmap rotate (Bitmap bitmap, int degree)
	{
		int w = bitmap.getWidth ();
		int h = bitmap.getHeight ();

		Matrix matrix = new Matrix ();

		matrix.postRotate (degree);

		return Bitmap.createBitmap (bitmap, 0, 0, w, h, matrix, true);
	}

	public static void fix(File imgFile, int resolution) {
		fix(imgFile.getPath(), resolution);
	}

	public static Bitmap resizeImage(File file, int width) {
		
		BitmapFactory.Options options = new BitmapFactory.Options ();
		
		options.inJustDecodeBounds = false;
		
		Bitmap image = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
		
		LogHelper.debug(DrawableHelper.class, "Resizing: " + file.getAbsolutePath());
		LogHelper.debug(DrawableHelper.class, "getWidth: " + image.getWidth());
		LogHelper.debug(DrawableHelper.class, "getHeight: " + image.getHeight());
		
		Integer height = (image.getHeight() * width) / image.getWidth();

		LogHelper.debug(DrawableHelper.class, "new width: " + width);
		LogHelper.debug(DrawableHelper.class, "new height: " + height);
		
		return resizeImage(file, width, height);
	}

	public static Bitmap resizeImage(String path, int width) {
		return resizeImage(new File(path), width);
	}
}