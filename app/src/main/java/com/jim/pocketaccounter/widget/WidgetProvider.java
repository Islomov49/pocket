package com.jim.pocketaccounter.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;

import com.jim.pocketaccounter.PocketAccounter;
import com.jim.pocketaccounter.R;
import com.jim.pocketaccounter.finance.Category;
import com.jim.pocketaccounter.finance.FinanceManager;
import com.jim.pocketaccounter.finance.RootCategory;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by DEV on 09.07.2016.
 */

public class WidgetProvider extends AppWidgetProvider {
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);

    }
    SharedPreferences sPref;
    ArrayList<RootCategory> listCategory;
    private String butID_1,butID_2,butID_3,butID_4;
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        sPref = context.getSharedPreferences("infoFirst", MODE_PRIVATE);
        FinanceManager financeManager=new FinanceManager(context);
        listCategory=financeManager.getCategories();

        /* Activatsiya buttonov */
        sPref.edit().putString(WidgetKeys.BUTTON_3_ID,listCategory.get(3).getId()).apply();
        sPref.edit().putString(WidgetKeys.BUTTON_1_ID,listCategory.get(0).getId()).apply();
        sPref.edit().putString(WidgetKeys.BUTTON_2_ID,listCategory.get(1).getId()).apply();
        sPref.edit().putString(WidgetKeys.BUTTON_4_ID,listCategory.get(2).getId()).apply();
        /* */

        butID_1=sPref.getString(WidgetKeys.BUTTON_1_ID,WidgetKeys.BUTTON_DISABLED);
        butID_2=sPref.getString(WidgetKeys.BUTTON_2_ID,WidgetKeys.BUTTON_DISABLED);
        butID_3=sPref.getString(WidgetKeys.BUTTON_3_ID,WidgetKeys.BUTTON_DISABLED);
        butID_4=sPref.getString(WidgetKeys.BUTTON_4_ID,WidgetKeys.BUTTON_DISABLED);


        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];


             RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);



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



            //diagrammani qowiw
            views.setImageViewResource(R.id.diagramma_widget,R.drawable.example_for_test_widget_test);

            //balanceni berisiz
            views.setTextViewText(R.id.balance_widget,"4555$");
            //rasxodni berisiz
            views.setTextViewText(R.id.expence_widget,"389$");

            //doxoddi berisiz
            views.setTextViewText(R.id.income_widget,"880$");





            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
