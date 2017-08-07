package com.example.alarmclock;

import android.app.Activity;
import android.app.AlertDialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AlarmActivity extends Activity implements OnClickListener {
	
	private Button bt;
	private AlertDialog dialog;
	private MediaPlayer  mp;
        
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm);
		
		
		
	    mp=MediaPlayer.create(this, R.raw.zhuxian);
	    mp.isLooping();
	    mp.start();
	    
		Log.i("info", "∆Ù∂ØAlarmActivity");
		
		View view=View.inflate(this, R.layout.alert_dialog, null);
		
		bt=(Button) view.findViewById(R.id.bt);
		bt.setOnClickListener(this);
		
		
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setTitle("ƒ÷÷”");
		builder.setView(view);
		dialog=builder.create();
		dialog.show();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		dialog.dismiss();
		mp.stop();
	}
}
