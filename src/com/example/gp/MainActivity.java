package com.example.gp;

import java.io.Console;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.util.Log;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;;

public class MainActivity extends Activity implements SensorEventListener {

	
	EditText ipText;
	Button sendButton;
	
	SensorManager sensorManager;
	
	Socket soc = null;
	DataOutputStream os = null;
	DataInputStream is = null;
	
	float refx, refy, refz;
	float[] ori = new float[3];
	TextView dbg;
	
	Button left, right;
	
	boolean startsending = false;
	
	private class SocketConnector extends AsyncTask<Void, Void, Void> {
		
		public Void doInBackground(Void... unused) {
			
			try {
				soc = new Socket("192.168.1.100", 1234);
				os = new DataOutputStream(soc.getOutputStream());
				Log.d("soc", "connection established");
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			return null;
			
		}
		
	}
	
	
	protected void onPause(){
		super.onPause();
		/*try {
			soc.close();
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
	}
	
	
	protected void onResume(){
		super.onResume();
		//new SocketConnector().execute();
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Log.d("soc", "sfjldfjfglfd");
        ipText = (EditText) findViewById(R.id.ip);
        sendButton = (Button) findViewById(R.id.send);
        dbg = (TextView) findViewById(R.id.dbg);
        left = (Button) findViewById(R.id.left);
        right = (Button) findViewById(R.id.right);
        
        sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR), SensorManager.SENSOR_DELAY_UI);
        Log.d("soc", "sfjldfjfglfd");
        refx=refy=refz=0;
        new SocketConnector().execute();
        
        left.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				try{
					if (event.getAction() == MotionEvent.ACTION_DOWN){
						
						os.writeBytes("{pl}");
						Log.d("soc", "left pressed");
						
						
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						
						os.writeBytes("{rl}");
						
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
			}
		});
        
        
        right.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				try{
					if (event.getAction() == MotionEvent.ACTION_DOWN){
						
						os.writeBytes("{pr}");
						Log.d("soc", "right pressed");
						
						
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						
						os.writeBytes("{rr}");
						
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
			}
		});
        
        dbg.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				refx = ori[0];
				refy = ori[1];
				refz = ori[2];
				Log.d("soc", "ref set");
				startsending = true;
			}
		});
        
        
        sendButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					soc.close();
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}	
		});
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		//Log.d("soc", "sfjldfjfglfd");
		if (event.sensor.getType()==Sensor.TYPE_ROTATION_VECTOR){
			float ax=event.values[0];
            float ay=event.values[1];
            float az=event.values[2];
            float[] rotV = new float[3];
            rotV[0] = ax; rotV[1]=ay; rotV[2]=az;
            float rotM[] = new float[9];
            SensorManager.getRotationMatrixFromVector(rotM, rotV);
            //ori = new float[3];
            ori = SensorManager.getOrientation(rotM, ori);
            try {
				if (startsending) os.writeBytes(String.format("%f#%f#%f#", ori[0]-refx, ori[1]-refy, ori[2]-refz));
				//Log.d("soc", String.format("%f#%f#%f", ori[0]-refx, ori[1]-refy, ori[2]-refz));
				dbg.setText(String.format("%f %f %f\n%f %f %f", ori[0]-refx, ori[1]-refy, ori[2]-refz, refx, refy, refz));
				//Log.d("soc", String.format("%f %f %f", ori[0]-refx, ori[1]-refy, ori[2]-refz));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
         }
	}
    
}


