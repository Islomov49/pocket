package com.jim.pocketaccounter.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.jim.pocketaccounter.PocketAccounter;
import com.jim.pocketaccounter.R;
import com.jim.pocketaccounter.finance.Category;
import com.jim.pocketaccounter.finance.FinanceManager;
import com.jim.pocketaccounter.finance.RootCategory;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static java.security.AccessController.getContext;

/**
 * Created by DEV on 09.07.2016.
 */

public class WidgetProvider extends AppWidgetProvider {

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);

    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        SharedPreferences sPref;
        sPref = context.getSharedPreferences("infoFirst", MODE_PRIVATE);
        ArrayList<RootCategory> listCategory;
        FinanceManager financeManager=new FinanceManager(context);
        listCategory=financeManager.getCategories();
     /* Activatsiya buttonov buni settingsi ichiga*/
        sPref.edit().putString(WidgetKeys.BUTTON_3_ID,listCategory.get(3).getId()).apply();
        sPref.edit().putString(WidgetKeys.BUTTON_1_ID,listCategory.get(0).getId()).apply();
        sPref.edit().putString(WidgetKeys.BUTTON_2_ID,listCategory.get(1).getId()).apply();
        sPref.edit().putString(WidgetKeys.BUTTON_4_ID,listCategory.get(2).getId()).apply();
        /* */
        super.onUpdate(context,appWidgetManager,appWidgetIds);
        for (int i : appWidgetIds) {
            updateWidget(context, appWidgetManager, i);
        }


    }


    static void updateWidget(Context context, AppWidgetManager appWidgetManager,
                             int widgetID) {
        SharedPreferences sPref;
        ArrayList<RootCategory> listCategory;
            String butID_1,butID_2,butID_3,butID_4;

        sPref = context.getSharedPreferences("infoFirst", MODE_PRIVATE);
        FinanceManager financeManager=new FinanceManager(context);
        listCategory=financeManager.getCategories();



        butID_1=sPref.getString(WidgetKeys.BUTTON_1_ID,WidgetKeys.BUTTON_DISABLED);
        butID_2=sPref.getString(WidgetKeys.BUTTON_2_ID,WidgetKeys.BUTTON_DISABLED);
        butID_3=sPref.getString(WidgetKeys.BUTTON_3_ID,WidgetKeys.BUTTON_DISABLED);
        butID_4=sPref.getString(WidgetKeys.BUTTON_4_ID,WidgetKeys.BUTTON_DISABLED);


            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
            Log.d(WidgetKeys.TAG, "UPDATE");


            // knopka instalizatsiya bloki
            if(!butID_1.matches(WidgetKeys.BUTTON_DISABLED)||!butID_2.matches(WidgetKeys.BUTTON_DISABLED)
                    ||!butID_3.matches(WidgetKeys.BUTTON_DISABLED)||!butID_4.matches(WidgetKeys.BUTTON_DISABLED))
                for (RootCategory temp:listCategory) {

                    if(!butID_1.matches(WidgetKeys.BUTTON_DISABLED)&&temp.getId().matches(butID_1)){
                        //ustanovka ikonki
                        views.setImageViewResource(R.id.button_1_ramka,R.drawable.shape_for_widget_black);
                        views.setImageViewResource(R.id.button_1_icon,temp.getIcon());
                        //ustanovka onclika
                        Intent button1 = new Intent(context, PocketAccounter.class);
                        button1.putExtra(WidgetKeys.CHOSED_CATEGORY,temp.getId());
                        PendingIntent pendingIntent_button1 = PendingIntent.getActivity(context, 0, button1, 0);
                        views.setOnClickPendingIntent(R.id.button_1,pendingIntent_button1);

                    }

                    if(!butID_2.matches(WidgetKeys.BUTTON_DISABLED)&&temp.getId().matches(butID_2)){
                        //ustanovka ikonki
                        views.setImageViewResource(R.id.button_2_ramka,R.drawable.shape_for_widget_black);
                        views.setImageViewResource(R.id.button_2_icon,temp.getIcon());
                        //ustanovka onclika
                        Intent button2 = new Intent(context, PocketAccounter.class);
                        button2.putExtra(WidgetKeys.CHOSED_CATEGORY,temp.getId());
                        PendingIntent pendingIntent_button2 = PendingIntent.getActivity(context, 0, button2, 0);
                        views.setOnClickPendingIntent(R.id.button_2,pendingIntent_button2);


                    }

                    if(!butID_3.matches(WidgetKeys.BUTTON_DISABLED)&&temp.getId().matches(butID_3)){
                        //ustanovka ikonki
                        views.setImageViewResource(R.id.button_3_ramka,R.drawable.shape_for_widget_black);
                        views.setImageViewResource(R.id.button_3_icon,temp.getIcon());
                        //ustanovka onclika
                        Intent button3 = new Intent(context, PocketAccounter.class);
                        button3.putExtra(WidgetKeys.CHOSED_CATEGORY,temp.getId());
                        PendingIntent pendingIntent_button3 = PendingIntent.getActivity(context, 0, button3, 0);
                        views.setOnClickPendingIntent(R.id.button_3,pendingIntent_button3);

                    }

                    if(!butID_4.matches(WidgetKeys.BUTTON_DISABLED)&&temp.getId().matches(butID_4)){
                        //ustanovka ikonki
                        views.setImageViewResource(R.id.button_4_ramka,R.drawable.shape_for_widget_black);
                        views.setImageViewResource(R.id.button_4_icon,temp.getIcon());
                        //ustanovka onclika
                        Intent button4 = new Intent(context, PocketAccounter.class);
                        button4.putExtra(WidgetKeys.CHOSED_CATEGORY,temp.getId());
                        PendingIntent pendingIntent_button4 = PendingIntent.getActivity(context, 0, button4, 0);
                        views.setOnClickPendingIntent(R.id.button_4,pendingIntent_button4);

                    }
                }
            if(butID_1.matches(WidgetKeys.BUTTON_DISABLED)){
                views.setImageViewResource(R.id.button_1_ramka,R.drawable.shape_for_widget);
                views.setImageViewResource(R.id.button_1_icon,R.drawable.ic_add_widget);
                views.setOnClickPendingIntent(R.id.button_1,null);

            }
            if(butID_2.matches(WidgetKeys.BUTTON_DISABLED)){
                views.setImageViewResource(R.id.button_2_ramka,R.drawable.shape_for_widget);
                views.setImageViewResource(R.id.button_2_icon,R.drawable.ic_add_widget);
                views.setOnClickPendingIntent(R.id.button_2,null);
            }
            if(butID_3.matches(WidgetKeys.BUTTON_DISABLED)){
                views.setImageViewResource(R.id.button_3_ramka,R.drawable.shape_for_widget);
                views.setImageViewResource(R.id.button_3_icon,R.drawable.ic_add_widget);
                views.setOnClickPendingIntent(R.id.button_3,null);
            }
            if(butID_4.matches(WidgetKeys.BUTTON_DISABLED)){
                views.setImageViewResource(R.id.button_4_ramka,R.drawable.shape_for_widget);
                views.setImageViewResource(R.id.button_4_icon,R.drawable.ic_add_widget);
                views.setOnClickPendingIntent(R.id.button_4,null);
            }

        if (sPref.getBoolean(WidgetKeys.ACTION_WIDGET_CHANGE_DIAGRAM_STATUS, true)) {
            //INCOME DIAGRAMMA YASASH
            Log.d(WidgetKeys.TAG, "INCOME");
            Toast.makeText(context, "INCOME", Toast.LENGTH_SHORT).show();

        } else {
            Log.d(WidgetKeys.TAG, "EXPENCE");
            Toast.makeText(context, "EXPENCE", Toast.LENGTH_SHORT).show();


        }


            //Diagrammani alishtirish
            Intent active = new Intent(context, WidgetProvider.class);
            active.setAction(WidgetKeys.ACTION_WIDGET_RECEIVER_CHANGE_DIAGRAM);
            active.putExtra(WidgetKeys.ACTION_WIDGET_RECEIVER_CHANGE_DIAGRAM_ID,
                     widgetID );
            Log.d(WidgetKeys.TAG, widgetID+"");
            PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, widgetID
                    , active, 0);
            views.setOnClickPendingIntent(R.id.diagramma_widget,actionPendingIntent);



        Intent active_s = new Intent(context, SettingsWidget.class);
        active_s.setAction(WidgetKeys.ACTION_WIDGET_RECEIVER_CHANGE_DIAGRAM_set);
        active_s.putExtra(WidgetKeys.ACTION_WIDGET_RECEIVER_CHANGE_DIAGRAM_ID,
                widgetID );
        PendingIntent actionPendingIntent_s = PendingIntent.getActivity(context, widgetID
                , active_s, 0);
        views.setOnClickPendingIntent(R.id.settings_widget,actionPendingIntent_s);


            //diagrammani qowiw
            views.setImageViewResource(R.id.diagramma_widget,R.drawable.example_for_test_widget_test);

            //balanceni berisiz
            views.setTextViewText(R.id.balance_widget,"4555$");
            //rasxodni berisiz
            views.setTextViewText(R.id.expence_widget,"389$");

            //doxoddi berisiz
            views.setTextViewText(R.id.income_widget,"880$");





            appWidgetManager.updateAppWidget(widgetID, views);


    }


        @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        if (WidgetKeys.ACTION_WIDGET_RECEIVER_CHANGE_DIAGRAM.equals(action)) {
            SharedPreferences sPref;
            Log.d(WidgetKeys.TAG, "coming");

            int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
            Bundle extras = intent.getExtras();
            if (extras != null) {
                Log.d(WidgetKeys.TAG, "bundle_not_null");
                mAppWidgetId = intent.getIntExtra(
                        WidgetKeys.ACTION_WIDGET_RECEIVER_CHANGE_DIAGRAM_ID,
                        AppWidgetManager.INVALID_APPWIDGET_ID);

            }
            Log.d(WidgetKeys.TAG, mAppWidgetId+"");

            if (mAppWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                sPref = context.getSharedPreferences("infoFirst", MODE_PRIVATE);
                if (sPref.getBoolean(WidgetKeys.ACTION_WIDGET_CHANGE_DIAGRAM_STATUS, true)) {
                    //INCOME DIAGRAMMA YASASH


                    sPref.edit().putBoolean(WidgetKeys.ACTION_WIDGET_CHANGE_DIAGRAM_STATUS, false).apply();
                } else {
                    //EXPENCE DIAGRAMMA YASASH

                    sPref.edit().putBoolean(WidgetKeys.ACTION_WIDGET_CHANGE_DIAGRAM_STATUS, true).apply();
                }

                updateWidget(context, AppWidgetManager.getInstance(context),
                        mAppWidgetId);
            }
        }
        super.onReceive(context, intent);
    }
}
