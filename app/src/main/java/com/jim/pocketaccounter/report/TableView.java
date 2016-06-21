package com.jim.pocketaccounter.report;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.jim.pocketaccounter.R;

/**
 * Created by ismoi on 6/13/2016.
 */

public class TableView extends View implements GestureDetector.OnGestureListener {

    private float height, width;
    private int count_rows;
    private int count_colums;
    private float min_height = getResources().getDimension(R.dimen.min_height);
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

    public void setTitles(String[] titles) {
        this.titles = new String[titles.length];
        this.titles = titles;
    }

    public void setTables(String[][] tables) {
        this.tables = new String[titles.length][tables.length];
        this.tables = tables;
    }

    private ClickableTable clickableTable = null;

    public TableView(Context context) {
        super(context);
        gestureDetector = new GestureDetectorCompat(getContext(), this);
        setClickable(true);
    }

    public TableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        gestureDetector = new GestureDetectorCompat(getContext(), this);
        setClickable(true);
    }


    public TableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        gestureDetector = new GestureDetectorCompat(getContext(), this);
        setClickable(true);
    }

    private boolean create_title = false, create_table = false;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        height = getHeight();
        width = getWidth();

        if (titles == null) {
            titles = new String[0];
        } else {
            onDrawTitles(canvas, titles);
        }

        if (tables == null) {
            tables = new String[0][count_colums];
        } else {
            onDrawTables(canvas, tables);
        }

        setMinimumHeight((int) min_height * (2 + count_rows));
    }

    private void onDrawTitles(Canvas canvas, String[] tables) {
        count_colums = titles.length;
        titles_rects = new RectF[count_colums];
        min_width = (width - 2 * margin_content) / count_colums;

        Paint paint_border = new Paint();
        paint_border.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint_border.setStyle(Paint.Style.STROKE);
        paint_border.setStrokeWidth(stroke_width);
        paint_border.setColor(Color.BLACK);

        Paint paint_text = new Paint();
        paint_text.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint_text.setTextSize(textHeight);
        paint_text.setColor(Color.BLACK);
        paint_text.setStyle(Paint.Style.FILL);

        for (int i = 0; i < count_colums; i++) {

            if (titles[i].equals("Category")) pos_categ = i;

            titles_rects[i] = new RectF(
                    margin_content + i * min_width,
                    margin_content,
                    margin_content + (i + 1) * min_width,
                    margin_content + min_height);

            canvas.drawRect(titles_rects[i], paint_border);

            textWidth = paint_text.measureText(titles[i]);
            canvas.drawText(titles[i], margin_content + i * min_width + min_width / 2 - textWidth / 2,
                    margin_content + min_height / 2 + textHeight / 2, paint_text);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);

    }

    private int position_row, position_col, pos_categ;
    private boolean single_tap = false;

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
                    position_col = j;
                    invalidate();
                    break;
                }
            }
        }
        return false;
    }

    private void onDrawTables(Canvas canvas, String[][] tables) {
        count_rows = tables.length;

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

        Paint paint_border = new Paint();
        paint_border.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint_border.setStyle(Paint.Style.STROKE);
        paint_border.setStrokeWidth(stroke_width);
        paint_border.setColor(Color.BLACK);

        Paint paint_text = new Paint();
        paint_text.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint_text.setTextSize(textHeight);
        paint_text.setColor(Color.BLACK);
        paint_text.setStyle(Paint.Style.FILL);

        Paint paint_fill = new Paint();
        paint_fill.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint_fill.setStyle(Paint.Style.FILL);
        paint_fill.setColor(Color.BLUE);

        min_width = (width - 2 * margin_content) / count_colums;
        tables_rect = new RectF[count_rows][count_colums];

        for (int i = 0; i < count_rows; i++) {
            for (int j = 0; j < count_colums; j++) {
                tables_rect[i][j] = new RectF(margin_content + j * min_width, margin_content + (i + 1) * min_height,
                        margin_content + (j + 1) * min_width, margin_content + (i + 2) * min_height);
            }
        }
        for (int i = 0; i < count_rows; i++) {
            for (int j = 0; j < count_colums; j++) {
                if (single_tap && position_row == i) {
                    String string_full = tables[position_row][0];
                    for (int z = 0; z < count_colums; z++) {
                        canvas.drawRect(tables_rect[position_row][z], paint_fill);
                    }
                    clickableTable.onTableClick(string_full);
                    single_tap = false;
                }
                canvas.drawRect(tables_rect[i][j], paint_border);

                if (tables[i][j].equals("1") || tables[i][j].equals("0") && j == 0) {
                    if (tables[i][j].equals("1"))
                        canvas.drawBitmap(minus,
                                margin_content + j * min_width + min_width / 2 - bitmap_size / 2,
                                margin_content + (1 + i) * min_height + min_height / 2 - bitmap_size / 2,
                                paint_text);
                    if (tables[i][j].equals("0"))
                        canvas.drawBitmap(plus,
                                margin_content + j * min_width + min_width / 2 - bitmap_size / 2,
                                margin_content + (1 + i) * min_height + min_height / 2 - bitmap_size / 2,
                                paint_text);
                } else if (j == pos_categ && tables[i][j].split(",").length == 2) {
                    String[] cat = tables[i][j].split(",");
                    textWidth = paint_text.measureText(cat[0]);
                    canvas.drawText(cat[0],
                            margin_content + j * min_width + min_width / 2 - textWidth / 2,
                            margin_content + (i + 1) * min_height + min_height / 4 + textHeight / 2,
                            paint_text);
                    textWidth = paint_text.measureText(cat[1]);
                    canvas.drawText(cat[1],
                            margin_content + j * min_width + min_width / 2 - textWidth / 2,
                            margin_content + (i + 1) * min_height + 3 * min_height / 4 + textHeight / 2,
                            paint_text);
                } else {
                    textWidth = paint_text.measureText(tables[i][j]);
                    canvas.drawText(tables[i][j],
                            margin_content + j * min_width + min_width / 2 - textWidth / 2,
                            margin_content + (i + 1) * min_height + min_height / 2 + textHeight / 2,
                            paint_text);
                }
            }
        }
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
        public void onTableClick(String string);
    }
}