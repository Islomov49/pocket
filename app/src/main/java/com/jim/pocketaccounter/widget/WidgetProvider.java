package com.jim.pocketaccounter.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;

import com.jim.pocketaccounter.PocketAccounter;
import com.jim.pocketaccounter.R;

/**
 * Created by DEV on 09.07.2016.
 */

public class WidgetProvider extends AppWidgetProvider {
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);

    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;


        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];


            Intent intent = new Intent(context, PocketAccounter.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
            //diagrammani qowiw
            views.setImageViewResource(R.id.diagramma_widget,R.drawable.example_for_test_widget_test);

            //balanceni berisiz
            views.setTextViewText(R.id.balance_widget,"4555$");
            //rasxodni berisiz
            views.setTextViewText(R.id.expence_widget,"389$");

            //doxoddi berisiz
            views.setTextViewText(R.id.income_widget,"880$");

            //onClick intent ochish
            views.setOnClickPendingIntent(R.id.button_1,pendingIntent);

            //ustanovka ikonki
            views.setImageViewResource(R.id.button_1_ramka,R.drawable.shape_for_widget_black);
            views.setImageViewResource(R.id.button_1_icon,R.drawable.icons_15);



            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
