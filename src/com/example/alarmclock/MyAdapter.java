package com.example.alarmclock;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.AvoidXfermode.Mode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private String[] week = { "只响一次","每天","周一", "周二", "周三", "周四", "周五", "周六", "周日" };
	private TextView repeatDay;
	private CheckBox cb;
	private Context context;
	private SharedPreferences sp;
	private String[] spWeekName = {"cb1", "cb2", "cb3", "cb4", "cb5", "cb6", "cb7", "cb8", "cb9" };
	// TODO Auto-generated method stub
	private String[] num1 = new String[8];

	public MyAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return week.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.repeat_item, null);
		repeatDay = (TextView) view.findViewById(R.id.item_tv);
		cb = (CheckBox) view.findViewById(R.id.cb);

		repeatDay.setText(week[position]);
		// Log.i("info", "repeatDay:"+repeatDay.toString());
		sp = context.getSharedPreferences("checkbox", Context.MODE_PRIVATE);

		if (sp.getInt(spWeekName[position], 0)!=0) {
				Log.i("info", "position:" + position);
			cb.setChecked(true);
			}

		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {

				
				
					if (sp.getInt(spWeekName[position], 0)!=0) {
						cb.setChecked(false);
						Editor editor = sp.edit();
						editor.remove(spWeekName[position]);
						//editor.putBoolean(spWeekName[position], false);
						editor.commit();
					} else {
						cb.setChecked(true);
						Editor editor = sp.edit();
						editor.putInt(spWeekName[position], position+1);
						editor.commit();
						Log.i("info", "isNull:" + spWeekName[position]);
						

						// Toast.makeText(context, "week:"+sp.getString("week",
						// ""), Toast.LENGTH_LONG).show();

					
					}
			}
			
		});
		

		// Toast.makeText(context, "repeat:"+sp.getString(spWeekName[position],
		// ""), Toast.LENGTH_LONG).show();
		return view;
	}

}
