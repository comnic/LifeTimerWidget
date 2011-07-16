package kr.comnic.LifeTimerWidget;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

public class LifeTimerWidget extends AppWidgetProvider  {
	private static final int DISPLAY_TIME_MODE_SEC	= 3;
	private static final int DISPLAY_TIME_MODE_MIN	= 2;
	private static final int DISPLAY_TIME_MODE_HOUR	= 1;
	private static final int DISPLAY_TIME_MODE_DAY	= 0;
	
	private static final int DISPLAY_COLOR_BLACK	= 0;
	private static final int DISPLAY_COLOR_WHITE	= 1;
	private static final int DISPLAY_COLOR_GRAY		= 2;
	private static final int DISPLAY_COLOR_DARKGRAY	= 3;
	private static final int DISPLAY_COLOR_LIGHTGRAY= 4;
	private static final int DISPLAY_COLOR_BLUE		= 5;
	private static final int DISPLAY_COLOR_YELLOW	= 6;

	private static final int WIDGET_UPDATE_INTERVAL = 1000;
/*
	private static final int 	BIRTH_YEAR 	= 1987;
	private static final String BIRTH_MONTH	= "01";
	private static final String BIRTH_DAY	= "28";
	
	private static final int END_AGE		= 26;

	private static final String BIRTH_DATE 	= BIRTH_YEAR + BIRTH_MONTH + BIRTH_DAY + "000000";
	private static final String DIE_DATE 	= (BIRTH_YEAR + END_AGE) + BIRTH_MONTH + BIRTH_DAY + "000000";
*/
	private LTWDBHelper m_db;
	private LTWInfomation m_info;

	private static PendingIntent mSender;
	private static AlarmManager mManager;

