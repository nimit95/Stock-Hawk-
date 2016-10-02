package com.sam_chordas.android.stockhawk.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.service.StockIntentService;


/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {
    private Intent mServiceIntent;


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int i = 0; i < appWidgetIds.length; ++i) {
            //mServiceIntent = new Intent(context, StockIntentService.class);
            //context.startService(mServiceIntent);
            Intent intent=new Intent(context,ListWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            rv.setRemoteAdapter(R.id.listView,intent);
            rv.setEmptyView(R.id.listView, R.id.empty_view);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds[i],R.id.listView);
            appWidgetManager.updateAppWidget(appWidgetIds[i], rv);

        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }




}

