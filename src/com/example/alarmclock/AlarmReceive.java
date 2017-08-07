package com.example.alarmclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceive extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String msg=intent.getStringExtra("msg");
		String ringtone=intent.getStringExtra("ringtone");
		long intervalMillis=intent.getLongExtra("intervalMillis", 0);
		if(intervalMillis!=0){
			AlarmManagerUtil.setAlarmTime(context,System.currentTimeMillis()+intervalMillis, intent);
		}
		int flag=intent.getIntExtra("soundOrVibrator", 0);
		Intent clockIntent=new Intent(context,ClockAlarmActivity.class);
		clockIntent.putExtra("msg", msg);
		clockIntent.putExtra("ringtone", ringtone);
		clockIntent.putExtra("flag", flag);
		clockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(clockIntent);
	}
}
