package com.example.kheireddine.popularmoviesstage2.ui.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.kheireddine.popularmoviesstage2.R;
import com.example.kheireddine.popularmoviesstage2.ui.fragments.MainFragment;
import com.example.kheireddine.popularmoviesstage2.utils.Utils;

public class MainActivity extends AppCompatActivity {
    IntentFilter mChargingIntentFilter;
    ChargingBroadCastReceiver mChargingBroadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            return;
        } else {
            addGridFragment();
        }

        mChargingIntentFilter = new IntentFilter();
        mChargingIntentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        mChargingIntentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);

        mChargingBroadcastReceiver = new ChargingBroadCastReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //registerReceiver(mChargingBroadcastReceiver, mChargingIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //unregisterReceiver(mChargingBroadcastReceiver);
    }

    public void addGridFragment() {
        if (!isFinishing()) {
            MainFragment mainFragment = MainFragment.create();

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.fl_main_grid, mainFragment)
                    .commit();
        }
    }

    // Test the Broadcast Receiver
    private void showCharging(boolean isCharging){
        if (isCharging){
            Utils.showLongToastMessage(this, "Message from BroadcastReceiver : The device is charging");
        } else {
            Utils.showLongToastMessage(this, "Message from BroadcastReceiver : The device is NOT charging");
        }
    }

    /**
     * BroadcastReceiver class that checks if the device is charging or not
     * */
    private class ChargingBroadCastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action =  intent.getAction();
            boolean isCharging = action.equals(Intent.ACTION_POWER_CONNECTED);
            showCharging(isCharging);
        }
    }

}

