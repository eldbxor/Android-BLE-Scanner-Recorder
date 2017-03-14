package com.example.taek.blescanner_bylyt.UI;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.taek.blescanner_bylyt.R;
import com.example.taek.blescanner_bylyt.Services.BLEScanService;
import com.example.taek.blescanner_bylyt.Utils.Constants;
import com.example.taek.blescanner_bylyt.Utils.IncomingHandler;

public class MainActivity extends AppCompatActivity {
    private Context mainActiviyContext = this;
    private String TAG = "MainActiviy";
    private Messenger incomingMessenger;
    private ServiceConnection mServiceConnection;
    private Messenger mMessenger;
    private Switch switch_scan;
    private TextView editView_saveData;
    private CheckBox checkbox_dataName;

    private void connectMessenger() {
        Log.d(TAG, "connectMessenger(): call connectMessenger");
        ComponentName cn = new ComponentName(mainActiviyContext, BLEScanService.class);
        Intent intent = new Intent();
        intent.setComponent(cn);

        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d(TAG, "connectMessenger(): connected to service");
                mMessenger = new Messenger(service);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };

        mainActiviyContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        switch_scan = (Switch) findViewById(R.id.BLEScanSwitch);
        editView_saveData = (TextView) findViewById(R.id.editView_dataName);
        checkbox_dataName = (CheckBox) findViewById(R.id.checkBox_saveData);
        editView_saveData.setEnabled(false);

        // BLE 관련 Permission 주기
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // Android M Permission check
            if(this.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect beacons.");
                builder.setPositiveButton("Ok", null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, Constants.PERMISSION_REQUEST_COARSE_LOCATION);
                    }
                });
                builder.show();
            }

            incomingMessenger = new Messenger(new IncomingHandler(Constants.HANDLER_TYPE_ACTIVITY, mainActiviyContext));

            connectMessenger();
        }


        switch_scan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Switch on
                if (isChecked) {
                    try { // BLE Scan
                        Message msg = Message.obtain(null, Constants.HANDLE_MESSAGE_TYPE_BLE_SCAN);
                        msg.replyTo = incomingMessenger;
                        mMessenger.send(msg);
                        Log.d(TAG, "MessengerCommunication: Activity send HANDLE_MESSAGE_TYPE_BLE_SCAN");
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                // Switch off
                else {
                    try { // stop scan
                        mMessenger.send(Message.obtain(null, Constants.HANDLE_MESSAGE_TYPE_STOP_SCAN));
                        Log.d(TAG, "MessengerCommunication: Activity send HANDLE_MESSAGE_TYPE_STOP_SCAN");
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        checkbox_dataName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // checkBox is checked
                if (isChecked) {
                    editView_saveData.setEnabled(true);
                }
                // checkBox isn't checked
                else {
                    editView_saveData.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int reqeustCode, String permission[], int[] grantResults){
        switch (reqeustCode){
            case Constants.PERMISSION_REQUEST_COARSE_LOCATION:{
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.d("permission", "coarse location permission granted");
                }else{
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, " +
                            "this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton("Ok", null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {

                        }
                    });
                    builder.show();
                }
                return;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mainActiviyContext.unbindService(mServiceConnection);
    }
}
