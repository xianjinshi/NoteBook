package com.ying.www;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MainActivity1 extends Activity implements OnTouchListener {  
	private DiaryDbAdapter mDbHelper;
	private Cursor mDiaryCursor;
	private static final int INSERT_ID = Menu.FIRST;
	private static final int DELETE_ID = Menu.FIRST + 1;
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
	private static final int ACTIVITY_CHECK = 2;
	//�Լ����
	//private Builder b = new AlertDialog.Builder(this);
	
	private ListView _l;
	private int _position;
	private long _id;
	private ListView listView;
	//private List<Diary>DiaryList = new ArrayList<Diary>();
	
	private DiaryAdapter changeAdapter(Cursor cursor){
		List<Diary>DiaryList = new ArrayList<Diary>();
		startManagingCursor(cursor);
		
		 if(cursor.moveToFirst()){
				do{
					String body = cursor.getString(cursor.getColumnIndex(DiaryDbAdapter.KEY_BODY));
					String title = cursor.getString(cursor.getColumnIndex(DiaryDbAdapter.KEY_TITLE));
					String created = cursor.getString(cursor.getColumnIndex(DiaryDbAdapter.KEY_CREATED));
					String type = cursor.getString(cursor.getColumnIndex(DiaryDbAdapter.KEY_TYPE));
					int imageId;
					if(type.equals("�˼ʽ���")){
						 imageId = R.drawable.bq_r;
					}else if(type.equals("�ճ�����")){
						 imageId = R.drawable.bq_s;
					}else if(type.equals("����ѧϰ")){
						 imageId = R.drawable.bq_g;
					}else if(type.equals("�������")){
						 imageId = R.drawable.bq_o;
					}else{
						 imageId = R.drawable.bq_o;
					}
					Diary mD = new Diary(title,body,type,created,imageId);
					DiaryList.add(mD);	
				}while(cursor.moveToNext());
		}
		 DiaryAdapter adapter = new DiaryAdapter(MainActivity1.this,
	        		R.layout.row,DiaryList);
		 DiaryList = null;
		return adapter;
	}

	private void renderListViewBy(String ss) {
		mDiaryCursor = mDbHelper.getDiaryByType(ss);
		//�����ɵ�Cursor���󽻸�Activity����������Ϊ����ϵͳ���Զ����ܶ���
				startManagingCursor(mDiaryCursor);
				
				DiaryAdapter DA = changeAdapter(mDiaryCursor);
				//����ListViewÿһ�Ŷ�Ӧ�����ݴ����ݿ����ĸ��б�ѡȡ
				/*String[] from = new String[] {DiaryDbAdapter.KEY_TYPE, DiaryDbAdapter.KEY_TITLE,
						DiaryDbAdapter.KEY_CREATED,DiaryDbAdapter.KEY_BODY };
				//��������ʽ���ListView��Id
				int[] to = new int[] {R.id.show_type, R.id.text1, R.id.created,R.id.body};
				//�������е�������LietView�е����ݶ�Ӧ����
				SimpleCursorAdapter notes = new SimpleCursorAdapter(this,
						R.layout.row, mDiaryCursor, from, to);*/
				//��SimpleCurAdapter��������ListActivity��ListView������ 
				ListView listView = (ListView)findViewById(R.id.list);
				//listView.setAdapter(notes);	
				//listView.setAdapter(notes);	
				listView.setAdapter(DA);	
	}
	
	private void renderListView() {
		mDiaryCursor = mDbHelper.getAllNotes();
		//mDiaryCursor = mDbHelper.getDiaryByType();
		//�����ɵ�Cursor���󽻸�Activity����������Ϊ����ϵͳ���Զ����ܶ���
		startManagingCursor(mDiaryCursor);
		DiaryAdapter DA = changeAdapter(mDiaryCursor);
		
		//����ListViewÿһ�Ŷ�Ӧ�����ݴ����ݿ����ĸ��б�ѡȡ
		/*String[] from = new String[] {DiaryDbAdapter.KEY_TYPE, DiaryDbAdapter.KEY_TITLE,
						DiaryDbAdapter.KEY_CREATED,DiaryDbAdapter.KEY_BODY };
		//��������ʽ���ListView��Id
		int[] to = new int[] {R.id.show_type, R.id.text1, R.id.created,R.id.body};
		//�������е�������LietView�е����ݶ�Ӧ����
		SimpleCursorAdapter notes = new SimpleCursorAdapter(this,
						R.layout.row, mDiaryCursor, from, to);*/
		//��SimpleCurAdapter��������ListActivity��ListView������ 
		ListView listView = (ListView)findViewById(R.id.list);
		//listView.setAdapter(notes);
		listView.setAdapter(DA);
	}
	private void createDiary() {
		Intent i = new Intent(this, DiaryEdit.class);
		startActivityForResult(i, ACTIVITY_CREATE);
		//Toast.makeText(getApplicationContext(),"laile",Toast.LENGTH_LONG).show();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		renderListView();
	}
    /** 
     * ������ʾ������menuʱ����ָ������Ҫ�ﵽ���ٶȡ� 
     */  
    public static final int SNAP_VELOCITY = 200;  
  
    /** 
     * ��Ļ���ֵ�� 
     */  
    private int screenWidth;  
  
    /** 
     * menu�����Ի����������Ե��ֵ��menu���ֵĿ��������marginLeft�����ֵ֮�󣬲����ټ��١� 
     */  
    private int leftEdge;  
  
    /** 
     * menu�����Ի��������ұ�Ե��ֵ��Ϊ0����marginLeft����0֮�󣬲������ӡ� 
     */  
    private int rightEdge = 0;  
  
    /** 
     * menu��ȫ��ʾʱ������content�Ŀ��ֵ�� 
     */  
    private int menuPadding = 350;  
  
    /** 
     * �����ݵĲ��֡� 
     */  
    private View content;  
  
    /** 
     * menu�Ĳ��֡� 
     */  
    private View menu;  
  
    /** 
     * menu���ֵĲ�����ͨ���˲���������leftMargin��ֵ�� 
     */  
    private LinearLayout.LayoutParams menuParams;  
  
    /** 
     * ��¼��ָ����ʱ�ĺ����ꡣ 
     */  
    private float xDown;  
  
    /** 
     * ��¼��ָ�ƶ�ʱ�ĺ����ꡣ 
     */  
    private float xMove;  
  
    /** 
     * ��¼�ֻ�̧��ʱ�ĺ����ꡣ 
     */  
    private float xUp;  
  
    /** 
     * menu��ǰ����ʾ�������ء�ֻ����ȫ��ʾ������menuʱ�Ż���Ĵ�ֵ�����������д�ֵ��Ч�� 
     */  
    private boolean isMenuVisible;  
  
    /** 
     * ���ڼ�����ָ�������ٶȡ� 
     */  
    private VelocityTracker mVelocityTracker;  
    private String[] data = {"apple","Banana","Orange"};
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main); 
        
        mDbHelper = new DiaryDbAdapter(this);
		mDbHelper.open();
		renderListView();
        initValues();
        
        
        /*
         * �����ı�manage�ĵ���¼�
         * */
        TextView manage = (TextView)findViewById(R.id.manage);
        manage.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				scrollToMenu();
				
			}
		});
        ListView listView = (ListView)findViewById(R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> l, View v, int position, long id) {
				_position = position;
				_id = id;
				Cursor c = mDiaryCursor;
				c.moveToPosition(_position);
				Intent i = new Intent(getApplicationContext(), DiaryEdit.class);
				i.putExtra(DiaryDbAdapter.KEY_ROWID, _id);
				i.putExtra(DiaryDbAdapter.KEY_TITLE, c.getString(c
						.getColumnIndexOrThrow(DiaryDbAdapter.KEY_TITLE)));
				i.putExtra(DiaryDbAdapter.KEY_BODY, c.getString(c
						.getColumnIndexOrThrow(DiaryDbAdapter.KEY_BODY)));
				i.putExtra(DiaryDbAdapter.KEY_TYPE, c.getString(c
						.getColumnIndexOrThrow(DiaryDbAdapter.KEY_TYPE)));
				i.putExtra(DiaryDbAdapter.KEY_CREATED, c.getString(c
						.getColumnIndexOrThrow(DiaryDbAdapter.KEY_CREATED)));
				i.putExtra(DiaryDbAdapter.KEY_W_TYPE, c.getString(c
						.getColumnIndexOrThrow(DiaryDbAdapter.KEY_W_TYPE)));
				//i.putExtra(DiaryDbAdapter.KEY_W_TYPE, c.getString(c
						//.getColumnIndexOrThrow(DiaryDbAdapter.KEY_W_TYPE)));
				startActivityForResult(i, ACTIVITY_EDIT);
				
			}
		});
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View v, int position, long id) {
				_position = position;
				_id = id;
				AlertDialog.Builder dialog = new AlertDialog
						.Builder(MainActivity1.this);
				dialog.setTitle("�Ƿ�ɾ����");
				dialog.setMessage("....");
				dialog.setCancelable(true);
				dialog.setPositiveButton("ɾ��", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						mDbHelper.deleteDiary(_id);
						renderListView();
						
					}
				});
				dialog.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						//mDbHelper.deleteDiary(_id);
						renderListView();
						
					}
				});
				dialog.show();
				
				return false;
			}
		} );
        ImageButton bt_buite = (ImageButton)findViewById(R.id.buite);
		//Button bt_edit = (Button)findViewById(R.id.edit);
		bt_buite.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
			createDiary();
			}
		});
		
		TextView type0 = (TextView)findViewById(R.id.type0);
		TextView type1 = (TextView)findViewById(R.id.type1);
		TextView type2 = (TextView)findViewById(R.id.type2);
		TextView type3 = (TextView)findViewById(R.id.type3);
		TextView type4 = (TextView)findViewById(R.id.type4);
        type0.setOnClickListener(new clickListener());
        type1.setOnClickListener(new clickListener());
        type2.setOnClickListener(new clickListener());
        type3.setOnClickListener(new clickListener());
        type4.setOnClickListener(new clickListener());
        
        content.setOnTouchListener(this); 	
    }
    public class clickListener implements View.OnClickListener{

		public void onClick(View v) {
			switch(v.getId()){
			case R.id.type0:
				renderListView();
				scrollToContent();
				break;
			case R.id.type1:
				renderListViewBy("'�ճ�����'");
				scrollToContent();
				break;
			case R.id.type2:
				renderListViewBy("'����ѧϰ'");
				scrollToContent();
				break;
			case R.id.type3:
				renderListViewBy("'�˼ʽ���'");
				scrollToContent();
				break;
			case R.id.type4:
				renderListViewBy("'�������'");
				scrollToContent();
				break;
			default:
				break;
				
			}
			
		}
    	
    }
    /** 
     * ��ʼ��һЩ�ؼ������ݡ�������ȡ��Ļ�Ŀ�ȣ���content�����������ÿ�ȣ���menu�����������ÿ�Ⱥ�ƫ�ƾ���ȡ� 
     */  
    private void initValues() {  
        WindowManager window = (WindowManager) getSystemService(Context.WINDOW_SERVICE);  
        screenWidth = window.getDefaultDisplay().getWidth();  
        content = findViewById(R.id.content);  
        menu = findViewById(R.id.menu); 
        
        
        menuParams = (LinearLayout.LayoutParams) menu.getLayoutParams();  
        // ��menu�Ŀ������Ϊ��Ļ��ȼ�ȥmenuPadding  
        menuParams.width = screenWidth - menuPadding;  
        // ���Ե��ֵ��ֵΪmenu��ȵĸ���  
        leftEdge = -menuParams.width;  
        // menu��leftMargin����Ϊ���Ե��ֵ��������ʼ��ʱmenu�ͱ�Ϊ���ɼ�  
        menuParams.leftMargin = leftEdge;  
        // ��content�Ŀ������Ϊ��Ļ���  
        content.getLayoutParams().width = screenWidth;  
    }  
  
    public boolean onTouch(View v, MotionEvent event) {  
        createVelocityTracker(event);  
        switch (event.getAction()) {  
        case MotionEvent.ACTION_DOWN:  
            // ��ָ����ʱ����¼����ʱ�ĺ�����  
            xDown = event.getRawX();  
            break;  
        case MotionEvent.ACTION_MOVE:  
            // ��ָ�ƶ�ʱ���ԱȰ���ʱ�ĺ����꣬������ƶ��ľ��룬������menu��leftMarginֵ���Ӷ���ʾ������menu  
            xMove = event.getRawX();  
            int distanceX = (int) (xMove - xDown);  
            if (isMenuVisible) {  
                menuParams.leftMargin = distanceX;  
            } else {  
                menuParams.leftMargin = leftEdge + distanceX;  
            }  
            if (menuParams.leftMargin < leftEdge) {  
                menuParams.leftMargin = leftEdge;  
            } else if (menuParams.leftMargin > rightEdge) {  
                menuParams.leftMargin = rightEdge;  
            }  
            menu.setLayoutParams(menuParams);  
            break;  
        case MotionEvent.ACTION_UP:  
            // ��ָ̧��ʱ�������жϵ�ǰ���Ƶ���ͼ���Ӷ������ǹ�����menu���棬���ǹ�����content����  
            xUp = event.getRawX();  
            if (wantToShowMenu()) {  
                if (shouldScrollToMenu()) {  
                    scrollToMenu();  
                } else {  
                    scrollToContent();  
                }  
            } else if (wantToShowContent()) {  
                if (shouldScrollToContent()) {  
                    scrollToContent();  
                } else {  
                    scrollToMenu();  
                }  
            }  
            recycleVelocityTracker();  
            break;  
        }  
        return true;  
    }  
  
    /** 
     * �жϵ�ǰ���Ƶ���ͼ�ǲ�������ʾcontent�������ָ�ƶ��ľ����Ǹ������ҵ�ǰmenu�ǿɼ��ģ�����Ϊ��ǰ��������Ҫ��ʾcontent�� 
     *  
     * @return ��ǰ��������ʾcontent����true�����򷵻�false�� 
     */  
    private boolean wantToShowContent() {  
        return xUp - xDown < 0 && isMenuVisible;  
    }  
  
    /** 
     * �жϵ�ǰ���Ƶ���ͼ�ǲ�������ʾmenu�������ָ�ƶ��ľ������������ҵ�ǰmenu�ǲ��ɼ��ģ�����Ϊ��ǰ��������Ҫ��ʾmenu�� 
     *  
     * @return ��ǰ��������ʾmenu����true�����򷵻�false�� 
     */  
    private boolean wantToShowMenu() {  
        return xUp - xDown > 0 && !isMenuVisible;  
    }  
  
    /** 
     * �ж��Ƿ�Ӧ�ù�����menuչʾ�����������ָ�ƶ����������Ļ��1/2��������ָ�ƶ��ٶȴ���SNAP_VELOCITY�� 
     * ����ΪӦ�ù�����menuչʾ������ 
     *  
     * @return ���Ӧ�ù�����menuչʾ��������true�����򷵻�false�� 
     */  
    private boolean shouldScrollToMenu() {  
        return xUp - xDown > screenWidth / 2 || getScrollVelocity() > SNAP_VELOCITY;  
    }  
  
    /** 
     * �ж��Ƿ�Ӧ�ù�����contentչʾ�����������ָ�ƶ��������menuPadding������Ļ��1/2�� 
     * ������ָ�ƶ��ٶȴ���SNAP_VELOCITY�� ����ΪӦ�ù�����contentչʾ������ 
     *  
     * @return ���Ӧ�ù�����contentչʾ��������true�����򷵻�false�� 
     */  
    private boolean shouldScrollToContent() {  
        return xDown - xUp + menuPadding > screenWidth / 2 || getScrollVelocity() > SNAP_VELOCITY;  
    }  
  
    /** 
     * ����Ļ������menu���棬�����ٶ��趨Ϊ30. 
     */  
    private void scrollToMenu() {  
        new ScrollTask().execute(30);  
    }  
  
    /** 
     * ����Ļ������content���棬�����ٶ��趨Ϊ-30. 
     */  
    private void scrollToContent() {  
        new ScrollTask().execute(-30);  
    }  
  
    /** 
     * ����VelocityTracker���󣬲�������content����Ļ����¼����뵽VelocityTracker���С� 
     *  
     * @param event 
     *            content����Ļ����¼� 
     */  
    private void createVelocityTracker(MotionEvent event) {  
        if (mVelocityTracker == null) {  
            mVelocityTracker = VelocityTracker.obtain();  
        }  
        mVelocityTracker.addMovement(event);  
    }  
  
    /** 
     * ��ȡ��ָ��content���滬�����ٶȡ� 
     *  
     * @return �����ٶȣ���ÿ�����ƶ��˶�������ֵΪ��λ�� 
     */  
    private int getScrollVelocity() {  
        mVelocityTracker.computeCurrentVelocity(1000);  
        int velocity = (int) mVelocityTracker.getXVelocity();  
        return Math.abs(velocity);  
    }  
  
    /** 
     * ����VelocityTracker���� 
     */  
    private void recycleVelocityTracker() {  
        mVelocityTracker.recycle();  
        mVelocityTracker = null;  
    }  
  
    class ScrollTask extends AsyncTask<Integer, Integer, Integer> {  
  
        @Override  
        protected Integer doInBackground(Integer... speed) {  
            int leftMargin = menuParams.leftMargin;  
            // ���ݴ�����ٶ����������棬������������߽���ұ߽�ʱ������ѭ����  
            while (true) {  
                leftMargin = leftMargin + speed[0];  
                if (leftMargin > rightEdge) {  
                    leftMargin = rightEdge;  
                    break;  
                }  
                if (leftMargin < leftEdge) {  
                    leftMargin = leftEdge;  
                    break;  
                }  
                publishProgress(leftMargin);  
                // Ϊ��Ҫ�й���Ч��������ÿ��ѭ��ʹ�߳�˯��20���룬�������۲��ܹ���������������  
                sleep(20);  
            }  
            if (speed[0] > 0) {  
                isMenuVisible = true;  
            } else {  
                isMenuVisible = false;  
            }  
            return leftMargin;  
        }  
  
        @Override  
        protected void onProgressUpdate(Integer... leftMargin) {  
            menuParams.leftMargin = leftMargin[0];  
            menu.setLayoutParams(menuParams);  
        }  
  
        @Override  
        protected void onPostExecute(Integer leftMargin) {  
            menuParams.leftMargin = leftMargin;  
            menu.setLayoutParams(menuParams);  
        }  
    }  
  
    /** 
     * ʹ��ǰ�߳�˯��ָ���ĺ������� 
     *  
     * @param millis 
     *            ָ����ǰ�߳�˯�߶�ã��Ժ���Ϊ��λ 
     */  
    private void sleep(long millis) {  
        try {  
            Thread.sleep(millis);  
        } catch (InterruptedException e) {  
            e.printStackTrace();  
        }  
    }  
}  