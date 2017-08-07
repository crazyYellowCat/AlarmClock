package com.example.alarmclock;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	private AlarmManager am;
	private Button confirm, cancel, button,confirm_repeat;
	private TextView repeat, ringtongOrVibrator,ringtone_pick;
	private TimePicker time;
	private Calendar calendar;
	private PendingIntent pi;
	private Intent intent;
	private ListView repeatLv;
	private ArrayList<String> date;
	private SharedPreferences sp,spRingtongOrVibrator, spRingtong,spVibrator,spRingtongAndVibrator;
	private String[] spWeekName = { "cb1", "cb2", "cb3", "cb4", "cb5", "cb6",
			"cb7" };
	private String[] num1 = new String[8];
	private HashMap weekNumber;
	private AlertDialog dialog;
	private AlarmManagerUtil amu;
	private int selectHour, selectMinute;
	private String tips = "您设置的时钟时间到！",ringtoneUri;
	private CheckBox cb_ringtong, cb_vibrator, cb_vibratorAndRingtong;
	private RadioGroup radiogroup,rg_repeat;
	private RadioButton ringtong,vibrator,ringtong_vibrator,single,daily,monday,tuesday,
	wednesday,thursday,friday,saturday,sunday;
	int[] dayOfWeek = new int[8];
	int flag, id, week;
    private final static int ALAMCODE=1;
    private Uri pickUri;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		time = (TimePicker) findViewById(R.id.tp);
		am = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
		button = (Button) findViewById(R.id.sure);
		repeat = (TextView) findViewById(R.id.repeat);
		ringtongOrVibrator = (TextView) findViewById(R.id.ringtong);
		ringtone_pick=(TextView) findViewById(R.id.ringtong_pick);
		calendar = Calendar.getInstance();
		sp = getSharedPreferences("checkbox", Context.MODE_PRIVATE);
		
		ringtone_pick.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
				intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
				intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "铃声选择");
				startActivityForResult(intent, ALAMCODE);
				
			}
		});
		
	
        
		ringtongOrVibrator.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				View view = View.inflate(MainActivity.this,
						R.layout.ring_or_vibrator, null);
				radiogroup=(RadioGroup) view.findViewById(R.id.radiogroup);
				ringtong=(RadioButton) view.findViewById(R.id.ringtong);
				vibrator=(RadioButton) view.findViewById(R.id.vibrator);
				ringtong_vibrator=(RadioButton)view.findViewById(R.id.ringtong_vibrator);
				/*cb_ringtong = (CheckBox) view.findViewById(R.id.cb_ringtong);
				cb_vibrator = (CheckBox) view.findViewById(R.id.cb_vibrator);
				cb_vibratorAndRingtong=(CheckBox) view.findViewById(R.id.vibratorAndRingtong);*/
				// final String[] items=new String[]{"震动","铃声","震动及铃声"};
				AlertDialog.Builder builder = new AlertDialog.Builder(
						MainActivity.this);
				builder.setTitle("铃声或震动");
				builder.setView(view);
				spRingtongOrVibrator=getSharedPreferences("promptingMode",Context.MODE_PRIVATE);
				spRingtong=getSharedPreferences("promptingModeRingtong", Context.MODE_PRIVATE);
				spVibrator=getSharedPreferences("promptingModeVibrator", Context.MODE_PRIVATE);
				
				spRingtongAndVibrator=getSharedPreferences("promptingModevibratorAndRingtong", Context.MODE_PRIVATE);
				
				if(spRingtongAndVibrator.getInt("promptingmode", 3)==1){
					ringtong.setChecked(true);
				}else if(spRingtongAndVibrator.getInt("promptingmode", 3)==0){
					vibrator.setChecked(true);
				}else if(spRingtongAndVibrator.getInt("promptingmode", 3)==2){
					ringtong_vibrator.setChecked(true);
				}
				
				radiogroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {		
					@Override
					public void onCheckedChanged(RadioGroup arg0, int checkedId) {
						// TODO Auto-generated method stub
						switch (checkedId) {
						case R.id.vibrator:
							Editor editor = spRingtongAndVibrator.edit();
							editor.putInt("promptingmode", 0);
							editor.commit();
							Toast.makeText(MainActivity.this, "您点击了铃声！", Toast.LENGTH_LONG).show();
							break;
						case R.id.ringtong:
							Editor editor1 = spRingtongAndVibrator.edit();
							editor1.putInt("promptingmode", 1);
							editor1.commit();
							break;
						case R.id.ringtong_vibrator:
							Editor editor2 = spRingtongAndVibrator.edit();
							editor2.putInt("promptingmode", 2);
							editor2.commit();
							break;

						default:
							break;
						}
						Log.i("info", "promptingmode:"+spRingtongAndVibrator.getInt("promptingmode", 5));
					}
				});
				
				builder.setPositiveButton("确定", null);
				AlertDialog dialog = builder.create();
				dialog.show();

				
			}
		});
		button.setOnClickListener(this);
		repeat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(
						MainActivity.this);
				View view = View.inflate(MainActivity.this,
						R.layout.repeat_select, null);
				single=(RadioButton) view.findViewById(R.id.single);
				daily=(RadioButton) view.findViewById(R.id.daily);
				monday=(RadioButton) view.findViewById(R.id.monday);
				tuesday=(RadioButton) view.findViewById(R.id.tuesday);
				wednesday=(RadioButton) view.findViewById(R.id.wednesday);
				thursday=(RadioButton) view.findViewById(R.id.thursday);
				friday=(RadioButton) view.findViewById(R.id.friday);
				saturday=(RadioButton) view.findViewById(R.id.saturday);
				sunday=(RadioButton) view.findViewById(R.id.sunday);
			   

				//repeatLv = (ListView) view.findViewById(R.id.lv);

				//confirm = (Button) view.findViewById(R.id.confirm);
				//MyAdapter adapter = new MyAdapter(MainActivity.this);

				//repeatLv.setAdapter(adapter);
				Log.i("info", "flag:"+flag+",week:"+week);
				if(flag==0&&week==0){
					single.setChecked(true);
				}else if(flag==1&&week==0){
					daily.setChecked(true);
				}else if(week==1){
					monday.setChecked(true);
				}else if(week==2){
					tuesday.setChecked(true);
				}else if(week==3){
					wednesday.setChecked(true);
				}else if(week==4){
					thursday.setChecked(true);
				}else if(week==5){
					friday.setChecked(true);
				}else if(week==6){
					saturday.setChecked(true);
				}else if(week==7){
					sunday.setChecked(true);
				}
				rg_repeat=(RadioGroup) view.findViewById(R.id.rg_repeat);
				rg_repeat.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(RadioGroup arg0, int checkedId) {
						// TODO Auto-generated method stub
						switch (checkedId) {
						case R.id.single:
							flag=0;
							week=0;
							Log.i("info", "flag:"+flag+",week:"+week);
							Toast.makeText(MainActivity.this, "点击了0", Toast.LENGTH_LONG).show();
							break;
						case R.id.daily:
							flag=1;
							week=0;
							Toast.makeText(MainActivity.this, "点击了1", Toast.LENGTH_LONG).show();
							Log.i("info", "flag:"+flag+",week:"+week);
							break;
						case R.id.monday:
							flag=2;
							week=1;
							Toast.makeText(MainActivity.this, "点击了2", Toast.LENGTH_LONG).show();
							Log.i("info", "flag:"+flag+",week:"+week);
							break;
						case R.id.tuesday:
							flag=2;
							week=2;
							Toast.makeText(MainActivity.this, "点击了3", Toast.LENGTH_LONG).show();
							Log.i("info", "flag:"+flag+",week:"+week);
							break;
						case R.id.wednesday:
							flag=2;
							week=3;
							Toast.makeText(MainActivity.this, "点击了4", Toast.LENGTH_LONG).show();
							Log.i("info", "flag:"+flag+",week:"+week);
							break;
						case R.id.thursday:
							flag=2;
							week=4;
							Log.i("info", "flag:"+flag+",week:"+week);
							break;
						case R.id.friday:
							flag=2;
							week=5;
							Log.i("info", "flag:"+flag+",week:"+week);
							break;
						case R.id.saturday:
							flag=2;
							week=6;
							
							break;
						case R.id.sunday:
							flag=2;
							week=7;
							
							break;

						default:
							break;
						}
					}
				});
                builder.setPositiveButton("确定", null);
                builder.setTitle("重复周期");
				builder.setView(view);
				dialog = builder.create();
				dialog.show();
	
			}

		});

		time.setOnTimeChangedListener(new OnTimeChangedListener() {

			@Override
			public void onTimeChanged(TimePicker tp, int hour, int minute) {
				// TODO Auto-generated method stu		

				/*
				 * calendar.set(Calendar.HOUR_OF_DAY, hour);
				 * calendar.set(Calendar.MINUTE, minute);
				 */
				selectHour = hour;
				selectMinute = minute;

				intent = new Intent();
				intent.setAction("com.loonggg.alarm.clock");
				pi = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);

			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode!=RESULT_OK){
    		return ;
    	}
		if(requestCode==ALAMCODE){
			pickUri=data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
			RingtoneManager.setActualDefaultRingtoneUri(MainActivity.this, RingtoneManager.TYPE_ALARM, pickUri);
			ringtoneUri=pickUri.toString();
			Log.i("info", "ringtoneUri:"+ringtoneUri);
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stu

		int soundOrVibrator = spRingtongAndVibrator.getInt("promptingmode",3);
		Log.i("info", "soundOrVibrator:" + soundOrVibrator);
		Log.i("info", "week:" + week);
		amu = new AlarmManagerUtil();
		Log.i("info", "riongtoneUri2:"+ringtoneUri);
		AlarmManagerUtil.setAlarm(MainActivity.this, flag, selectHour,
				selectMinute, id, week, tips, soundOrVibrator,ringtoneUri);

		Toast.makeText(MainActivity.this, "设置成功", Toast.LENGTH_LONG).show();

	}

	public int getCurrentDayOfWeek() {
		Calendar now = Calendar.getInstance();
		// 一周第一天是否为星期天
		boolean isFirstSunday = (now.getFirstDayOfWeek() == Calendar.SUNDAY);
		// 获取周几
		System.out.println("isFirstSunday:" + isFirstSunday);
		int weekDay = now.get(Calendar.DAY_OF_WEEK);
		// 若一周第一天为星期天，则-1
		/*
		 * if(isFirstSunday){ weekDay = weekDay - 1; if(weekDay == 0){ weekDay =
		 * 7; } }
		 */
		System.out.println("weekDay:" + weekDay);
		return weekDay;
	}

}
