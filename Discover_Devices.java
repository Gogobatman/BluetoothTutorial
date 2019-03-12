package com.example.bluetoothchateng;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    BluetoothAdapter bluetoothAdapter;
    Button button;
    Button button2;
    TextView textView;
    public ArrayList<BluetoothDevice> Devices = new ArrayList<>();
    public DeviceListAdapter deviceListAdapter;
    ListView listView;
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(bluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,bluetoothAdapter.ERROR);
                switch (state){
                    case BluetoothAdapter.STATE_OFF:
                        textView.setText("STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        textView.setText("STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        textView.setText("STATE ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        textView.setText("STATE TURNING ON");
                        break;

                }
            }
        }
    };
    private final BroadcastReceiver receiver2 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(bluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE,bluetoothAdapter.ERROR);
                switch (state){
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        textView.setText("Discoverability enabled");
                        break;
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        textView.setText("Discoverability enabled. Able to receive connections");
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        textView.setText("Discoverability disabled. Not able to receive connections");
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        textView.setText("Connecting");
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        textView.setText("Connected");
                        break;
                }
            }
        }
    };
    private final BroadcastReceiver receiver3 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Devices.add(device);
                deviceListAdapter= new DeviceListAdapter(context,R.layout.device_adapter_view,Devices);
                listView.setAdapter(deviceListAdapter);
            }
        }
    };
    @Override
    protected  void onDestroy(){
        super.onDestroy();
        unregisterReceiver(receiver);
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
        textView = findViewById(R.id.textView);
        listView = findViewById(R.id.listView);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        buttonik();
    }
    public void buttonik(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bluetoothAdapter==null){
                    textView.setText("bluetooth not suportet");
                }
                else{
                    if(!bluetoothAdapter.isEnabled()){
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivity(enableBtIntent);

                        IntentFilter intentFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
                        registerReceiver(receiver,intentFilter);
                    }else if(bluetoothAdapter.isEnabled()){
                        bluetoothAdapter.disable();

                        IntentFilter intentFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
                        registerReceiver(receiver,intentFilter);
                    }

                }
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,300);
                startActivity(discoverableIntent);

                IntentFilter intentFilter = new IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
                registerReceiver(receiver2,intentFilter);
                }

        });
    }
    public void btnDiscover(){
        if(bluetoothAdapter.isDiscovering()){
            bluetoothAdapter.cancelDiscovery();
            textView.setText("Caneling discovery");
            bluetoothAdapter.startDiscovery();
            IntentFilter discoverDeviceIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(receiver3,discoverDeviceIntent);
        }
        if(!bluetoothAdapter.isDiscovering()){
            bluetoothAdapter.startDiscovery();
            IntentFilter discoverDeviceIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(receiver3,discoverDeviceIntent);
        }
    }
}
