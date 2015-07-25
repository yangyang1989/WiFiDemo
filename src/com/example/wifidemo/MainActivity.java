package com.example.wifidemo;

import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	private static final String TAG = "WiFiDemo";
	private WifiManager mWifiManager;
	private IntentFilter mFilter;
	private BroadcastReceiver mReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mWifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
		mFilter = new IntentFilter();
		
		mFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //handleEvent(intent);
            	List<ScanResult> results = mWifiManager.getScanResults();
                if (results != null) {
                    for (ScanResult result : results) {
                        // Ignore hidden and ad-hoc networks.
                        if (result.SSID == null || result.SSID.length() == 0 ||
                                result.capabilities.contains("[IBSS]")) {
                            continue;
                        } else {
                        	Log.d(TAG, "BSSID: " + result.BSSID);
                        	Log.d(TAG, "SSID: " + result.SSID);
                        	Log.d(TAG, "frequency: " + result.frequency);
                        	Log.d(TAG, "level: " + result.level);
                        	Log.d(TAG, "");
                        }
                    }
                }
            }
        };

        if (mWifiManager != null) {
			if (!mWifiManager.isWifiEnabled()) {
				startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
			} else {
				// Request a scan
				mWifiManager.startScan();
				Log.d(TAG, "startScan");
			}
        } else {
        	Log.d(TAG, "mWifiManager == null !!!!");
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		registerReceiver(mReceiver, mFilter);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		unregisterReceiver(mReceiver);
	}
}
