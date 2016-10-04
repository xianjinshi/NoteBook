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
		
		//����ѡ��Ի���
		final Builder b = new AlertDialog.Builder(this);
		//ѡ��ʱ��Ի���
		final Builder b1 = new AlertDialog.Builder(this);
		final Builder b2 = new AlertDialog.Builder(this);
		mTitleText = (EditText) findViewById(R.id.title);
		mBodyText = (EditText) findViewById(R.id.body);
		//���������ı���
		mTypeText = (TextView)findViewById(R.id.tv_type);
		//����ʱ����ʾ
		
		Tv_createdTime = (TextView)findViewById(R.id.tv_createdTime);
		//�������ð�ť
		//tv_type = (TextView)findViewById(R.id.TextView_type);
		//������ʾʱ��
		w_time = (TextView)findViewById(R.id.w_time);
		//������ʾ����
		wraming_type = (TextView)findViewById(R.id.wraming_type);
		Button confirmButton = (Button) findViewById(R.id.confirm);

		mRowId = null;
		// ÿһ��intent�����һ��Bundle�͵�extras���ݡ�
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
				b2.setTitle("ѡ����ʾ/��������:");
				b2.setItems(new String[]{"������ʾ","��������","��������"},new 
						DialogInterface.OnClickListener(){

					public void onClick(DialogInterface dialog, int which) {
						switch(which){
						case 0:
							wraming_type.setText("������ʾ");
							break;
						case 1:
							String textTitle = mTitleText.getText().toString();
							String textBody = mBodyText.getText().toString();
							final String ss = "���⣺" + textTitle + "\n" + "���ݣ�" + textBody + "(����app����)";
							wraming_type.setText("��������");
							Intent intent=new Intent(Intent.ACTION_SEND);    
			                intent.setType("image/*");    
			                intent.putExtra(Intent.EXTRA_SUBJECT, "Share");    
			                intent.putExtra(Intent.EXTRA_TEXT,ss);    
			                 
			                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);    
			                startActivity(Intent.createChooser(intent, getTitle()));   
							break;
						case 2:
							wraming_type.setText("��������");
							Intent alarmas = new Intent(AlarmClock.ACTION_SET_ALARM);// ������д���˵ģ�Ӧ����AlarmClock.ACTION_SET_ALARM
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
				b.setTitle("ѡ������:");
				b.setItems(new String[]{"�ճ�����","����ѧϰ","�˼ʽ���","�������"},new 
						DialogInterface.OnClickListener(){

					public void onClick(DialogInterface dialog, int which) {
						switch(which){
						case 0:
							mTypeText.setText("�ճ�����");
							break;
						case 1:
							mTypeText.setText("����ѧϰ");
							break;
						case 2:
							mTypeText.setText("�˼ʽ���");
							break;
						case 3:
							mTypeText.setText("�������");
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
