package com.asksven.betterbatterystats;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.asksven.betterbatterystats.R;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity
{

	final static String TAG = "BetteryInfoTest.MainActivity";
	final static String APK = "com.asksven.betterbatterystats";
	
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
