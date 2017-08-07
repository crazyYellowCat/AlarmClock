package com.example.alarmclock;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ClockAlarmActivity extends Activity {
	private MediaPlayer mediaplayer;
	private Vibrator vibrator;
	private Button bt;
	private Uri uri;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clock_alrm_activity);
		
		
		Intent intent=getIntent();
	    String ringtone=intent.getStringExtra("ringtone");
		Log.i("info", "ringtone:"+ringtone);
		String msg=intent.getStringExtra("msg");
		int flag=intent.getIntExtra("flag", 0);
		
		
		
		showDialogInBroadcastReceiver(msg,ringtone,flag);
	}

	private void showDialogInBroadcastReceiver(String msg,String ringtone, final int flag) {
		// TODO Auto-generated method stub
		uri=Uri.parse(ringtone);
		Log.i("info", "uri:"+uri);
		if(flag==1||flag==2){
			mediaplayer=new MediaPlayer();
			try {
				mediaplayer.setDataSource(ClockAlarmActivity.this, uri);
				mediaplayer.setAudioStreamType(AudioManager.STREAM_ALARM);
				mediaplayer.setLooping(true);
				mediaplayer.prepare();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			mediaplayer.start();
		}
		if(flag==0||flag==2){
			vibrator=(Vibrator) this.getSystemService(Service.VIBRATOR_SERVICE);
			vibrator.vibrate(new long[]{100,10,100,600}, 0);	
		}
		
	  AlertDialog.Builder builder=new AlertDialog.Builder(this);
	  View view=View.inflate(this, R.layout.clock_alrm_activity, null);
	  bt=(Button) view.findViewById(R.id.bt);
	  builder.setView(view);
	  builder.setTitle("ƒ÷÷”Ã·–—");
	  builder.setMessage(msg);
	  final AlertDialog dialog=builder.create();
	  dialog.show();
	  
	  bt.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(flag==1||flag==2){
				mediaplayer.stop();
				mediaplayer.release();
			}
			if(flag==0||flag==2){
				vibrator.cancel();
			}
			dialog.dismiss();
			finish();
		}
	});
	  
	  
	  
	}

   @Override
protected void onNewIntent(Intent intent) {
	// TODO Auto-generated method stub
	   setIntent(intent);
	   int test=intent.getIntExtra("test", 0);
		Log.i("info", "test2:"+test);
	super.onNewIntent(intent);
}

}
