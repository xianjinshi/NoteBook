package com.ying.www;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DiaryAdapter extends ArrayAdapter{
	private int resourceId;
	
	@SuppressWarnings("unchecked")
	public DiaryAdapter(Context context, int textViewResourceId, List<Diary> diaryList) {
		super(context, textViewResourceId, diaryList);
		resourceId = textViewResourceId;		
	}
	@Override
	public View getView(int position,View convertView,ViewGroup parent){
		Diary diary = (Diary)getItem(position);
		View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
		ImageView image = (ImageView)view.findViewById(R.id.im_type);
		TextView body = (TextView)view.findViewById(R.id.body);
		TextView title = (TextView)view.findViewById(R.id.text1);
		TextView created = (TextView)view.findViewById(R.id.created);
		TextView type = (TextView)view.findViewById(R.id.show_type);
		image.setImageResource(diary.getImageId());
		body.setText(diary.getBody());
		title.setText(diary.getTitle());
		created.setText(diary.getCreated());
		type.setText(diary.getType());
		return view;
		
	}

}