	@Override
	public void onReceive(Context context, Intent intent) {
	    // TODO Auto-generated method stub
		String action = intent.getAction();
	    if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action)) {
	    	
	    	Log.i("LifeTimerWidget", "android.appwidget.action.APPWIDGET_UPDATE");
	        removePreviousAlarm();
	        
	        long firstTime = System.currentTimeMillis() + WIDGET_UPDATE_INTERVAL;
	        mSender = PendingIntent.getBroadcast(context, 0, intent, 0);
	        mManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	        mManager.set(AlarmManager.RTC, firstTime, mSender);
   	
	    	
	    	
	        Bundle extras = intent.getExtras();
	        if (extras != null) {
	            int[] appWidgetIds = extras.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS);
	            if (appWidgetIds != null && appWidgetIds.length > 0) {
	                this.onUpdate(context, AppWidgetManager.getInstance(context), appWidgetIds);
	            }
	        }
	    }
	    else if (AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action)) {
	        Bundle extras = intent.getExtras();
	        if (extras != null && extras.containsKey(AppWidgetManager.EXTRA_APPWIDGET_ID)) {
	            final int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
	            this.onDeleted(context, new int[] { appWidgetId });
	        }
	    }
	    else if (AppWidgetManager.ACTION_APPWIDGET_ENABLED.equals(action)) {
	        this.onEnabled(context);
	    }
	    else if (AppWidgetManager.ACTION_APPWIDGET_DISABLED.equals(action)) {
	        Log.w("LifeTimerWidget", "android.appwidget.action.APPWIDGET_DISABLED");
	        removePreviousAlarm();

	    	this.onDisabled(context);
	    }
	}
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
		if(m_db == null){
			m_db = new LTWDBHelper(context);
		}
		LTWInfomation info = m_db.select();

		final int N = appWidgetIds.length;

		ComponentName widget;
		
		for (int i = 0; i < N; i++) {
			int appWidgetId = appWidgetIds[i];
			// RemoteViews views = new RemoteViews(context.getPackageName(),
			// R.layout.lifetimerwidget);
			RemoteViews views = buildViews(context);
			appWidgetManager.updateAppWidget(appWidgetId, views);

			if(info.birth_year.equals("0000")){
				return;
			}


			widget = new ComponentName(context, LifeTimerWidget.class);

			if(info.isChk1){
				views.setViewVisibility(R.id.tvText, View.VISIBLE);
				views.setTextColor(R.id.tvText, getColor(info.color));
				views.setTextViewText(R.id.tvText, info.text1
						+ " : \n" + getFormatedTime(1, info));
			}else{
				views.setViewVisibility(R.id.tvText, View.GONE);
				
			}
			if(info.isChk2){
				views.setViewVisibility(R.id.tvText2, View.VISIBLE);
				views.setTextColor(R.id.tvText2, getColor(info.color));
				views.setTextViewText(R.id.tvText2, info.text2
						+ " : \n" + getFormatedTime(2, info));
			}else{
				views.setViewVisibility(R.id.tvText2, View.GONE);
			}
			

			appWidgetManager.updateAppWidget(widget, views);
		}
	}
	
	private int getColor(int c){
		int _c = Color.BLACK;
		switch(c){
			case DISPLAY_COLOR_BLACK:
				_c = Color.BLACK;
				break;
			case DISPLAY_COLOR_WHITE:
				_c = Color.WHITE;
				break;
			case DISPLAY_COLOR_GRAY:
				_c = Color.GRAY;
				break;
			case DISPLAY_COLOR_DARKGRAY:
				_c = Color.DKGRAY;
				break;
			case DISPLAY_COLOR_LIGHTGRAY:
				_c = Color.LTGRAY;
				break;
			case DISPLAY_COLOR_BLUE:
				_c = Color.BLUE;
				break;
			case DISPLAY_COLOR_YELLOW:
				_c = Color.YELLOW;
				break;	
		}
		
		return _c;		
	}
	
	private PendingIntent buildActivityIntent(Context context){
		Intent intent = new Intent(context, SettingActivity.class);
		PendingIntent pi = PendingIntent.getActivity(context, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
		return pi;
	}
	
	private RemoteViews buildViews(Context context){
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.lifetimerwidget);
		views.setOnClickPendingIntent(R.id.widgetLayout, buildActivityIntent(context));
		return views;
	}
	
	private String getFormatedTime(int mode, LTWInfomation info){
		String formatedTime = "";
		
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss"); 
		Date curDate = new Date();            //  현재 날짜 계산 
		Date tmpDate = null;

		long sec = 0;
		
		String strCurDate = format.format(curDate);
		
		if(mode == 1){
			try{
				tmpDate = format.parse(info.birth_year + info.birth_month + info.birth_day + "000000");
				curDate = format.parse(strCurDate);
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			sec = (curDate.getTime() - tmpDate.getTime()) / 1000;
		}else if(mode == 2){
			try{
				tmpDate = format.parse((Integer.valueOf(info.birth_year) + info.expAge) + info.birth_month + info.birth_day + "000000");
				curDate = format.parse(strCurDate);
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			sec = (tmpDate.getTime() - curDate.getTime()) / 1000;
		}
		
		NumberFormat numberFormat = NumberFormat.getInstance();
		  
		if ( numberFormat instanceof DecimalFormat ){
			((DecimalFormat)numberFormat).setDecimalSeparatorAlwaysShown(false);
		}
		
		switch(info.kind){
			case DISPLAY_TIME_MODE_SEC:
				formatedTime = String.format("%s초", numberFormat.format(sec));
				break;
			case DISPLAY_TIME_MODE_MIN:
				formatedTime = String.format("%s분", numberFormat.format(sec/60));
				break;
			case DISPLAY_TIME_MODE_HOUR:
				formatedTime = String.format("%s시간", numberFormat.format(sec/(60*60)));
				break;
			case DISPLAY_TIME_MODE_DAY:
				formatedTime = String.format("%s일", numberFormat.format(sec/(60*60*24)));
				break;
		}

		
		return formatedTime;		
	}
	
	public void removePreviousAlarm(){
		if(mManager != null && mSender != null)
	    {
	      mSender.cancel();
	      mManager.cancel(mSender);
	    }
	}


}
