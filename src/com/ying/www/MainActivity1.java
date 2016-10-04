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
	//自己添加
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
					if(type.equals("人际交往")){
						 imageId = R.drawable.bq_r;
					}else if(type.equals("日常生活")){
						 imageId = R.drawable.bq_s;
					}else if(type.equals("工作学习")){
						 imageId = R.drawable.bq_g;
					}else if(type.equals("情感生活")){
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
		//将生成的Cursor对象交给Activity管理，这样是为了让系统能自动做很多事
				startManagingCursor(mDiaryCursor);
				
				DiaryAdapter DA = changeAdapter(mDiaryCursor);
				//定义ListView每一排对应的数据从数据库中哪个列表选取
				/*String[] from = new String[] {DiaryDbAdapter.KEY_TYPE, DiaryDbAdapter.KEY_TITLE,
						DiaryDbAdapter.KEY_CREATED,DiaryDbAdapter.KEY_BODY };
				//以数组形式存放ListView的Id
				int[] to = new int[] {R.id.show_type, R.id.text1, R.id.created,R.id.body};
				//把数组中的数据与LietView中的数据对应起来
				SimpleCursorAdapter notes = new SimpleCursorAdapter(this,
						R.layout.row, mDiaryCursor, from, to);*/
				//将SimpleCurAdapter的数据与ListActivity的ListView绑定起来 
				ListView listView = (ListView)findViewById(R.id.list);
				//listView.setAdapter(notes);	
				//listView.setAdapter(notes);	
				listView.setAdapter(DA);	
	}
	
	private void renderListView() {
		mDiaryCursor = mDbHelper.getAllNotes();
		//mDiaryCursor = mDbHelper.getDiaryByType();
		//将生成的Cursor对象交给Activity管理，这样是为了让系统能自动做很多事
		startManagingCursor(mDiaryCursor);
		DiaryAdapter DA = changeAdapter(mDiaryCursor);
		
		//定义ListView每一排对应的数据从数据库中哪个列表选取
		/*String[] from = new String[] {DiaryDbAdapter.KEY_TYPE, DiaryDbAdapter.KEY_TITLE,
						DiaryDbAdapter.KEY_CREATED,DiaryDbAdapter.KEY_BODY };
		//以数组形式存放ListView的Id
		int[] to = new int[] {R.id.show_type, R.id.text1, R.id.created,R.id.body};
		//把数组中的数据与LietView中的数据对应起来
		SimpleCursorAdapter notes = new SimpleCursorAdapter(this,
						R.layout.row, mDiaryCursor, from, to);*/
		//将SimpleCurAdapter的数据与ListActivity的ListView绑定起来 
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
     * 滚动显示和隐藏menu时，手指滑动需要达到的速度。 
     */  
    public static final int SNAP_VELOCITY = 200;  
  
    /** 
     * 屏幕宽度值。 
     */  
    private int screenWidth;  
  
    /** 
     * menu最多可以滑动到的左边缘。值由menu布局的宽度来定，marginLeft到达此值之后，不能再减少。 
     */  
    private int leftEdge;  
  
    /** 
     * menu最多可以滑动到的右边缘。值恒为0，即marginLeft到达0之后，不能增加。 
     */  
    private int rightEdge = 0;  
  
    /** 
     * menu完全显示时，留给content的宽度值。 
     */  
    private int menuPadding = 350;  
  
    /** 
     * 主内容的布局。 
     */  
    private View content;  
  
    /** 
     * menu的布局。 
     */  
    private View menu;  
  
    /** 
     * menu布局的参数，通过此参数来更改leftMargin的值。 
     */  
    private LinearLayout.LayoutParams menuParams;  
  
    /** 
     * 记录手指按下时的横坐标。 
     */  
    private float xDown;  
  
    /** 
     * 记录手指移动时的横坐标。 
     */  
    private float xMove;  
  
    /** 
     * 记录手机抬起时的横坐标。 
     */  
    private float xUp;  
  
    /** 
     * menu当前是显示还是隐藏。只有完全显示或隐藏menu时才会更改此值，滑动过程中此值无效。 
     */  
    private boolean isMenuVisible;  
  
    /** 
     * 用于计算手指滑动的速度。 
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
         * 设置文本manage的点击事件
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
				dialog.setTitle("是否删除？");
				dialog.setMessage("....");
				dialog.setCancelable(true);
				dialog.setPositiveButton("删除", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						mDbHelper.deleteDiary(_id);
						renderListView();
						
					}
				});
				dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
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
				renderListViewBy("'日常生活'");
				scrollToContent();
				break;
			case R.id.type2:
				renderListViewBy("'工作学习'");
				scrollToContent();
				break;
			case R.id.type3:
				renderListViewBy("'人际交往'");
				scrollToContent();
				break;
			case R.id.type4:
				renderListViewBy("'情感生活'");
				scrollToContent();
				break;
			default:
				break;
				
			}
			
		}
    	
    }
    /** 
     * 初始化一些关键性数据。包括获取屏幕的宽度，给content布局重新设置宽度，给menu布局重新设置宽度和偏移距离等。 
     */  
    private void initValues() {  
        WindowManager window = (WindowManager) getSystemService(Context.WINDOW_SERVICE);  
        screenWidth = window.getDefaultDisplay().getWidth();  
        content = findViewById(R.id.content);  
        menu = findViewById(R.id.menu); 
        
        
        menuParams = (LinearLayout.LayoutParams) menu.getLayoutParams();  
        // 将menu的宽度设置为屏幕宽度减去menuPadding  
        menuParams.width = screenWidth - menuPadding;  
        // 左边缘的值赋值为menu宽度的负数  
        leftEdge = -menuParams.width;  
        // menu的leftMargin设置为左边缘的值，这样初始化时menu就变为不可见  
        menuParams.leftMargin = leftEdge;  
        // 将content的宽度设置为屏幕宽度  
        content.getLayoutParams().width = screenWidth;  
    }  
  
    public boolean onTouch(View v, MotionEvent event) {  
        createVelocityTracker(event);  
        switch (event.getAction()) {  
        case MotionEvent.ACTION_DOWN:  
            // 手指按下时，记录按下时的横坐标  
            xDown = event.getRawX();  
            break;  
        case MotionEvent.ACTION_MOVE:  
            // 手指移动时，对比按下时的横坐标，计算出移动的距离，来调整menu的leftMargin值，从而显示和隐藏menu  
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
            // 手指抬起时，进行判断当前手势的意图，从而决定是滚动到menu界面，还是滚动到content界面  
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
     * 判断当前手势的意图是不是想显示content。如果手指移动的距离是负数，且当前menu是可见的，则认为当前手势是想要显示content。 
     *  
     * @return 当前手势想显示content返回true，否则返回false。 
     */  
    private boolean wantToShowContent() {  
        return xUp - xDown < 0 && isMenuVisible;  
    }  
  
    /** 
     * 判断当前手势的意图是不是想显示menu。如果手指移动的距离是正数，且当前menu是不可见的，则认为当前手势是想要显示menu。 
     *  
     * @return 当前手势想显示menu返回true，否则返回false。 
     */  
    private boolean wantToShowMenu() {  
        return xUp - xDown > 0 && !isMenuVisible;  
    }  
  
    /** 
     * 判断是否应该滚动将menu展示出来。如果手指移动距离大于屏幕的1/2，或者手指移动速度大于SNAP_VELOCITY， 
     * 就认为应该滚动将menu展示出来。 
     *  
     * @return 如果应该滚动将menu展示出来返回true，否则返回false。 
     */  
    private boolean shouldScrollToMenu() {  
        return xUp - xDown > screenWidth / 2 || getScrollVelocity() > SNAP_VELOCITY;  
    }  
  
    /** 
     * 判断是否应该滚动将content展示出来。如果手指移动距离加上menuPadding大于屏幕的1/2， 
     * 或者手指移动速度大于SNAP_VELOCITY， 就认为应该滚动将content展示出来。 
     *  
     * @return 如果应该滚动将content展示出来返回true，否则返回false。 
     */  
    private boolean shouldScrollToContent() {  
        return xDown - xUp + menuPadding > screenWidth / 2 || getScrollVelocity() > SNAP_VELOCITY;  
    }  
  
    /** 
     * 将屏幕滚动到menu界面，滚动速度设定为30. 
     */  
    private void scrollToMenu() {  
        new ScrollTask().execute(30);  
    }  
  
    /** 
     * 将屏幕滚动到content界面，滚动速度设定为-30. 
     */  
    private void scrollToContent() {  
        new ScrollTask().execute(-30);  
    }  
  
    /** 
     * 创建VelocityTracker对象，并将触摸content界面的滑动事件加入到VelocityTracker当中。 
     *  
     * @param event 
     *            content界面的滑动事件 
     */  
    private void createVelocityTracker(MotionEvent event) {  
        if (mVelocityTracker == null) {  
            mVelocityTracker = VelocityTracker.obtain();  
        }  
        mVelocityTracker.addMovement(event);  
    }  
  
    /** 
     * 获取手指在content界面滑动的速度。 
     *  
     * @return 滑动速度，以每秒钟移动了多少像素值为单位。 
     */  
    private int getScrollVelocity() {  
        mVelocityTracker.computeCurrentVelocity(1000);  
        int velocity = (int) mVelocityTracker.getXVelocity();  
        return Math.abs(velocity);  
    }  
  
    /** 
     * 回收VelocityTracker对象。 
     */  
    private void recycleVelocityTracker() {  
        mVelocityTracker.recycle();  
        mVelocityTracker = null;  
    }  
  
    class ScrollTask extends AsyncTask<Integer, Integer, Integer> {  
  
        @Override  
        protected Integer doInBackground(Integer... speed) {  
            int leftMargin = menuParams.leftMargin;  
            // 根据传入的速度来滚动界面，当滚动到达左边界或右边界时，跳出循环。  
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
                // 为了要有滚动效果产生，每次循环使线程睡眠20毫秒，这样肉眼才能够看到滚动动画。  
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
     * 使当前线程睡眠指定的毫秒数。 
     *  
     * @param millis 
     *            指定当前线程睡眠多久，以毫秒为单位 
     */  
    private void sleep(long millis) {  
        try {  
            Thread.sleep(millis);  
        } catch (InterruptedException e) {  
            e.printStackTrace();  
        }  
    }  
}  