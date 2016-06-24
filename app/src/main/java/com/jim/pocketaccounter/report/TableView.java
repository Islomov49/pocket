package com.jim.pocketaccounter.report;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.jim.pocketaccounter.R;

import java.util.ArrayList;


public class TableView extends View implements GestureDetector.OnGestureListener {

    private int count_rows;
    private int count_colums;
    private float min_height;
    private float min_width;
    private float margin_content = getResources().getDimension(R.dimen.margin_contetn);
    private float stroke_width = getResources().getDimension(R.dimen.stroke_width);
    private float textHeight = getResources().getDimension(R.dimen.text_height);
    private float textWidth, bitmap_size = getResources().getDimension(R.dimen.bitmap_size);
    private Bitmap plus, minus;
    private RectF[][] tables_rect;
    private RectF[] titles_rects;
    private GestureDetectorCompat gestureDetector;
    private String[] titles;
    private String[][] tables;
    private int position_row = -1, pos_categ, pos_cur;
    private boolean single_tap = false;
    private ClickableTable clickableTable = null;
    private RectF container = new RectF();
    public void setTitles(String[] titles) {
        this.titles = titles;
        titles_rects = new RectF[titles.length];
        for (int i=0; i<titles_rects.length; i++) {
            titles_rects[i] = new RectF();
        }
    }

    public void setTables(String[][] tables) {
        this.tables = tables;
        tables_rect = new RectF[tables.length][titles.length];
        for (int i=0; i < tables.length; i++) {
            for (int j = 0; j<titles.length; j++) {
                tables_rect[i][j] = new RectF();
            }
        }
    }

    public TableView(Context context) {
        super(context);
        gestureDetector = new GestureDetectorCompat(getContext(), this);
        setClickable(true);
        plus = BitmapFactory.decodeResource(getResources(), R.drawable.add_green);
        plus = Bitmap.createScaledBitmap(plus,
                (int) bitmap_size,
                (int) bitmap_size,
                true);
        minus = BitmapFactory.decodeResource(getResources(), R.drawable.remove_red);
        minus = Bitmap.createScaledBitmap(minus,
                (int) bitmap_size,
                (int) bitmap_size,
                true);
    }

