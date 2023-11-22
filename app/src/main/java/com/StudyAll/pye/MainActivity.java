package com.StudyAll.pye;

import android.app.*;
import android.os.*;
import android.view.*;
import android.graphics.*;
import android.service.notification.*;
import android.widget.*;
import android.content.*;
import android.content.pm.*;
import android.view.View.*;
import java.io.*;

public class MainActivity extends Activity //implements View.OnClickListener
{
	//boolean startNotice = false;
	
	int sleepTime = 1000;
	
	static MainActivity instance;
	/*
	private Context mContext;
	private Button showButton, cancelButton;
	private Notification mNotification;
	private NotificationManager mNotificationManager;
	private final static int NOTIFICATION_ID = 0x0001;
	*/
	
	//ResolveInfo homeInfo;
	
	EditText ett;
	Button et;
	TextView optv;
	TextView rstt;
	boolean firstInit = false;
	
	static Handler h,h2;
	static Runnable r,r2;
	
	static PowerManager p;
	
	static int openTime = 0;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		instance = this;
    }

	@Override
	protected void onStart()
	{
		if(!firstInit){
			firstInit = true;
			ett = findViewById(R.id.input0);
			et = findViewById(R.id.sure);
			rstt = findViewById(R.id.rst);
			optv = findViewById(R.id.tip);
			et.setOnClickListener(new View.OnClickListener(){

					@Override
					public void onClick(View p1)
					{
						String s = MainActivity.instance.ett.getText().toString();
						float timemin = 50;
						try{
							timemin = Float.parseFloat(s);
						}catch(Exception e){
							android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
							builder.setTitle("提示");
							builder.setMessage("只能输数字哦");
							builder.setPositiveButton("我知道了", null);
							builder.show();
							return;
						}
						if(timemin < 30.0){
							android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
							builder.setTitle("提示");
							builder.setMessage("太短了，难道你想让我叽叽喳喳吗");
							builder.setPositiveButton("我知道了", null);
							builder.show();
							return;
						}else if(timemin > 120){
							android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
							builder.setTitle("提示");
							builder.setMessage("太长了，有我和没有一样");
							builder.setPositiveButton("我知道了", null);
							builder.show();
							return;
						}
						sleepTime = (int)(timemin * 1000 * 60);
						MainActivity.instance.h.postDelayed(MainActivity.instance.r,1000);
						MainActivity.instance.et.setVisibility(View.INVISIBLE);
						MainActivity.instance.ett.setVisibility(View.INVISIBLE);
						MainActivity.instance.findViewById(R.id.hide0).setVisibility(View.INVISIBLE);
						MainActivity.instance.rstt.setVisibility(View.VISIBLE);
						MainActivity.instance.rstt.setText("");
						optv.setVisibility(optv.VISIBLE);
						MainActivity.sToast("已开启，删掉程序后台可以关闭(•‿•)");
					}


				});
			findViewById(R.id.egg0).setOnClickListener(new View.OnClickListener(){

					@Override
					public void onClick(View p1)
					{
						MainActivity.sToast("点我没有用");
					}


				});
			//PackageManager pm = getPackageManager();
			//homeInfo =pm.resolveActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME), 0);
			h =  new Handler();
			r =new Runnable(){

				int restTime = 0;

				@Override
				public void run()
				{
					if(MainActivity.p.isScreenOn()){
						restTime += 1000;
						MainActivity.instance.rstt.setText("还有"+(float)(sleepTime-restTime)/(float)1000+"秒钟即将提醒");
						if(restTime >= sleepTime){
							restTime = 0;
							notice();
						}
					}
					MainActivity.instance.h.postDelayed(MainActivity.instance.r,1000);
				}


			};
			h2 = new Handler();
			r2 = new Runnable(){
				
				@Override
				public void run()
				{
					if(MainActivity.p.isScreenOn()){
						openTime += 1;
						int hour = (openTime/60/60);
						int min = (int)((float)openTime/(float)60 - hour * 60);
						int rstSec = openTime - min*60 - hour*60*60;
						optv.setText("已经使用手机共"+hour+"小时"+min+"分钟"+rstSec+"秒");
					}
					MainActivity.instance.h2.postDelayed(MainActivity.instance.r2,1000);
				}

				
			};
			p = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
			//boolean savemode = p.isScreenOn();
			MainActivity.instance.h2.postDelayed(MainActivity.instance.r2,1000);
			sToast("如未开启后台耗电记得开一下，否则无法提醒的哦(・∀・)");
			//setupViews();
			//goToIdle();
		}
		super.onStart();
	}

	@Override
	protected void onPause()
	{
		// TODO: Implement this method
		super.onPause();
	}

	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
	}
	
	void SaveDayData(){
		try{
			File file = new File("openDay.pyeLog");
			if(!file.exists()){
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file);
		}catch(IOException e){
			MainActivity.sToast("读写文件出错");
		}
	}
	
	/*public void setupViews() {
		mContext = MainActivity.this;
		showButton = findViewById(R.id.show);
		cancelButton = findViewById(R.id.cancel);
		mNotification = new Notification(R.drawable.ic_launcher, null, System
										 .currentTimeMillis());
		mNotificationManager = (NotificationManager) this
			.getSystemService(NOTIFICATION_SERVICE);
		showButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		if (v == showButton) {
			Intent mIntent = new Intent(mContext, MainActivity.class);
			mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			PendingIntent mContentIntent = PendingIntent.getActivity(mContext,
																	 0, mIntent, 0);

			//mNotification.setLatestEventInfo(mContext, null, null,mContentIntent);
			mNotificationManager.notify(NOTIFICATION_ID, mNotification);
		} else if (v == cancelButton) {
			mNotificationManager.cancel(NOTIFICATION_ID);
		}
	}
	
	/*void goToIdle(){
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}*//*

	@Override
	public void finish()
	{
		moveTaskToBack(true);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode ==KeyEvent.KEYCODE_BACK) {
			goToIdle();
			return true;
		} else{
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	protected void onStop()
	{
		super.onStop();
	}
	
	
	void startActivitySafely(Intent intent) {
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			startActivity(intent);
		} catch(ActivityNotFoundException e) {
			Toast.makeText(this, "work wrongly",Toast.LENGTH_SHORT).show();
		} catch(SecurityException e) {
			Toast.makeText(this, "not security",Toast.LENGTH_SHORT).show();
		}
	}*/
	
    
	
	static void sToast(String s){
		sToast(s,Toast.LENGTH_SHORT);
	}

	static void sToast(String s,int t){
		Handler handler = new Handler(Looper.getMainLooper());
		final String as = s;
		final int ft = t;
		handler.post(new Runnable() {
				@Override
				public void run() {
					//放在UI线程弹Toast
					Toast.makeText(MainActivity.instance,as,ft).show();
				}
			});
	}
	
	
	public void notice() {
		NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		String id = "ProtectEyeWarning";
		CharSequence name = getString(R.string.app_name);
		String description = getString(R.string.app_name);
		NotificationChannel notificationChannel= new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
		notificationChannel.setDescription(description);
		notificationChannel.enableLights(true);
		notificationChannel.setLightColor(Color.RED);
		notificationChannel.enableVibration(true);
		notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 100});
		manager.createNotificationChannel(notificationChannel);
		Notification notification = new Notification.Builder(MainActivity.this, id)
			.setContentTitle("提示")
			.setContentText("记得保护眼睛，休息一下")
			.setWhen(System.currentTimeMillis())
			.setSmallIcon(R.drawable.ic_launcher)
			.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
			.build();
		manager.notify(1, notification);
	}
}
