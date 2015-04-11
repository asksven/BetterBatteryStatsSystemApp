/*
 * Copyright (C) 2014-2015 asksven
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.asksven.betterbatterystats;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.security.auth.x500.X500Principal;

import com.asksven.android.common.utils.SystemAppInstaller;
import com.asksven.android.common.utils.SystemAppInstaller.Status;
import com.asksven.betterbatterystats_xdaedition.R;

import android.net.Uri;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity
{

	final static String TAG = "BetteryInfoTest.MainActivity";
	final static String PACKAGE = "com.asksven.betterbatterystats";
	
	final static String RECOVERY_GPLAY = "http://better.asksven.org/bbs-systemapp/";
	final static String RECOVERY_XDA = "http://forum.xda-developers.com/showpost.php?p=15869904&postcount=3";

	Object m_stats = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle(getString(R.string.app_name));
	    setSupportActionBar(toolbar);

		final TextView permBattery = (TextView) findViewById(R.id.textViewPermBATTERY_STATS);
		if (hasBatteryStatsPermission(this))
		{
			permBattery.setText("BATTERY_STATS " + getString(R.string.label_granted));
		}
		else
		{
			permBattery.setText("BATTERY_STATS  " + getString(R.string.label_not_granted));
		}

		final TextView permDump = (TextView) findViewById(R.id.textViewPermDUMP);
		if (hasDumpPermission(this))
		{
			permDump.setText("DUMP " + getString(R.string.label_granted));
		}
		else
		{
			permDump.setText("DUMP  " + getString(R.string.label_not_granted));
		}
		
		final Button buttonUninstall = (Button) findViewById(R.id.buttonUninstall);

		buttonUninstall.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				Status status;
				try
				{
					status = SystemAppInstaller.uninstall(PACKAGE);
						
				
					if (status.getSuccess())
					{
						Toast.makeText(MainActivity.this, getString(R.string.info_succeeded), Toast.LENGTH_LONG).show();
						// prepare the alert box
			            AlertDialog.Builder alertbox = new AlertDialog.Builder(MainActivity.this);
			 
			            // set the message to display
		            	alertbox.setMessage(getString(R.string.info_uninstalled_system_app));
			   			 
			            // add a neutral button to the alert box and assign a click listener
			            alertbox.setNeutralButton("Ok", new DialogInterface.OnClickListener()
			            {
			 
			                // click listener on the alert box
			                public void onClick(DialogInterface arg0, int arg1)
			                {
			                }
			            });
			 
			            // show it
			            alertbox.show();

					}
					else
					{
						Toast.makeText(MainActivity.this, getString(R.string.info_failed), Toast.LENGTH_LONG).show();
						Log.e(TAG,"History: " + status.toString());
					}						
				}
				catch (Exception e)
				{
					Log.e(TAG, "Exception: " + Log.getStackTraceString(e));
					Toast.makeText(MainActivity.this, getString(R.string.info_failed), Toast.LENGTH_LONG).show();
				}
			}
		});
		
		final Button buttonRecovery = (Button) findViewById(R.id.button3);


		buttonRecovery.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				String url = "";
				if (isDebuggable(MainActivity.this))
				{
					// use XDA
					url = RECOVERY_XDA;
				}
				else
				{
					url = RECOVERY_GPLAY;
				}
				
            	Intent i = new Intent(Intent.ACTION_VIEW);
            	i.setData(Uri.parse(url));
            	startActivity(i);
			}
		});
	}

		
	private boolean hasBatteryStatsPermission(Context context)
	{
		return wasPermissionGranted(context, android.Manifest.permission.BATTERY_STATS);
	}

	private boolean hasDumpPermission(Context context)
	{
		return wasPermissionGranted(context, android.Manifest.permission.DUMP);
	}

	private boolean wasPermissionGranted(Context context, String permission)
	{
		PackageManager pm = context.getPackageManager();
		int hasPerm = pm.checkPermission(
		    permission, 
		    context.getPackageName());
		return (hasPerm == PackageManager.PERMISSION_GRANTED);
	}

	private static final X500Principal DEBUG_DN = new X500Principal("CN=Android Debug,O=Android,C=US");
	private boolean isDebuggable(Context ctx)
	{
	    boolean debuggable = false;

	    try
	    {
	        PackageInfo pinfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(),PackageManager.GET_SIGNATURES);
	        Signature signatures[] = pinfo.signatures;

	        CertificateFactory cf = CertificateFactory.getInstance("X.509");

	        for ( int i = 0; i < signatures.length;i++)
	        {   
	            ByteArrayInputStream stream = new ByteArrayInputStream(signatures[i].toByteArray());
	            X509Certificate cert = (X509Certificate) cf.generateCertificate(stream);       
	            debuggable = cert.getSubjectX500Principal().equals(DEBUG_DN);
	            if (debuggable)
	                break;
	        }
	    }
	    catch (NameNotFoundException e)
	    {
	        //debuggable variable will remain false
	    }
	    catch (CertificateException e)
	    {
	        //debuggable variable will remain false
	    }
	    return debuggable;
	}
}
