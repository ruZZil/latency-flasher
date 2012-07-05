package com.screenovate.latency.flasher;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class FlasherServiceWidget extends AppWidgetProvider {

	public static String ACTION_WIDGET_STOPSERVICE = "StopService";
	public static String ACTION_WIDGET_STARTSERVICE = "StartService";

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.main);

		Intent startService = new Intent(context, FlasherServiceWidget.class);
		startService.setAction(ACTION_WIDGET_STARTSERVICE);

		PendingIntent startServicePendingIntent = PendingIntent.getBroadcast(
				context, 0, startService, 0);

		remoteViews.setOnClickPendingIntent(R.id.buttonStart,
				startServicePendingIntent);

		Intent stopService = new Intent(context, FlasherServiceWidget.class);
		stopService.setAction(ACTION_WIDGET_STOPSERVICE);

		PendingIntent stopServicePendingIntent = PendingIntent.getBroadcast(
				context, 0, stopService, 0);

		remoteViews.setOnClickPendingIntent(R.id.buttonStop,
				stopServicePendingIntent);

		appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);

	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);

		
		final String action = intent.getAction();

		if (action.equals(ACTION_WIDGET_STOPSERVICE)) {
			context.stopService(new Intent(context, FlasherServiceService.class));
		} else if (action.equals(ACTION_WIDGET_STARTSERVICE)) {
			context.startService(new Intent(context,FlasherServiceService.class));
		}

	}

}