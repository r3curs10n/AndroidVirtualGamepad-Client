package com.example.gp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

public class MyTask extends AsyncTask<Void, Void, Void> {


	  public Void doInBackground(Void... unused) {
		  
		  Socket socket = null;
			DataOutputStream dataOutputStream = null;
			DataInputStream dataInputStream = null;
			Log.d("sffsf","sfsdfsdf");
			try {
				
				socket = new Socket("192.168.1.100", 1234);
				Log.d("sffsf","sfsdfsdfgggggggggggggggggggggg");
				//Log.d("sffsf--------",ipText.getText().toString());
				dataOutputStream = new DataOutputStream(socket.getOutputStream());
				dataInputStream = new DataInputStream(socket.getInputStream());
				dataOutputStream.writeChars("jskfhkfdkhgkfdhgkfd");
				//dataOutputStream.flush();
				dataOutputStream.writeChars("iyiyiyiyiuyiuyi");
				//dataOutputStream.writeUTF("hello world");
				//String xx = (dataInputStream.readUTF());
				
			} catch (UnknownHostException e) {
				Log.d("sffsf--------failed","sdf");
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally {
				
				if (socket != null){
					try {
						socket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (dataOutputStream != null){
					try {
						dataOutputStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (dataInputStream != null){
					try {
						dataInputStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			return null;
		
		  
		  
	  }

	}
