package com.asksven.betterbatterystats;

import com.asksven.android.common.utils.SystemAppInstaller;
import com.asksven.android.common.utils.SystemAppInstaller.Status;
import com.asksven.betterbatterystats.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity
{

	final static String TAG = "BetteryInfoTest.MainActivity";
	final static String PACKAGE = "com.asksven.betterbatterystats";

	Object m_stats = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final TextView permBattery = (TextView) findViewById(R.id.textViewPermBATTERY_STATS);
		if (hasBatteryStatsPermission(this))
		{
			permBattery.setText("BATTERY_STATS Granted");
		}
		else
		{
			permBattery.setText("BATTERY_STATS  not granted");
		}

		final TextView permDump = (TextView) findViewById(R.id.textViewPermDUMP);
		if (hasDumpPermission(this))
		{
			permDump.setText("DUMP Granted");
		}
		else
		{
			permDump.setText("DUMP  not granted");
		}
		
		final Button buttonUninstall = (Button) findViewById(R.id.buttonUninstall);

		buttonUninstall.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				try
				{
					Status status = SystemAppInstaller.uninstall(PACKAGE);

					if (status.getSuccess())
					{
						Toast.makeText(MainActivity.this, "Succeeded", Toast.LENGTH_LONG).show();
						// prepare the alert box
			            AlertDialog.Builder alertbox = new AlertDialog.Builder(MainActivity.this);
			 
			            // set the message to display
		            	alertbox.setMessage("Uninstalled as system app: your preferences have been reset. Please reboot to clean up and update your preferences.");
			   			 
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
						Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_LONG).show();
						Log.e(TAG,"History: " + status.toString());
					}						
				}
				catch (Exception e)
				{
					Log.e(TAG, "Exception: " + Log.getStackTraceString(e));
					Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_LONG).show();
				}
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

}
