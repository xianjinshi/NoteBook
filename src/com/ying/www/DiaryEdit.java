package com.ying.www;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DiaryEdit extends Activity {
	private EditText mTitleText;
	private EditText mBodyText;
	private Long mRowId;
	private DiaryDbAdapter mDbHelper;
	private TextView tv_type;
	private TextView Tv_createdTime;
	private TextView wraming_type;
	private TextView mTypeText;
	private TextView w_time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDbHelper = new DiaryDbAdapter(this);
		mDbHelper.open();
		setContentView(R.layout.edit);
		
		//类型选择对话框
		final Builder b = new AlertDialog.Builder(this);
		//选择时间对话框
		final Builder b1 = new AlertDialog.Builder(this);
		final Builder b2 = new AlertDialog.Builder(this);
		mTitleText = (EditText) findViewById(R.id.title);
		mBodyText = (EditText) findViewById(R.id.body);
		//类型设置文本域
		mTypeText = (TextView)findViewById(R.id.tv_type);
		//创建时间显示
		
		Tv_createdTime = (TextView)findViewById(R.id.tv_createdTime);
		//类型设置按钮
		//tv_type = (TextView)findViewById(R.id.TextView_type);
		//设置提示时间
		w_time = (TextView)findViewById(R.id.w_time);
		//设置提示类型
		wraming_type = (TextView)findViewById(R.id.wraming_type);
		Button confirmButton = (Button) findViewById(R.id.confirm);

		mRowId = null;
		// 每一个intent都会带一个Bundle型的extras数据。
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String title = extras.getString(DiaryDbAdapter.KEY_TITLE);
			String body = extras.getString(DiaryDbAdapter.KEY_BODY);
			String type = extras.getString(DiaryDbAdapter.KEY_TYPE);
			String creat =extras.getString(DiaryDbAdapter.KEY_CREATED);
			String w_type = extras.getString(DiaryDbAdapter.KEY_W_TYPE);
			mRowId = extras.getLong(DiaryDbAdapter.KEY_ROWID);

			if (title != null) {
				mTitleText.setText(title);
			}
			if (body != null) {
				mBodyText.setText(body);
			}
			if (type != null){
				mTypeText.setText(type);
			}
			if(creat != null){
				Tv_createdTime.setText(creat);
			}
			if(w_type != null){
				wraming_type.setText(w_type);
			}
			
			
		}
		
		wraming_type.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				b2.setTitle("选择提示/分享类型:");
				b2.setItems(new String[]{"不用提示","分享他人","闹钟提醒"},new 
						DialogInterface.OnClickListener(){

					public void onClick(DialogInterface dialog, int which) {
						switch(which){
						case 0:
							wraming_type.setText("不用提示");
							break;
						case 1:
							String textTitle = mTitleText.getText().toString();
							String textBody = mBodyText.getText().toString();
							final String ss = "主题：" + textTitle + "\n" + "内容：" + textBody + "(来自app分享)";
							wraming_type.setText("分享他人");
							Intent intent=new Intent(Intent.ACTION_SEND);    
			                intent.setType("image/*");    
			                intent.putExtra(Intent.EXTRA_SUBJECT, "Share");    
			                intent.putExtra(Intent.EXTRA_TEXT,ss);    
			                 
			                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);    
			                startActivity(Intent.createChooser(intent, getTitle()));   
							break;
						case 2:
							wraming_type.setText("闹钟提醒");
							Intent alarmas = new Intent(AlarmClock.ACTION_SET_ALARM);// 这行你写错了的，应该是AlarmClock.ACTION_SET_ALARM
			                startActivity(alarmas);
							break;
						}		
					}	
				});
				b2.create().show();
			}
		});
		
		w_time.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
					
			}
		});
		
		mTypeText.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				b.setTitle("选择类型:");
				b.setItems(new String[]{"日常生活","工作学习","人际交往","情感生活"},new 
						DialogInterface.OnClickListener(){

					public void onClick(DialogInterface dialog, int which) {
						switch(which){
						case 0:
							mTypeText.setText("日常生活");
							break;
						case 1:
							mTypeText.setText("工作学习");
							break;
						case 2:
							mTypeText.setText("人际交往");
							break;
						case 3:
							mTypeText.setText("情感生活");
							break;
						}		
					}
				});
				
				b.create().show();
			}
		});
		confirmButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				String title = mTitleText.getText().toString();
				String body = mBodyText.getText().toString();
				String type = mTypeText.getText().toString();
				String w_type = wraming_type.getText().toString();
				if (mRowId != null) {
					String creat = Tv_createdTime.getText().toString();
					mDbHelper.updateDiary(mRowId, title, body,type,creat,w_type);
				} else
					mDbHelper.createDiary(title, body,type,w_type);
				Intent mIntent = new Intent();
				setResult(RESULT_OK, mIntent);
				finish();
			}
		});
	}
}
