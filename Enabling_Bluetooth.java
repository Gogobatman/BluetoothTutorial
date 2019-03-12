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
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    BluetoothAdapter bluetoothAdapter;
    Button button;
    TextView textView;

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
    @Override
    protected  void onDestroy(){
        super.onDestroy();
        unregisterReceiver(receiver);
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);
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
    }
}