    public TableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        gestureDetector = new GestureDetectorCompat(getContext(), this);
        setClickable(true);
        plus = BitmapFactory.decodeResource(getResources(), R.drawable.add_green);
        plus = Bitmap.createScaledBitmap(plus,
                (int) bitmap_size,
                (int) bitmap_size,
                true);
        minus = BitmapFactory.decodeResource(getResources(), R.drawable.remove_red);
        minus = Bitmap.createScaledBitmap(minus,
                (int) bitmap_size,
                (int) bitmap_size,
                true);
    }
    public TableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        gestureDetector = new GestureDetectorCompat(getContext(), this);
        setClickable(true);
        plus = BitmapFactory.decodeResource(getResources(), R.drawable.add_green);
        plus = Bitmap.createScaledBitmap(plus,
                (int) bitmap_size,
                (int) bitmap_size,
                true);
        minus = BitmapFactory.decodeResource(getResources(), R.drawable.remove_red);
        minus = Bitmap.createScaledBitmap(minus,
                (int) bitmap_size,
                (int) bitmap_size,
                true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        min_height = getResources().getDimension(R.dimen.thirty_dp);
        container.set(margin_content, margin_content, getWidth()-margin_content, min_height*count_rows+2*margin_content);
        setMinimumHeight((int)(container.height()+getResources().getDimension(R.dimen.fourty_dp)+4*margin_content));
        pos_categ = -1;
        pos_cur = -1;
        if (titles != null) {
            min_height = getResources().getDimension(R.dimen.fifty_dp);
            onDrawTitles(canvas);
        }
        if (tables != null) {
            onDrawTables(canvas, tables);
        }
    }
    private void onDrawTitles(Canvas canvas) {
        count_colums = titles_rects.length;
        min_width = container.width()/count_colums;
        Paint paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(textHeight);
        for (int i = 0; i < count_colums; i++) {
            if (titles[i].matches("Category")) pos_categ = i;
            if (titles[i].matches("Amount")) pos_cur = i;
            titles_rects[i].set(
                    container.left + i * min_width,
                    container.top,
                    container.left + (i + 1) * min_width,
                    container.top + min_height);
            paint.setColor(ContextCompat.getColor(getContext(), R.color.table_title));
            canvas.drawRect(titles_rects[i], paint);
            paint.setColor(Color.WHITE);
            textWidth = paint.measureText(titles[i]);
            canvas.drawText(titles[i], container.left + i * min_width + min_width / 2 - textWidth / 2,
                    container.top + min_height / 2 + textHeight / 2, paint);
        }
        container.top = margin_content + min_height;
    }

    private void onDrawTables(Canvas canvas, String[][] tables) {
        count_rows = tables.length;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < count_rows; i++) {
            if (i == position_row)
                min_height = getResources().getDimension(R.dimen.fifty_dp);
            else
                min_height = getResources().getDimension(R.dimen.thirty_dp);
            for (int j = 0; j < count_colums; j++) {
                tables_rect[i][j].set(container.left + j * min_width, container.top,
                        container.left + (j + 1) * min_width, container.top + min_height);
                if (i % 2 == 0) {
                    paint.setColor(ContextCompat.getColor(getContext(), R.color.table_odd));
                }
                else {
                    paint.setColor(ContextCompat.getColor(getContext(), R.color.table_double));
                }
                canvas.drawRect(tables_rect[i][j], paint);
                if (i == position_row) {
                    paint.setColor(ContextCompat.getColor(getContext(), R.color.table_selected));
                    canvas.drawRect(tables_rect[i][j], paint);
                }
            }
            container = new RectF(container.left, container.top + min_height, container.right, container.bottom);
        }
        paint.setTextSize(getResources().getDimension(R.dimen.ten_sp));
        paint.setColor(ContextCompat.getColor(getContext(), R.color.toolbar_text_color));
        for (int i = 0; i < count_rows; i++) {
            for (int j = 0; j < count_colums; j++) {
                String text = tables[i][j];
                Rect bound = new Rect();
                paint.getTextBounds(text, 0, text.length(), bound);
                if (i != position_row) {
                    if (bound.width() >= tables_rect[i][j].width()) {
                        for (int k=5; k<tables[i][j].length(); k++) {
                            text = tables[i][j].substring(0, k);
                            paint.getTextBounds(text, 0, text.length(), bound);
                            if (bound.width() >= tables_rect[i][j].width()) {
                                text = text.substring(0, text.length()-3) + "...";
                                break;
                            }
                        }
                    }
                    paint.getTextBounds(text, 0, text.length(), bound);
                    RectF temp = tables_rect[i][j];
                    boolean isBitmap =  (tables[i][j].matches("0") || tables[i][j].matches("1"));
                    if (isBitmap) {
                        Bitmap sign = null;
                        if (tables[i][j].matches("0")) {
                            Bitmap temp1 = BitmapFactory.decodeResource(getResources(), R.drawable.add_green);
                            sign = Bitmap.createScaledBitmap(temp1, (int)getResources().getDimension(R.dimen.twenty_dp), (int)getResources().getDimension(R.dimen.twenty_dp), false);
                        } else {
                            Bitmap temp1 = BitmapFactory.decodeResource(getResources(), R.drawable.remove_red);
                            sign = Bitmap.createScaledBitmap(temp1, (int)getResources().getDimension(R.dimen.twenty_dp), (int)getResources().getDimension(R.dimen.twenty_dp), false);
                        }
                        canvas.drawBitmap(sign, temp.left+temp.width()/2-sign.getWidth()/2, temp.top+temp.height()/2-sign.getHeight()/2,paint);
                    }
                    else
                        canvas.drawText(text, temp.left+temp.width()/2-bound.width()/2, temp.top + temp.height()/2+bound.height()/2, paint);
                }
                else {
                    ArrayList<String> texts = new ArrayList<>();
                    if (tables[i][j].indexOf(':') == -1) {
                        if (bound.width() >= tables_rect[i][j].width()) {
                            int pos = 0;
                            boolean entered = false;
                            for (int k=5; k<tables[i][j].length()+1; k++) {
                                text = tables[i][j].substring(pos, k);
                                paint.getTextBounds(text, 0, text.length(), bound);
                                if (bound.width() >= tables_rect[i][j].width()) {
                                    k -= 2;
                                    String temp = tables[i][j].substring(pos, k);
                                    texts.add(temp);
                                    entered = true;
                                    pos = k;
                                }
                                if (k == tables[i][j].length()-1 && entered) {
                                    String temp = tables[i][j].substring(pos, k+1);
                                    texts.add(temp);
                                }
                            }
                        }
                        else {
                            texts.add(tables[i][j]);
                        }
                    }
                    else {
                        String first = tables[i][j].substring(0, tables[i][j].indexOf(':'));
                        paint.getTextBounds(first, 0, first.length(), bound);

                        if (bound.width() >= tables_rect[i][j].width()) {
                            int pos = 0;
                            boolean entered = false;
                            for (int k=5; k<first.length(); k++) {
                                text = first.substring(pos, k);
                                paint.getTextBounds(text, 0, text.length(), bound);
                                if (bound.width() >= tables_rect[i][j].width()) {
                                    k -= 2;
                                    String temp = first.substring(pos, k);
                                    texts.add(temp);
                                    entered = true;
                                    pos = k;
                                }
                                if (k == first.length()-1 && entered) {
                                    String temp = first.substring(pos, k+1);
                                    texts.add(temp);
                                }
                            }
                        }
                        else {
                            texts.add(first);
                        }
                        String second = tables[i][j].substring(tables[i][j].indexOf(':')+1, tables[i][j].length());
                        paint.getTextBounds(second, 0, second.length(), bound);
                        if (bound.width() >= tables_rect[i][j].width()) {
                            int pos = 0;
                            boolean entered = false;
                            for (int k=5; k<second.length(); k++) {
                                text = second.substring(pos, k);
                                paint.getTextBounds(text, 0, text.length(), bound);
                                if (bound.width() >= tables_rect[i][j].width()) {
                                    k -= 2;
                                    String temp = second.substring(pos, k);
                                    texts.add(temp);
                                    entered = true;
                                    pos = k;
                                }
                                if (k == second.length()-1 && entered) {
                                    String temp = second.substring(pos, k+1);
                                    texts.add(temp);
                                }
                            }
                        }
                        else {
                            texts.add(second);
                        }
                    }
                    int size = texts.size();
                    if (size == 1) {
                        RectF temp = tables_rect[i][j];
                        boolean isBitmap =  (tables[i][j].matches("0") || tables[i][j].matches("1"));
                        if (isBitmap) {
                            Bitmap sign = null;
                            if (tables[i][j].matches("0")) {
                                Bitmap temp1 = BitmapFactory.decodeResource(getResources(), R.drawable.add_green);
                                sign = Bitmap.createScaledBitmap(temp1, (int)getResources().getDimension(R.dimen.twenty_dp), (int)getResources().getDimension(R.dimen.twenty_dp), false);
                            } else {
                                Bitmap temp1 = BitmapFactory.decodeResource(getResources(), R.drawable.remove_red);
                                sign = Bitmap.createScaledBitmap(temp1, (int)getResources().getDimension(R.dimen.twenty_dp), (int)getResources().getDimension(R.dimen.twenty_dp), false);
                            }
                            canvas.drawBitmap(sign, temp.left+temp.width()/2-sign.getWidth()/2, temp.top+temp.height()/2-sign.getHeight()/2,paint);
                        }
                        else
                            canvas.drawText(texts.get(0), temp.left+temp.width()/2-bound.width()/2, temp.top + temp.height()/2+bound.height()/2, paint);
                    }
                    else {
                        float rectHeight = tables_rect[i][j].height();
                        for (int k=0; k<size; k++) {
                            paint.getTextBounds(texts.get(k), 0, texts.get(k).length(), bound);
                            int height = bound.height();
                            int width = bound.width();
                            canvas.drawText(texts.get(k), tables_rect[i][j].left+tables_rect[i][j].width()/2-width/2, tables_rect[i][j].top + rectHeight/size+ k*(height+rectHeight/30),paint);
                        }
                    }
                }
            }
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);

    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    public boolean onSingleTapUp(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();
        for (int i = 0; i < count_rows; i++) {
            for (int j = 0; j < count_colums; j++) {
                if (tables_rect[i][j].contains(x, y)) {
                    single_tap = true;
                    position_row = i;
                    invalidate();
                    clickableTable.onTableClick(position_row);
                    break;
                }
            }
        }
        return false;
    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    public void setOnTableClickListener(ClickableTable clickableTable) {
        this.clickableTable = clickableTable;
    }

    public interface ClickableTable {
        public void onTableClick(int row);
    }
}