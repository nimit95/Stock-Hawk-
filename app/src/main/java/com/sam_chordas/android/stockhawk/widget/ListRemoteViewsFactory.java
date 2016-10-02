package com.sam_chordas.android.stockhawk.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

/**
 * Created by Nimit Agg on 19-06-2016.
 */
public class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory  {
    private Context mContext;
    private int mAppWidgetId;
    RemoteViews row;
    Cursor cursor;
    public ListRemoteViewsFactory(Context context, Intent intent){
        mContext = context;
      //  Log.e("gg","hhh");
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

    }
    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        initData();
    }

    @Override
    public void onDestroy() {
        if (cursor != null) {
            cursor.close();
        }
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        String symbol = "";
        String bidPrice = "";
        String change = "";

        if(cursor.moveToPosition(position)){
            symbol = cursor.getString(cursor.getColumnIndex(QuoteColumns.SYMBOL));

            bidPrice = cursor.getString(cursor.getColumnIndex(QuoteColumns.BIDPRICE));

            change = cursor.getString(cursor.getColumnIndex(QuoteColumns.PERCENT_CHANGE));

        }

        row=new RemoteViews(mContext.getPackageName(), R.layout.list_item_quote);
        row.setTextViewText(R.id.stock_symbol,symbol);
        row.setTextViewText(R.id.bid_price,bidPrice);
        row.setTextViewText(R.id.change,change);
        return row;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
    public void initData() {

        // Refresh the cursor
        if (cursor != null) {
            cursor.close();
        }

        cursor = mContext.getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
                new String[]{QuoteColumns._ID, QuoteColumns.SYMBOL, QuoteColumns.BIDPRICE,
                        QuoteColumns.PERCENT_CHANGE, QuoteColumns.CHANGE, QuoteColumns.ISUP},
                QuoteColumns.ISCURRENT + " = ?",
                new String[]{"1"},null);

    }
}
